package com.riwi.learningPlatform.api.dto.response;

import com.riwi.learningPlatform.util.enums.RoleUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResp {

  private Long id;

  private String username;

  private String email;

  private String fullName;

  private RoleUser roleUser;
  
}
