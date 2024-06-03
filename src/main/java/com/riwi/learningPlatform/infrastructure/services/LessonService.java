package com.riwi.learningPlatform.infrastructure.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.riwi.learningPlatform.api.dto.request.LessonReq;
import com.riwi.learningPlatform.api.dto.response.AssignmentResp;
import com.riwi.learningPlatform.api.dto.response.LessonResp;
import com.riwi.learningPlatform.api.dto.response.LessonRespWithAssignments;
import com.riwi.learningPlatform.domain.entities.Assignment;
import com.riwi.learningPlatform.domain.entities.Course;
import com.riwi.learningPlatform.domain.entities.Lesson;
import com.riwi.learningPlatform.domain.repositories.AssignmentRepository;
import com.riwi.learningPlatform.domain.repositories.CourseRepository;
import com.riwi.learningPlatform.domain.repositories.LessonRepository;
import com.riwi.learningPlatform.infrastructure.abstract_services.ILessonService;
import com.riwi.learningPlatform.util.enums.SortType;
import com.riwi.learningPlatform.util.exceptions.BadRequestException;
import com.riwi.learningPlatform.util.messages.ErrorMessages;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class LessonService implements ILessonService {
  @Autowired
  private final LessonRepository lessonRepository;

  @Autowired
  private final CourseRepository courseRepository;

  @Autowired
  private final AssignmentRepository assignmentRepository;

  @Override
  public LessonResp create(LessonReq request) {
    Course course = courseRepository.findById(request.getCourseId())
            .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("course")));
    Lesson lesson = this.requestToEntity(request);
    lesson.setCourse(course);
    return this.entityToResp(this.lessonRepository.save(lesson));
  }

  @Override
  public LessonResp get(Long id) {
    return this.entityToResp(this.find(id));
  }

  @Override
  public LessonResp update(LessonReq request, Long id) {
    Lesson lessonFound = this.find(id);
    Lesson lesson = this.requestToEntity(request);
    lesson.setId(id);
    lesson.setCourse(lessonFound.getCourse());
    return this.entityToResp(this.lessonRepository.save(lesson));
  }

  @Override
  public void delete(Long id) {
    this.lessonRepository.delete(this.find(id));
  }

  @Override
  public Page<LessonResp> getAll(int page, int size, SortType sortType) {
      if (page < 0) page = 0;

    PageRequest pagination = null;

    //validar de que tipo es el sortType
    switch (sortType) {
      case NONE -> pagination = PageRequest.of(page, size);
      case ASC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
      case DESC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        
    }

    return this.lessonRepository.findAll(pagination).map(this::entityToResp);
  }

    private LessonResp entityToResp(Lesson entity){
    return LessonResp.builder()
          .id(entity.getId())
          .content(entity.getContent())
          .lessonTitle(entity.getLessonTitle())
          .build();
  }

  private Lesson requestToEntity(LessonReq request){
    //sintaxis con el builder
    Course course = courseRepository.findById(request.getCourseId())
            .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("course")));
    return Lesson.builder()
    .course(course)
    .lessonTitle(request.getLessonTitle())
    .content(request.getContent())
    .build();
  }


    private Lesson find(Long id){
    return this.lessonRepository.findById(id)
      .orElseThrow(() -> new BadRequestException(ErrorMessages.
      idNotFound("lesson")));
  }

  private AssignmentResp AssigmentResp (Assignment assignment){
    return AssignmentResp.builder()
        .id(assignment.getId())
        .assignmentTitle(assignment.getAssignmentTitle())
        .description(assignment.getDescription())
        .dueDate(assignment.getDueDate())
        .build();
  }

    @Override
    public LessonRespWithAssignments getLessonWithAssignments(Long id) {
      List<AssignmentResp> assignments = assignmentRepository.findByLessonId(id).stream()
            .map(assignment -> this.AssigmentResp(assignment)).toList();
      Lesson lesson = find(id);

      return LessonRespWithAssignments.builder()
          .id(lesson.getId())
          .lessonTitle(lesson.getLessonTitle())
          .content(lesson.getContent())
          .assignments(assignments)
          .build();
    }
}
