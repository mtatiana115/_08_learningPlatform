package com.riwi.learningPlatform.infrastructure.services;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.riwi.learningPlatform.api.dto.request.UserReq;
import com.riwi.learningPlatform.api.dto.response.CourseResp;
import com.riwi.learningPlatform.api.dto.response.EnrollmentResp;
import com.riwi.learningPlatform.api.dto.response.SubmissionResp;
import com.riwi.learningPlatform.api.dto.response.UserResp;
import com.riwi.learningPlatform.api.dto.response.UserRespWithCourses;
import com.riwi.learningPlatform.api.dto.response.UserRespWithSubmissions;
import com.riwi.learningPlatform.domain.entities.Course;
import com.riwi.learningPlatform.domain.entities.Enrollment;
import com.riwi.learningPlatform.domain.entities.Submission;
import com.riwi.learningPlatform.domain.entities.User;
import com.riwi.learningPlatform.domain.repositories.EnrollmentRepository;
import com.riwi.learningPlatform.domain.repositories.SubmissionRepository;
import com.riwi.learningPlatform.domain.repositories.UserRepository;
import com.riwi.learningPlatform.infrastructure.abstract_services.IUserService;
import com.riwi.learningPlatform.util.enums.SortType;
import com.riwi.learningPlatform.util.exceptions.BadRequestException;
import com.riwi.learningPlatform.util.messages.ErrorMessages;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class UserService implements IUserService{

  @Autowired
  private final UserRepository userRepository;

  @Autowired
  private final EnrollmentRepository enrollmentRepository;

  @Autowired
  private final SubmissionRepository submissionRepository;

  @Override
  public UserResp create(UserReq request) {
    User user = this.requestToEntity(request);
    return this.entityToResp(this.userRepository.save(user));
  }

  @Override
  public UserResp get(Long id) {
    return this.entityToResp(this.find(id));
  }

  @Override
  public UserResp update(UserReq request, Long id) {
        //validar que existe el id
        User user = this.find(id);
        //convertir el request a una entidad
        user = this.requestToEntity(request);
        user.setId(id);
        //gusradar el id en un servicio para que se actualice en vez de agregar
        return this.entityToResp(this.userRepository.save(user));
  }

  @Override
  public void delete(Long id) {
    this.userRepository.delete(this.find(id));
  }

  @Override
  public Page<UserResp> getAll(int page, int size, SortType sortType) {
      if (page < 0) page = 0;

    PageRequest pagination = null;

    //validar de que tipo es el sortType
    switch (sortType) {
      case NONE -> pagination = PageRequest.of(page, size);
      case ASC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
      case DESC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        
    }

    return this.userRepository.findAll(pagination).map(this::entityToResp);
  }

  private UserResp entityToResp(User entity){
    return UserResp.builder()
          .id(entity.getId())
          .username(entity.getUsername())
          .email(entity.getEmail())
          .fullName(entity.getFullName())
          .roleUser(entity.getRoleUser())
          .build();
  }

  private User requestToEntity(UserReq request){
    //sintaxis con el builder
    return User.builder()
    .username(request.getUserName())
    .password(request.getPassword())
    .email(request.getEmail())
    .fullName(request.getFullName())
    .roleUser(request.getRoleUser())
    .build();
  }


    private User find(Long id){
    return this.userRepository.findById(id)
      .orElseThrow(() -> new BadRequestException(ErrorMessages.
      idNotFound("user")));
  }

    @Override
    public UserRespWithCourses getUsersWithCourses(Long id) {
      List<EnrollmentResp> enrollments = enrollmentRepository.findByStudentId(id).stream()
      .map(enrollemnt -> this.enrollmentToResp(enrollemnt)).toList();

      User student = find(id);
      return UserRespWithCourses.builder()
          .id(student.getId())
          .userName(student.getUsername())
          .email(student.getEmail())
          .fullname(student.getFullName())
          .roleUser(student.getRoleUser())
          .courses(enrollments)
          .build();
    }

    @Override
    public UserRespWithSubmissions getUserWithSubmissions(Long id) {

      List<SubmissionResp> submissions = submissionRepository.findByUserId(id).stream()
          .map(submission -> this.submissionToResp(submission)).toList();
      
          User user = find(id);

          return UserRespWithSubmissions.builder()
              .id(user.getId())
              .userName(user.getUsername())
              .email(user.getEmail())
              .fullname(user.getFullName())
              .roleUser(user.getRoleUser())
              .submissions(submissions)
              .build();
    }

    private EnrollmentResp enrollmentToResp(Enrollment entity){
    return EnrollmentResp.builder()
        .id(entity.getId())
        .date(entity.getDate())
        .courseResp(courseToResp(entity.getCourse()))
        .userResp(entityToResp(entity.getStudent()))
        .build();
          
  }
    private CourseResp courseToResp(Course entity) {
        return CourseResp.builder()
                .id(entity.getId())
                .courseName(entity.getCourseName())
                .description(entity.getDescription())
                .instructor(userToResp(entity.getInstructor()))
                .build();
    }

    private UserResp userToResp(User entity) {
      return UserResp.builder()
              .id(entity.getId())
              .username(entity.getUsername())
              .email(entity.getEmail())
              .fullName(entity.getFullName())
              .roleUser(entity.getRoleUser())
              .build();
  }

    private SubmissionResp submissionToResp (Submission entity){
    return SubmissionResp.builder()
          .id(entity.getId())
          .content(entity.getContent())
          .submissionDate(entity.getSubmissionDate())
          .grade(entity.getGrade())
          .build();
  }

}
