import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../enviroments/environment';
import { ConversationResponseModel } from '../models/conversation.model';

@Injectable({
  providedIn: 'root',
})
export class ConversationService {



  private apiUrl =
    `${environment.apiUrl}conversations`;

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Get By Id
  // ==========================

  getById(
    id: number
  ): Observable<ConversationResponseModel> {

    return this.http.get<ConversationResponseModel>(
      `${this.apiUrl}/${id}`
    );

  }

  // ==========================
  // Get By Gig Order
  // ==========================

  getByGigOrderId(
    gigOrderId: number
  ): Observable<ConversationResponseModel> {

    return this.http.get<ConversationResponseModel>(
      `${this.apiUrl}/gig-order/${gigOrderId}`
    );

  }

  // ==========================
  // Buyer Conversations
  // ==========================

  getBuyerConversations(
    buyerId: number
  ): Observable<ConversationResponseModel[]> {

    return this.http.get<ConversationResponseModel[]>(
      `${this.apiUrl}/buyer/${buyerId}`
    );

  }

  // ==========================
  // Seller Conversations
  // ==========================

  getSellerConversations(
    sellerUserProfileId: number
  ): Observable<ConversationResponseModel[]> {

    return this.http.get<ConversationResponseModel[]>(
      `${this.apiUrl}/seller/${sellerUserProfileId}`
    );

  }

  // ==========================
  // Count Buyer Conversations
  // ==========================

  countBuyerConversations(
    buyerId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}/buyer/${buyerId}/count`
    );

  }

  // ==========================
  // Count Seller Conversations
  // ==========================

  countSellerConversations(
    sellerUserProfileId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}/seller/${sellerUserProfileId}/count`
    );

  }



}
