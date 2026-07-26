import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { WalletResponseModel } from '../models/wallet.model';
import { environment } from '../../../enviroments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class WalletService {




  private apiUrl = environment.apiUrl + 'wallets/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Get Wallet By Id
  // ==========================

  getById(
    id: number
  ): Observable<WalletResponseModel> {

    return this.http.get<WalletResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Get Wallet By User Id
  // ==========================

  getByUserId(
    userId: number
  ): Observable<WalletResponseModel> {

    return this.http.get<WalletResponseModel>(
      `${this.apiUrl}user/${userId}`
    );

  }

  // ==========================
  // Get Balance
  // ==========================

  getBalance(
    userId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}user/${userId}/balance`
    );

  }

  // ==========================
  // Get Frozen Balance
  // ==========================

  getFrozenBalance(
    userId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}user/${userId}/frozen-balance`
    );

  }



}
