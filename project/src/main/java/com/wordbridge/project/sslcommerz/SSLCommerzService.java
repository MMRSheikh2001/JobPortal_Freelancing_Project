package com.wordbridge.project.sslcommerz;

import com.wordbridge.project.payment.Payment;
import com.wordbridge.project.sslcommerz.dto.SSLCallbackDTO;
import com.wordbridge.project.sslcommerz.dto.SSLSessionResponseDTO;
import com.wordbridge.project.sslcommerz.dto.SSLValidationResponseDTO;
import org.springframework.stereotype.Service;


public interface SSLCommerzService {
    SSLSessionResponseDTO createSession(Payment payment);

    void handleSuccess(SSLCallbackDTO callback);

    void handleFailure(SSLCallbackDTO callback);

    void handleCancellation(SSLCallbackDTO callback);

    SSLValidationResponseDTO validatePayment(String validationId);

}
