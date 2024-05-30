package com.riwi.learningPlatform.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.riwi.learningPlatform.domain.entities.Lesson;

public interface LessonRepository extends JpaRepository <Lesson, Long> {

}
