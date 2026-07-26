import { GigOrderResponseDTO } from "../../gig/models/gig-order.model";
import { GigResponseModel } from "../../gig/models/gig.model";

export interface FreelancerDashboardDTO {
    userName: string;
    profileImage: string;
    profileCompletion: number;
    totalGigs: number;
    activeGigs: number;
    inactiveGigs: number;
    totalOrders: number;
    pendingOrders: number;
    inProgressOrders: number;
    deliveredOrders: number;
    completedOrders: number;
    cancelledOrders: number;
    disputedOrders: number;
    walletBalance: number;
    lifetimeEarnings: number;
    pendingWithdrawals: number;
    averageRating: number;
    totalReviews: number;
    activeConversations: number;
    unreadMessages: number;
    unreadNotifications: number;
    recentOrders: GigOrderResponseDTO[];
    myPopularGigs: GigResponseModel[];
}