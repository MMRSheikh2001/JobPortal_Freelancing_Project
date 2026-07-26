package com.wordbridge.project.conversation;

import com.wordbridge.project.enums.ConversationStatus;
import com.wordbridge.project.gigorder.GigOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "conversations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime    lastMessageAt;

    @OneToOne
    @JoinColumn(name = "gig_order_id", unique = true)
    private GigOrder gigOrder;

    @Enumerated(EnumType.STRING)
    private ConversationStatus status;

}
