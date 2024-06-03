package com.riwi.learningPlatform.domain.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.riwi.learningPlatform.domain.entities.Lesson;

@Repository
public interface LessonRepository extends JpaRepository <Lesson, Long> {

  public List<Lesson> findByCourseId(Long id);
}
