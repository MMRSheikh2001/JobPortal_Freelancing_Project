package com.wordbridge.project.payment;

import com.wordbridge.project.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;




    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private BigDecimal amount;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String gatewayTransactionId;

    private String validationId;

    private String paymentMethod;

    private String gateway;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String failureReason;


}
