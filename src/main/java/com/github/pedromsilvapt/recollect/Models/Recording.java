package com.github.pedromsilvapt.recollect.Models;

import jakarta.persistence.*;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Recording represents a file stored on the database
 */
@Entity
public class Recording {
    @Id
    @GeneratedValue
    private Long recordingId;
    private String title;
    private Duration duration;
    private String filePath;
    private Instant date;
    private RecordingProcessingState processingState;
    @ManyToOne
    @JoinColumn(name="project_id")
    private Project project;
    @ManyToOne
    @JoinColumn(name="meeting_id")
    private Meeting meeting;
    @OneToMany(mappedBy = "recording")
    private List<RecordingLine> lines;

    public Recording() {
    }

    public Recording(String title, Duration duration, String filePath, Instant date, RecordingProcessingState processingState, Project project, Meeting meeting) {
        this.title = title;
        this.duration = duration;
        this.filePath = filePath;
        this.date = date;
        this.processingState = processingState;
        this.project = project;
        this.meeting = meeting;
    }

    public Long getRecordingId() {
        return recordingId;
    }

    public void setRecordingId(Long recordingId) {
        this.recordingId = recordingId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public RecordingProcessingState getProcessingState() {
        return processingState;
    }

    public void setProcessingState(RecordingProcessingState processingState) {
        this.processingState = processingState;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public List<RecordingLine> getLines() {
        return lines;
    }

    public void setLines(List<RecordingLine> lines) {
        this.lines = lines;
    }

    @Override
    public String toString() {
        return "Recording{" +
                "recordingId=" + recordingId +
                ", title='" + title + '\'' +
                ", duration=" + duration +
                ", filePath='" + filePath + '\'' +
                ", date=" + date +
                ", processingState=" + processingState +
                ", project=" + project +
                ", meeting=" + meeting +
                ", lines=" + lines +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recording recording = (Recording) o;
        return Objects.equals(recordingId, recording.recordingId) && Objects.equals(title, recording.title) && Objects.equals(duration, recording.duration) && Objects.equals(filePath, recording.filePath) && Objects.equals(date, recording.date) && processingState == recording.processingState && Objects.equals(project, recording.project) && Objects.equals(meeting, recording.meeting) && Objects.equals(lines, recording.lines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordingId, title, duration, filePath, date, processingState, project, meeting, lines);
    }

}
