package com.riwi.learningPlatform.infrastructure.abstract_services;

import com.riwi.learningPlatform.api.dto.request.UserReq;
import com.riwi.learningPlatform.api.dto.response.UserResp;
import com.riwi.learningPlatform.api.dto.response.UserRespWithCourses;
import com.riwi.learningPlatform.api.dto.response.UserRespWithSubmissions;

public interface IUserService extends CrudService <UserReq, UserResp, Long> {

  public final String FIELD_BY_SORT = "userName";

  public UserRespWithCourses getUsersWithCourses(Long id);

  public UserRespWithSubmissions getUserWithSubmissions(Long id);
}
