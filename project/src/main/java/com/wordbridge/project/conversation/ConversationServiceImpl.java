package com.wordbridge.project.conversation;

import com.wordbridge.project.enums.ConversationStatus;
import com.wordbridge.project.enums.GigOrderStatus;
import com.wordbridge.project.gigorder.GigOrder;
import com.wordbridge.project.gigorder.GigOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationServiceImpl implements ConversationService {
    private final ConversationRepository conversationRepository;
    private final ConversationMapper conversationMapper;
    private final GigOrderRepository gigOrderRepository;


    @Override
    @Transactional
    public Conversation create(Long gigOrderId) {
        if (conversationRepository.existsByGigOrderId(gigOrderId)) {
            throw new RuntimeException("A Chat room already exists for this Gig Order");
        }
        Conversation conversation = new Conversation();
        GigOrder gigOrder = gigOrderRepository.findById(gigOrderId)
                .orElseThrow(() -> new RuntimeException("No gig order found"));
        if (gigOrder.getStatus() != GigOrderStatus.ORDER_PLACED) {
            throw new RuntimeException("Conversation can only be created for a newly placed order.");
        }

        conversation.setGigOrder(gigOrder);
        conversation.setStatus(ConversationStatus.ACTIVE);
        conversation.setLastMessageAt(LocalDateTime.now());
        Conversation saved = conversationRepository.save(conversation);

        return saved;
    }

    @Override
    public ConversationResponseDTO getById(Long id) {
        Conversation conversation = conversationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Conversation found"));
        return conversationMapper.toDTO(conversation);
    }

    @Override
    public ConversationResponseDTO getByGigOrderId(Long gigOrderId) {
        Conversation conversation = conversationRepository.findByGigOrderId(gigOrderId)
                .orElseThrow(() -> new RuntimeException("No Conversation found"));
        return conversationMapper.toDTO(conversation);
    }

    @Override
    public List<ConversationResponseDTO> getBuyerConversations(Long buyerId) {
        return conversationRepository.findByGigOrderBuyerIdOrderByLastMessageAtDesc(buyerId).stream().map(conversationMapper::toDTO).toList();
    }

    @Override
    public List<ConversationResponseDTO> getSellerConversations(Long sellerUserProfileId) {
        return conversationRepository.findByGigOrderGigUserProfileIdOrderByLastMessageAtDesc(sellerUserProfileId).stream().map(conversationMapper::toDTO).toList();
    }

    @Override
    public Long countBuyerConversations(Long buyerId) {
        return conversationRepository.countByGigOrderBuyerId(buyerId);
    }

    @Override
    public Long countSellerConversations(Long sellerUserProfileId) {
        return conversationRepository.countByGigOrderGigUserProfileId(sellerUserProfileId);
    }

    @Override
    public void closeConversation(Long conversationId) {
        Conversation conversation = conversationRepository.findById(conversationId)
        .orElseThrow(()->new RuntimeException("No conversation found"));

        conversation.setStatus(ConversationStatus.CLOSED);

        conversationRepository.save(conversation);
    }

    @Override
    public Long countActiveConversations(Long userProfileId) {
        return conversationRepository.countActiveConversations(userProfileId);
    }
}
