package com.github.pedromsilvapt.recollect;

import com.github.pedromsilvapt.recollect.Models.Recording;
import com.github.pedromsilvapt.recollect.Repositories.RecordingLineRepository;
import com.github.pedromsilvapt.recollect.Repositories.RecordingRepository;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class RecordingPipeline {
    private static final Logger log = LoggerFactory.getLogger(RecordingPipeline.class);

    private RecordingRepository recordingsRepository;

    private RecordingLineRepository recordingLinesRepository;

    public RecordingPipeline(RecordingRepository recordingsRepository, RecordingLineRepository recordingLinesRepository) {
        this.recordingsRepository = recordingsRepository;
        this.recordingLinesRepository = recordingLinesRepository;
    }

    public void transcribe(Recording recording) {
        throw new NotImplementedException();
    }

    /**
     * Generate a thumbnail from the video recording and save it in the default thumbnails folder
     * @param recording
     */
    public void generateThumbnail(Recording recording) throws IOException {
        var targetPath = Paths.get(System.getProperty("user.dir")).resolve("storage").resolve("thumbnails");

        // Makes sure the directory exists
        Files.createDirectories(targetPath);

        targetPath = targetPath.resolve(recording.getRecordingId() + ".jpg");

        if (Files.exists(targetPath)) {
            Files.delete(targetPath);
        }

        generateThumbnail(recording, targetPath.toString());
    }

    /**
     * Generate a thumbnail from the video recording and save it in the provided path
     *
     * @param recording
     * @param targetPath
     * @throws IOException
     */
    public void generateThumbnail(Recording recording, String targetPath) throws IOException {
        FFmpeg ffmpeg = new FFmpeg("ffmpeg");
        FFprobe ffprobe = new FFprobe("ffprobe");

        FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(recording.getFilePath())
                .addOutput(targetPath)
                .setFrames(1)
                .setVideoFilter("select='gte(n\\,500)',scale=200:-1")
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);

        // Run a one-pass encode
        executor.createJob(builder).run();
    }
}
