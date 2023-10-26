package com.github.pedromsilvapt.recollect.Models.Views;

import java.util.List;

/**
 * Model Class for the Index & Search pages, containing the data to pre-fill on the forms when rendering the templates
 */
public class IndexForm {
    private String text;
    private List<IndexFormProject> projects;
    private List<IndexFormMeeting> meetings;

    public IndexForm(String text, List<IndexFormProject> projects, List<IndexFormMeeting> meetings) {
        this.text = text;
        this.projects = projects;
        this.meetings = meetings;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<IndexFormProject> getProjects() {
        return projects;
    }

    public void setProjects(List<IndexFormProject> projects) {
        this.projects = projects;
    }

    public List<IndexFormMeeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<IndexFormMeeting> meetings) {
        this.meetings = meetings;
    }
}
