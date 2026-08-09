package com.wordbridge.project.report;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.ReportStatus;
import com.wordbridge.project.enums.ReportType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Enumerated(EnumType.STRING)
    private ReportType type;

    private String subject;


    @Column(columnDefinition = "TEXT")
    private String description;

    private String attachmentUrl;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;


    @Column(columnDefinition = "TEXT")
    private String adminReply;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime resolvedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


}
