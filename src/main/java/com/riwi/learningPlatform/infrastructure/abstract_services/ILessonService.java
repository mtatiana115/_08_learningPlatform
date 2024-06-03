package com.riwi.learningPlatform.infrastructure.abstract_services;

import com.riwi.learningPlatform.api.dto.request.LessonReq;
import com.riwi.learningPlatform.api.dto.response.LessonResp;
import com.riwi.learningPlatform.api.dto.response.LessonRespWithAssignments;

public interface ILessonService extends CrudService <LessonReq,LessonResp,Long> {

  public final String FIELD_BY_SORT = "lessonTitle";

  public LessonRespWithAssignments getLessonWithAssignments (Long id);
}
