package com.javamentor.qa.platform.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GroupChatDto {
    private Long id;
    private String chatName;
    private PageDto<MessageDto> pageOfMessageDto;
    private LocalDateTime persistDateTime;
    private Boolean isGlobal;

    public GroupChatDto(Long id, String chatName, LocalDateTime persistDateTime, Boolean isGlobal) {
        this.id = id;
        this.chatName = chatName;
        this.persistDateTime = persistDateTime;
        this.isGlobal = isGlobal;
    }
}
