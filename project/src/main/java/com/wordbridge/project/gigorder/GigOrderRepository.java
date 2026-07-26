package com.wordbridge.project.gigorder;

import com.wordbridge.project.enums.GigOrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface GigOrderRepository extends JpaRepository<GigOrder, Long>,
        JpaSpecificationExecutor<GigOrder> {

    // Orders placed by a buyer
    List<GigOrder> findByBuyerId(Long buyerId);

    // Orders received by a seller (seller is derived from Gig)
    List<GigOrder> findByGigUserProfileId(Long userProfileId);

    // Orders for a specific gig
    List<GigOrder> findByGigId(Long gigId);

    Long countByGigId(Long gigId);

    // Filter by status
    List<GigOrder> findByStatus(GigOrderStatus status);

    Long countByStatus(GigOrderStatus status);


    // Buyer's orders by status
    List<GigOrder> findByBuyerIdAndStatus(Long buyerId, GigOrderStatus status);

    Long countByBuyerIdAndStatus(
            Long buyerId,
            GigOrderStatus status
    );

    // Seller's orders by status
    List<GigOrder> findByGigUserProfileIdAndStatus(Long userProfileId, GigOrderStatus status);

    Long countByGigUserProfileIdAndStatus(
            Long sellerId,
            GigOrderStatus status
    );

    // Prevent duplicate active orders (optional but useful)
    boolean existsByGigIdAndBuyerIdAndStatusIn(
            Long gigId,
            Long buyerId,
            Collection<GigOrderStatus> statuses
    );

    Boolean existsByGigIdAndBuyerId(
            Long gigId,
            Long buyerId
    );

    Long countByGigUserProfileId(Long sellerId);

    Long countByBuyerId(Long buyerId);


    List<GigOrder> findByStatusInAndSellerDisputeDeadlineBefore(
            List<GigOrderStatus> statuses,
            LocalDateTime now
    );

    Optional<GigOrder> findByGigIdAndBuyerIdAndStatusIn(
            Long gigId,
            Long buyerId,
            List<GigOrderStatus> statuses
    );

    Long countByBuyerIdAndStatusIn(Long buyerId, List<GigOrderStatus> statuses);

    @Query("""
            SELECT go
            FROM GigOrder go
            WHERE go.buyer.id = :userId
            ORDER BY go.createdAt DESC
            """)
    List<GigOrder> findRecentOrdersByUserId(
            Long userId,
            Pageable pageable
    );


    @Query("""
            SELECT go
            FROM GigOrder go
            WHERE go.buyer.id = :userId
            AND go.status IN (
                com.wordbridge.project.enums.GigOrderStatus.ORDER_PLACED,
                com.wordbridge.project.enums.GigOrderStatus.QUOTED,
                com.wordbridge.project.enums.GigOrderStatus.QUOTE_ACCEPTED,
                com.wordbridge.project.enums.GigOrderStatus.BUYER_CANCELLED,
                com.wordbridge.project.enums.GigOrderStatus.DELIVERED,
                com.wordbridge.project.enums.GigOrderStatus.BUYER_REJECTED,
                com.wordbridge.project.enums.GigOrderStatus.SELLER_DISPUTED
            )
            ORDER BY go.createdAt DESC
            """)
    List<GigOrder> findRecentActiveOrdersByUserId(
            Long userId,
            Pageable pageable
    );

    @Query("""
            SELECT COUNT(DISTINCT go.buyer.id)
            FROM GigOrder go
            WHERE go.status IN :statuses
            """)
    Long countActiveClients(
            @Param("statuses")
            List<GigOrderStatus> statuses
    );

    Long countByStatusIn(List<GigOrderStatus> statuses);

    Long countByGigUserProfileIdAndStatusIn(Long userProfileId,List<GigOrderStatus> statuses);

    @Query("""
SELECT COALESCE(SUM(g.finalPrice), 0)
FROM GigOrder g
WHERE g.gig.userProfile.id = :userProfileId
AND g.status IN (
    com.wordbridge.project.enums.GigOrderStatus.BUYER_ACCEPTED,
    com.wordbridge.project.enums.GigOrderStatus.PAYMENT_RELEASED
)
""")
    BigDecimal getLifetimeEarnings(
            @Param("userProfileId") Long userProfileId
    );


    Page<GigOrder> findByGigUserProfileIdOrderByCreatedAtDesc(
            Long userProfileId,
            Pageable pageable
    );

}
