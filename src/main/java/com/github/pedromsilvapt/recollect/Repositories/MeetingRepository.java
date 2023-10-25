package com.github.pedromsilvapt.recollect.Repositories;

import com.github.pedromsilvapt.recollect.Models.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

}
