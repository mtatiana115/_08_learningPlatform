package com.riwi.learningPlatform.infrastructure.abstract_services;

import com.riwi.learningPlatform.api.dto.request.CourseReq;
import com.riwi.learningPlatform.api.dto.response.CourseResp;
import com.riwi.learningPlatform.api.dto.response.CourseRespWithLessons;
import com.riwi.learningPlatform.api.dto.response.CourseRespWithStudents;

public interface ICourseService extends CrudService <CourseReq, CourseResp, Long>{

  public final String FIELD_BY_SORT = "courseName";

    public CourseRespWithLessons getCourseWithLessons(Long id);

    public CourseRespWithStudents getCourseWithStudents(Long id);
}
