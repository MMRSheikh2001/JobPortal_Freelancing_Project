package com.wordbridge.project.message;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface MessageService {
    MessageResponseDTO sendMessage(
            MessageRequestDTO dto,
            Long senderId,
            MultipartFile attachment
    );

    MessageResponseDTO getById(Long id);

    List<MessageResponseDTO> getConversationMessages(Long conversationId);

    List<MessageResponseDTO> getSenderMessages(Long senderId);

    List<MessageResponseDTO> getUnreadMessages(Long conversationId);

    Long countUnreadMessages(Long conversationId);

    Long countUnreadMessagesForUser(Long conversationId, Long senderId);

    void markConversationAsRead(Long conversationId, Long readerId);

    MessageResponseDTO getLatestMessage(Long conversationId);

    Long countUnreadMessagesByUserId(Long userId);


}
