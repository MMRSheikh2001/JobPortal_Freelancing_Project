import { UserRole } from "../../enums/user-role.enum";
import { WithdrawMethod } from "../../enums/withdraw-method.enum";
import { WithdrawStatus } from "../../enums/withdraw-status.enum";


export interface WithdrawRequestModel {
    userId: number;
    amount: number;
    withdrawMethod: WithdrawMethod;
    accountNumber: string;
    accountName: string;
}

export interface WithdrawResponseModel {
    id: number;
    walletId: number;
    walletBalance: number;
    userId: number;
    userName: string;
    userEmail: string;
    userRole: UserRole;
    amount: number;
    withdrawMethod: WithdrawMethod;
    accountNumber: string;
    accountName: string;
    withdrawStatus: WithdrawStatus;
    createdAt: string;
    updatedAt: string;
    adminRemarks: string;
    transactionReference: string;
}