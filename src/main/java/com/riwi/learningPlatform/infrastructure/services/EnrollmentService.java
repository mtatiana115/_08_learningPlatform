package com.riwi.learningPlatform.infrastructure.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.riwi.learningPlatform.api.dto.request.EnrollmentReq;
import com.riwi.learningPlatform.api.dto.response.CourseResp;
import com.riwi.learningPlatform.api.dto.response.EnrollmentResp;
import com.riwi.learningPlatform.api.dto.response.UserResp;
import com.riwi.learningPlatform.domain.entities.Course;
import com.riwi.learningPlatform.domain.entities.Enrollment;
import com.riwi.learningPlatform.domain.entities.User;
import com.riwi.learningPlatform.domain.repositories.CourseRepository;
import com.riwi.learningPlatform.domain.repositories.EnrollmentRepository;
import com.riwi.learningPlatform.domain.repositories.UserRepository;
import com.riwi.learningPlatform.infrastructure.abstract_services.IEnrollmentService;
import com.riwi.learningPlatform.util.enums.SortType;
import com.riwi.learningPlatform.util.exceptions.BadRequestException;
import com.riwi.learningPlatform.util.messages.ErrorMessages;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class EnrollmentService implements IEnrollmentService {
  
  @Autowired
  private final EnrollmentRepository enrollmentRepository;
      @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final CourseRepository courseRepository;
  
@Override
  public EnrollmentResp create(EnrollmentReq request) {
    Course course = courseRepository.findById(request.getCourseId())
            .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("course")));
    User student = userRepository.findById(request.getStudentId())
            .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("user")));
    Enrollment enrollment = new Enrollment();
    enrollment.setCourse(course);
    enrollment.setStudent(student);

    return this.entityToResp(enrollmentRepository.save(enrollment));
  }

  @Override
  public EnrollmentResp get(Long id) {
    return this.entityToResp(this.find(id));
  }

  @Override
  public EnrollmentResp update(EnrollmentReq request, Long id) {
    Enrollment existingEnrollment = this.find(id);
    Enrollment updatedEnrollment = this.requestToEntity(request);
    updatedEnrollment.setId(existingEnrollment.getId());
    return this.entityToResp(this.enrollmentRepository.save(updatedEnrollment));
}

  @Override
  public void delete(Long id) {
    this.enrollmentRepository.delete(this.find(id));
  }

  @Override
  public Page<EnrollmentResp> getAll(int page, int size, SortType sortType) {
      if (page < 0) page = 0;

    PageRequest pagination = null;

    //validar de que tipo es el sortType
    switch (sortType) {
      case NONE -> pagination = PageRequest.of(page, size);
      case ASC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
      case DESC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        
    }

    return this.enrollmentRepository.findAll(pagination).map(this::entityToResp);
  }

  private EnrollmentResp entityToResp(Enrollment entity){
    return EnrollmentResp.builder()
        .id(entity.getId())
        .date(entity.getDate())
        .courseResp(courseResp(entity.getCourse()))
        .userResp(userResp(entity.getStudent()))
        .build();
          
  }

  private CourseResp courseResp (Course entity){
    return CourseResp.builder()
        .id(entity.getId())
        .courseName(entity.getCourseName())
        .description(entity.getDescription())
        .build();
  }

  private UserResp userResp (User entity){
    return UserResp.builder()
        .id(entity.getId())
        .username(entity.getUsername())
        .email(entity.getEmail())
        .fullName(entity.getFullName())
        .roleUser(entity.getRoleUser())
        .build();
  }

  private Enrollment requestToEntity(EnrollmentReq request) {
    Course course = courseRepository.findById(request.getCourseId())
            .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("course")));
    User student = userRepository.findById(request.getStudentId())
            .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("user")));

    return Enrollment.builder()
            .course(course)
            .student(student)
            .build();
}


  private Enrollment find(Long id){
    return this.enrollmentRepository.findById(id)
      .orElseThrow(() -> new BadRequestException(ErrorMessages.
      idNotFound("enrollment")));
  }

}
