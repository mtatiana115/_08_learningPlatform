package com.riwi.learningPlatform.infrastructure.abstract_services;

import com.riwi.learningPlatform.api.dto.request.EnrollmentReq;
import com.riwi.learningPlatform.api.dto.response.EnrollmentResp;

public interface IEnrollmentService extends CrudService <EnrollmentReq, EnrollmentResp, Long> {

  public final String FIELD_BY_SORT = "date";

}
