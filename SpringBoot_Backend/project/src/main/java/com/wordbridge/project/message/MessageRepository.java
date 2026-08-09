package com.wordbridge.project.message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {


    // All messages of a conversation (chat history)
    List<Message> findByConversationIdOrderBySentAtAsc(Long conversationId);

    // Messages sent by a specific user
    List<Message> findBySenderId(Long senderId);

    // Unread messages in a conversation
    List<Message> findByConversationIdAndIsReadFalse(Long conversationId);

    // Count unread messages in a conversation
    Long countByConversationIdAndIsReadFalse(Long conversationId);

    // Count unread messages sent by others in a conversation
    Long countByConversationIdAndSenderIdNotAndIsReadFalse(
            Long conversationId,
            Long senderId
    );

    // Delete all messages if a conversation is ever removed
    void deleteByConversationId(Long conversationId);

    //All unread conversation not send by reader
    List<Message> findByConversationIdAndSenderIdNotAndIsReadFalse(
            Long conversationId,
            Long senderId
    );

    Optional<Message> findTopByConversationIdOrderBySentAtDesc(Long conversationId);

    Long countBySenderIdAndIsReadFalse(Long senderId);


    @Query("""
            SELECT COUNT(m)
            FROM Message m
            WHERE m.isRead = false
            AND (
                (
                    m.conversation.gigOrder.buyer.id = :userId
                    AND
                    m.sender.id <> :userId
                )
                OR
                (
                    m.conversation.gigOrder.gig.userProfile.user.id = :userId
                    AND
                    m.sender.id <> :userId
                )
            )
            """)
    Long countUnreadMessagesByUserId(Long userId);

}
