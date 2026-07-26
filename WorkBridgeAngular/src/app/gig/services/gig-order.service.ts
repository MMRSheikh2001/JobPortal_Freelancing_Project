import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { GigOrderFilterRequestDTO, GigOrderResponseDTO } from '../models/gig-order.model';
import { GigOrderStatus } from '../../enums/gig-order-status.enum';
import { environment } from '../../../enviroments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class GigOrderService {




  private apiUrl = `${environment.apiUrl}gig-orders`;

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Place Order
  // ==========================

  placeOrder(
    gigId: number,
    buyerId: number
  ): Observable<GigOrderResponseDTO> {

    return this.http.post<GigOrderResponseDTO>(
      `${this.apiUrl}?gigId=${gigId}&buyerId=${buyerId}`,
      {}
    );

  }

  // ==========================
  // Send Quote
  // ==========================

  sendQuote(
    orderId: number,
    quotedPrice: number
  ): Observable<GigOrderResponseDTO> {

    return this.http.patch<GigOrderResponseDTO>(
      `${this.apiUrl}/${orderId}/quote?quotedPrice=${quotedPrice}`,
      {}
    );

  }

  // ==========================
  // Accept Quote
  // ==========================

  acceptQuote(
    orderId: number
  ): Observable<GigOrderResponseDTO> {

    return this.http.patch<GigOrderResponseDTO>(
      `${this.apiUrl}/${orderId}/accept-quote`,
      {}
    );

  }

  // ==========================
  // Reject Quote
  // ==========================

  rejectQuote(
    orderId: number
  ): Observable<GigOrderResponseDTO> {

    return this.http.patch<GigOrderResponseDTO>(
      `${this.apiUrl}/${orderId}/reject-quote`,
      {}
    );

  }

  // ==========================
  // Deliver Order
  // ==========================

  deliverOrder(
    orderId: number,
    deliveryMessage: string,
    deliveryFile: File
  ): Observable<GigOrderResponseDTO> {

    const formData = new FormData();

    if (deliveryMessage) {
      formData.append(
        'deliveryMessage',
        deliveryMessage
      );
    }

    formData.append(
      'deliveryFile',
      deliveryFile
    );

    return this.http.post<GigOrderResponseDTO>(
      `${this.apiUrl}/${orderId}/deliver`,
      formData
    );

  }

  // ==========================
  // Accept Delivery
  // ==========================

  acceptDelivery(
    orderId: number
  ): Observable<GigOrderResponseDTO> {

    return this.http.patch<GigOrderResponseDTO>(
      `${this.apiUrl}/${orderId}/accept-delivery`,
      {}
    );

  }

  // ==========================
  // Reject Delivery
  // ==========================

  rejectDelivery(
    orderId: number
  ): Observable<GigOrderResponseDTO> {

    return this.http.patch<GigOrderResponseDTO>(
      `${this.apiUrl}/${orderId}/reject-delivery`,
      {}
    );

  }

  // ==========================
  // Buyer Cancel
  // ==========================

  buyerCancel(
    orderId: number
  ): Observable<GigOrderResponseDTO> {

    return this.http.patch<GigOrderResponseDTO>(
      `${this.apiUrl}/${orderId}/buyer-cancel`,
      {}
    );

  }

  // ==========================
  // Seller Cancel
  // ==========================

  sellerCancel(
    orderId: number
  ): Observable<GigOrderResponseDTO> {

    return this.http.patch<GigOrderResponseDTO>(
      `${this.apiUrl}/${orderId}/seller-cancel`,
      {}
    );

  }

  // ==========================
  // Raise Dispute
  // ==========================

  raiseDispute(
    orderId: number
  ): Observable<GigOrderResponseDTO> {

    return this.http.patch<GigOrderResponseDTO>(
      `${this.apiUrl}/${orderId}/dispute`,
      {}
    );

  }

  // ==========================
  // Release Payment
  // ==========================

  releasePayment(
    orderId: number
  ): Observable<GigOrderResponseDTO> {

    return this.http.patch<GigOrderResponseDTO>(
      `${this.apiUrl}/${orderId}/release-payment`,
      {}
    );

  }

  // ==========================
  // Refund Buyer
  // ==========================

  refundBuyer(
    orderId: number
  ): Observable<GigOrderResponseDTO> {

    return this.http.patch<GigOrderResponseDTO>(
      `${this.apiUrl}/${orderId}/refund`,
      {}
    );

  }

  // ==========================
  // Get All
  // ==========================

  getAll(): Observable<GigOrderResponseDTO[]> {

    return this.http.get<GigOrderResponseDTO[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get By Id
  // ==========================

  getById(
    id: number
  ): Observable<GigOrderResponseDTO> {

    return this.http.get<GigOrderResponseDTO>(
      `${this.apiUrl}/${id}`
    );

  }

  // ==========================
  // Buyer Orders
  // ==========================

  getBuyerOrders(
    buyerId: number
  ): Observable<GigOrderResponseDTO[]> {

    return this.http.get<GigOrderResponseDTO[]>(
      `${this.apiUrl}/buyer/${buyerId}`
    );

  }

  // ==========================
  // Seller Orders
  // ==========================

  getSellerOrders(
    sellerId: number
  ): Observable<GigOrderResponseDTO[]> {

    return this.http.get<GigOrderResponseDTO[]>(
      `${this.apiUrl}/seller/${sellerId}`
    );

  }

  // ==========================
  // Orders By Status
  // ==========================

  getByStatus(
    status: GigOrderStatus
  ): Observable<GigOrderResponseDTO[]> {

    return this.http.get<GigOrderResponseDTO[]>(
      `${this.apiUrl}/status/${status}`
    );

  }

  // ==========================
  // Buyer Orders By Status
  // ==========================

  getBuyerOrdersByStatus(
    buyerId: number,
    status: GigOrderStatus
  ): Observable<GigOrderResponseDTO[]> {

    return this.http.get<GigOrderResponseDTO[]>(
      `${this.apiUrl}/buyer/${buyerId}/status/${status}`
    );

  }

  // ==========================
  // Seller Orders By Status
  // ==========================

  getSellerOrdersByStatus(
    sellerId: number,
    status: GigOrderStatus
  ): Observable<GigOrderResponseDTO[]> {

    return this.http.get<GigOrderResponseDTO[]>(
      `${this.apiUrl}/seller/${sellerId}/status/${status}`
    );

  }

  // ==========================
  // Orders By Gig
  // ==========================

  getByGigId(
    gigId: number
  ): Observable<GigOrderResponseDTO[]> {

    return this.http.get<GigOrderResponseDTO[]>(
      `${this.apiUrl}/gig/${gigId}`
    );

  }

  // ==========================
  // Count By Gig
  // ==========================

  countByGigId(
    gigId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}/gig/${gigId}/count`
    );

  }

  // ==========================
  // Count By Seller
  // ==========================

  countBySellerId(
    sellerId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}/seller/${sellerId}/count`
    );

  }

  // ==========================
  // Count By Buyer
  // ==========================

  countByBuyerId(
    buyerId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}/buyer/${buyerId}/count`
    );

  }

  // ==========================
  // Count Buyer Orders By Status
  // ==========================

  countBuyerOrdersByStatus(
    buyerId: number,
    status: GigOrderStatus
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}/buyer/${buyerId}/status/${status}/count`
    );

  }

  // ==========================
  // Count Seller Orders By Status
  // ==========================

  countSellerOrdersByStatus(
    sellerId: number,
    status: GigOrderStatus
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}/seller/${sellerId}/status/${status}/count`
    );

  }

  // ==========================
  // Exists
  // ==========================

  exists(
    gigId: number,
    buyerId: number
  ): Observable<boolean> {

    return this.http.get<boolean>(
      `${this.apiUrl}/gig/${gigId}/buyer/${buyerId}/exist`
    );

  }

  // ==========================
  // Active Order
  // ==========================

  getActiveOrder(
    gigId: number,
    buyerId: number
  ): Observable<GigOrderResponseDTO> {

    return this.http.get<GigOrderResponseDTO>(
      `${this.apiUrl}/gig/${gigId}/buyer/${buyerId}/active`
    );

  }

  // ==========================
  // Search
  // ==========================

  search(
    filter: GigOrderFilterRequestDTO
  ): Observable<GigOrderResponseDTO[]> {

    return this.http.post<GigOrderResponseDTO[]>(
      `${this.apiUrl}/search`,
      filter
    );

  }





}
