package com.wordbridge.project.transaction;

import com.wordbridge.project.enums.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> , JpaSpecificationExecutor<Transaction> {

    // Transactions sent by a user
    List<Transaction> findByFromUserId(Long fromUserId);

    // Transactions received by a user
    List<Transaction> findByToUserId(Long toUserId);

    // All transactions of a specific type
    List<Transaction> findByType(TransactionType type);

    // User's transaction history (money in or out)
    List<Transaction> findByFromUserIdOrToUserIdOrderByCreatedAtDesc(
            Long fromUserId,
            Long toUserId
    );

    // Count
    long countByType(TransactionType type);

    //Admin panel
    List<Transaction> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    List<Transaction> findByTypeAndCreatedAtBetween(
            TransactionType type,
            LocalDateTime start,
            LocalDateTime end
    );


}
