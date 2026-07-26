export interface GigRequestModel {
    title: string;
    shortDescription: string;
    description: string;
    startingPrice: number;
    deliveryDays: number;
    revisions: number;
    categoryId: number;
    userProfileId: number;
}

export interface GigResponseModel {
    id: number;
    title: string;
    shortDescription: string;
    description: string;
    startingPrice: number;
    deliveryDays: number;
    revisions: number;
    gigImage: string;
    isActive: boolean;
    createdAt: string;
    updatedAt: string;
    categoryId: number;
    categoryName: string;
    userProfileId: number;
    userName: string;
    averageRating: number;
    totalReviews: number;
    completedOrders: number;
}

export interface GigSearchRequestModel {
    keyword: string;
    categoryId: number;
    minPrice: number;
    maxPrice: number;
    maxDeliveryDays: number;
    active: boolean;
    minimumRating: number;
    minimumOrders: number;
}
