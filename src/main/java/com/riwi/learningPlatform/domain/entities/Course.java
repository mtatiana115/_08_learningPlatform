package com.riwi.learningPlatform.domain.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity(name = "course")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String courseName;

  @Lob
  private String description;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "instructor_id",
    referencedColumnName = "id"
  )
  private User instructor;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "course",
  fetch = FetchType.LAZY,
  cascade = CascadeType.ALL,
  orphanRemoval = false
  )
  private List<Enrollment> enrollments;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "course",
  fetch = FetchType.LAZY,
  cascade = CascadeType.ALL,
  orphanRemoval = false
  )
  private List<Message> messages;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "course",
  fetch = FetchType.LAZY,
  cascade = CascadeType.ALL,
  orphanRemoval = false
  )
  private List<Lesson> lessons;

}
