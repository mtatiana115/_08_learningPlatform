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
public class LessonReq {

  @NotBlank(message = "Lesson title required")
  private String lessonTitle;

  private String content;
  
  @NotNull(message = "Id required")
  private Long courseId;
}
