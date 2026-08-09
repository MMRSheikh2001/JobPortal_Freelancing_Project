import { TransactionType } from "../../enums/transaction-type.enum";
import { UserRole } from "../../enums/user-role.enum";

export interface TransactionResponseDTO {
    id: number;
    type: TransactionType;
    fromUserId: number;
    fromUserName: string;
    toUserId: number;
    toUserName: string;
    amount: number;
    description: string;
    createdAt: string;
}

export interface TransactionFilterDTO {

  transactionType?: TransactionType;

  userRole?: UserRole;

  keyword?: string;

  fromDate?: string;

  toDate?: string;

  userId?: number;

}