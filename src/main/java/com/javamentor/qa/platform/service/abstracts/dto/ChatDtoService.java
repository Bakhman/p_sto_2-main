package com.javamentor.qa.platform.service.abstracts.dto;

import com.javamentor.qa.platform.models.dto.ChatDto;
import com.javamentor.qa.platform.models.dto.GroupChatDto;
import com.javamentor.qa.platform.models.dto.SingleChatDto;

import java.util.List;
import java.util.Optional;

public interface ChatDtoService extends PaginationServiceDto<ChatDto> {
    List<SingleChatDto> getAllSingleChatDto(Long id);
    Optional<GroupChatDto> getGroupChatByIdWithPaginationMessage(int itemsOnPage, int currentPageNumber,Long chatId);
    List<ChatDto> getChatsByString(String string);

}
