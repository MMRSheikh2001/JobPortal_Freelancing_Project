package com.wordbridge.project.message;

import com.wordbridge.project.conversation.ConversationResponseDTO;
import com.wordbridge.project.conversation.ConversationService;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/messages/")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    private final ConversationService conversationService;
    private final UserProfileService userProfileService;
    private final AuthenticationService authenticationService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<MessageResponseDTO> sendMessage(
            @RequestPart("message") MessageRequestDTO dto,
            @RequestParam Long senderId,
            @RequestPart(value = "attachment", required = false) MultipartFile attachment
    ) {

        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(senderId)) {
            throw new AccessDeniedException("Not allowed");
        }
        checkParticipantOrAdmin(dto.getConversationId());
        MessageResponseDTO response =
                messageService.sendMessage(dto, senderId, attachment);

        return ResponseEntity.ok(response);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<MessageResponseDTO> getById(@PathVariable Long id) {
        MessageResponseDTO msg = messageService.getById(id);
        checkParticipantOrAdmin(msg.getConversationId());
        return ResponseEntity.ok(messageService.getById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("conversation/{conversationId}")
    public List<MessageResponseDTO> getConversationMessages(
            @PathVariable Long conversationId) {

        checkParticipantOrAdmin(conversationId);
        return messageService.getConversationMessages(conversationId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("sender/{senderId}")
    public List<MessageResponseDTO> getSenderMessages(
            @PathVariable Long senderId) {

        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(senderId) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
        return messageService.getSenderMessages(senderId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("conversation/{conversationId}/unread")
    public List<MessageResponseDTO> getUnreadMessages(
            @PathVariable Long conversationId) {

        checkParticipantOrAdmin(conversationId);
        return messageService.getUnreadMessages(conversationId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("conversation/{conversationId}/unread/count")
    public Long countUnreadMessages(
            @PathVariable Long conversationId) {
        checkParticipantOrAdmin(conversationId);

        return messageService.countUnreadMessages(conversationId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("conversation/{conversationId}/unread/count/{senderId}")
    public Long countUnreadMessagesForUser(
            @PathVariable Long conversationId,
            @PathVariable Long senderId) {

        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(senderId) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
        return messageService.countUnreadMessagesForUser(
                conversationId,
                senderId
        );
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("conversation/{conversationId}/read")
    public ResponseEntity<String> markConversationAsRead(
            @PathVariable Long conversationId,
            @RequestParam Long readerId) {

        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(readerId)) {
            throw new AccessDeniedException("Not allowed");
        }
        checkParticipantOrAdmin(conversationId);
        messageService.markConversationAsRead(conversationId, readerId);

        return ResponseEntity.ok("Conversation marked as read.");
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("conversation/{conversationId}/latest")
    public ResponseEntity<MessageResponseDTO> getLatestMessage(
            @PathVariable Long conversationId) {
        checkParticipantOrAdmin(conversationId);

        return ResponseEntity.ok(
                messageService.getLatestMessage(conversationId)
        );
    }

    private void checkParticipantOrAdmin(Long conversationId) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() == UserRole.ADMIN) return;
        ConversationResponseDTO convo = conversationService.getById(conversationId);
        boolean isBuyer = convo.getBuyerId().equals(currentUser.getId());
        boolean isSeller = userProfileService.findById(convo.getSellerUserProfileId()).getUserId().equals(currentUser.getId());
        if (!isBuyer && !isSeller) {
            throw new AccessDeniedException("Not allowed");
        }
    }

}
