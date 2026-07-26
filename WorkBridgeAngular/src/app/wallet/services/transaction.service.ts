import { Injectable } from '@angular/core';
import { TransactionType } from '../../enums/transaction-type.enum';
import { Observable } from 'rxjs';
import { TransactionFilterDTO, TransactionResponseDTO } from '../models/transaction.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../enviroments/environment';

@Injectable({
  providedIn: 'root',
})
export class TransactionService {






  private apiUrl =
    environment.apiUrl + 'transactions/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Get By Id
  // ==========================

  getById(
    id: number
  ): Observable<TransactionResponseDTO> {

    return this.http.get<TransactionResponseDTO>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Get All
  // ==========================

  getAll(): Observable<TransactionResponseDTO[]> {

    return this.http.get<TransactionResponseDTO[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get By From User
  // ==========================

  getByFromUser(
    userId: number
  ): Observable<TransactionResponseDTO[]> {

    return this.http.get<TransactionResponseDTO[]>(
      `${this.apiUrl}from/${userId}`
    );

  }

  // ==========================
  // Get By To User
  // ==========================

  getByToUser(
    userId: number
  ): Observable<TransactionResponseDTO[]> {

    return this.http.get<TransactionResponseDTO[]>(
      `${this.apiUrl}to/${userId}`
    );

  }

  // ==========================
  // User Transaction History
  // ==========================

  getUserHistory(
    userId: number
  ): Observable<TransactionResponseDTO[]> {

    return this.http.get<TransactionResponseDTO[]>(
      `${this.apiUrl}history/${userId}`
    );

  }

  // ==========================
  // Get By Type
  // ==========================

  getByType(
    type: TransactionType
  ): Observable<TransactionResponseDTO[]> {

    return this.http.get<TransactionResponseDTO[]>(
      `${this.apiUrl}type/${type}`
    );

  }

  // ==========================
  // Count By Type
  // ==========================

  countByType(
    type: TransactionType
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}count/${type}`
    );

  }

  // ==========================
  // Search Transactions
  // ==========================

  search(
    filter: TransactionFilterDTO
  ): Observable<TransactionResponseDTO[]> {

    return this.http.post<TransactionResponseDTO[]>(
      `${this.apiUrl}search`,
      filter
    );

  }



}
