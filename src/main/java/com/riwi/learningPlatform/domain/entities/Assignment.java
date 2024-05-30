package com.riwi.learningPlatform.domain.entities;

import java.time.LocalDate;
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
@Entity(name = "assignment")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Assignment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String assignmentTitle;

  @Lob
  private String description;

  private LocalDate dueDate;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(
    name = "lesson_id",
    referencedColumnName = "id"
  )
  private Lesson lesson;

  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @OneToMany(mappedBy = "assignment",
  fetch = FetchType.LAZY,
  cascade = CascadeType.ALL,
  orphanRemoval = false
  )
  private List<Submission> submissions; 

}
