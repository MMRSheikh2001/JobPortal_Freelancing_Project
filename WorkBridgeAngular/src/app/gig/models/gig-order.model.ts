import { GigOrderStatus } from "../../enums/gig-order-status.enum";
import { UserRole } from "../../enums/user-role.enum";


export interface GigOrderResponseDTO {
    id: number;
    quotedPrice: number;
    agreedPrice: number;
    finalPrice: number;
    deliveryMessage?: string;
    deliveryFileUrl?: string;
    paymentLocked: boolean;
    status: GigOrderStatus;
    createdAt: string;
    quotedAt?: string;
    quoteAcceptedAt?: string;
    expectedDeliveryAt?: string;
    deliveredAt?: string;
    buyerAcceptedAt?: string;
    buyerRejectedAt?: string;
    buyerCancelledAt?: string;
    sellerCancelledAt?: string;
    sellerDisputeOpenedAt?: string;
    sellerDisputeDeadline?: string;
    paymentReleasedAt?: string;
    refundedAt?: string;
    gigId: number;
    gigTitle: string;
    gigImage: string;
    sellerId: number;
    sellerName: string;
    buyerId: number;
    buyerName: string;
    buyerUserProfileId?: number;
    buyerCompanyProfileId?: number;
    buyerRole: UserRole;

    conversationId: number;
}

export interface GigOrderFilterRequestDTO {
    keyword?: string;
    buyerId?: number;
    sellerId?: number;
    gigId?: number;
    categoryId?: number;
    status?: GigOrderStatus;
    paymentLocked?: boolean;
    createdFrom?: string;
    createdTo?: string;
}