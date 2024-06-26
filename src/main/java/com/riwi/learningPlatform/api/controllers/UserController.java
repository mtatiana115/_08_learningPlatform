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
import com.riwi.learningPlatform.api.dto.request.UserReq;
import com.riwi.learningPlatform.api.dto.response.UserResp;
import com.riwi.learningPlatform.api.dto.response.UserRespWithCourses;
import com.riwi.learningPlatform.api.dto.response.UserRespWithSubmissions;
import com.riwi.learningPlatform.infrastructure.abstract_services.IUserService;
import com.riwi.learningPlatform.util.enums.SortType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/user")
@AllArgsConstructor
public class UserController {

  private final IUserService iUserService;

  @Operation(summary = "Get the entire users list in a paginated manner")
  @GetMapping
  public ResponseEntity<Page<UserResp>> getAll(
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(defaultValue = "5") int size,
    @RequestHeader(required = false) SortType sortType
  ){
    if (Objects.isNull(sortType)) {
      sortType = sortType.NONE;
    }
    return ResponseEntity.ok(this.iUserService.getAll(page -1, size, sortType));
  }

  @Operation(summary = "Create an user")
  @ApiResponse(responseCode = "400", description = "When the request is not valid", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @PostMapping
  public ResponseEntity <UserResp> create (
    @Validated @RequestBody UserReq request
  ){
    return ResponseEntity.ok(iUserService.create(request));
  }

  @Operation(summary = "Get an user by its ID number")
  @ApiResponse(responseCode = "400", description = "When the ID is not found", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @GetMapping(path = "/{id}")
  public ResponseEntity<UserResp> get(
    @PathVariable Long id
  ){
    return ResponseEntity.ok(this.iUserService.get(id));
  }

  @Operation(summary = "Delete an user by its ID number")
  @ApiResponse(responseCode = "204", description = "User deleted successfully")
  @ApiResponse(responseCode = "400", description = "When the ID is not found", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    this.iUserService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @Operation(summary = "Update an user by its ID number")
  @ApiResponse(responseCode = "400", description = "When the request is not valid", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @PutMapping(path = "/{id}")
  public ResponseEntity<UserResp> update(
    @PathVariable Long id,
    @Validated @RequestBody UserReq request
  ){
    return ResponseEntity.ok(this.iUserService.update(request, id));
  }

  @Operation(summary = "Get an user with courses by its ID number")
  @ApiResponse(responseCode = "400", description = "When the ID is not found", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @GetMapping("/{id}/courses")
  public ResponseEntity<UserRespWithCourses> getByIdWithCourses(@PathVariable Long id) {
    return ResponseEntity.ok(iUserService.getUsersWithCourses(id));
  }

  @Operation(summary = "Get an user with submissions by its ID number")
  @ApiResponse(responseCode = "400", description = "When the ID is not found", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @GetMapping("/{id}/submissions")
  public ResponseEntity<UserRespWithSubmissions> getByIdWithSubmisions(@PathVariable Long id) {
    return ResponseEntity.ok(iUserService.getUserWithSubmissions(id));
  }

}
