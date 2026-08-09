package com.wordbridge.project.message;

import com.wordbridge.project.conversation.Conversation;
import com.wordbridge.project.conversation.ConversationRepository;
import com.wordbridge.project.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageMapper {
    private final ConversationRepository conversationRepository;

    public MessageResponseDTO toDTO(Message m) {
        MessageResponseDTO dto = new MessageResponseDTO();
        dto.setId(m.getId());
        dto.setMessageText(m.getMessageText());
        dto.setAttachment(m.getAttachment());
        dto.setIsRead(m.getIsRead());
        dto.setSentAt(m.getSentAt());
        dto.setSenderId(m.getSender().getId());

        if (m.getSender().getRole() == UserRole.USER) {
            dto.setSenderName(m.getSender().getUserProfile().getName());
        } else {
            dto.setSenderName(m.getSender().getCompanyProfile().getName());
        }
        dto.setConversationId(m.getConversation().getId());

        return dto;
    }

    public Message toEntity(MessageRequestDTO dto) {
        Message m = new Message();

        m.setMessageText(dto.getMessageText());

        Conversation conversation = conversationRepository.findById(dto.getConversationId())
                .orElseThrow(() -> new RuntimeException("No conversation found"));
        m.setConversation(conversation);


        return m;
    }


}
