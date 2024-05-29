package com.riwi.learningPlatform.domain.entities;

import java.util.List;

import com.riwi.learningPlatform.util.enums.RoleUser;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity(name = "user")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 50)
  private String username;

  @Column(nullable = false, length = 255)
  private String password;
  
  @Column(nullable = false, length = 100)
  private String email;
  
  @Column(length = 100)
  private String fullName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private RoleUser roleUser;


  //Relaciones

  @OneToMany(mappedBy = "instructor",
  fetch = FetchType.LAZY,
  cascade = CascadeType.ALL,
  orphanRemoval = false
  )
  private List<Course> courses;

  @OneToMany(mappedBy = "user",
  fetch = FetchType.LAZY,
  cascade = CascadeType.ALL,
  orphanRemoval = false
  )
  private List<Submission> submission;

  @OneToMany(mappedBy = "student",
  fetch = FetchType.LAZY,
  cascade = CascadeType.ALL,
  orphanRemoval = false
  )
  private List<Enrollment> enrollments;

  @OneToMany(mappedBy = "sender",
  fetch = FetchType.LAZY,
  cascade = CascadeType.ALL,
  orphanRemoval = false
  )
  private List<Message> sentMessages;

  @OneToMany(mappedBy = "receiver",
  fetch = FetchType.LAZY,
  cascade = CascadeType.ALL,
  orphanRemoval = false
  )
  private List<Message> receivedMessages;
}
