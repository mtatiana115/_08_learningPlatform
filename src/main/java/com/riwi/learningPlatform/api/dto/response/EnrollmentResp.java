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
public class EnrollmentResp {

  private Long id;

  private LocalDate date;

  private CourseResp courseResp;

  private UserResp userResp;

}
