import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { DivisionRequestModel, DivisionResponseModel } from '../models/division.model';
import { Observable } from 'rxjs';
import { environment } from '../../../../enviroments/environment';

@Injectable({
  providedIn: 'root',
})
export class DivisionService {


  private apiUrl = environment.apiUrl+'divisions/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Save Division
  // ==========================

  save(
    division: DivisionRequestModel
  ): Observable<DivisionResponseModel> {

    return this.http.post<DivisionResponseModel>(
      this.apiUrl,
      division
    );

  }

  // ==========================
  // Get All Divisions
  // ==========================

  getAll(): Observable<DivisionResponseModel[]> {

    return this.http.get<DivisionResponseModel[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get Division By Id
  // ==========================

  getById(
    id: number
  ): Observable<DivisionResponseModel> {

    return this.http.get<DivisionResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Update Division
  // ==========================

  update(
    id: number,
    division: DivisionRequestModel
  ): Observable<DivisionResponseModel> {

    return this.http.put<DivisionResponseModel>(
      `${this.apiUrl}${id}`,
      division
    );

  }

  // ==========================
  // Delete Division
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
  // Get Divisions By Country Id
  // ==========================

  getByCountryId(
    countryId: number
  ): Observable<DivisionResponseModel[]> {

    return this.http.get<DivisionResponseModel[]>(
      `${this.apiUrl}country/${countryId}`
    );

  }

  // ==========================
  // Get Divisions By Country Name
  // ==========================

  getByCountryName(
    countryName: string
  ): Observable<DivisionResponseModel[]> {

    return this.http.get<DivisionResponseModel[]>(
      `${this.apiUrl}country/name/${countryName}`
    );

  }


}
