package com.riwi.learningPlatform.api.controllers;

import java.util.List;
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
import com.riwi.learningPlatform.api.dto.request.MessageReq;
import com.riwi.learningPlatform.api.dto.response.MessageResp;
import com.riwi.learningPlatform.infrastructure.abstract_services.IMessageService;
import com.riwi.learningPlatform.util.enums.SortType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/message")
@AllArgsConstructor
public class MessageController {

  private IMessageService iMessageService;

  @Operation(summary = "Get the entire messages list in a paginated manner")
  @GetMapping
  public ResponseEntity<Page<MessageResp>> getAll(
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(defaultValue = "5") int size,
    @RequestHeader(required = false) SortType sortType
  ){
    if (Objects.isNull(sortType)) {
      sortType = sortType.NONE;
    }
    return ResponseEntity.ok(this.iMessageService.getAll(page -1, size, sortType));
  }

  @Operation(summary = "Create an message")
  @ApiResponse(responseCode = "400", description = "When the request is not valid", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @PostMapping
  public ResponseEntity <MessageResp> create (
    @Validated @RequestBody MessageReq request
  ){
    return ResponseEntity.ok(iMessageService.create(request));
  }

  @Operation(summary = "Get an message by its ID number")
  @ApiResponse(responseCode = "400", description = "When the ID is not found", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
    })
  @GetMapping(path = "/{id}")
  public ResponseEntity<MessageResp> get(
    @PathVariable Long id
  ){
    return ResponseEntity.ok(this.iMessageService.get(id));
  }

  @Operation(summary = "Delete an message by its ID number")
  @ApiResponse(responseCode = "204", description = "Enrollment deleted successfully")
  @ApiResponse(responseCode = "400", description = "When the ID is not found", content = {
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
  })
  @DeleteMapping(path = "/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id){
    this.iMessageService.delete(id);
    return ResponseEntity.noContent().build();
  }

    @Operation(summary = "Update an message by its ID number")
    @ApiResponse(responseCode = "400", description = "When the request is not valid", content = { 
      @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorsResp.class))
    })
  @PutMapping(path = "/{id}")
  public ResponseEntity<MessageResp> update(
    @PathVariable Long id,
    @Validated @RequestBody MessageReq request
  ){
    return ResponseEntity.ok(this.iMessageService.update(request, id));
  }

  @Operation(summary = "Get the entire messages list in a paginated manner")
  @GetMapping
  public ResponseEntity<List<MessageResp>> getMessagesBetweenUsers(
          @RequestParam(required = false) Long senderId,
          @RequestParam(required = false) Long receiverId
          ) {
      if (Objects.isNull(senderId) || Objects.isNull(receiverId)) {
          if (Objects.isNull(senderId) && Objects.isNull(receiverId)) {
              return ResponseEntity.ok(iMessageService.findAllMessages());
          } else {
              throw new IllegalArgumentException("Both senderId and receiverId are required");
          }
      }
      return ResponseEntity.ok(iMessageService.getMessagesBetweenUsers(senderId,receiverId));
  }
}
