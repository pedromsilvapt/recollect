package com.github.pedromsilvapt.recollect;

import com.github.pedromsilvapt.recollect.Models.Views.IndexForm;
import com.github.pedromsilvapt.recollect.Models.Views.IndexFormMeeting;
import com.github.pedromsilvapt.recollect.Models.Views.IndexFormProject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Arrays;
import java.util.Map;

@SpringBootApplication
@Controller
public class ReCollectApplication {

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
}
