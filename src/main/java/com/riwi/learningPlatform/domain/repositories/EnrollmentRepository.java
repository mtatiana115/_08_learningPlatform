package com.riwi.learningPlatform.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.riwi.learningPlatform.domain.entities.Enrollment;

public interface EnrollmentRepository extends JpaRepository <Enrollment, Long> {

}
