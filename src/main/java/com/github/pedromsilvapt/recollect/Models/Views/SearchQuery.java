package com.github.pedromsilvapt.recollect.Models.Views;

import java.util.Objects;

public class SearchQuery {
    public int project;

    public int meeting;

    public String text;

    public SearchQuery() {
    }

    public SearchQuery(int project, int meeting, String text) {
        this.project = project;
        this.meeting = meeting;
        this.text = text;
    }

    public int getProject() {
        return project;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public int getMeeting() {
        return meeting;
    }

    public void setMeeting(int meeting) {
        this.meeting = meeting;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SearchQuery that = (SearchQuery) o;
        return project == that.project && meeting == that.meeting && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(project, meeting, text);
    }

    @Override
    public String toString() {
        return "SearchQuery{" +
                "project=" + project +
                ", meeting=" + meeting +
                ", text='" + text + '\'' +
                '}';
    }
}
