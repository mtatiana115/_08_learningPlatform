package com.riwi.learningPlatform.api.controllers;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.riwi.learningPlatform.api.dto.request.LessonReq;
import com.riwi.learningPlatform.api.dto.response.LessonResp;
import com.riwi.learningPlatform.api.dto.response.LessonRespWithAssignments;
import com.riwi.learningPlatform.infrastructure.abstract_services.ILessonService;
import com.riwi.learningPlatform.util.enums.SortType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/lesson")
@AllArgsConstructor
public class LessonController {

  @Autowired
  private final ILessonService iLessonService;

  @Operation(summary = "Get the entire lessons list in a paginated manner")
  @GetMapping
  public ResponseEntity<Page<LessonResp>> getAll(
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(defaultValue = "5") int size,
    @RequestHeader(required = false) SortType sortType
  ){
    if (Objects.isNull(sortType)) {
      sortType = sortType.NONE;
    }
    return ResponseEntity.ok(this.iLessonService.getAll(page -1, size, sortType));
  }

  @Operation(summary = "Create a lesson")
  @ApiResponse(responseCode = "400", description = "When the request is not valid", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @PostMapping
  public ResponseEntity <LessonResp> create (
    @Validated @RequestBody LessonReq request
  ){
    return ResponseEntity.ok(iLessonService.create(request));
  }

  @Operation(summary = "Get a lesson by its ID number")
  @ApiResponse(responseCode = "400", description = "When the ID is not found", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @GetMapping(path = "/{id}")
  public ResponseEntity<LessonResp> get(
    @PathVariable Long id
  ){
    return ResponseEntity.ok(this.iLessonService.get(id));
  }

  @Operation(summary = "Delete a lesson by its ID number")
  @ApiResponse(responseCode = "204", description = "User deleted successfully")
  @ApiResponse(responseCode = "400", description = "When the ID is not found", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    this.iLessonService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Update a lesson by its ID number")
  @ApiResponse(responseCode = "400", description = "When the request is not valid", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @PutMapping(path = "/{id}")
  public ResponseEntity<LessonResp> update(
    @PathVariable Long id,
    @Validated @RequestBody LessonReq request
  ){
    return ResponseEntity.ok(this.iLessonService.update(request, id));
  }

  @Operation(summary = "Get an course with lessons by its ID number")
  @ApiResponse(responseCode = "400", description = "When the ID is not found", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @GetMapping("/{id}/assignments")
  public ResponseEntity<LessonRespWithAssignments> getByIdWithLessons(@PathVariable Long id) {
    return ResponseEntity.ok(iLessonService.getLessonWithAssignments(id));
  }
}

