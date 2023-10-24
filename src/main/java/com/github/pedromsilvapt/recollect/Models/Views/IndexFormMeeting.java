package com.github.pedromsilvapt.recollect.Models.Views;

/**
 * Model Class for the Index & Search pages, containing the data to pre-fill the meetings select box when rendering the templates
 */
public class IndexFormMeeting {
    private int id;

    private String name;

    private String selected;

    public IndexFormMeeting(int id, String name, String selected) {
        this.id = id;
        this.name = name;
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
