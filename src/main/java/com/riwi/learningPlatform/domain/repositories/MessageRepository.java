package com.riwi.learningPlatform.domain.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.riwi.learningPlatform.domain.entities.Message;

public interface MessageRepository extends JpaRepository <Message, Long>{

}
