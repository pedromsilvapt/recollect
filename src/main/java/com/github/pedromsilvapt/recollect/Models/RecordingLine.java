package com.github.pedromsilvapt.recollect.Models;

import jakarta.persistence.*;

import java.time.Duration;
import java.util.Objects;

@Entity
public class RecordingLine {
    @Id
    @GeneratedValue
    private Long recordingLineId;
    @ManyToOne
    @JoinColumn(name = "recording_id")
    private Recording recording;
    private int index;
    private Duration startTime;
    private Duration endTime;
    @Column(columnDefinition = "TEXT")
    private String text;

    public RecordingLine() {
    }

    public RecordingLine(Recording recording, int index, Duration startTime, Duration endTime, String text) {
        this.recording = recording;
        this.index = index;
        this.startTime = startTime;
        this.endTime = endTime;
        this.text = text;
    }

    public Long getRecordingLineId() {
        return recordingLineId;
    }

    public void setRecordingLineId(Long recordingLineId) {
        this.recordingLineId = recordingLineId;
    }

    public Recording getRecording() {
        return recording;
    }

    public void setRecording(Recording recording) {
        this.recording = recording;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Duration getStartTime() {
        return startTime;
    }

    public void setStartTime(Duration startTime) {
        this.startTime = startTime;
    }

    public Duration getEndTime() {
        return endTime;
    }

    public void setEndTime(Duration endTime) {
        this.endTime = endTime;
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
        RecordingLine that = (RecordingLine) o;
        return index == that.index && Objects.equals(recordingLineId, that.recordingLineId) && Objects.equals(recording, that.recording) && Objects.equals(startTime, that.startTime) && Objects.equals(endTime, that.endTime) && Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(recordingLineId, recording, index, startTime, endTime, text);
    }

    @Override
    public String toString() {
        return "RecordingLine{" +
                "recordingLineId=" + recordingLineId +
                ", recording=" + recording +
                ", index=" + index +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", text='" + text + '\'' +
                '}';
    }
}
