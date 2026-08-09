package com.wordbridge.project.withdraw;

import com.wordbridge.project.wallet.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface WithdrawRepository extends JpaRepository<Withdraw, Long> {

    // All withdraws of a wallet
    List<Withdraw> findByWalletId(Long walletId);

    // All withdraws of a user
    List<Withdraw> findByWalletUserId(Long userId);

    Optional<Withdraw> findByIdAndWalletUserId(Long withdrawId, Long userId);

    // Pending requests (Admin page)
    List<Withdraw> findByWithdrawStatusOrderByCreatedAtDesc(WithdrawStatus withdrawStatus);

    Long countByWithdrawStatus(WithdrawStatus withdrawStatus);

    // Pending requests of a specific user
    List<Withdraw> findByWalletUserIdAndWithdrawStatus(
            Long userId,
            WithdrawStatus withdrawStatus
    );

    // Optional: newest first
    List<Withdraw> findByWalletUserIdOrderByCreatedAtDesc(Long userId);

    // Optional: admin dashboard
    List<Withdraw> findAllByOrderByCreatedAtDesc();


    List<Withdraw> findByWalletIdAndWithdrawStatus(Long walletId, WithdrawStatus status);

    @Query("""
                SELECT COALESCE(SUM(w.amount), 0)
                FROM Withdraw w
                WHERE w.withdrawStatus = :status
            """)
    BigDecimal sumAmountByStatus(@Param("status") WithdrawStatus status);


    @Query("""
            SELECT COALESCE(SUM(w.amount), 0)
            FROM Withdraw w
            WHERE w.wallet.user.id = :userId
            AND w.withdrawStatus =
            com.wordbridge.project.withdraw.WithdrawStatus.PENDING
            """)
    BigDecimal getPendingWithdrawals(
            @Param("userId") Long userId
    );

}
