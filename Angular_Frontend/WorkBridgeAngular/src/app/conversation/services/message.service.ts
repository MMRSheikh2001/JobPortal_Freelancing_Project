import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { MessageRequestModel, MessageResponseModel } from '../models/message.model';
import { environment } from '../../../enviroments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class MessageService {



  private apiUrl =
    `${environment.apiUrl}messages`;

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Send Message
  // ==========================

  sendMessage(
    message: MessageRequestModel,
    senderId: number,
    attachment?: File
  ): Observable<MessageResponseModel> {

    const formData = new FormData();

    formData.append(
      'message',
      new Blob(
        [JSON.stringify(message)],
        { type: 'application/json' }
      )
    );

    if (attachment) {

      formData.append(
        'attachment',
        attachment
      );

    }

    return this.http.post<MessageResponseModel>(
      `${this.apiUrl}/?senderId=${senderId}`,
      formData
    );

  }

  // ==========================
  // Get By Id
  // ==========================

  getById(
    id: number
  ): Observable<MessageResponseModel> {

    return this.http.get<MessageResponseModel>(
      `${this.apiUrl}/${id}`
    );

  }

  // ==========================
  // Conversation Messages
  // ==========================

  getConversationMessages(
    conversationId: number
  ): Observable<MessageResponseModel[]> {

    return this.http.get<MessageResponseModel[]>(
      `${this.apiUrl}/conversation/${conversationId}`
    );

  }

  // ==========================
  // Sender Messages
  // ==========================

  getSenderMessages(
    senderId: number
  ): Observable<MessageResponseModel[]> {

    return this.http.get<MessageResponseModel[]>(
      `${this.apiUrl}/sender/${senderId}`
    );

  }

  // ==========================
  // Unread Messages
  // ==========================

  getUnreadMessages(
    conversationId: number
  ): Observable<MessageResponseModel[]> {

    return this.http.get<MessageResponseModel[]>(
      `${this.apiUrl}/conversation/${conversationId}/unread`
    );

  }

  // ==========================
  // Count Unread Messages
  // ==========================

  countUnreadMessages(
    conversationId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}/conversation/${conversationId}/unread/count`
    );

  }

  // ==========================
  // Count Unread For User
  // ==========================

  countUnreadMessagesForUser(
    conversationId: number,
    senderId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}/conversation/${conversationId}/unread/count/${senderId}`
    );

  }

  // ==========================
  // Mark Conversation As Read
  // ==========================

  markConversationAsRead(
    conversationId: number,
    readerId: number
  ): Observable<string> {

    return this.http.put(
      `${this.apiUrl}/conversation/${conversationId}/read?readerId=${readerId}`,
      {},
      {
        responseType: 'text'
      }
    );

  }

  // ==========================
  // Latest Message
  // ==========================

  getLatestMessage(
    conversationId: number
  ): Observable<MessageResponseModel> {

    return this.http.get<MessageResponseModel>(
      `${this.apiUrl}/conversation/${conversationId}/latest`
    );

  }


}
