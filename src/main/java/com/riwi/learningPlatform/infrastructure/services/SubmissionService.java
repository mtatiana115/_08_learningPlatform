package com.riwi.learningPlatform.infrastructure.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.riwi.learningPlatform.api.dto.request.SubmissionReq;
import com.riwi.learningPlatform.api.dto.response.SubmissionResp;
import com.riwi.learningPlatform.domain.entities.Assignment;
import com.riwi.learningPlatform.domain.entities.Submission;
import com.riwi.learningPlatform.domain.entities.User;
import com.riwi.learningPlatform.domain.repositories.AssignmentRepository;
import com.riwi.learningPlatform.domain.repositories.SubmissionRepository;
import com.riwi.learningPlatform.domain.repositories.UserRepository;
import com.riwi.learningPlatform.infrastructure.abstract_services.ISubmissionService;
import com.riwi.learningPlatform.util.enums.SortType;
import com.riwi.learningPlatform.util.exceptions.BadRequestException;
import com.riwi.learningPlatform.util.messages.ErrorMessages;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class SubmissionService implements ISubmissionService {
  
@Autowired
private final SubmissionRepository submissionRepository;

@Autowired
private final AssignmentRepository assignmentRepository;

@Autowired
private final UserRepository userRepository;

  @Override
  public SubmissionResp create(SubmissionReq request) {
    Assignment assignment = assignmentRepository.findById(request.getAssignmentId())
    .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("assignment")));
    User user = userRepository.findById(request.getUserId())
    .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("user")));
    Submission submission = this.requestToEntity(request);
    submission.setAssignment(assignment);
    submission.setUser(user);
    return this.entityToResp(this.submissionRepository.save(submission));

  }

  @Override
  public SubmissionResp get(Long id) {
    return this.entityToResp(this.find(id));
  }

  @Override
  public SubmissionResp update(SubmissionReq request, Long id) {
    Submission submissionFound = this.find(id);
    Submission submission = this.requestToEntity(request);
    submission.setId(submissionFound.getId());
    return this.entityToResp(this.submissionRepository.save(submission));
  }

  @Override
  public void delete(Long id) {
    this.submissionRepository.delete(this.find(id));
  }

  @Override
  public Page<SubmissionResp> getAll(int page, int size, SortType sortType) {
  if (page < 0) page = 0;

    PageRequest pagination = null;

    switch (sortType) {
      case NONE -> pagination = PageRequest.of(page, size);
      case ASC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
      case DESC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
      default -> throw new IllegalArgumentException("No valid sort: " + sortType);
    }
    return this.submissionRepository.findAll(pagination).map(this::entityToResp);
  }

  private SubmissionResp entityToResp (Submission entity){
    return SubmissionResp.builder()
          .id(entity.getId())
          .content(entity.getContent())
          .submissionDate(entity.getSubmissionDate())
          .grade(entity.getGrade())
          .build();
  }

  private Submission requestToEntity (SubmissionReq request){
    return Submission.builder()
        .content(request.getContent())
        .grade(request.getGrade())
        .build();
  }

  private Submission find (Long id){
    return this.submissionRepository.findById(id)
    .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("submission")));
  }

}
