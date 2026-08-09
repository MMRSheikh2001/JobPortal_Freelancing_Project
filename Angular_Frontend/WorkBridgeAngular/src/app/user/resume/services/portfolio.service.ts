import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PortfolioRequestModel, PortfolioResponseModel } from '../models/portfolio.model';
import { environment } from '../../../../enviroments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class PortfolioService {



  private apiUrl = environment.apiUrl + 'portfolios/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Save Portfolio
  // ==========================

  save(
    portfolio: PortfolioRequestModel,
    file?: File
  ): Observable<PortfolioResponseModel> {

    const formData = new FormData();

    formData.append(
      'portfolio',
      new Blob(
        [JSON.stringify(portfolio)],
        {
          type: 'application/json'
        }
      )
    );

    if (file) {
      formData.append('file', file);
    }

    return this.http.post<PortfolioResponseModel>(
      this.apiUrl,
      formData
    );

  }

  // ==========================
  // Get All Portfolios
  // ==========================

  getAll(): Observable<PortfolioResponseModel[]> {

    return this.http.get<PortfolioResponseModel[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get Portfolio By Id
  // ==========================

  getById(
    id: number
  ): Observable<PortfolioResponseModel> {

    return this.http.get<PortfolioResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Update Portfolio
  // ==========================

  update(
    id: number,
    portfolio: PortfolioRequestModel,
    file?: File
  ): Observable<PortfolioResponseModel> {

    const formData = new FormData();

    formData.append(
      'portfolio',
      new Blob(
        [JSON.stringify(portfolio)],
        {
          type: 'application/json'
        }
      )
    );

    if (file) {
      formData.append('file', file);
    }

    return this.http.put<PortfolioResponseModel>(
      `${this.apiUrl}${id}`,
      formData
    );

  }

  // ==========================
  // Delete Portfolio
  // ==========================

  delete(
    id: number
  ): Observable<string> {

    return this.http.delete(
      `${this.apiUrl}${id}`,
      {
        responseType: 'text'
      }
    );

  }

  // ==========================
  // Delete Portfolio File
  // ==========================

  deleteFile(
    id: number
  ): Observable<string> {

    return this.http.delete(
      `${this.apiUrl}${id}/file`,
      {
        responseType: 'text'
      }
    );

  }

  // ==========================
  // Get Portfolios By User Profile Id
  // ==========================

  getByUserProfileId(
    userProfileId: number
  ): Observable<PortfolioResponseModel[]> {

    return this.http.get<PortfolioResponseModel[]>(
      `${this.apiUrl}userprofile/${userProfileId}`
    );

  }

  // ==========================
  // Count Portfolios By User Profile Id
  // ==========================

  countByUserProfileId(
    userProfileId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}count/userprofile/${userProfileId}`
    );

  }


}
