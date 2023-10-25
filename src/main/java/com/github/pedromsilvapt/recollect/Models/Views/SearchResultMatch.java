package com.github.pedromsilvapt.recollect.Models.Views;

import java.time.Duration;

public class SearchResultMatch {
    private int index;
    private String startTime;
    private String endTime;
    private String text;
    private boolean hasGap;

    public SearchResultMatch(int index, String startTime, String endTime, String text, boolean hasGap) {
        this.index = index;
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = text;
        this.hasGap = hasGap;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isHasGap() {
        return hasGap;
    }

    public void setHasGap(boolean hasGap) {
        this.hasGap = hasGap;
    }
}
