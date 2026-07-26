import { Injectable } from '@angular/core';
import { PaymentStatus } from '../../enums/payment-status.enum';
import { Observable } from 'rxjs';
import { DepositSessionResponseDTO, PaymentResponseDTO } from '../models/payment.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../enviroments/environment';

@Injectable({
  providedIn: 'root',
})
export class PaymentService {






  private apiUrl =
    environment.apiUrl + 'payments/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================================
  // Create Deposit Session
  // ==========================================

  createDeposit(
    userId: number,
    amount: number
  ): Observable<DepositSessionResponseDTO> {

    return this.http.post<DepositSessionResponseDTO>(
      `${this.apiUrl}deposit/${userId}?amount=${amount}`,
      {}
    );

  }

  // ==========================================
  // Get Payment By Id
  // ==========================================

  getById(
    id: number
  ): Observable<PaymentResponseDTO> {

    return this.http.get<PaymentResponseDTO>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================================
  // Get By Gateway Transaction Id
  // ==========================================

  getByGatewayTransactionId(
    gatewayTransactionId: string
  ): Observable<PaymentResponseDTO> {

    return this.http.get<PaymentResponseDTO>(
      `${this.apiUrl}gateway/${gatewayTransactionId}`
    );

  }

  // ==========================================
  // Get User Payments
  // ==========================================

  getUserPayments(
    userId: number
  ): Observable<PaymentResponseDTO[]> {

    return this.http.get<PaymentResponseDTO[]>(
      `${this.apiUrl}user/${userId}`
    );

  }

  // ==========================================
  // Get All Payments
  // ==========================================

  getAll(): Observable<PaymentResponseDTO[]> {

    return this.http.get<PaymentResponseDTO[]>(
      this.apiUrl
    );

  }

  // ==========================================
  // Get Payments By Status
  // ==========================================

  getByStatus(
    status: PaymentStatus
  ): Observable<PaymentResponseDTO[]> {

    return this.http.get<PaymentResponseDTO[]>(
      `${this.apiUrl}status/${status}`
    );

  }



}
