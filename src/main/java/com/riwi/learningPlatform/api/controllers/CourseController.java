package com.riwi.learningPlatform.api.controllers;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.riwi.learningPlatform.api.dto.errors.ErrorsResp;
import com.riwi.learningPlatform.api.dto.request.CourseReq;
import com.riwi.learningPlatform.api.dto.response.CourseResp;
import com.riwi.learningPlatform.api.dto.response.CourseRespWithLessons;
import com.riwi.learningPlatform.api.dto.response.CourseRespWithStudents;
import com.riwi.learningPlatform.infrastructure.abstract_services.ICourseService;
import com.riwi.learningPlatform.util.enums.SortType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/course")
@AllArgsConstructor
public class CourseController {

  private ICourseService iCourseService;

  @Operation(summary = "Get the entire courses list")
  @GetMapping
  public ResponseEntity<Page<CourseResp>> getAll(
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(defaultValue = "5") int size,
    @RequestHeader(required = false) SortType sortType
  ){
    if (Objects.isNull(sortType)) {
      sortType = sortType.NONE;
    }
    return ResponseEntity.ok(this.iCourseService.getAll(page -1, size, sortType));

  }

  @Operation(summary = "Create an course")
  @ApiResponse(responseCode = "400", description = "When the request is not valid", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @PostMapping
  public ResponseEntity <CourseResp> create (
    @Validated @RequestBody CourseReq request
  ){
    return ResponseEntity.ok(iCourseService.create(request));
  }

  @Operation(summary = "Get an course by its ID number")
  @ApiResponse(responseCode = "400", description = "When the ID is not found", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @GetMapping(path = "/{id}")
  public ResponseEntity<CourseResp> get(
    @PathVariable Long id
  ){
    return ResponseEntity.ok(this.iCourseService.get(id));
  }

  @Operation(summary = "Delete an course by its ID number")
  @ApiResponse(responseCode = "204", description = "course deleted successfully")
  @ApiResponse(responseCode = "400", description = "When the ID is not found", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    this.iCourseService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Update an course by its ID number")
  @ApiResponse(responseCode = "400", description = "When the request is not valid", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @PutMapping(path = "/{id}")
  public ResponseEntity<CourseResp> update(
    @PathVariable Long id,
    @Validated @RequestBody CourseReq request
  ){
    return ResponseEntity.ok(this.iCourseService.update(request, id));
  }

  @Operation(summary = "Get an course with lessons by its ID number")
  @ApiResponse(responseCode = "400", description = "When the ID is not found", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @GetMapping("/{id}/lessons")
  public ResponseEntity<CourseRespWithLessons> getByIdWithLessons(@PathVariable Long id) {
    return ResponseEntity.ok(iCourseService.getCourseWithLessons(id));
  }

  @Operation(summary = "Get an course with students by its ID number")
  @ApiResponse(responseCode = "400", description = "When the ID is not found", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @GetMapping("/{id}/users")
  public ResponseEntity<CourseRespWithStudents> getByIdWithStudents(@PathVariable Long id) {
    return ResponseEntity.ok(iCourseService.getCourseWithStudents(id));
  }
}

