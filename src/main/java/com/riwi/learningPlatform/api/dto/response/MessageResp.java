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
public class MessageResp {

  private Long id;

  private String messageContent;
  
  private LocalDate sentDate;

  private UserResp sender;
  
  private UserResp receiver;
}
