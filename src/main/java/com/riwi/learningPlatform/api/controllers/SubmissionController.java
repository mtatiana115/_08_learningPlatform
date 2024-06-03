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
import com.riwi.learningPlatform.api.dto.request.SubmissionReq;
import com.riwi.learningPlatform.api.dto.response.SubmissionResp;
import com.riwi.learningPlatform.infrastructure.abstract_services.ISubmissionService;
import com.riwi.learningPlatform.util.enums.SortType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/submission")
@AllArgsConstructor
public class SubmissionController {

  @Autowired
  private final ISubmissionService iSubmissionService;

  @Operation(summary = "Get the entire submissions list in a paginated manner")
  @GetMapping
  public ResponseEntity<Page<SubmissionResp>> getAll(
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(defaultValue = "5") int size,
    @RequestHeader(required = false) SortType sortType
  ){
    if (Objects.isNull(sortType)) {
      sortType = sortType.NONE;
    }
    return ResponseEntity.ok(this.iSubmissionService.getAll(page -1, size, sortType));
  }

  @Operation(summary = "Create a submission")
  @ApiResponse(responseCode = "400", description = "When the request is not valid", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @PostMapping
  public ResponseEntity <SubmissionResp> create (
    @Validated @RequestBody SubmissionReq request
  ){
    return ResponseEntity.ok(iSubmissionService.create(request));
  }

  @Operation(summary = "Get a submission by its ID number")
  @ApiResponse(responseCode = "400", description = "When the ID is not found", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @GetMapping(path = "/{id}")
  public ResponseEntity<SubmissionResp> get(
    @PathVariable Long id
  ){
    return ResponseEntity.ok(this.iSubmissionService.get(id));
  }

  @Operation(summary = "Delete a submission by its ID number")
  @ApiResponse(responseCode = "204", description = "User deleted successfully")
  @ApiResponse(responseCode = "400", description = "When the ID is not found", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    this.iSubmissionService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Update a submission by its ID number")
  @ApiResponse(responseCode = "400", description = "When the request is not valid", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @PutMapping(path = "/{id}")
  public ResponseEntity<SubmissionResp> update(
    @PathVariable Long id,
    @Validated @RequestBody SubmissionReq request
  ){
    return ResponseEntity.ok(this.iSubmissionService.update(request, id));
  }

}
