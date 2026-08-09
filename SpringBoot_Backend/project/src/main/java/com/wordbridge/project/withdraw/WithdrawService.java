package com.wordbridge.project.withdraw;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface WithdrawService {

    // User creates a withdraw request
    WithdrawResponseDTO createWithdraw(WithdrawRequestDTO requestDTO);

    // User views all of their withdraws
    List<WithdrawResponseDTO> getUserWithdraws(Long userId);

    // User views one withdraw
    WithdrawResponseDTO getWithdrawById(Long withdrawId, Long userId);

    // Admin views all pending requests
    List<WithdrawResponseDTO> getPendingWithdraws();
    Long  countPendingWithdraws();

    List<WithdrawResponseDTO> getRejectedWithdraws();
    List<WithdrawResponseDTO> getApproovedWithdraws();

    // Admin approves a withdraw
    WithdrawResponseDTO approveWithdraw(
            Long withdrawId,
            String adminRemarks,
            String transactionReference
    );

    // Admin rejects a withdraw
    WithdrawResponseDTO rejectWithdraw(
            Long withdrawId,
            String adminRemarks
    );

    BigDecimal getTotalPendingWithdrawals(Long userId);

}
