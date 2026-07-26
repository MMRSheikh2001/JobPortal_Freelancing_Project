export interface MessageRequestModel {
    messageText: string;
    conversationId: number;
}

export interface MessageResponseModel {
    id: number;
    messageText: string;
    attachment: string;
    isRead: boolean;
    sentAt: string;
    senderId: number;
    senderName: string;
    conversationId: number;
}