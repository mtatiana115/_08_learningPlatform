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
public class SubmissionResp {

  private Long id;
  
  private String content;

  private LocalDate submissionDate;
  
  private double grade;

}
