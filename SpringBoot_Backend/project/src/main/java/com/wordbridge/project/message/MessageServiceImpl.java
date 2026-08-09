package com.wordbridge.project.message;

import com.wordbridge.project.conversation.Conversation;
import com.wordbridge.project.conversation.ConversationRepository;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.ConversationStatus;
import com.wordbridge.project.enums.GigOrderStatus;
import com.wordbridge.project.repository.UserRepository;
import com.wordbridge.project.util.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final MessageMapper messageMapper;
    private final FileStorageService fileStorageService;


    @Override
    @Transactional
    public MessageResponseDTO sendMessage(MessageRequestDTO dto, Long senderId, MultipartFile attachment) {


        Message message = messageMapper.toEntity(dto);
        if (message.getConversation().getStatus() == ConversationStatus.CLOSED) {
            throw new RuntimeException("Conversation has been closed.");
        }
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("No user found"));
        message.setSender(sender);
        message.setIsRead(false);

        if (attachment != null && !attachment.isEmpty()) {
            String fileName = fileStorageService.uploadPortfolioFile(attachment,
                    sender.getEmail(),
                    "messages");
            message.setAttachment(fileName);

        }
        Conversation conversation = message.getConversation();

        boolean allowed =
                conversation.getGigOrder().getBuyer().getId().equals(senderId)
                        ||
                        conversation.getGigOrder().getGig().getUserProfile().getUser().getId().equals(senderId);

        if (!allowed) {
            throw new RuntimeException("You are not part of this conversation.");
        }

        conversation.setLastMessageAt(LocalDateTime.now());

        conversationRepository.save(conversation);

        return messageMapper.toDTO(messageRepository.save(message));
    }

    @Override
    public MessageResponseDTO getById(Long id) {
        Message message = messageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No message found"));
        return messageMapper.toDTO(message);
    }

    @Override
    public List<MessageResponseDTO> getConversationMessages(Long conversationId) {
        return messageRepository.findByConversationIdOrderBySentAtAsc(conversationId).stream().map(messageMapper::toDTO).toList();
    }

    @Override
    public List<MessageResponseDTO> getSenderMessages(Long senderId) {
        return messageRepository.findBySenderId(senderId).stream().map(messageMapper::toDTO).toList();
    }

    @Override
    public List<MessageResponseDTO> getUnreadMessages(Long conversationId) {
        return messageRepository.findByConversationIdAndIsReadFalse(conversationId).stream().map(messageMapper::toDTO).toList();
    }

    @Override
    public Long countUnreadMessages(Long conversationId) {
        return messageRepository.countByConversationIdAndIsReadFalse(conversationId);
    }

    @Override
    public Long countUnreadMessagesForUser(Long conversationId, Long senderId) {
        return messageRepository.countByConversationIdAndSenderIdNotAndIsReadFalse(conversationId, senderId);
    }

    @Override
    public void markConversationAsRead(Long conversationId, Long readerId) {
        List<Message> unreadMessages =
                messageRepository.findByConversationIdAndSenderIdNotAndIsReadFalse(
                        conversationId,
                        readerId
                );
        if (unreadMessages.isEmpty()) {
            return;
        }

        for (Message message : unreadMessages) {
            message.setIsRead(true);
        }

        messageRepository.saveAll(unreadMessages);


    }

    @Override
    public MessageResponseDTO getLatestMessage(Long conversationId) {

        Message message = messageRepository
                .findTopByConversationIdOrderBySentAtDesc(conversationId)
                .orElseThrow(() ->
                        new RuntimeException("No messages found."));

        return messageMapper.toDTO(message);
    }

    @Override
    public Long countUnreadMessagesByUserId(Long userId) {
        return messageRepository.countUnreadMessagesByUserId(userId);
    }

}
