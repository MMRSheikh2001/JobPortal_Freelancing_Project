package com.wordbridge.project.message;

import com.wordbridge.project.dto.requestdto.UserProfileRequestDTO;
import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/messages/")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    @PostMapping
    public ResponseEntity<MessageResponseDTO> sendMessage(
            @RequestPart("message") MessageRequestDTO dto,
            @RequestParam Long senderId,
            @RequestPart(value = "attachment", required = false) MultipartFile attachment
    ) {
        MessageResponseDTO response =
                messageService.sendMessage(dto, senderId, attachment);

        return ResponseEntity.ok(response);
    }


    @GetMapping("{id}")
    public ResponseEntity<MessageResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(messageService.getById(id));
    }

    @GetMapping("conversation/{conversationId}")
    public List<MessageResponseDTO> getConversationMessages(
            @PathVariable Long conversationId) {

        return messageService.getConversationMessages(conversationId);
    }

    @GetMapping("sender/{senderId}")
    public List<MessageResponseDTO> getSenderMessages(
            @PathVariable Long senderId) {

        return messageService.getSenderMessages(senderId);
    }

    @GetMapping("conversation/{conversationId}/unread")
    public List<MessageResponseDTO> getUnreadMessages(
            @PathVariable Long conversationId) {

        return messageService.getUnreadMessages(conversationId);
    }

    @GetMapping("conversation/{conversationId}/unread/count")
    public Long countUnreadMessages(
            @PathVariable Long conversationId) {

        return messageService.countUnreadMessages(conversationId);
    }

    @GetMapping("conversation/{conversationId}/unread/count/{senderId}")
    public Long countUnreadMessagesForUser(
            @PathVariable Long conversationId,
            @PathVariable Long senderId) {

        return messageService.countUnreadMessagesForUser(
                conversationId,
                senderId
        );
    }

    @PutMapping("conversation/{conversationId}/read")
    public ResponseEntity<String> markConversationAsRead(
            @PathVariable Long conversationId,
            @RequestParam Long readerId) {

        messageService.markConversationAsRead(conversationId, readerId);

        return ResponseEntity.ok("Conversation marked as read.");
    }

    @GetMapping("conversation/{conversationId}/latest")
    public ResponseEntity<MessageResponseDTO> getLatestMessage(
            @PathVariable Long conversationId) {

        return ResponseEntity.ok(
                messageService.getLatestMessage(conversationId)
        );
    }

}
