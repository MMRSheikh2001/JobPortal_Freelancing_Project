package com.wordbridge.project.gigorder;

import com.wordbridge.project.conversation.Conversation;
import com.wordbridge.project.conversation.ConversationResponseDTO;
import com.wordbridge.project.conversation.ConversationService;
import com.wordbridge.project.entity.User;
import com.wordbridge.project.enums.GigOrderStatus;
import com.wordbridge.project.enums.TransactionType;
import com.wordbridge.project.enums.UserRole;
import com.wordbridge.project.gig.Gig;
import com.wordbridge.project.gig.GigRepository;
import com.wordbridge.project.notification.NotificationService;
import com.wordbridge.project.notification.NotificationType;
import com.wordbridge.project.repository.UserRepository;
import com.wordbridge.project.security.AuthenticationService;
import com.wordbridge.project.transaction.TransactionService;
import com.wordbridge.project.util.FileStorageService;
import com.wordbridge.project.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GigOrderServiceImpl implements GigOrderService {
    private final GigOrderRepository gigOrderRepository;
    private final GigOrderMapper gigOrderMapper;
    private final GigRepository gigRepository;
    private final UserRepository userRepository;
    private final FileStorageService fileStorageService;
    private final ConversationService conversationService;
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final NotificationService notificationService;



    List<GigOrderStatus> activeStatuses = List.of(
            GigOrderStatus.ORDER_PLACED,
            GigOrderStatus.QUOTED,
            GigOrderStatus.QUOTE_ACCEPTED,
            GigOrderStatus.DELIVERED,
            GigOrderStatus.BUYER_CANCELLED,
            GigOrderStatus.BUYER_REJECTED,
            GigOrderStatus.SELLER_DISPUTED
    );
    private static final BigDecimal PLATFORM_COMMISSION =
            BigDecimal.valueOf(0.05);

    @Override
    @Transactional
    public GigOrderResponseDTO placeOrder(Long gigId, Long buyerId) {
        if (gigOrderRepository.existsByGigIdAndBuyerIdAndStatusIn(gigId, buyerId, activeStatuses)) {
            throw new RuntimeException(
                    "You already have an active order for this gig."
            );
        }

        GigOrder gigOrder = new GigOrder();


        Gig gig = gigRepository.findById(gigId)
                .orElseThrow(() -> new RuntimeException("No gig found"));
        if (!gig.getIsActive()) {
            throw new RuntimeException("This gig is no longer available.");
        }

        gigOrder.setGig(gig);

        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new RuntimeException("No buyer found"));

        if (buyer.getUserProfile() != null &&
                buyer.getUserProfile().getId().equals(gig.getUserProfile().getId())) {
            throw new RuntimeException("You cannot order your own gig.");
        }

        gigOrder.setBuyer(buyer);

        gigOrder.setPaymentLocked(false);
        gigOrder.setStatus(GigOrderStatus.ORDER_PLACED);
        gigOrder.setQuotedPrice(BigDecimal.ZERO);
        gigOrder.setAgreedPrice(BigDecimal.ZERO);
        gigOrder.setFinalPrice(BigDecimal.ZERO);

        GigOrder saved = gigOrderRepository.save(gigOrder);

        //Creating chat room after gig order creation
      Conversation conversation= conversationService.create(saved.getId());

        saved.setConversation(conversation);

        String buyerName;

        if (saved.getBuyer().getRole() == UserRole.USER) {
            buyerName = saved.getBuyer().getUserProfile().getName();
        } else if (saved.getBuyer().getRole() == UserRole.COMPANY) {
            buyerName = saved.getBuyer().getCompanyProfile().getName();
        } else {
            buyerName = "ADMIN";
        }

        notificationService.createNotification(
                saved.getGig().getUserProfile().getUser().getId(),
                "New Gig Order",
                buyerName + " ordered your gig.",
                NotificationType.GIG_ORDER,
                saved.getId()
        );

        return gigOrderMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public GigOrderResponseDTO acceptQuote(Long orderId) {
        GigOrder gigOrder = gigOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("No Gig Order found"));

        if (gigOrder.getStatus() != GigOrderStatus.QUOTED) {
            throw new RuntimeException("Order has not been Quoted.");
        }
        if (gigOrder.getQuotedPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Seller has not quoted yet.");
        }

        gigOrder.setStatus(GigOrderStatus.QUOTE_ACCEPTED);
        gigOrder.setAgreedPrice(gigOrder.getQuotedPrice());

        walletService.freezeAmount(gigOrder.getBuyer().getId(), gigOrder.getAgreedPrice());
        gigOrder.setPaymentLocked(true);
        gigOrder.setQuoteAcceptedAt(LocalDateTime.now());

        gigOrder.setExpectedDeliveryAt(LocalDateTime.now()
                .plusDays(gigOrder.getGig().getDeliveryDays()));

        GigOrder saved = gigOrderRepository.save(gigOrder);

        return gigOrderMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public GigOrderResponseDTO rejectQuote(Long orderId) {
        GigOrder gigOrder = gigOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("No Gig Order found"));
        if (gigOrder.getStatus() != GigOrderStatus.QUOTED) {
            throw new RuntimeException("Order has not been Quoted.");
        }
        if (gigOrder.getQuotedPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Seller has not quoted yet.");
        }

        gigOrder.setStatus(GigOrderStatus.QUOTE_REJECTED);

        GigOrder saved = gigOrderRepository.save(gigOrder);

        conversationService.closeConversation(
                gigOrder.getConversation().getId()
        );

        return gigOrderMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public GigOrderResponseDTO acceptDelivery(Long orderId) {
        GigOrder gigOrder = gigOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("No Gig Order found"));
        if (gigOrder.getStatus() != GigOrderStatus.DELIVERED) {
            throw new RuntimeException("Order has not been delivered.");
        }
        gigOrder.setStatus(GigOrderStatus.BUYER_ACCEPTED);
        BigDecimal sellerPayout = gigOrder.getAgreedPrice()
                .multiply(BigDecimal.ONE.subtract(PLATFORM_COMMISSION));

        gigOrder.setFinalPrice(sellerPayout);
        gigOrder.setBuyerAcceptedAt(LocalDateTime.now());

        walletService.releasePayment(gigOrder.getBuyer().getId(),
                gigOrder.getGig().getUserProfile().getUser().getId(),
                gigOrder.getAgreedPrice(), sellerPayout
        );

        gigOrder.setPaymentLocked(false);
        gigOrder.setPaymentReleasedAt(LocalDateTime.now());


        GigOrder saved = gigOrderRepository.save(gigOrder);

        //Gig status update
        Gig gig = saved.getGig();
        gig.setCompletedOrders(
                gig.getCompletedOrders() + 1
        );
        gigRepository.save(gig);

        transactionService.createTransaction(TransactionType.SELLER_PAYOUT, saved.getBuyer(), saved.getGig().getUserProfile().getUser(), saved.getFinalPrice(),
                "Seller Obtained money from Gig Order #" + saved.getId() + "  after buyer accepted delivery");

        //Admin Commission
        User admin = userRepository.findByRole(UserRole.ADMIN)
                .orElseThrow(() -> new RuntimeException("No admin found"));
        BigDecimal commission = saved.getAgreedPrice()
                .subtract(sellerPayout);
        transactionService.createTransaction(TransactionType.PLATFORM_COMMISSION, saved.getBuyer(), admin, commission,
                "System Obtained Commission from Gig Order #" + saved.getId() + "  after buyer accepted delivery");

        conversationService.closeConversation(
                gigOrder.getConversation().getId()
        );
        String buyerName;

        if (saved.getBuyer().getRole() == UserRole.USER) {
            buyerName = saved.getBuyer().getUserProfile().getName();
        } else if (saved.getBuyer().getRole() == UserRole.COMPANY) {
            buyerName = saved.getBuyer().getCompanyProfile().getName();
        } else {
            buyerName = "ADMIN";
        }

        notificationService.createNotification(
                saved.getGig().getUserProfile().getUser().getId(),
                "Gig marked completed",
                buyerName + " is satisfied and marked completed",
                NotificationType.GIG_COMPLETED,
                saved.getId()
        );

        return gigOrderMapper.toDTO(saved);


    }

    @Override
    public GigOrderResponseDTO rejectDelivery(Long orderId) {
        GigOrder gigOrder = gigOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("No Gig Order found"));
        if (gigOrder.getStatus() != GigOrderStatus.DELIVERED) {
            throw new RuntimeException("Order has not been delivered.");
        }

        gigOrder.setStatus(GigOrderStatus.BUYER_REJECTED);

        gigOrder.setBuyerRejectedAt(LocalDateTime.now());
        gigOrder.setSellerDisputeDeadline(LocalDateTime.now().plusDays(7));

        GigOrder saved = gigOrderRepository.save(gigOrder);

        return gigOrderMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public GigOrderResponseDTO buyerCancelOrder(Long orderId) {
        GigOrder gigOrder = gigOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("No Gig Order found"));

        if (
                gigOrder.getStatus() != GigOrderStatus.ORDER_PLACED &&
                        gigOrder.getStatus() != GigOrderStatus.QUOTED &&
                        gigOrder.getStatus() != GigOrderStatus.QUOTE_ACCEPTED
        ) {
            throw new RuntimeException("Order cannot be cancelled.");
        }

        gigOrder.setStatus(GigOrderStatus.BUYER_CANCELLED);

        gigOrder.setSellerDisputeDeadline(LocalDateTime.now().plusDays(7));


        GigOrder saved = gigOrderRepository.save(gigOrder);


        return gigOrderMapper.toDTO(saved);

    }

    @Override
    public GigOrderResponseDTO sendQuote(Long orderId, BigDecimal quotedPrice) {
        GigOrder gigOrder = gigOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("No Gig Order found"));
        if (gigOrder.getStatus() != GigOrderStatus.ORDER_PLACED) {
            throw new RuntimeException("Buyer didn't even place order");
        }
        if (gigOrder.getQuotedPrice().compareTo(BigDecimal.ZERO) > 0) {
            throw new RuntimeException("Quote already sent.");
        }
        if (quotedPrice == null || quotedPrice.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Invalid quoted price.");
        }
        gigOrder.setQuotedPrice(quotedPrice);
        gigOrder.setStatus(GigOrderStatus.QUOTED);
        gigOrder.setQuotedAt(LocalDateTime.now());

        GigOrder saved = gigOrderRepository.save(gigOrder);

        return gigOrderMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public GigOrderResponseDTO deliverOrder(Long orderId, String deliveryMessage, MultipartFile deliveryFile) {


        if (deliveryFile == null || deliveryFile.isEmpty()) {
            throw new RuntimeException("Delivery file required.");
        }
        GigOrder gigOrder = gigOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("No Gig Order found"));


        if (gigOrder.getStatus() != GigOrderStatus.QUOTE_ACCEPTED) {
            throw new RuntimeException("Buyer didn't accept the quote");
        }
        if (LocalDateTime.now().isAfter(gigOrder.getExpectedDeliveryAt())) {
            throw new RuntimeException("Delivery deadline has passed.");
        }

        if (deliveryFile != null && !deliveryFile.isEmpty()) {
            String fileName = fileStorageService.uploadPortfolioFile(deliveryFile,
                    gigOrder.getGig().getUserProfile().getUser().getEmail() + gigOrder.getBuyer().getEmail(),
                    "gigdeliveries");
            gigOrder.setDeliveryFileUrl(fileName);
            gigOrder.setDeliveryMessage(deliveryMessage);
            gigOrder.setStatus(GigOrderStatus.DELIVERED);
            gigOrder.setDeliveredAt(LocalDateTime.now());

        }

        GigOrder saved = gigOrderRepository.save(gigOrder);

        return gigOrderMapper.toDTO(saved);
    }

    @Override
    public GigOrderResponseDTO cancelOrderBySeller(Long orderId) {
        GigOrder gigOrder = gigOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("No Gig Order found"));
        if (gigOrder.getStatus() != GigOrderStatus.QUOTE_ACCEPTED) {
            throw new RuntimeException("Buyer didn't accept the quote");
        }

        gigOrder.setStatus(GigOrderStatus.SELLER_CANCELLED);
        gigOrder.setPaymentLocked(false);

        walletService.refundBuyer(
                gigOrder.getBuyer().getId(),
                gigOrder.getAgreedPrice()
        );
        transactionService.createTransaction(
                TransactionType.REFUND,
                gigOrder.getBuyer(),
                gigOrder.getBuyer(),
                gigOrder.getAgreedPrice(),
                "Refund for Seller cancelled Gig Order #" + gigOrder.getId()
        );
        conversationService.closeConversation(
                gigOrder.getConversation().getId()
        );

        GigOrder saved = gigOrderRepository.save(gigOrder);

        return gigOrderMapper.toDTO(saved);
    }

    @Override
    public GigOrderResponseDTO raiseDispute(Long orderId) {
        GigOrder gigOrder = gigOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("No Gig Order found"));

        if (gigOrder.getSellerDisputeDeadline() == null) {
            throw new RuntimeException("Dispute not available.");
        }
        if (
                gigOrder.getStatus() != GigOrderStatus.BUYER_CANCELLED &&
                        gigOrder.getStatus() != GigOrderStatus.BUYER_REJECTED
        ) {
            throw new RuntimeException("Dispute cannot be raised.");
        }

        if (gigOrder.getSellerDisputeDeadline().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("You passed the dispute deadline time");
        }
        gigOrder.setStatus(GigOrderStatus.SELLER_DISPUTED);
        gigOrder.setSellerDisputeOpenedAt(LocalDateTime.now());

        GigOrder saved = gigOrderRepository.save(gigOrder);

        return gigOrderMapper.toDTO(saved);
    }

    @Override
    public GigOrderResponseDTO releasePayment(Long orderId) {
        GigOrder gigOrder = gigOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("No Gig Order found"));
        if (gigOrder.getStatus() != GigOrderStatus.SELLER_DISPUTED) {
            throw new RuntimeException("Seller didn't dispute order");
        }
        gigOrder.setStatus(GigOrderStatus.PAYMENT_RELEASED);

        BigDecimal sellerPayout = gigOrder.getAgreedPrice()
                .multiply(BigDecimal.ONE.subtract(PLATFORM_COMMISSION));

        gigOrder.setFinalPrice(sellerPayout);
        gigOrder.setPaymentReleasedAt(LocalDateTime.now());
        gigOrder.setPaymentLocked(false);


        walletService.releasePayment(
                gigOrder.getBuyer().getId(),
                gigOrder.getGig().getUserProfile().getUser().getId(),
                gigOrder.getAgreedPrice(),
                sellerPayout
        );

        GigOrder saved = gigOrderRepository.save(gigOrder);

        //Gig status update
        Gig gig = saved.getGig();
        gig.setCompletedOrders(
                gig.getCompletedOrders() + 1
        );
        gigRepository.save(gig);

        transactionService.createTransaction(TransactionType.SELLER_PAYOUT, saved.getBuyer(), saved.getGig().getUserProfile().getUser(), saved.getFinalPrice(),
                "Seller Obtained money from Gig Order #" + saved.getId() + "  after Admin  solved dispute");

        //Admin Commission
        User admin = userRepository.findByRole(UserRole.ADMIN)
                .orElseThrow(() -> new RuntimeException("No admin found"));

        BigDecimal commission = saved.getAgreedPrice()
                .subtract(sellerPayout);
        transactionService.createTransaction(TransactionType.PLATFORM_COMMISSION, saved.getBuyer(), admin, commission,
                "System Obtained Commission from Gig Order #" + saved.getId() + "  after  Admin  solved dispute");

        conversationService.closeConversation(
                gigOrder.getConversation().getId()
        );


        notificationService.createNotification(
                saved.getGig().getUserProfile().getUser().getId(),
                "Gig marked completed",
                "Admin solved the dispute and marked completed",
                NotificationType.GIG_COMPLETED,
                saved.getId()
        );

        return gigOrderMapper.toDTO(saved);
    }

    @Override
    public GigOrderResponseDTO refundBuyer(Long orderId) {
        GigOrder gigOrder = gigOrderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("No Gig Order found"));
        if (gigOrder.getStatus() != GigOrderStatus.SELLER_DISPUTED) {
            throw new RuntimeException("Seller didn't dispute order");
        }
        gigOrder.setStatus(GigOrderStatus.REFUNDED);
        gigOrder.setRefundedAt(LocalDateTime.now());
        gigOrder.setPaymentLocked(false);

        walletService.refundBuyer(
                gigOrder.getBuyer().getId(),
                gigOrder.getAgreedPrice()
        );
        transactionService.createTransaction(
                TransactionType.REFUND,
                gigOrder.getBuyer(),
                gigOrder.getBuyer(),
                gigOrder.getAgreedPrice(),
                "Refund for Gig Order #" + gigOrder.getId()
        );

        GigOrder saved = gigOrderRepository.save(gigOrder);
        conversationService.closeConversation(
                gigOrder.getConversation().getId()
        );

        return gigOrderMapper.toDTO(saved);
    }

    @Override
    public List<GigOrderResponseDTO> getAll() {
        return gigOrderRepository.findAll().stream().map(gigOrderMapper::toDTO).toList();
    }

    @Override
    public GigOrderResponseDTO getById(Long id) {
        GigOrder gigOrder = gigOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No Gig Order found"));
        return gigOrderMapper.toDTO(gigOrder);
    }

    @Override
    public List<GigOrderResponseDTO> getBuyerOrders(Long buyerId) {
        return gigOrderRepository.findByBuyerId(buyerId).stream().map(gigOrderMapper::toDTO).toList();
    }

    @Override
    public List<GigOrderResponseDTO> getSellerOrders(Long sellerId) {
        return gigOrderRepository.findByGigUserProfileId(sellerId).stream().map(gigOrderMapper::toDTO).toList();
    }

    @Override
    public List<GigOrderResponseDTO> getByStatus(GigOrderStatus status) {
        return gigOrderRepository.findByStatus(status).stream().map(gigOrderMapper::toDTO).toList();
    }

    @Override
    public Long countByStatus(GigOrderStatus status) {
        return gigOrderRepository.countByStatus(status);
    }

    @Override
    public List<GigOrderResponseDTO> findByBuyerIdAndStatus(Long buyerId, GigOrderStatus status) {
        return gigOrderRepository.findByBuyerIdAndStatus(buyerId, status).stream().map(gigOrderMapper::toDTO).toList();
    }

    @Override
    public Long countByBuyerIdAndStatus(Long buyerId, GigOrderStatus status) {
        return gigOrderRepository.countByBuyerIdAndStatus(buyerId, status);
    }

    @Override
    public List<GigOrderResponseDTO> findByGigUserProfileIdAndStatus(Long sellerId, GigOrderStatus status) {
        return gigOrderRepository.findByGigUserProfileIdAndStatus(sellerId, status).stream().map(gigOrderMapper::toDTO).toList();
    }

    @Override
    public Long countByGigUserProfileIdAndStatus(Long sellerId, GigOrderStatus status) {
        return gigOrderRepository.countByGigUserProfileIdAndStatus(sellerId, status);
    }

    @Override
    public Boolean existsByGigIdAndBuyerId(Long gigId, Long buyerId) {
        return gigOrderRepository.existsByGigIdAndBuyerId(gigId, buyerId);
    }

    @Override
    public List<GigOrderResponseDTO> findByGigId(Long gigId) {
        return gigOrderRepository.findByGigId(gigId).stream().map(gigOrderMapper::toDTO).toList();
    }

    @Override
    public Long countByGigId(Long gigId) {
        return gigOrderRepository.countByGigId(gigId);
    }

    @Override
    public Long countByGigUserProfileId(Long sellerId) {
        return gigOrderRepository.countByGigUserProfileId(sellerId);
    }

    @Override
    public Long countByBuyerId(Long buyerId) {
        return gigOrderRepository.countByBuyerId(buyerId);
    }

    @Override
    public void processExpiredDisputes() {
        List<GigOrderStatus> statuses = List.of(
                GigOrderStatus.BUYER_CANCELLED, GigOrderStatus.BUYER_REJECTED
        );
        List<GigOrder> expiredOrders = gigOrderRepository.findByStatusInAndSellerDisputeDeadlineBefore(statuses, LocalDateTime.now());
        for (GigOrder order : expiredOrders) {
            walletService.refundBuyer(order.getBuyer().getId(), order.getAgreedPrice());


            order.setStatus(GigOrderStatus.REFUNDED);
            order.setRefundedAt(LocalDateTime.now());
            order.setPaymentLocked(false);
            conversationService.closeConversation(
                    order.getConversation().getId()
            );


        }
        gigOrderRepository.saveAll(expiredOrders);


    }

    @Override
    public GigOrderResponseDTO findActiveOrder(
            Long gigId,
            Long buyerId
    ) {

        List<GigOrderStatus> activeStatuses = List.of(
                GigOrderStatus.ORDER_PLACED,
                GigOrderStatus.QUOTED,
                GigOrderStatus.QUOTE_ACCEPTED,
                GigOrderStatus.DELIVERED,
                GigOrderStatus.BUYER_REJECTED,
                GigOrderStatus.BUYER_CANCELLED,
                GigOrderStatus.SELLER_DISPUTED
        );

        GigOrder order = gigOrderRepository
                .findByGigIdAndBuyerIdAndStatusIn(
                        gigId,
                        buyerId,
                        activeStatuses
                )
                .orElseThrow(() ->
                        new RuntimeException("No active order found"));

        return gigOrderMapper.toDTO(order);
    }

    @Override
    public Long countActiveByBuyerId(Long userId) {
        List<GigOrderStatus> activeStatuses = List.of(
                GigOrderStatus.ORDER_PLACED,
                GigOrderStatus.QUOTED,
                GigOrderStatus.QUOTE_ACCEPTED,
                GigOrderStatus.DELIVERED,
                GigOrderStatus.BUYER_REJECTED,
                GigOrderStatus.BUYER_CANCELLED,
                GigOrderStatus.SELLER_DISPUTED
        );


        return gigOrderRepository.countByBuyerIdAndStatusIn(userId, activeStatuses);
    }

    @Override
    public List<GigOrderResponseDTO> getRecentOrdersByUserId(Long userId) {

        Pageable pageable = PageRequest.of(0, 5);

        return gigOrderRepository
                .findRecentOrdersByUserId(userId, pageable)
                .stream()
                .map(gigOrderMapper::toDTO)
                .toList();
    }

    @Override
    public List<GigOrderResponseDTO> getRecentActiveOrdersByUserId(Long userId) {
        Pageable pageable = PageRequest.of(0, 5);

        return gigOrderRepository
                .findRecentActiveOrdersByUserId(userId, pageable)
                .stream()
                .map(gigOrderMapper::toDTO)
                .toList();
    }

    @Override
    public Long countActiveClients() {
        return gigOrderRepository.countActiveClients(activeStatuses);
    }

    @Override
    public Long countTotalOrders() {
        return gigOrderRepository.count();
    }

    @Override
    public Long countCompletedOrders() {

        List<GigOrderStatus> completedStatuses = List.of(
                GigOrderStatus.BUYER_ACCEPTED,
                GigOrderStatus.PAYMENT_RELEASED
        );
        return gigOrderRepository.countByStatusIn(completedStatuses);
    }

    @Override
    public Long countCancelledOrders() {
        List<GigOrderStatus> cancelledStatuses = List.of(
                GigOrderStatus.QUOTE_REJECTED,
                GigOrderStatus.SELLER_CANCELLED,
                GigOrderStatus.REFUNDED
        );
        return gigOrderRepository.countByStatusIn(cancelledStatuses);
    }

    @Override
    public Long countSellerPendingOrders(Long userProfileId) {
        List<GigOrderStatus> pendingStatuses = List.of(
                GigOrderStatus.ORDER_PLACED,
                GigOrderStatus.QUOTED
        );

        return gigOrderRepository.countByGigUserProfileIdAndStatusIn(userProfileId, pendingStatuses);
    }

    @Override
    public Long countSellerInProgressOrders(Long userProfileId) {
        return gigOrderRepository.countByGigUserProfileIdAndStatus(userProfileId, GigOrderStatus.QUOTE_ACCEPTED);
    }

    @Override
    public Long countSellerDeliveredOrders(Long userProfileId) {
        return gigOrderRepository.countByGigUserProfileIdAndStatus(userProfileId, GigOrderStatus.DELIVERED);
    }

    @Override
    public Long countSellerCompletedOrders(Long userProfileId) {
        List<GigOrderStatus> completedStatuses = List.of(
                GigOrderStatus.BUYER_ACCEPTED,
                GigOrderStatus.PAYMENT_RELEASED
        );

        return gigOrderRepository.countByGigUserProfileIdAndStatusIn(userProfileId, completedStatuses);
    }

    @Override
    public Long countSellerCancelledOrders(Long userProfileId) {
        List<GigOrderStatus> cancelledStatuses = List.of(
                GigOrderStatus.QUOTE_REJECTED,
                GigOrderStatus.SELLER_CANCELLED,
                GigOrderStatus.BUYER_CANCELLED,
                GigOrderStatus.REFUNDED
        );

        return gigOrderRepository.countByGigUserProfileIdAndStatusIn(userProfileId, cancelledStatuses);
    }

    @Override
    public Long countSellerDisputedOrders(Long userProfileId) {
        return gigOrderRepository.countByGigUserProfileIdAndStatus(userProfileId, GigOrderStatus.SELLER_DISPUTED);
    }

    @Override
    public BigDecimal getLifetimeEarnings(Long userProfileId) {
        return gigOrderRepository.getLifetimeEarnings(userProfileId);
    }

    @Override
    public List<GigOrderResponseDTO> getRecentOrdersByFreelancerId(
            Long userProfileId
    ) {

        Pageable pageable = PageRequest.of(0, 5);

        return gigOrderRepository
                .findByGigUserProfileIdOrderByCreatedAtDesc(
                        userProfileId,
                        pageable
                )
                .stream()
                .map(gigOrderMapper::toDTO)
                .toList();
    }

    @Override
    public List<GigOrderResponseDTO> search(
            GigOrderFilterRequestDTO dto
    ) {

        return gigOrderRepository

                .findAll(
                        GigOrderSpecification.filter(dto)
                )

                .stream()

                .map(gigOrderMapper::toDTO)

                .toList();

    }


}
