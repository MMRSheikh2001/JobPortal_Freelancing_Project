package com.wordbridge.project.conversation;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ConversationService {

    Conversation create(Long gigOrderId);

    ConversationResponseDTO getById(Long id);

    ConversationResponseDTO getByGigOrderId(Long gigOrderId);

    List<ConversationResponseDTO> getBuyerConversations(Long buyerId);

    List<ConversationResponseDTO> getSellerConversations(Long sellerUserProfileId);

    Long countBuyerConversations(Long buyerId);

    Long countSellerConversations(Long sellerUserProfileId);


    void closeConversation(Long conversationId);

    Long countActiveConversations(Long userProfileId);


}
