package com.github.pedromsilvapt.recollect.Repositories;

import com.github.pedromsilvapt.recollect.Models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
