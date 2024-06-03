package com.riwi.learningPlatform.domain.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.riwi.learningPlatform.domain.entities.Assignment;

@Repository
public interface AssignmentRepository extends JpaRepository <Assignment, Long> {

  public List<Assignment> findByLessonId (Long id);

}
