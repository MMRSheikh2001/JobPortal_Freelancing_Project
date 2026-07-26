package com.wordbridge.project.message;

import com.wordbridge.project.conversation.Conversation;
import com.wordbridge.project.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    private Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private User sender;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String messageText;

    private String attachment;

    private Boolean isRead;

    @CreationTimestamp
    private LocalDateTime sentAt;

}
