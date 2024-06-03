package com.riwi.learningPlatform.infrastructure.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.riwi.learningPlatform.api.dto.request.CourseReq;
import com.riwi.learningPlatform.api.dto.response.CourseResp;
import com.riwi.learningPlatform.api.dto.response.CourseRespWithLessons;
import com.riwi.learningPlatform.api.dto.response.CourseRespWithStudents;
import com.riwi.learningPlatform.api.dto.response.EnrollmentResp;
import com.riwi.learningPlatform.api.dto.response.LessonResp;
import com.riwi.learningPlatform.api.dto.response.UserResp;
import com.riwi.learningPlatform.domain.entities.Course;
import com.riwi.learningPlatform.domain.entities.Enrollment;
import com.riwi.learningPlatform.domain.entities.Lesson;
import com.riwi.learningPlatform.domain.entities.User;
import com.riwi.learningPlatform.domain.repositories.CourseRepository;
import com.riwi.learningPlatform.domain.repositories.EnrollmentRepository;
import com.riwi.learningPlatform.domain.repositories.LessonRepository;
import com.riwi.learningPlatform.domain.repositories.UserRepository;
import com.riwi.learningPlatform.infrastructure.abstract_services.ICourseService;
import com.riwi.learningPlatform.util.enums.SortType;
import com.riwi.learningPlatform.util.exceptions.BadRequestException;
import com.riwi.learningPlatform.util.messages.ErrorMessages;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class CourseService implements ICourseService {
  
  @Autowired
  private final CourseRepository courseRepository;

  @Autowired
  private final UserRepository userRepository;

  @Autowired
  private final LessonRepository lessonRepository;

  @Autowired
  private final EnrollmentRepository enrollmentRepository;
  
  @Override
  public CourseResp create(CourseReq request) {
    User instructor = userRepository.findById(request.getInstructorId())
            .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("user")));
    Course course = this.requestToEntity(request);
    course.setInstructor(instructor);
    return this.entityToResp(this.courseRepository.save(course));
  }

  @Override
  public CourseResp get(Long id) {
    return this.entityToResp(this.find(id));
  }

  @Override
  public CourseResp update(CourseReq request, Long id) {

    Course courseFound = this.find(id);
    Course course = this.requestToEntity(request);
    course.setId(id);
    course.setInstructor(courseFound.getInstructor());

    return this.entityToResp(this.courseRepository.save(course));
  }

  @Override
  public void delete(Long id) {
    this.courseRepository.delete(this.find(id));
  }

  @Override
  public Page<CourseResp> getAll(int page, int size, SortType sortType) {
    if (page < 0) page = 0;

    PageRequest pagination = null;

    switch (sortType) {
      case NONE -> pagination = PageRequest.of(page, size);
      case ASC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
      case DESC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
      default -> throw new IllegalArgumentException("No valid sort: " + sortType);
    }
    return this.courseRepository.findAll(pagination).map(this::entityToResp);
      
  }

  private CourseResp entityToResp(Course entity) {
    return CourseResp.builder()
            .id(entity.getId())
            .courseName(entity.getCourseName())
            .description(entity.getDescription())
            .instructor(userToResp(entity.getInstructor()))
            .build();
}

private LessonResp lessonResp (Lesson entity){
  return LessonResp.builder()
          .id(entity.getId())
          .lessonTitle(entity.getLessonTitle())
          .content(entity.getContent())
          .build();
}

  private Course requestToEntity (CourseReq request) {
    return Course.builder()
            .courseName(request.getCourseName())
            .description(request.getDescription())
            .build();
  }

  private UserResp userToResp (User entity){
    return UserResp.builder()
        .id(entity.getId())
        .username(entity.getUsername())
        .email(entity.getEmail())
        .fullName(entity.getFullName())
        .roleUser(entity.getRoleUser())
        .build();
  }

  private EnrollmentResp enrollmentResp (Enrollment entity){
    return EnrollmentResp.builder()
        .id(entity.getId())
        .date(entity.getDate())
        .courseResp(entityToResp(entity.getCourse()))
        .userResp(userToResp(entity.getStudent()))
        .build();
  }

  private Course find(Long id){
    return this.courseRepository.findById(id)
        .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("course")));
  }


  @Override
  public CourseRespWithStudents getCourseWithStudents(Long id) {
    Course course = find(id);
    List<EnrollmentResp> enrollments = enrollmentRepository.findByCourseId(id).stream()
            .map(enrollment -> this.enrollmentResp(enrollment)).toList();


        return CourseRespWithStudents.builder()
        .id(course.getId())
        .courseName(course.getCourseName())
        .description(course.getDescription())
        .instructor(userToResp(course.getInstructor()))
        .students(enrollments)
        .build();
  }

  @Override
  public CourseRespWithLessons getCourseWithLessons(Long id) {
    List<LessonResp> lessons = lessonRepository.findByCourseId(id).stream()
                .map(lesson -> this.lessonResp(lesson)).toList();
    Course course = find(id);

    return CourseRespWithLessons.builder()
        .id(course.getId())
        .courseName(course.getCourseName())
        .description(course.getDescription())
        .instructor(this.userToResp(course.getInstructor()))
        .lessons(lessons)
        .build();

  }

}
