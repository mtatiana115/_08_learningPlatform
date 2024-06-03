package com.riwi.learningPlatform.api.dto.request;

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
public class CourseReq {

  @NotBlank(message = "Course name required.")
  private String courseName;

  private String description;
  
  @NotNull(message = "Id instructor is required")
  private Long instructorId;
}
