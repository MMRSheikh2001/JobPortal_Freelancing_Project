package com.wordbridge.project.sslcommerz;


import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.notification.NotificationService;
import com.wordbridge.project.notification.NotificationType;
import com.wordbridge.project.payment.Payment;
import com.wordbridge.project.payment.PaymentRepository;
import com.wordbridge.project.payment.PaymentStatus;
import com.wordbridge.project.sslcommerz.dto.SSLCallbackDTO;
import com.wordbridge.project.sslcommerz.dto.SSLSessionRequestDTO;
import com.wordbridge.project.sslcommerz.dto.SSLSessionResponseDTO;
import com.wordbridge.project.sslcommerz.dto.SSLValidationResponseDTO;
import com.wordbridge.project.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class SSLCommerzServiceImpl implements SSLCommerzService {
    private final SSLCommerzConfig sslCommerzConfig;
    private final RestClient restClient;
    private final PaymentRepository paymentRepository;
    private final NotificationService notificationService;
    private final WalletService walletService;


    @Override
    public SSLSessionResponseDTO createSession(Payment payment) {
        SSLSessionRequestDTO requestDTO = new SSLSessionRequestDTO();

        requestDTO.setStore_id(sslCommerzConfig.getStoreId());
        requestDTO.setStore_passwd(sslCommerzConfig.getStorePassword());
        requestDTO.setTotal_amount(payment.getAmount().toString());
        requestDTO.setCurrency("BDT");
        requestDTO.setTran_id(payment.getGatewayTransactionId());
        requestDTO.setSuccess_url(sslCommerzConfig.getSuccessUrl());
        requestDTO.setFail_url(sslCommerzConfig.getFailUrl());
        requestDTO.setCancel_url(sslCommerzConfig.getCancelUrl());
        requestDTO.setProduct_name("Wallet Deposit");
        requestDTO.setProduct_category("Deposit");
        requestDTO.setProduct_profile("general");
        if (payment.getUser().getRole() == UserRole.USER) {
            requestDTO.setCus_name(payment.getUser().getUserProfile().getName());
            requestDTO.setCus_phone(payment.getUser().getUserProfile().getPhone());
        } else if (payment.getUser().getRole() == UserRole.COMPANY) {
            requestDTO.setCus_name(payment.getUser().getCompanyProfile().getName());
            requestDTO.setCus_phone(payment.getUser().getCompanyProfile().getPhone());
        } else {
            throw new RuntimeException("Admin can not deposit money");
        }
        requestDTO.setCus_email(payment.getUser().getEmail());


        //Making Request dto SSL Commerz compatible
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

        form.add("store_id", requestDTO.getStore_id());
        form.add("store_passwd", requestDTO.getStore_passwd());

        form.add("total_amount", requestDTO.getTotal_amount());
        form.add("currency", requestDTO.getCurrency());
        form.add("tran_id", requestDTO.getTran_id());

        form.add("success_url", requestDTO.getSuccess_url());
        form.add("fail_url", requestDTO.getFail_url());
        form.add("cancel_url", requestDTO.getCancel_url());

        form.add("product_name", requestDTO.getProduct_name());
        form.add("product_category", requestDTO.getProduct_category());
        form.add("product_profile", requestDTO.getProduct_profile());

        form.add("cus_name", requestDTO.getCus_name());
        form.add("cus_email", requestDTO.getCus_email());
        form.add("cus_phone", requestDTO.getCus_phone());

        System.out.println("========== SESSION REQUEST ==========");
        form.forEach((k, v) -> System.out.println(k + " = " + v));
        System.out.println("=====================================");

        //Sending request and receiving response
        SSLSessionResponseDTO response = restClient.post()
                .uri(sslCommerzConfig.getSessionUrl())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(SSLSessionResponseDTO.class);

        System.out.println(response);

        return response;
    }

    @Override
    @Transactional
    public void handleSuccess(SSLCallbackDTO callback) {

        // Verify payment with SSLCommerz
        SSLValidationResponseDTO validation =
                validatePayment(callback.getValidationId());

        // Ensure Validation API responded successfully
        if (!"DONE".equalsIgnoreCase(validation.getApiConnect())) {
            throw new RuntimeException("Could not connect to SSLCommerz Validation API.");
        }
        System.out.println("========== VALIDATION ==========");
        System.out.println("API Connect : " + validation.getApiConnect());
        System.out.println("Status      : " + validation.getStatus());
        System.out.println("Amount      : " + validation.getAmount());
        System.out.println("ValidationId: " + validation.getValidationId());
        System.out.println("PaymentType : " + validation.getPaymentMethod());
        System.out.println("================================");

        // Ensure SSLCommerz confirms payment is valid
        String status = validation.getStatus().toUpperCase();

        if (!(status.equals("VALID") || status.equals("VALIDATED"))) {
            throw new RuntimeException("Payment validation failed.");
        }

        // Find payment in database
        Payment payment = paymentRepository
                .findByGatewayTransactionId(callback.getTransactionId())
                .orElseThrow(() -> new RuntimeException("Payment not found."));

        // Prevent duplicate deposits
        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            return;
        }

        // Verify amount
        BigDecimal paidAmount = new BigDecimal(validation.getAmount());

        if (payment.getAmount().compareTo(paidAmount) != 0) {
            throw new RuntimeException("Payment amount mismatch.");
        }

        // Update payment
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setValidationId(validation.getValidationId());
        payment.setPaymentMethod(validation.getPaymentMethod());

        paymentRepository.save(payment);

        // Deposit into wallet
        walletService.deposit(
                payment.getUser().getId(),
                payment.getAmount()
        );

        //Notification
        notificationService.createNotification(
                payment.getUser().getId(),
                "Deposit Successful",
                "৳" + payment.getAmount() + " has been added to your wallet.",
                NotificationType.DEPOSIT_SUCCESS,
                payment.getId()
        );

    }

    @Override
    public void handleFailure(SSLCallbackDTO callback) {


        Payment payment = paymentRepository.findByGatewayTransactionId(callback.getTransactionId())
                .orElseThrow(() -> new RuntimeException("No payment found"));

        payment.setPaymentStatus(PaymentStatus.FAILED);
        payment.setFailureReason(callback.getFailedReason());

        paymentRepository.save(payment);

    }

    @Override
    public void handleCancellation(SSLCallbackDTO callback) {


        Payment payment = paymentRepository.findByGatewayTransactionId(callback.getTransactionId())
                .orElseThrow(() -> new RuntimeException("No payment found"));

        payment.setPaymentStatus(PaymentStatus.CANCELLED);
        payment.setFailureReason(callback.getFailedReason());

        paymentRepository.save(payment);
    }

    @Override
    public SSLValidationResponseDTO validatePayment(String validationId) {

        String url = sslCommerzConfig.getValidationUrl()
                + "?val_id=" + validationId
                + "&store_id=" + sslCommerzConfig.getStoreId()
                + "&store_passwd=" + sslCommerzConfig.getStorePassword()
                + "&format=json";
        System.out.println(url);
        //temporary
        String response = restClient.get()
                .uri(url)
                .retrieve()
                .body(String.class);

        System.out.println(response);


        return restClient.get()
                .uri(url)
                .retrieve()
                .body(SSLValidationResponseDTO.class);
    }
}
