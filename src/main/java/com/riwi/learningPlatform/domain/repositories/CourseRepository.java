package com.riwi.learningPlatform.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.riwi.learningPlatform.domain.entities.Course;

public interface CourseRepository extends JpaRepository <Course, Long> {

}
