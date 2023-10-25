package com.github.pedromsilvapt.recollect;

import com.github.pedromsilvapt.recollect.Models.Views.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.Duration;
import java.util.Arrays;
import java.util.Map;

@SpringBootApplication
@Controller
public class ReCollectApplication {
    public static String recordingsLocation = "";

    public static void main(String[] args) {
        SpringApplication.run(ReCollectApplication.class, args);
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
        // TODO Dummy projects for now just to serve as mock data
        var projects = Arrays.asList(
                new IndexFormProject(1, "Boeing", "selected"),
                new IndexFormProject(2, "Airbus", "")
        );

        var meetings = Arrays.asList(
                new IndexFormMeeting(1, "Backlog Refinement", "selected"),
                new IndexFormMeeting(1, "Planning", ""),
                new IndexFormMeeting(3, "Technical Meeting", ""),
                new IndexFormMeeting(2, "Sprint Review", "")
        );

        model.put("form", new IndexForm(projects, meetings));

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
    public ModelAndView search(Map<String, Object> model) {
        // TODO Dummy projects for now just to serve as mock data
        var projects = Arrays.asList(
                new IndexFormProject(1, "Boeing", "selected"),
                new IndexFormProject(2, "Airbus", "")
        );

        var meetings = Arrays.asList(
                new IndexFormMeeting(1, "Backlog Refinement", "selected"),
                new IndexFormMeeting(1, "Planning", ""),
                new IndexFormMeeting(3, "Technical Meeting", ""),
                new IndexFormMeeting(2, "Sprint Review", "")
        );

        var matches = Arrays.asList(
                new SearchResultMatch(1, Utilities.formatDuration(Duration.ofSeconds(20)), Utilities.formatDuration(Duration.ofSeconds(24)), "So, the topics for this meeting will be SPC and Quality Control.", false),
                new SearchResultMatch(2, Utilities.formatDuration(Duration.ofSeconds(25)), Utilities.formatDuration(Duration.ofSeconds(30)), "We will start with Quality Control, and discuss SPC later.", false),
                new SearchResultMatch(445, Utilities.formatDuration(Duration.ofSeconds(10025)), Utilities.formatDuration(Duration.ofSeconds(10031)), "So, moving on to SPC.", true)
        );

        var results = Arrays.asList(
                new SearchResult(1, 1, "https://images.hindustantimes.com/tech/img/2022/01/03/960x540/Microsoft_Teams_-_Breakout_Rooms_-_Manage_1628228764394_1641198077262.jpg", "Boeing :: Sprint 5 Review", Utilities.humanizeDuration(Duration.ofMinutes(37)), "06 Oct 2023", "Boeing", "Sprint Reviews", matches),
                new SearchResult(2, 2, "https://images.hindustantimes.com/tech/img/2022/01/03/960x540/Microsoft_Teams_-_Breakout_Rooms_-_Manage_1628228764394_1641198077262.jpg", "Boeing :: Sprint 6 Review", Utilities.humanizeDuration(Duration.ofMinutes(28)), "20 Oct 2023", "Boeing", "Sprint Reviews", matches)
        );

        var elapsedTime = Duration.ofMillis(7);

        model.put("form", new IndexForm(projects, meetings));
        model.put("results", results);
        model.put("elapsedTime", Utilities.humanizeDuration(elapsedTime));

        return new ModelAndView("search", model);
    }
}
