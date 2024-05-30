package com.riwi.learningPlatform.infrastructure.abstract_services;

import com.riwi.learningPlatform.api.dto.request.UserReq;
import com.riwi.learningPlatform.api.dto.response.UserResp;

public interface IUserService extends CrudService <UserReq, UserResp, Long> {

  public final String FIELD_BY_SORT = "userName";
}
