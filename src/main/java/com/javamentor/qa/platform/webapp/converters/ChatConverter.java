package com.javamentor.qa.platform.webapp.converters;

import com.javamentor.qa.platform.models.dto.CreateSingleChatDto;
import com.javamentor.qa.platform.models.entity.chat.SingleChat;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ChatConverter {

    public abstract SingleChat createSingleChatDtoToSingleChat(CreateSingleChatDto createSingleChatDto);
}
