package com.github.pedromsilvapt.recollect.Repositories;

import com.github.pedromsilvapt.recollect.Models.Recording;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RecordingRepository extends JpaRepository<Recording, Long> {
    @Query(value = "SELECT rec FROM  Recording rec LEFT JOIN FETCH rec.lines LEFT JOIN FETCH rec.project LEFT JOIN FETCH rec.meeting WHERE rec.project.projectId = ?1 AND rec.meeting.meetingId = ?2")
    List<Recording> findForProjectMeetingWithLines(long projectId, long meetingId);
}
