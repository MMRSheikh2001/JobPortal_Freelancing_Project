package com.wordbridge.project.withdraw;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/withdraws/")
@RequiredArgsConstructor
public class WithdrawController {

    private final WithdrawService withdrawService;

    private final AuthenticationService authenticationService;


    // USER


    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<WithdrawResponseDTO> createWithdraw(
            @RequestBody WithdrawRequestDTO requestDTO
    ) {
        User currentUser = authenticationService.getCurrentUser();
        requestDTO.setUserId(currentUser.getId());
        return ResponseEntity.ok(withdrawService.createWithdraw(requestDTO));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("user/{userId}")
    public ResponseEntity<List<WithdrawResponseDTO>> getUserWithdraws(
            @PathVariable Long userId
    ) {
        checkUserIdOwnership(userId);
        return ResponseEntity.ok(
                withdrawService.getUserWithdraws(userId)
        );
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{withdrawId}/user/{userId}")
    public ResponseEntity<WithdrawResponseDTO> getWithdrawById(
            @PathVariable Long withdrawId,
            @PathVariable Long userId
    ) {
        checkUserIdOwnership(userId);
        return ResponseEntity.ok(
                withdrawService.getWithdrawById(withdrawId, userId)
        );
    }


    // ADMIN


    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("pending")
    public ResponseEntity<List<WithdrawResponseDTO>> getPendingWithdraws() {
        return ResponseEntity.ok(
                withdrawService.getPendingWithdraws()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("approved")
    public ResponseEntity<List<WithdrawResponseDTO>> getApprovedWithdraws() {
        return ResponseEntity.ok(
                withdrawService.getApproovedWithdraws()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("rejected")
    public ResponseEntity<List<WithdrawResponseDTO>> getRejectedWithdraws() {
        return ResponseEntity.ok(
                withdrawService.getRejectedWithdraws()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
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

    @PreAuthorize("hasRole('ADMIN')")
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


    private void checkUserIdOwnership(Long userId) {
        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(userId) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
    }
}
