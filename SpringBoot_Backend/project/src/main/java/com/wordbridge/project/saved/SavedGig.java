package com.wordbridge.project.saved;

import com.wordbridge.project.entity.User;
import com.wordbridge.project.gig.Gig;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "savedgigs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SavedGig {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Gig gig;

    @CreationTimestamp
    private LocalDateTime createdAt;


}
