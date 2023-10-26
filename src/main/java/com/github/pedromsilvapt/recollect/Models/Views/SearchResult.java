package com.github.pedromsilvapt.recollect.Models.Views;

import java.util.List;

public class SearchResult {
    private int score;
    private int index;
    private long videoId;
    private String videoThumbnail;
    private String videoTitle;
    private String videoDuration;
    private String videoDate;
    private String project;
    private String meeting;
    private List<SearchResultMatch> matches;

    public SearchResult(int score, int index, long videoId, String videoThumbnail, String videoTitle, String videoDuration, String videoDate, String project, String meeting, List<SearchResultMatch> matches) {
        this.score = score;
        this.index = index;
        this.videoId = videoId;
        this.videoThumbnail = videoThumbnail;
        this.videoTitle = videoTitle;
        this.videoDuration = videoDuration;
        this.videoDate = videoDate;
        this.project = project;
        this.meeting = meeting;
        this.matches = matches;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public long getVideoId() {
        return videoId;
    }

    public void setVideoId(long videoId) {
        this.videoId = videoId;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
    }

    public List<SearchResultMatch> getMatches() {
        return matches;
    }

    public void setMatches(List<SearchResultMatch> matches) {
        this.matches = matches;
    }

    public String getVideoDate() {
        return videoDate;
    }

    public void setVideoDate(String videoDate) {
        this.videoDate = videoDate;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }

    public String getMeeting() {
        return meeting;
    }

    public void setMeeting(String meeting) {
        this.meeting = meeting;
    }
}
