package com.wordbridge.project.transaction;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.TransactionType;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public interface TransactionService {

    // Internal
    void createTransaction(
            TransactionType type,
            User fromUser,
            User toUser,
            BigDecimal amount,
            String description
    );

    // Read
    TransactionResponseDTO getById(Long id);

    List<TransactionResponseDTO> getAll();

    List<TransactionResponseDTO> getByFromUser(Long fromUserId);

    List<TransactionResponseDTO> getByToUser(Long toUserId);

    List<TransactionResponseDTO> getUserHistory(Long userId);

    List<TransactionResponseDTO> getByType(TransactionType type);

    Long countByType(TransactionType type);

    // Admin Reports
    List<TransactionResponseDTO> getBetweenDates(
            LocalDateTime start,
            LocalDateTime end
    );

    List<TransactionResponseDTO> getByTypeBetweenDates(
            TransactionType type,
            LocalDateTime start,
            LocalDateTime end
    );

    List<TransactionResponseDTO> search(TransactionFilterDTO filter);
}
