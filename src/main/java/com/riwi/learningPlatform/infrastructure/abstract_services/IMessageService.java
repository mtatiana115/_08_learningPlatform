package com.riwi.learningPlatform.infrastructure.abstract_services;

import java.util.List;

import com.riwi.learningPlatform.api.dto.request.MessageReq;
import com.riwi.learningPlatform.api.dto.response.MessageResp;

public interface IMessageService extends CrudService <MessageReq, MessageResp, Long>{

  public final String FIELD_BY_SORT = "sentDate";

  public List<MessageResp> getMessagesBetweenUsers(Long senderId, Long receiverId);
  
  public List<MessageResp> findAllMessages();
}
