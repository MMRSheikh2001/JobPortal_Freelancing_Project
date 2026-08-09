export interface ReviewResponseModel {
    id: number;
    rating: number;
    comment: string;
    createdAt: string;
    gigOrderId: number;
    reviewerId: number;
    reviewerName: string;
    sellerUserProfileId: number;
    sellerName: string;
    gigId: number;
    gigTitle: string;
}

export interface ReviewRequestDTO {
    rating: number;
    comment: string;
    gigOrderId: number;
}