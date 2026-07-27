package com.wordbridge.project.transaction;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.TransactionType;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions/")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;
    private final AuthenticationService authenticationService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<TransactionResponseDTO> getById(@PathVariable Long id) {
        TransactionResponseDTO dto = transactionService.getById(id);
        checkParticipantOrAdmin(dto);
        return ResponseEntity.ok(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAll() {
        return ResponseEntity.ok(transactionService.getAll());
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("from/{userId}")
    public ResponseEntity<List<TransactionResponseDTO>> getByFromUser(@PathVariable Long userId) {
        checkUserIdOwnership(userId);
        return ResponseEntity.ok(transactionService.getByFromUser(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("to/{userId}")
    public ResponseEntity<List<TransactionResponseDTO>> getByToUser(@PathVariable Long userId) {
        checkUserIdOwnership(userId);
        return ResponseEntity.ok(transactionService.getByToUser(userId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("history/{userId}")
    public ResponseEntity<List<TransactionResponseDTO>> getUserHistory(@PathVariable Long userId) {
        checkUserIdOwnership(userId);
        return ResponseEntity.ok(transactionService.getUserHistory(userId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("type/{type}")
    public ResponseEntity<List<TransactionResponseDTO>> getByType(
            @PathVariable TransactionType type) {
        return ResponseEntity.ok(transactionService.getByType(type));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("count/{type}")
    public ResponseEntity<Long> countByType(
            @PathVariable TransactionType type) {
        return ResponseEntity.ok(transactionService.countByType(type));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("between")
    public ResponseEntity<List<TransactionResponseDTO>> getBetweenDates(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(transactionService.getBetweenDates(start, end));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/between/type/{type}")
    public ResponseEntity<List<TransactionResponseDTO>> getByTypeBetweenDates(
            @PathVariable TransactionType type,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(transactionService.getByTypeBetweenDates(type, start, end));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("search")
    public ResponseEntity<List<TransactionResponseDTO>> search(
            @RequestBody TransactionFilterDTO filter
    ) {
        System.out.println("Controller = " + filter);
        return ResponseEntity.ok(
                transactionService.search(filter)
        );
    }


    private void checkUserIdOwnership(Long userId) {
        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(userId) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
    }

    private void checkParticipantOrAdmin(TransactionResponseDTO dto) {
        User currentUser = authenticationService.getCurrentUser();
        if (currentUser.getRole() == UserRole.ADMIN) return;
        boolean isSender = dto.getFromUserId() != null && dto.getFromUserId().equals(currentUser.getId());
        boolean isReceiver = dto.getToUserId() != null && dto.getToUserId().equals(currentUser.getId());
        if (!isSender && !isReceiver) {
            throw new AccessDeniedException("Not allowed");
        }
    }

}
