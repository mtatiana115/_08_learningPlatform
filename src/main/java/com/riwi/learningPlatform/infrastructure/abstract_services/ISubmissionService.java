package com.riwi.learningPlatform.infrastructure.abstract_services;

import com.riwi.learningPlatform.api.dto.request.SubmissionReq;
import com.riwi.learningPlatform.api.dto.response.SubmissionResp;

public interface ISubmissionService extends CrudService <SubmissionReq, SubmissionResp, Long> {

  public final String FIELD_BY_SORT = "grade";

}
