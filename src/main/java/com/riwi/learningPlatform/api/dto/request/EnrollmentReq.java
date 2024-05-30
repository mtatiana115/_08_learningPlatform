package com.riwi.learningPlatform.api.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentReq {

  @NotNull(message = "Id required")
  private Long studentId;
  
  @NotNull(message = "Id required")
  private Long courseId;
}
