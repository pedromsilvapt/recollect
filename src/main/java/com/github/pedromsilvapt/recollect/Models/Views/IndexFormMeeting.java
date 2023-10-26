package com.github.pedromsilvapt.recollect.Models.Views;

/**
 * Model Class for the Index & Search pages, containing the data to pre-fill the meetings select box when rendering the templates
 */
public class IndexFormMeeting {
    private long id;

    private String name;

    private String selected;

    public IndexFormMeeting(long id, String name, String selected) {
        this.id = id;
        this.name = name;
        this.selected = selected;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelected() {
        return selected;
    }

    public void setSelected(String selected) {
        this.selected = selected;
    }
}
