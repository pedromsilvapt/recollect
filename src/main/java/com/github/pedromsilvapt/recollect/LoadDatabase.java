package com.github.pedromsilvapt.recollect;

import com.github.pedromsilvapt.recollect.Models.Meeting;
import com.github.pedromsilvapt.recollect.Models.Project;
import com.github.pedromsilvapt.recollect.Models.Recording;
import com.github.pedromsilvapt.recollect.Models.RecordingProcessingState;
import com.github.pedromsilvapt.recollect.Repositories.MeetingRepository;
import com.github.pedromsilvapt.recollect.Repositories.ProjectRepository;
import com.github.pedromsilvapt.recollect.Repositories.RecordingLineRepository;
import com.github.pedromsilvapt.recollect.Repositories.RecordingRepository;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Load the files from the Recordings folder, and pre-fill the database with the Projects, Meetings, and Recordings found
 */
@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    private static String[] videoExtensions = new String[]{".mkv", ".mp4", ".avi"};

    @Bean
    CommandLineRunner initDatabase(RecordingRepository recordingsRepo, ProjectRepository projectsRepo, MeetingRepository meetingsRepo, RecordingLineRepository linesRepo) {
        return args -> {
            // Since the meetings are shared by multiple projects, when we find a meeting folder, we want to check by name and make sure we have already created one
            // And to avoid going to the database for that, just store the meetings by their names on a Map
            Map<String, Meeting> meetingsByName = new HashMap<>();

            var recordingsPath = Paths.get(ReCollectApplication.recordingsLocation);

            // Create instance of the class responsible for the various processing requirements for a new recording
            var pipeline = new RecordingPipeline(recordingsRepo, linesRepo);

            // An example of the expected folder/file structure is as follows:
            //   - Recordings
            //      - Project A
            //         - Sprint Reviews
            //            - 2023 05 31 - Project A Sprint Review.mkv
            //            - 2023 05 31 - Project A Sprint Review.srt
            //      - Project B
            //         - Backlog Refinements
            //            - 2023 04 26 - Project B Backlog Refinement.mkv
            //         - Sprint Reviews
            //            - 2023 06 13 - Project B Sprint Review.mkv
            //            - 2023 06 13 - Project B Sprint Review.srt
            // Which will create:
            //   - 2 Projects (Project A, Project B)
            //   - 2 Meetings (Sprint Reviews, Backlog Refinements)
            //   - 3 Recordings (one of which will be queued to be transcribed, since it has no subtitles)
            //   - All Recording Lines loaded from the .srt files
            if (Files.exists(recordingsPath)) {
                for (var projectPath : Files.list(recordingsPath).toList()) {
                    // Ignore any direct files inside the root Recordings directory
                    if (!Files.isDirectory(projectPath)) {
                        continue;
                    }

                    var projectName = projectPath.getFileName().toString();
                    var project = projectsRepo.save(new Project(projectName));

                    log.info("Preloading " + project);

                    for (var meetingPath : Files.list(projectPath).toList()) {
                        // Ignore any direct files inside the Project's directory
                        if (!Files.isDirectory(meetingPath)) {
                            continue;
                        }

                        var meetingName = meetingPath.getFileName().toString();
                        var meeting = meetingsByName.getOrDefault(meetingName, null);

                        // If no Project has had a meeting with this name, we can create a new one
                        if (meeting == null) {
                            meeting = meetingsRepo.save(new Meeting(meetingName));
                            meetingsByName.put(meetingName, meeting);

                            log.info("Preloading " + meeting);
                        }

                        for (var recordingPath : Files.list(meetingPath).toList()) {
                            // Ignore anything that is not a file inside the Project Meeting's directory
                            if (!Files.isRegularFile(recordingPath)) {
                                continue;
                            }

                            // Check if this file's extension is a video file
                            boolean isVideo = Arrays.stream(videoExtensions).anyMatch(recordingPath.toString()::endsWith);

                            if (!isVideo) {
                                continue;
                            }

                            FFprobe ffprobe = new FFprobe("ffprobe");
                            FFmpegProbeResult probeResult = ffprobe.probe(recordingPath.toString());

                            // Total duration in seconds, as a double
                            long duration = (long) Math.ceil(probeResult.getFormat().duration);

                            Instant recordingDate = Files.getLastModifiedTime(recordingPath, LinkOption.NOFOLLOW_LINKS).toInstant();

                            // We assume that the subtitles file (if one exists) always has the same name as the recording file,
                            // but with a different extension (in this case, let's support only .srt for now)
                            var recordingSubtitlesPath = Utilities.pathChangeExtension(recordingPath, ".srt");

                            var state = RecordingProcessingState.QUEUED;

                            // We can check if the file exists, in which case the state of the recording can be assumed to be
                            // READY, otherwise will be QUEUED
                            if (Files.exists(recordingSubtitlesPath) && Files.isRegularFile(recordingSubtitlesPath)) {
                                state = RecordingProcessingState.READY;
                            }

                            var recording = recordingsRepo.save(new Recording(
                                    recordingPath.getFileName().toString(),
                                    Duration.ofSeconds(duration),
                                    recordingPath.toString(),
                                    recordingDate,
                                    state,
                                    project,
                                    meeting
                            ));

                            log.info("Preloading " + recording);

                            // If the state of this recording is ready, it means the subtitles are already available
                            // So we can just parse them and save them into the database too
                            if (state == RecordingProcessingState.READY) {
                                try {
                                    var lines = linesRepo.saveAll(Utilities.parseSubtitleFile(recording, recordingSubtitlesPath));

                                    log.info(String.format("Preloading %d lines", lines.size()));
                                } catch (Exception ex) {
                                    log.error("Failed to parse and save lines for recording " + recordingPath.toString(), ex);
                                }
                            }

                            // Generate the thumbnail for the recording
                            pipeline.generateThumbnail(recording);

                            // TODO Queue transcription
                        }
                    }
                }
            }
        };
    }
}
