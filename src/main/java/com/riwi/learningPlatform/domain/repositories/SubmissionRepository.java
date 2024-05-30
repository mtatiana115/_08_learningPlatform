package com.riwi.learningPlatform.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.riwi.learningPlatform.domain.entities.Submission;

public interface SubmissionRepository extends JpaRepository <Submission, Long> {

}
