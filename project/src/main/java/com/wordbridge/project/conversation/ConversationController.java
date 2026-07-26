package com.wordbridge.project.conversation;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/conversations/")
@RequiredArgsConstructor
public class ConversationController {
    private final ConversationService conversationService;

    @GetMapping("{id}")
    public ResponseEntity<ConversationResponseDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(conversationService.getById(id));
    }

    @GetMapping("gig-order/{gigOrderId}")
    public ResponseEntity<ConversationResponseDTO> getByGigOrderId(@PathVariable Long gigOrderId) {
        return ResponseEntity.ok(conversationService.getByGigOrderId(gigOrderId));
    }

    @GetMapping("buyer/{buyerId}")
    public List<ConversationResponseDTO> getBuyerConversations(@PathVariable Long buyerId) {
        return conversationService.getBuyerConversations(buyerId);
    }

    @GetMapping("seller/{sellerUserProfileId}")
    public List<ConversationResponseDTO> getSellerConversations(@PathVariable Long sellerUserProfileId) {
        return conversationService.getSellerConversations(sellerUserProfileId);
    }

    @GetMapping("buyer/{buyerId}/count")
    public Long countBuyerConversations(@PathVariable Long buyerId) {
        return conversationService.countBuyerConversations(buyerId);
    }

    @GetMapping("seller/{sellerUserProfileId}/count")
    public Long countSellerConversations(@PathVariable Long sellerUserProfileId) {
        return conversationService.countSellerConversations(sellerUserProfileId);
    }


}
