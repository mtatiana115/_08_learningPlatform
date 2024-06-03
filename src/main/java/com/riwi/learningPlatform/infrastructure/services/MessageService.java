package com.riwi.learningPlatform.infrastructure.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.riwi.learningPlatform.api.dto.request.MessageReq;
import com.riwi.learningPlatform.api.dto.response.MessageResp;
import com.riwi.learningPlatform.api.dto.response.UserResp;
import com.riwi.learningPlatform.domain.entities.Course;
import com.riwi.learningPlatform.domain.entities.Message;
import com.riwi.learningPlatform.domain.entities.User;
import com.riwi.learningPlatform.domain.repositories.CourseRepository;
import com.riwi.learningPlatform.domain.repositories.MessageRepository;
import com.riwi.learningPlatform.domain.repositories.UserRepository;
import com.riwi.learningPlatform.infrastructure.abstract_services.IMessageService;
import com.riwi.learningPlatform.util.enums.SortType;
import com.riwi.learningPlatform.util.exceptions.BadRequestException;
import com.riwi.learningPlatform.util.messages.ErrorMessages;

import lombok.AllArgsConstructor;

@Service
@Transactional
@AllArgsConstructor
public class MessageService implements IMessageService {

  @Autowired
  private final MessageRepository messageRepository;

  @Autowired
  private final UserRepository userRepository;

  @Autowired
  private final CourseRepository courseRepository;

  @Override
  public MessageResp create(MessageReq request) {
    User sender = userRepository.findById(request.getSenderId())
          .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("user")));
    User receiver = userRepository.findById(request.getReceiverId())
          .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("user")));
    Course course = courseRepository.findById(request.getCourseId())
          .orElseThrow(() -> new BadRequestException(ErrorMessages.idNotFound("course")));

    Message message = this.requestToEntity(request);

    message.setCourse(course);
    message.setSender(sender);
    message.setReceiver(receiver);
    return this.entityToResp(messageRepository.save(message));
  }

  @Override
  public MessageResp get(Long id) {
    return this.entityToResp(this.find(id));
  }

  @Override
  public MessageResp update(MessageReq request, Long id) {
    Message messageFound = this.find(id);
    Message message = this.requestToEntity(request);
    message.setId(messageFound.getId());
    return this.entityToResp(this.messageRepository.save(message));
  }

  @Override
  public void delete(Long id) {
    this.messageRepository.delete(this.find(id));
  }

  @Override
  public Page<MessageResp> getAll(int page, int size, SortType sortType) {
    if (page < 0) page = 0;

    PageRequest pagination = null;

    //validar de que tipo es el sortType
    switch (sortType) {
      case NONE -> pagination = PageRequest.of(page, size);
      case ASC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
      case DESC -> pagination = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        
    }

    return this.messageRepository.findAll(pagination).map(this::entityToResp);
  }

  private MessageResp entityToResp (Message entity){
    return MessageResp.builder()
          .id(entity.getId())
          .sentDate(entity.getSentDate())
          .messageContent(entity.getMessageContent())
          .sender(userResp(entity.getSender()))
          .receiver(userResp(entity.getReceiver()))

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

  private Message requestToEntity (MessageReq request){
    return  Message.builder()
    .messageContent(request.getMessageContent())
    .build();
  }

  private Message find (Long id){
    return this.messageRepository.findById(id)
            .orElseThrow(() -> new BadRequestException(ErrorMessages.
      idNotFound("message")));
  }

  @Override
  public List<MessageResp> getMessagesBetweenUsers(Long senderId, Long receiverId) {
    return this.getMessagesBetweenUsersById(senderId, receiverId).stream().map(message -> this.entityToResp(message)).toList();
  }

  @Override
  public List<MessageResp> findAllMessages() {
    return this.messageRepository.findAll().stream().map(message -> this.entityToResp(message)).toList();
  }

  private List<Message> getMessagesBetweenUsersById(Long senderId, Long receiverId) {
    userRepository.findById(senderId).orElseThrow(() -> new BadRequestException(ErrorMessages.
          idNotFound("user")));
    userRepository.findById(receiverId).orElseThrow(() -> new BadRequestException(ErrorMessages.
    idNotFound("user")));

    return messageRepository.findBySenderIdAndReceiverId(senderId, receiverId);

  }

}
