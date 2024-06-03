package com.riwi.learningPlatform.api.dto.response;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssignmentRespWithSubmissions {

  private Long id;
  private String assignmentTitle;
  private String description;
  private LocalDate dueDate;
  private List<SubmissionResp> submissions;
}
