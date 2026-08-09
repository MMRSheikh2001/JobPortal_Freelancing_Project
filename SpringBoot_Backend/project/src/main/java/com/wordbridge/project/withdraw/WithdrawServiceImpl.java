package com.wordbridge.project.withdraw;

import com.wordbridge.project.enums.TransactionType;
import com.wordbridge.project.notification.NotificationService;
import com.wordbridge.project.notification.NotificationType;
import com.wordbridge.project.transaction.TransactionService;
import com.wordbridge.project.wallet.Wallet;
import com.wordbridge.project.wallet.WalletRepository;
import com.wordbridge.project.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WithdrawServiceImpl implements WithdrawService {
    private final WithdrawRepository withdrawRepository;
    private final WithdrawMapper withdrawMapper;
    private final WalletRepository walletRepository;
    private final WalletService walletService;
    private final NotificationService notificationService;
    private final TransactionService transactionService;


    @Override
    public WithdrawResponseDTO createWithdraw(WithdrawRequestDTO requestDTO) {
        Withdraw withdraw = new Withdraw();
        Wallet wallet = walletRepository.findByUserId(requestDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("No wallet found"));
        if (wallet.getBalance().compareTo(requestDTO.getAmount()) < 0) {
            throw new RuntimeException("Insufficient wallet balance.");
        }
        if (requestDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid amount.");
        }

        withdraw.setWallet(wallet);
        withdraw.setAmount(requestDTO.getAmount());
        withdraw.setWithdrawMethod(requestDTO.getWithdrawMethod());
        withdraw.setAccountNumber(requestDTO.getAccountNumber());
        withdraw.setAccountName(requestDTO.getAccountName());

        withdraw.setWithdrawStatus(WithdrawStatus.PENDING);

        Withdraw saved = withdrawRepository.save(withdraw);

        return withdrawMapper.toDTO(saved);
    }

    @Override
    public List<WithdrawResponseDTO> getUserWithdraws(Long userId) {


        return withdrawRepository.findByWalletUserId(userId).stream().map(withdrawMapper::toDTO).toList();
    }

    @Override
    public WithdrawResponseDTO getWithdrawById(Long withdrawId, Long userId) {
        Withdraw withdraw = withdrawRepository.findByIdAndWalletUserId(withdrawId, userId)
                .orElseThrow(() -> new RuntimeException("No withdraw found"));
        return withdrawMapper.toDTO(withdraw);
    }

    @Override
    public List<WithdrawResponseDTO> getPendingWithdraws() {
        return withdrawRepository.findByWithdrawStatusOrderByCreatedAtDesc(WithdrawStatus.PENDING).stream().map(withdrawMapper::toDTO).toList();
    }

    @Override
    public Long countPendingWithdraws() {
        return withdrawRepository.countByWithdrawStatus(WithdrawStatus.PENDING);
    }

    @Override
    public List<WithdrawResponseDTO> getRejectedWithdraws() {
        return withdrawRepository.findByWithdrawStatusOrderByCreatedAtDesc(WithdrawStatus.REJECTED).stream().map(withdrawMapper::toDTO).toList();
    }

    @Override
    public List<WithdrawResponseDTO> getApproovedWithdraws() {
        return withdrawRepository.findByWithdrawStatusOrderByCreatedAtDesc(WithdrawStatus.APPROVED).stream().map(withdrawMapper::toDTO).toList();
    }

    @Override
    @Transactional
    public WithdrawResponseDTO approveWithdraw(Long withdrawId, String adminRemarks, String transactionReference) {
        Withdraw withdraw = withdrawRepository.findById(withdrawId)
                .orElseThrow(() -> new RuntimeException("No withdraw found"));
        if (withdraw.getWithdrawStatus() != WithdrawStatus.PENDING) {
            throw new RuntimeException("Withdraw request already processed.");
        }
        Wallet wallet = withdraw.getWallet();

        if (wallet.getBalance().compareTo(withdraw.getAmount()) < 0) {
            throw new RuntimeException(
                    "User no longer has sufficient balance.");
        }

        walletService.withdraw(withdraw.getWallet().getUser().getId(), withdraw.getAmount());
        withdraw.setWithdrawStatus(WithdrawStatus.APPROVED);
        withdraw.setAdminRemarks(adminRemarks);
        withdraw.setTransactionReference(transactionReference);

        Withdraw saved = withdrawRepository.save(withdraw);


        notificationService.createNotification(
                saved.getWallet().getUser().getId(),
                "Withdrawal Approved",
                "Your withdrawal request has been approved.",
                NotificationType.WITHDRAW_APPROVED,
                withdraw.getId()
        );
        transactionService.createTransaction(TransactionType.WITHDRAW, null, saved.getWallet().getUser(), withdraw.getAmount(), "Money withdrawn");

        return withdrawMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public WithdrawResponseDTO rejectWithdraw(Long withdrawId, String adminRemarks) {
        Withdraw withdraw = withdrawRepository.findById(withdrawId)
                .orElseThrow(() -> new RuntimeException("No withdraw found"));
        if (withdraw.getWithdrawStatus() != WithdrawStatus.PENDING) {
            throw new RuntimeException("Withdraw request already processed.");
        }
        withdraw.setWithdrawStatus(WithdrawStatus.REJECTED);
        withdraw.setAdminRemarks(adminRemarks);

        Withdraw saved = withdrawRepository.save(withdraw);

        notificationService.createNotification(
                saved.getWallet().getUser().getId(),
                "Withdrawal Rejected",
                adminRemarks,
                NotificationType.WITHDRAW_REJECTED,
                withdraw.getId()
        );

        return withdrawMapper.toDTO(saved);
    }

    @Override
    public BigDecimal getTotalPendingWithdrawals(Long userId) {
        return withdrawRepository.getPendingWithdrawals(userId);
    }
}
