package com.riwi.learningPlatform.domain.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.riwi.learningPlatform.domain.entities.User;
import com.riwi.learningPlatform.util.enums.RoleUser;

@Repository
public interface UserRepository extends JpaRepository <User, Long> { 

  public Optional<User> findByIdAndRoleUser(Long id, RoleUser roleUser);
}
