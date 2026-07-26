package com.wordbridge.project.transaction;

import com.wordbridge.project.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions/")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("{id}")
    public ResponseEntity<TransactionResponseDTO> getById(@PathVariable Long id) {
        TransactionResponseDTO dto = transactionService.getById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAll() {
        return ResponseEntity.ok(transactionService.getAll());
    }

    @GetMapping("from/{userId}")
    public ResponseEntity<List<TransactionResponseDTO>> getByFromUser(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionService.getByFromUser(userId));
    }

    @GetMapping("to/{userId}")
    public ResponseEntity<List<TransactionResponseDTO>> getByToUser(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionService.getByToUser(userId));
    }

    @GetMapping("history/{userId}")
    public ResponseEntity<List<TransactionResponseDTO>> getUserHistory(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionService.getUserHistory(userId));
    }

    @GetMapping("type/{type}")
    public ResponseEntity<List<TransactionResponseDTO>> getByType(
            @PathVariable TransactionType type) {
        return ResponseEntity.ok(transactionService.getByType(type));
    }

    @GetMapping("count/{type}")
    public ResponseEntity<Long> countByType(
            @PathVariable TransactionType type) {
        return ResponseEntity.ok(transactionService.countByType(type));
    }

    @GetMapping("between")
    public ResponseEntity<List<TransactionResponseDTO>> getBetweenDates(
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(transactionService.getBetweenDates(start, end));
    }

    @GetMapping("/between/type/{type}")
    public ResponseEntity<List<TransactionResponseDTO>> getByTypeBetweenDates(
            @PathVariable TransactionType type,
            @RequestParam LocalDateTime start,
            @RequestParam LocalDateTime end) {
        return ResponseEntity.ok(transactionService.getByTypeBetweenDates(type, start, end));
    }

    @PostMapping("search")
    public ResponseEntity<List<TransactionResponseDTO>> search(
            @RequestBody TransactionFilterDTO filter
    ) {
        System.out.println("Controller = " + filter);
        return ResponseEntity.ok(
                transactionService.search(filter)
        );
    }

}
