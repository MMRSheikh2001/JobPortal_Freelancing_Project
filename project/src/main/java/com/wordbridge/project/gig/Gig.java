package com.wordbridge.project.gig;

import com.wordbridge.project.entity.Category;
import com.wordbridge.project.entity.UserProfile;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "gigs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Gig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 500)
    private String shortDescription;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal startingPrice;
    private Integer deliveryDays;
    private Integer revisions;

    private String gigImage;

    private Boolean isActive;

    @CreationTimestamp
    private LocalDateTime createdAt;


    @CreationTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne
    private Category category;

    @ManyToOne
    @JoinColumn(name = "user_profile_id")
    private UserProfile userProfile;


    private Double averageRating;

    private Integer totalReviews;

    private Integer completedOrders;


}
