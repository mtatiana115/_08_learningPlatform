package com.riwi.learningPlatform.api.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentResp {

  private Long id;

  private String assignmentTitle;

  private String description;

  private LocalDate dueDate;
}
