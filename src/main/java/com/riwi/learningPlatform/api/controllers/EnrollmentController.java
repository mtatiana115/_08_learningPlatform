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
import com.riwi.learningPlatform.api.dto.request.EnrollmentReq;
import com.riwi.learningPlatform.api.dto.response.EnrollmentResp;
import com.riwi.learningPlatform.infrastructure.abstract_services.IEnrollmentService;
import com.riwi.learningPlatform.util.enums.SortType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/enrollment")
@AllArgsConstructor
public class EnrollmentController {

  private IEnrollmentService iEnrollmentService;

  @Operation(summary = "Get the entire enrollments list in a paginated manner")
  @GetMapping
  public ResponseEntity<Page<EnrollmentResp>> getAll(
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(defaultValue = "5") int size,
    @RequestHeader(required = false) SortType sortType
  ){
    if (Objects.isNull(sortType)) {
      sortType = sortType.NONE;
    }
    return ResponseEntity.ok(this.iEnrollmentService.getAll(page -1, size, sortType));
  }

  @Operation(summary = "Create an enrollment")
  @ApiResponse(responseCode = "400", description = "When the request is not valid", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @PostMapping
  public ResponseEntity <EnrollmentResp> create (
    @Validated @RequestBody EnrollmentReq request
  ){
    return ResponseEntity.ok(iEnrollmentService.create(request));
  }

  @Operation(summary = "Get an enrollment by its ID number")
  @ApiResponse(responseCode = "400", description = "When the ID is not found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
    })
  @GetMapping(path = "/{id}")
  public ResponseEntity<EnrollmentResp> get(
    @PathVariable Long id
  ){
    return ResponseEntity.ok(this.iEnrollmentService.get(id));
  }

  @Operation(summary = "Delete an enrollment by its ID number")
  @ApiResponse(responseCode = "204", description = "Enrollment deleted successfully")
  @ApiResponse(responseCode = "400", description = "When the ID is not found", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    this.iEnrollmentService.delete(id);
    return ResponseEntity.noContent().build();
  }

    @Operation(summary = "Update an enrollment by its ID number")
    @ApiResponse(responseCode = "400", description = "When the request is not valid", content = { 
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
    })
  @PutMapping(path = "/{id}")
  public ResponseEntity<EnrollmentResp> update(
    @PathVariable Long id,
    @Validated @RequestBody EnrollmentReq request
  ){
    return ResponseEntity.ok(this.iEnrollmentService.update(request, id));
  }

}
