package com.wordbridge.project.transaction;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;


    @Override
    public void createTransaction(TransactionType type, User fromUser, User toUser, BigDecimal amount, String description) {
        Transaction transaction = new Transaction();
        transaction.setType(type);
        transaction.setFromUser(fromUser);
        transaction.setToUser(toUser);
        transaction.setAmount(amount);
        transaction.setDescription(description);

        transactionRepository.save(transaction);
    }

    @Override
    public TransactionResponseDTO getById(Long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No transaction found"));
        return transactionMapper.toDTO(transaction);
    }

    @Override
    public List<TransactionResponseDTO> getAll() {
        return transactionRepository.findAll().stream().map(transactionMapper::toDTO).toList();
    }

    @Override
    public List<TransactionResponseDTO> getByFromUser(Long fromUserId) {
        return transactionRepository.findByFromUserId(fromUserId).stream().map(transactionMapper::toDTO).toList();
    }

    @Override
    public List<TransactionResponseDTO> getByToUser(Long toUserId) {
        return transactionRepository.findByToUserId(toUserId).stream().map(transactionMapper::toDTO).toList();
    }

    @Override
    public List<TransactionResponseDTO> getUserHistory(Long userId) {
        return transactionRepository.findByFromUserIdOrToUserIdOrderByCreatedAtDesc(userId, userId).stream().map(transactionMapper::toDTO).toList();
    }

    @Override
    public List<TransactionResponseDTO> getByType(TransactionType type) {
        return transactionRepository.findByType(type).stream().map(transactionMapper::toDTO).toList();
    }

    @Override
    public Long countByType(TransactionType type) {
        return transactionRepository.countByType(type);
    }

    @Override
    public List<TransactionResponseDTO> getBetweenDates(LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByCreatedAtBetween(start, end).stream().map(transactionMapper::toDTO).toList();
    }

    @Override
    public List<TransactionResponseDTO> getByTypeBetweenDates(TransactionType type, LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByTypeAndCreatedAtBetween(type, start, end).stream().map(transactionMapper::toDTO).toList();
    }

    @Override
    public List<TransactionResponseDTO> search(
            TransactionFilterDTO filter
    ) {

        System.out.println(filter);
        System.out.println(filter.getTransactionType());

        return transactionRepository

                .findAll(

                        TransactionSpecification.filter(filter)

                )

                .stream()

                .map(transactionMapper::toDTO)

                .toList();

    }
}
