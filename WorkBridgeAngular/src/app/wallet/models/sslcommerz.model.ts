export interface SSLCallbackDTO {
    tran_id: string;          // Maps to transactionId
    val_id: string;           // Maps to validationId
    status: string;           // VALID / FAILED / CANCELLED
    amount: string;           // Note: Kept as string since the Java type is String
    card_type: string;        // Maps to paymentMethod
    bank_tran_id: string;     // Maps to bankTransactionId
    currency: string;
    failedreason: string;     // Maps to failedReason
}