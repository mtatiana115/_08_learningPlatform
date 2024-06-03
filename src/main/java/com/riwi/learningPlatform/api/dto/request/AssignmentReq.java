package com.riwi.learningPlatform.api.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentReq {

  @NotBlank(message = "Assignment title required.")
  private String assignmentTitle;

  private String description;

  @FutureOrPresent(message = "The date cannot be in the past.")
  private LocalDate dueDate;
  
  @NotNull(message = "Id  lesson is required.")
  private Long lessonId;
}
