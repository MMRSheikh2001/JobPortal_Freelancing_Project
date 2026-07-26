package com.wordbridge.project.payment;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface PaymentService {

    // Create a new deposit payment (PENDING)
    DepositSessionResponseDTO createDeposit(Long userId, BigDecimal amount);


    // Get payment
    PaymentResponseDTO getById(Long id);

    // Find by SSLCommerz transaction id (tran_id)
    PaymentResponseDTO getByGatewayTransactionId(String gatewayTransactionId);

    // User payment history
    List<PaymentResponseDTO> getUserPayments(Long userId);

    // Admin
    List<PaymentResponseDTO> getAll();

    List<PaymentResponseDTO> getByStatus(PaymentStatus status);



    // SSLCommerz callback methods
    void markSuccess(
            String gatewayTransactionId,
            String validationId,
            String paymentMethod
    );

    void markFailed(
            String gatewayTransactionId,
            String reason
    );

    void markCancelled(
            String gatewayTransactionId,
            String reason
    );


}
