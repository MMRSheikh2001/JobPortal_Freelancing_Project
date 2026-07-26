import { Injectable } from '@angular/core';
import { WithdrawRequestModel, WithdrawResponseModel } from '../models/withdraw.model';
import { Observable } from 'rxjs';
import { environment } from '../../../enviroments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class WithdrawService {





  // =====================================
  // API
  // =====================================

  private readonly apiUrl =
    environment.apiUrl + 'withdraws/';

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private http: HttpClient

  ) { }

  // =====================================
  // USER
  // =====================================

  createWithdraw(
    request: WithdrawRequestModel
  ): Observable<WithdrawResponseModel> {

    return this.http.post<WithdrawResponseModel>(
      this.apiUrl,
      request
    );

  }

  getUserWithdraws(
    userId: number
  ): Observable<WithdrawResponseModel[]> {

    return this.http.get<WithdrawResponseModel[]>(
      `${this.apiUrl}user/${userId}`
    );

  }

  getWithdrawById(
    withdrawId: number,
    userId: number
  ): Observable<WithdrawResponseModel> {

    return this.http.get<WithdrawResponseModel>(
      `${this.apiUrl}${withdrawId}/user/${userId}`
    );

  }

  // =====================================
  // ADMIN
  // =====================================

  getPendingWithdraws(): Observable<WithdrawResponseModel[]> {

    return this.http.get<WithdrawResponseModel[]>(
      `${this.apiUrl}pending`
    );

  }

  getApprovedWithdraws(): Observable<WithdrawResponseModel[]> {

    return this.http.get<WithdrawResponseModel[]>(
      `${this.apiUrl}approved`
    );

  }

  getRejectedWithdraws(): Observable<WithdrawResponseModel[]> {

    return this.http.get<WithdrawResponseModel[]>(
      `${this.apiUrl}rejected`
    );

  }

  approveWithdraw(
    withdrawId: number,
    adminRemarks: string,
    transactionReference: string
  ): Observable<WithdrawResponseModel> {

    const params = new HttpParams()
      .set(
        'adminRemarks',
        adminRemarks
      )
      .set(
        'transactionReference',
        transactionReference
      );

    return this.http.patch<WithdrawResponseModel>(
      `${this.apiUrl}${withdrawId}/approve`,
      {},
      { params }
    );

  }

  rejectWithdraw(
    withdrawId: number,
    adminRemarks: string
  ): Observable<WithdrawResponseModel> {

    const params = new HttpParams()
      .set(
        'adminRemarks',
        adminRemarks
      );

    return this.http.patch<WithdrawResponseModel>(
      `${this.apiUrl}${withdrawId}/reject`,
      {},
      { params }
    );

  }



}
