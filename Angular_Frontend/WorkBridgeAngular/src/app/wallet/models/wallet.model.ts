export interface WalletResponseModel {
    id: number;
    balance: number;
    frozenBalance: number;
    createdAt: string;
    userId: number;
    userName: string;
}