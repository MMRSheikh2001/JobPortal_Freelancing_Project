import { ConversationStatus } from "../../enums/conversation-status.enum";
import { GigOrderStatus } from "../../enums/gig-order-status.enum";


export interface ConversationResponseModel {
    id: number;
    createdAt: string;
    lastMessageAt: string;
    gigOrderId: number;
    status: GigOrderStatus;
    gigId: number;
    gigTitle: string;
    gigImage: string;
    sellerUserProfileId: number;
    sellerName: string;
    buyerId: number;
    buyerName: string;
    conversationStatus: ConversationStatus;
}