package com.wordbridge.project.payment;

import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.sslcommerz.SSLCommerzService;
import com.wordbridge.project.sslcommerz.dto.SSLCallbackDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("deposit/{userId}")
    public ResponseEntity<DepositSessionResponseDTO> createDeposit(
            @PathVariable Long userId,
            @RequestParam BigDecimal amount
    ) {
        return ResponseEntity.ok(
                paymentService.createDeposit(userId, amount)
        );
    }

    @GetMapping("{id}")
    public ResponseEntity<PaymentResponseDTO> getById(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                paymentService.getById(id)
        );
    }

    @GetMapping("gateway/{gatewayTransactionId}")
    public ResponseEntity<PaymentResponseDTO> getByGatewayTransactionId(
            @PathVariable String gatewayTransactionId
    ) {
        return ResponseEntity.ok(
                paymentService.getByGatewayTransactionId(gatewayTransactionId)
        );
    }

    @GetMapping("user/{userId}")
    public ResponseEntity<List<PaymentResponseDTO>> getUserPayments(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                paymentService.getUserPayments(userId)
        );
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponseDTO>> getAll() {
        return ResponseEntity.ok(
                paymentService.getAll()
        );
    }

    @GetMapping("status/{status}")
    public ResponseEntity<List<PaymentResponseDTO>> getByStatus(
            @PathVariable PaymentStatus status
    ) {
        return ResponseEntity.ok(
                paymentService.getByStatus(status)
        );
    }

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
}
