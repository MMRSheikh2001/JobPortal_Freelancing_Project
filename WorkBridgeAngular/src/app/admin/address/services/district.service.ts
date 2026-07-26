import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DistrictRequestModel, DistrictResponseModel } from '../models/district.model';
import { Observable } from 'rxjs';
import { environment } from '../../../../enviroments/environment';

@Injectable({
  providedIn: 'root',
})
export class DistrictService {


  private apiUrl = environment.apiUrl+'districts/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Save District
  // ==========================

  save(
    district: DistrictRequestModel
  ): Observable<DistrictResponseModel> {

    return this.http.post<DistrictResponseModel>(
      this.apiUrl,
      district
    );

  }

  // ==========================
  // Get All Districts
  // ==========================

  getAll(): Observable<DistrictResponseModel[]> {

    return this.http.get<DistrictResponseModel[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get District By Id
  // ==========================

  getById(
    id: number
  ): Observable<DistrictResponseModel> {

    return this.http.get<DistrictResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Update District
  // ==========================

  update(
    id: number,
    district: DistrictRequestModel
  ): Observable<DistrictResponseModel> {

    return this.http.put<DistrictResponseModel>(
      `${this.apiUrl}${id}`,
      district
    );

  }

  // ==========================
  // Delete District
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
  // Get Districts By Division Id
  // ==========================

  getByDivisionId(
    divisionId: number
  ): Observable<DistrictResponseModel[]> {

    return this.http.get<DistrictResponseModel[]>(
      `${this.apiUrl}division/${divisionId}`
    );

  }

  // ==========================
  // Get Districts By Division Name
  // ==========================

  getByDivisionName(
    divisionName: string
  ): Observable<DistrictResponseModel[]> {

    return this.http.get<DistrictResponseModel[]>(
      `${this.apiUrl}division/name/${divisionName}`
    );

  }
}
