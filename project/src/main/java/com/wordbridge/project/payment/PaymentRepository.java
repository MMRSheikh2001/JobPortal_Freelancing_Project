package com.wordbridge.project.payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    // Find by gateway transaction ID (most important)
    Optional<Payment> findByGatewayTransactionId(String gatewayTransactionId);

    // User payment history
    List<Payment> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Filter by payment status
    List<Payment> findByPaymentStatus(PaymentStatus paymentStatus);


    // Admin reports
    List<Payment> findByCreatedAtBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    List<Payment> findByPaymentStatusAndCreatedAtBetween(
            PaymentStatus paymentStatus,
            LocalDateTime start,
            LocalDateTime end
    );


    // Dashboard counts
    long countByPaymentStatus(PaymentStatus paymentStatus);


}
