package com.wordbridge.project.conversation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    // Get conversation for a gig order
    Optional<Conversation> findByGigOrderId(Long gigOrderId);

    // Buyer's conversations
    List<Conversation> findByGigOrderBuyerIdOrderByLastMessageAtDesc(Long buyerId);



    // Seller's conversations
    List<Conversation> findByGigOrderGigUserProfileIdOrderByLastMessageAtDesc(Long sellerUserProfileId);

    // Check if conversation already exists
    boolean existsByGigOrderId(Long gigOrderId);

    // Count conversations
    long countByGigOrderBuyerId(Long buyerId);

    long countByGigOrderGigUserProfileId(Long sellerUserProfileId);

    @Query("""
SELECT COUNT(c)
FROM Conversation c
WHERE c.gigOrder.gig.userProfile.id = :userProfileId
AND c.status <> com.wordbridge.project.enums.ConversationStatus.CLOSED
""")
    Long countActiveConversations(
            @Param("userProfileId") Long userProfileId
    );


}
