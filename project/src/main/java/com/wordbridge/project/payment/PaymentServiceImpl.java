package com.wordbridge.project.payment;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.TransactionType;
import com.wordbridge.project.repository.UserRepository;
import com.wordbridge.project.sslcommerz.SSLCommerzService;
import com.wordbridge.project.sslcommerz.dto.SSLSessionResponseDTO;
import com.wordbridge.project.transaction.TransactionService;
import com.wordbridge.project.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    public final PaymentMapper paymentMapper;
    public final WalletService walletService;
    public final SSLCommerzService sslCommerzService;
    private static final String PAYMENT_GATEWAY = "SSLCommerz";




    @Override
    @Transactional
    public DepositSessionResponseDTO createDeposit(Long userId, BigDecimal amount) {
        Payment payment = new Payment();

        payment.setPaymentStatus(PaymentStatus.PENDING);

        validateAmount(amount);
        payment.setAmount(amount);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("No user found"));

        payment.setUser(user);

        payment.setGatewayTransactionId(
                "DEP-" + System.currentTimeMillis() + user.getEmail().substring(0, 5)
        );

        // Will be updated after SSLCommerz callback
        payment.setValidationId(null);
        payment.setPaymentMethod(null);
        payment.setGateway(PAYMENT_GATEWAY);
        payment.setFailureReason(null);

        Payment saved = paymentRepository.save(payment);

        SSLSessionResponseDTO session =
                sslCommerzService.createSession(saved);

        DepositSessionResponseDTO dto = new DepositSessionResponseDTO();
        dto.setPaymentId(saved.getId());
        dto.setGatewayTransactionId(saved.getGatewayTransactionId());
        dto.setGatewayPageUrl(session.getGatewayPageURL());
        dto.setPaymentStatus(saved.getPaymentStatus());

        return dto;


    }


    @Override
    public PaymentResponseDTO getById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Payment found"));
        return paymentMapper.toDTO(payment);
    }

    @Override
    public PaymentResponseDTO getByGatewayTransactionId(String gatewayTransactionId) {

        Payment payment = paymentRepository.findByGatewayTransactionId(gatewayTransactionId)
                .orElseThrow(() -> new RuntimeException("No Payment found"));
        return paymentMapper.toDTO(payment);
    }

    @Override
    public List<PaymentResponseDTO> getUserPayments(Long userId) {

        return paymentRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(paymentMapper::toDTO).toList();
    }

    @Override
    public List<PaymentResponseDTO> getAll() {
        return paymentRepository.findAll().stream().map(paymentMapper::toDTO).toList();
    }

    @Override
    public List<PaymentResponseDTO> getByStatus(PaymentStatus status) {
        return paymentRepository.findByPaymentStatus(status).stream().map(paymentMapper::toDTO).toList();
    }


    @Override
    @Transactional
    public void markSuccess(String gatewayTransactionId, String validationId, String paymentMethod) {
        Payment payment = paymentRepository.findByGatewayTransactionId(gatewayTransactionId)
                .orElseThrow(() -> new RuntimeException("No payment found"));
        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            return;
        }
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setValidationId(validationId);
        payment.setPaymentMethod(paymentMethod);

        paymentRepository.save(payment);

        walletService.deposit(
                payment.getUser().getId(), payment.getAmount()
        );



    }

    @Override
    @Transactional
    public void markFailed(String gatewayTransactionId, String reason) {
        Payment payment = paymentRepository.findByGatewayTransactionId(gatewayTransactionId)
                .orElseThrow(() -> new RuntimeException("No payment found"));

        payment.setPaymentStatus(PaymentStatus.FAILED);
        payment.setFailureReason(reason);

        paymentRepository.save(payment);

    }

    @Override
    @Transactional
    public void markCancelled(String gatewayTransactionId, String reason) {
        Payment payment = paymentRepository.findByGatewayTransactionId(gatewayTransactionId)
                .orElseThrow(() -> new RuntimeException("No payment found"));

        payment.setPaymentStatus(PaymentStatus.CANCELLED);
        payment.setFailureReason(reason);

        paymentRepository.save(payment);

    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new RuntimeException("Amount is required.");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount must be greater than zero.");
        }
    }
}
