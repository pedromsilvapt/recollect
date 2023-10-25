package com.github.pedromsilvapt.recollect.Models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.List;
import java.util.Objects;

@Entity
public class Meeting {
    @Id
    @GeneratedValue
    private Long meetingId;
    private String name;
    @OneToMany(mappedBy = "meeting")
    private List<Recording> recordings;

    public Meeting() {
    }

    public Meeting(String name) {
        this.name = name;
    }

    public Long getMeetingId() {
        return meetingId;
    }

    public void setMeetingId(Long meetingId) {
        this.meetingId = meetingId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Recording> getRecordings() {
        return recordings;
    }

    public void setRecordings(List<Recording> recordings) {
        this.recordings = recordings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Meeting meeting = (Meeting) o;
        return Objects.equals(meetingId, meeting.meetingId) && Objects.equals(name, meeting.name) && Objects.equals(recordings, meeting.recordings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meetingId, name, recordings);
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "meetingId=" + meetingId +
                ", name='" + name + '\'' +
                ", recordings=" + recordings +
                '}';
    }
}
