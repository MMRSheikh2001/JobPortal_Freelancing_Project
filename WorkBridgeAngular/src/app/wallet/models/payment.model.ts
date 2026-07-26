import { PaymentStatus } from "../../enums/payment-status.enum";

export interface DepositSessionResponseDTO {
    paymentId: number;
    gatewayTransactionId: string;
    gatewayPageUrl: string;
    paymentStatus: PaymentStatus;
}

export interface PaymentResponseDTO {
    id: number;
    paymentStatus: PaymentStatus;
    createdAt: string;
    updatedAt: string;
    amount: number;
    userId: number;
    userName: string;
    gatewayTransactionId: string;
    validationId: string;
    paymentMethod: string;
    gateway: string;
    failureReason: string;
}