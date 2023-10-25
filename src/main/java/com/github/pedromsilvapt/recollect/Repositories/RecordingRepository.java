package com.github.pedromsilvapt.recollect.Repositories;

import com.github.pedromsilvapt.recollect.Models.Recording;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordingRepository extends JpaRepository<Recording, Long> {

}
