package com.github.pedromsilvapt.recollect;

import com.github.pedromsilvapt.recollect.Models.Recording;
import com.github.pedromsilvapt.recollect.Models.Views.*;
import com.github.pedromsilvapt.recollect.Repositories.MeetingRepository;
import com.github.pedromsilvapt.recollect.Repositories.ProjectRepository;
import com.github.pedromsilvapt.recollect.Repositories.RecordingLineRepository;
import com.github.pedromsilvapt.recollect.Repositories.RecordingRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@SpringBootApplication
@Controller
public class ReCollectApplication {
    public static String recordingsLocation = "B:\\Videos\\Work\\Critical Manufacturing";

    public static final int MAX_TERM_DIFF = 1;

    public static final int MAX_TERM_DIST = 10;

    public static void main(String[] args) {
        SpringApplication.run(ReCollectApplication.class, args);
    }

    private final RecordingRepository recordingsRepository;
    private final RecordingLineRepository recordingLinesRepository;
    private final ProjectRepository projectsRepository;
    private final MeetingRepository meetingsRepository;

    ReCollectApplication(
            RecordingRepository recordingsRepository,
            RecordingLineRepository recordingLinesRepository,
            ProjectRepository projectsRepository,
            MeetingRepository meetingsRepository) {
        this.recordingsRepository = recordingsRepository;
        this.recordingLinesRepository = recordingLinesRepository;
        this.projectsRepository = projectsRepository;
        this.meetingsRepository = meetingsRepository;
    }

    /**
     * Index page controller mapping.
     * <p>
     * Renders a minimalist HTML page with two main focuses:
     * - A search bar, with a filter by project
     * - A button to upload a new recording to the collection
     *
     * @return The rendered HTML source code
     */
    @GetMapping("/")
    public ModelAndView index(Map<String, Object> model) {
        var form = getSearchFormObject(new SearchQuery());

        model.put("form", form);

        return new ModelAndView("index", model);
    }

    /**
     * Search page controller mapping.
     * <p>
     * Renders an HTML page with two main focuses:
     * - A search bar, with a filter by project and meeting type. Values are pre-filled with the current search
     * - A list of search results, ordered by relevance
     *
     * @return The rendered HTML source code
     */
    @GetMapping("/search")
    public ModelAndView search(Map<String, Object> model, SearchQuery query) {
        var searchStart = Instant.now();

        var form = getSearchFormObject(query);

        var results = getSearchResultsList(query);

        model.put("form", form);
        model.put("results", results);
        model.put("elapsedTime", Utilities.humanizeDuration(Duration.between(searchStart, Instant.now())));

        return new ModelAndView("search", model);
    }


    @RequestMapping(value = "/thumbnail/{recordingId}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
    public void thumbnail(HttpServletResponse response, @PathVariable long recordingId) throws IOException {
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);

        var targetPath = Paths.get(System.getProperty("user.dir"))
                .resolve("storage")
                .resolve("thumbnails")
                .resolve(recordingId + ".jpg");

        if (Files.exists(targetPath)) {
            StreamUtils.copy(Files.newInputStream(targetPath), response.getOutputStream());
        } else {
            response.setStatus(404);
        }
    }

    private List<SearchResult> getSearchResultsList(SearchQuery query) {
        var recordings = recordingsRepository.findForProjectMeetingWithLines(query.project, query.meeting);

        var results = new ArrayList<SearchResult>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")
                .withZone(ZoneId.systemDefault());

        var textSearch = new TextSearch(query.text);

        for (Recording recording : recordings) {
            var searchResult = textSearch.calculateSearchResultScore(recording.getLines(), MAX_TERM_DIFF, MAX_TERM_DIST);

            if (searchResult.getScore() > 0) {
                results.add(new SearchResult(
                        searchResult.getScore(),
                        // Hard-code index as zero for now. After the list is fully created, we sort it by score
                        // and only then we calculate the proper indexes
                        0,
                        recording.getRecordingId(),
                        "/thumbnail/" + recording.getRecordingId(),
                        recording.getTitle(),
                        Utilities.humanizeDuration(recording.getDuration()),
                        formatter.format(recording.getDate()),
                        recording.getProject().getName(),
                        recording.getMeeting().getName(),
                        searchResult.getMatches()
                ));
            }
        }

        // Sort by the Score field, in descending order
        results.sort(Comparator.comparing(SearchResult::getScore));
        Collections.reverse(results);

        // Update the indexes
        int index = 1;
        for (SearchResult result : results) {
            result.setIndex(index++);
        }

        return results;
    }

    private IndexForm getSearchFormObject(SearchQuery query) {
        var projects = projectsRepository
                .findAll()
                .stream()
                .map(proj -> new IndexFormProject(proj.getProjectId(), proj.getName(), proj.getProjectId() == query.project ? "selected" : ""))
                .toList();

        var meetings = meetingsRepository
                .findAll()
                .stream()
                .map(meet -> new IndexFormMeeting(meet.getMeetingId(), meet.getName(), meet.getMeetingId() == query.meeting ? "selected" : ""))
                .toList();

        return new IndexForm(query.text, projects, meetings);
    }
}
