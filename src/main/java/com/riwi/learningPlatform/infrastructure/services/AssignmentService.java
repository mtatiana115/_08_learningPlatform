package com.riwi.learningPlatform.infrastructure.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.riwi.learningPlatform.api.dto.request.AssignmentReq;
import com.riwi.learningPlatform.api.dto.response.AssignmentResp;
import com.riwi.learningPlatform.api.dto.response.AssignmentRespWithSubmissions;
import com.riwi.learningPlatform.api.dto.response.SubmissionResp;
import com.riwi.learningPlatform.domain.entities.Assignment;
import com.riwi.learningPlatform.domain.entities.Lesson;
import com.riwi.learningPlatform.domain.entities.Submission;
import com.riwi.learningPlatform.domain.repositories.AssignmentRepository;
import com.riwi.learningPlatform.domain.repositories.LessonRepository;
import com.riwi.learningPlatform.domain.repositories.SubmissionRepository;
import com.riwi.learningPlatform.infrastructure.abstract_services.IAssignmentService;
import com.riwi.learningPlatform.util.enums.SortType;
import com.riwi.learningPlatform.util.exceptions.BadRequestException;
import com.riwi.learningPlatform.util.messages.ErrorMessages;


import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class AssignmentService implements IAssignmentService {
  
  @Autowired
  private final AssignmentRepository assignmentRepository;

  @Autowired
  private final LessonRepository lessonRepository;

  @Autowired
  private final SubmissionRepository submissionRepository;
  
  @Override
  public AssignmentResp create(AssignmentReq request) {
    Lesson lesson = lessonRepository.findById(request.getLessonId())
            .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("lesson")));
    Assignment assignment = this.requestToEntity(request);
    assignment.setLesson(lesson);
    return this.entityToResp(this.assignmentRepository.save(assignment));
  }

  @Override
  public AssignmentResp get(Long id) {
    return this.entityToResp(this.find(id));
  }

  @Override
  public AssignmentResp update(AssignmentReq request, Long id) {
    Assignment assignmentFound = this.find(id);
    Assignment assignment = this.requestToEntity(request);
    assignment.setId(assignmentFound.getId());
    return this.entityToResp(this.assignmentRepository.save(assignment));
  }

  @Override
  public void delete(Long id) {
    this.assignmentRepository.delete(this.find(id));
  }

  @Override
  public Page<AssignmentResp> getAll(int page, int size, SortType sortType) {
        if (page < 0) page = 0;

    PageRequest pagination = null;

    //validar de que tipo es el sortType
    switch (sortType) {
      case NONE -> pagination = PageRequest.of(page, size);
      case ASC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
      case DESC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        
    }

    return this.assignmentRepository.findAll(pagination).map(this::entityToResp);
  }

  private AssignmentResp entityToResp (Assignment entity) {
    return AssignmentResp.builder()
          .id(entity.getId())
          .assignmentTitle(entity.getAssignmentTitle())
          .description(entity.getDescription())
          .dueDate(entity.getDueDate())
          .build();
  }

  private Assignment requestToEntity (AssignmentReq request) {
    return Assignment.builder()
            .assignmentTitle(request.getAssignmentTitle())
            .description(request.getDescription())
            .dueDate(request.getDueDate())
            .build();
  }

  private Assignment find (Long id){
    return this.assignmentRepository.findById(id)
    .orElseThrow( () -> new BadRequestException(ErrorMessages.idNotFound("assignment")));
  }

  private SubmissionResp submissionResp (Submission entity) {
    return SubmissionResp.builder()
    .id(entity.getId())
    .content(entity.getContent())
    .submissionDate(entity.getSubmissionDate())
    .grade(entity.getGrade())
    .build();
  }

  @Override
  public AssignmentRespWithSubmissions getActivityWithSubmissions(Long id) {
    List<SubmissionResp> submissions = submissionRepository.findById(id).stream()
    .map(submission -> this.submissionResp(submission)).toList();
    Assignment assignment = find(id);
    return AssignmentRespWithSubmissions.builder()
    .id(assignment.getId())
    .assignmentTitle(assignment.getAssignmentTitle())
    .description(assignment.getDescription())
    .dueDate(assignment.getDueDate())
    .submissions(submissions)
    .build();
  }
}
