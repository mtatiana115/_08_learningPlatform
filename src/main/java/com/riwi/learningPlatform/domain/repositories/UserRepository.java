package com.riwi.learningPlatform.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.riwi.learningPlatform.domain.entities.User;

public interface UserRepository extends JpaRepository <User, Long> { 

}
