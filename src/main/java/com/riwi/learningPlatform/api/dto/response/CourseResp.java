package com.riwi.learningPlatform.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CourseResp {

  private Long id;

  private String courseName;

  private String description;
  
  private UserResp instructor;
}
