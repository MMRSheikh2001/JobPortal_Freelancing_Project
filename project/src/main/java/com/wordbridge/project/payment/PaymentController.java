package com.wordbridge.project.payment;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.sslcommerz.SSLCommerzService;
import com.wordbridge.project.sslcommerz.dto.SSLCallbackDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/payments/")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;
    private final SSLCommerzService sslCommerzService;

    private final AuthenticationService authenticationService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("deposit/{userId}")
    public ResponseEntity<DepositSessionResponseDTO> createDeposit(
            @PathVariable Long userId,
            @RequestParam BigDecimal amount
    ) {
        checkUserIdOwnership(userId);
        return ResponseEntity.ok(
                paymentService.createDeposit(userId, amount)
        );
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("{id}")
    public ResponseEntity<PaymentResponseDTO> getById(
            @PathVariable Long id
    ) {
        PaymentResponseDTO payment = paymentService.getById(id);
        checkUserIdOwnership(payment.getUserId());
        return ResponseEntity.ok(payment);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("gateway/{gatewayTransactionId}")
    public ResponseEntity<PaymentResponseDTO> getByGatewayTransactionId(
            @PathVariable String gatewayTransactionId
    ) {
        PaymentResponseDTO payment = paymentService.getByGatewayTransactionId(gatewayTransactionId);
        checkUserIdOwnership(payment.getUserId());
        return ResponseEntity.ok(payment);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("user/{userId}")
    public ResponseEntity<List<PaymentResponseDTO>> getUserPayments(
            @PathVariable Long userId
    ) {
        checkUserIdOwnership(userId);
        return ResponseEntity.ok(
                paymentService.getUserPayments(userId)
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAll() {
        return ResponseEntity.ok(
                paymentService.getAll()
        );
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("status/{status}")
    public ResponseEntity<List<PaymentResponseDTO>> getByStatus(
            @PathVariable PaymentStatus status
    ) {
        return ResponseEntity.ok(
                paymentService.getByStatus(status)
        );
    }

    @PreAuthorize("permitAll()")
    @PostMapping("success")
    public void paymentSuccess(
            @RequestParam("tran_id") String transactionId,
            @RequestParam("val_id") String validationId,
            @RequestParam("status") String status,
            @RequestParam("amount") String amount,
            @RequestParam(value = "card_type", required = false) String paymentMethod,
            @RequestParam(value = "bank_tran_id", required = false) String bankTransactionId,
            @RequestParam(value = "currency", required = false) String currency,
            HttpServletResponse response
    ) throws IOException {

        SSLCallbackDTO dto = new SSLCallbackDTO();

        dto.setTransactionId(transactionId);
        dto.setValidationId(validationId);
        dto.setStatus(status);
        dto.setAmount(amount);
        dto.setPaymentMethod(paymentMethod);
        dto.setBankTransactionId(bankTransactionId);
        dto.setCurrency(currency);

        sslCommerzService.handleSuccess(dto);

        PaymentResponseDTO payment =
                paymentService.getByGatewayTransactionId(transactionId);
        if (payment.getUserRole() == UserRole.USER) {
            response.sendRedirect("http://localhost:4200/user/payment/success");
        } else {
            response.sendRedirect("http://localhost:4200/company/payment/success");
        }
    }


//    @PostMapping("success")
//    public ResponseEntity<String> success(HttpServletRequest request) {
//
//        System.out.println("========= RAW CALLBACK =========");
//
//        request.getParameterMap().forEach((k, v) ->
//                System.out.println(k + " = " + Arrays.toString(v)));
//
//        System.out.println("===============================");
//
//        return ResponseEntity.ok("OK");
//    }

    @PreAuthorize("permitAll()")
    @PostMapping("fail")
    public void paymentFailed(
            @ModelAttribute SSLCallbackDTO callback,
            HttpServletResponse response
    ) throws IOException {
        sslCommerzService.handleFailure(callback);

        PaymentResponseDTO payment =
                paymentService.getByGatewayTransactionId(callback.getTransactionId());
        if (payment.getUserRole() == UserRole.USER) {
            response.sendRedirect("http://localhost:4200/user/payment/failure");
        } else {
            response.sendRedirect("http://localhost:4200/company/payment/failure");
        }


    }

    @PreAuthorize("permitAll()")
    @PostMapping("cancel")
    public void paymentCancelled(
            @ModelAttribute SSLCallbackDTO callback,
            HttpServletResponse response
    ) throws IOException {
        sslCommerzService.handleCancellation(callback);

        PaymentResponseDTO payment =
                paymentService.getByGatewayTransactionId(callback.getTransactionId());
        if (payment.getUserRole() == UserRole.USER) {
            response.sendRedirect("http://localhost:4200/user/payment/cancel");
        } else {
            response.sendRedirect("http://localhost:4200/company/payment/cancel");
        }
    }

    private void checkUserIdOwnership(Long userId) {
        User currentUser = authenticationService.getCurrentUser();
        if (!currentUser.getId().equals(userId) && currentUser.getRole() != UserRole.ADMIN) {
            throw new AccessDeniedException("Not allowed");
        }
    }
}
