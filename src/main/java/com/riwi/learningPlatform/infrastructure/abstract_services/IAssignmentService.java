package com.riwi.learningPlatform.infrastructure.abstract_services;

import com.riwi.learningPlatform.api.dto.request.AssignmentReq;
import com.riwi.learningPlatform.api.dto.response.AssignmentResp;
import com.riwi.learningPlatform.api.dto.response.AssignmentRespWithSubmissions;

public interface IAssignmentService extends CrudService <AssignmentReq, AssignmentResp, Long> {
  
  public final String FIELD_BY_SORT = "assignmentTitle";

  public AssignmentRespWithSubmissions getActivityWithSubmissions(Long id);
}
