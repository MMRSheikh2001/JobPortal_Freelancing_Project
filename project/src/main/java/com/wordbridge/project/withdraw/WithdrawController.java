package com.wordbridge.project.withdraw;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/withdraws/")
@RequiredArgsConstructor
public class WithdrawController {

    private final WithdrawService withdrawService;


    // USER


    @PostMapping
    public ResponseEntity<WithdrawResponseDTO> createWithdraw(
            @RequestBody WithdrawRequestDTO requestDTO
    ) {
        return ResponseEntity.ok(
                withdrawService.createWithdraw(requestDTO)
        );
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<List<WithdrawResponseDTO>> getUserWithdraws(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                withdrawService.getUserWithdraws(userId)
        );
    }

    @GetMapping("{withdrawId}/user/{userId}")
    public ResponseEntity<WithdrawResponseDTO> getWithdrawById(
            @PathVariable Long withdrawId,
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                withdrawService.getWithdrawById(withdrawId, userId)
        );
    }


    // ADMIN


    @GetMapping("pending")
    public ResponseEntity<List<WithdrawResponseDTO>> getPendingWithdraws() {
        return ResponseEntity.ok(
                withdrawService.getPendingWithdraws()
        );
    }

    @GetMapping("approved")
    public ResponseEntity<List<WithdrawResponseDTO>> getApprovedWithdraws() {
        return ResponseEntity.ok(
                withdrawService.getApproovedWithdraws()
        );
    }

    @GetMapping("rejected")
    public ResponseEntity<List<WithdrawResponseDTO>> getRejectedWithdraws() {
        return ResponseEntity.ok(
                withdrawService.getRejectedWithdraws()
        );
    }

    @PatchMapping("{withdrawId}/approve")
    public ResponseEntity<WithdrawResponseDTO> approveWithdraw(
            @PathVariable Long withdrawId,
            @RequestParam String adminRemarks,
            @RequestParam String transactionReference
    ) {
        return ResponseEntity.ok(
                withdrawService.approveWithdraw(
                        withdrawId,
                        adminRemarks,
                        transactionReference
                )
        );
    }

    @PatchMapping("{withdrawId}/reject")
    public ResponseEntity<WithdrawResponseDTO> rejectWithdraw(
            @PathVariable Long withdrawId,
            @RequestParam String adminRemarks
    ) {
        return ResponseEntity.ok(
                withdrawService.rejectWithdraw(
                        withdrawId,
                        adminRemarks
                )
        );
    }
}
