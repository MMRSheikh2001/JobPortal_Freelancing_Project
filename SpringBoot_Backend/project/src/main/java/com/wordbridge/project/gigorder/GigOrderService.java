package com.wordbridge.project.gigorder;

import com.wordbridge.project.enums.GigOrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface GigOrderService {

    // Buyer

    GigOrderResponseDTO placeOrder(Long gigId, Long buyerId);

    GigOrderResponseDTO acceptQuote(Long orderId);

    GigOrderResponseDTO rejectQuote(Long orderId);

    GigOrderResponseDTO acceptDelivery(Long orderId);

    GigOrderResponseDTO rejectDelivery(Long orderId);

    GigOrderResponseDTO buyerCancelOrder(Long orderId);


    // Seller

    GigOrderResponseDTO sendQuote(Long orderId, BigDecimal quotedPrice);

    GigOrderResponseDTO deliverOrder(
            Long orderId,
            String deliveryMessage,
            MultipartFile deliveryFile
    );

    GigOrderResponseDTO cancelOrderBySeller(Long orderId);

    GigOrderResponseDTO raiseDispute(Long orderId);


    // Admin

    GigOrderResponseDTO releasePayment(Long orderId);

    GigOrderResponseDTO refundBuyer(Long orderId);


    // Queries

    List<GigOrderResponseDTO> getAll();

    GigOrderResponseDTO getById(Long id);

    List<GigOrderResponseDTO> getBuyerOrders(Long buyerId);

    List<GigOrderResponseDTO> getSellerOrders(Long sellerId);

    List<GigOrderResponseDTO> getByStatus(GigOrderStatus status);

    Long countByStatus(GigOrderStatus status);

    List<GigOrderResponseDTO> findByBuyerIdAndStatus(Long buyerId, GigOrderStatus status);

    Long countByBuyerIdAndStatus(
            Long buyerId,
            GigOrderStatus status
    );

    List<GigOrderResponseDTO> findByGigUserProfileIdAndStatus(Long sellerId, GigOrderStatus status);

    Long countByGigUserProfileIdAndStatus(
            Long sellerId,
            GigOrderStatus status
    );

    Boolean existsByGigIdAndBuyerId(
            Long gigId,
            Long buyerId
    );


    List<GigOrderResponseDTO> findByGigId(Long gigId);

    Long countByGigId(Long gigId);

    Long countByGigUserProfileId(Long sellerId);

    Long countByBuyerId(Long buyerId);

    //process expired disputes
    void processExpiredDisputes();

    //Redirect User if he wants to order duplicate when one order is active
    GigOrderResponseDTO findActiveOrder(
            Long gigId,
            Long buyerId
    );


    Long countActiveByBuyerId(Long userId);

    List<GigOrderResponseDTO> getRecentOrdersByUserId(Long userId);

    List<GigOrderResponseDTO> getRecentActiveOrdersByUserId(Long userId);

    Long countActiveClients();

    Long countTotalOrders();

    Long countCompletedOrders();

    Long countCancelledOrders();


    Long countSellerPendingOrders(Long userProfileId);

    Long countSellerInProgressOrders(Long userProfileId);

    Long countSellerDeliveredOrders(Long userProfileId);

    Long countSellerCompletedOrders(Long userProfileId);

    Long countSellerCancelledOrders(Long userProfileId);

    Long countSellerDisputedOrders(Long userProfileId);

    BigDecimal getLifetimeEarnings(Long userProfileId);

    List<GigOrderResponseDTO> getRecentOrdersByFreelancerId(Long userProfileId);

    List<GigOrderResponseDTO> search(
            GigOrderFilterRequestDTO dto
    );


}
