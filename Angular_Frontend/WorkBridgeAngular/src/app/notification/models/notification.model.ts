import { NotificationType } from "../../enums/notification-type.enum";

export interface NotificationResponseDTO {
  id: number;
  userId: number;
  userName: string;
  title: string;
  message: string;
  type: NotificationType;
  referenceId: number;
  isRead: boolean;
  createdAt: string;              
}
export interface NotificationFilterDTO {
  type?: NotificationType;
  isRead?: boolean;
  userId?: number;
  keyword?: string;
}