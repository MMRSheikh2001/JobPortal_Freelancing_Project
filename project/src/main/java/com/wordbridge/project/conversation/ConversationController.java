package com.wordbridge.project.conversation;


import com.wordbridge.project.dto.responsedto.UserProfileResponseDTO;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
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

    private final UserProfileService userProfileService;
    private final AuthenticationService authenticationService;


    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<ConversationResponseDTO> getById(@PathVariable Long id) {
        checkParticipantOrAdmin(id);
        return ResponseEntity.ok(conversationService.getById(id));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("gig-order/{gigOrderId}")
    public ResponseEntity<ConversationResponseDTO> getByGigOrderId(@PathVariable Long gigOrderId) {
        ConversationResponseDTO convo = conversationService.getByGigOrderId(gigOrderId);
        checkParticipantOrAdmin(convo.getId());
        return ResponseEntity.ok(conversationService.getByGigOrderId(gigOrderId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("buyer/{buyerId}")
    public List<ConversationResponseDTO> getBuyerConversations(@PathVariable Long buyerId) {
        checkBuyerIdOwnership(buyerId);
        return conversationService.getBuyerConversations(buyerId);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("seller/{sellerUserProfileId}")
    public List<ConversationResponseDTO> getSellerConversations(@PathVariable Long sellerUserProfileId) {
        checkSellerProfileOwnershipOrAdmin(sellerUserProfileId);
        return conversationService.getSellerConversations(sellerUserProfileId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("buyer/{buyerId}/count")
    public Long countBuyerConversations(@PathVariable Long buyerId) {
        checkBuyerIdOwnership(buyerId);
        return conversationService.countBuyerConversations(buyerId);
    }

    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @GetMapping("seller/{sellerUserProfileId}/count")
    public Long countSellerConversations(@PathVariable Long sellerUserProfileId) {
        checkSellerProfileOwnershipOrAdmin(sellerUserProfileId);
        return conversationService.countSellerConversations(sellerUserProfileId);
    }


    // private methods
    private void checkBuyerIdOwnership(Long buyerId) {
        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(buyerId) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    private void checkSellerProfileOwnershipOrAdmin(Long sellerUserProfileId) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() == UserRole.ADMIN) return;
        UserProfileResponseDTO profile = userProfileService.findById(sellerUserProfileId);
        if (!profile.getUserId().equals(currentUser.getId())) {
            throw new AccessDeniedException("Not allowed");
        }
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
