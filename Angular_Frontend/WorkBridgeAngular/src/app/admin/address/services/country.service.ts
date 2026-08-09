import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { CountryRequestModel, CountryResponseModel } from '../models/country.model';
import { Observable } from 'rxjs';
import { environment } from '../../../../enviroments/environment';

@Injectable({
  providedIn: 'root',
})
export class CountryService {


  private apiUrl = environment.apiUrl+'countries/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Save Country
  // ==========================

  save(country: CountryRequestModel): Observable<CountryResponseModel> {

    return this.http.post<CountryResponseModel>(
      this.apiUrl,
      country
    );

  }

  // ==========================
  // Get All Countries
  // ==========================

  getAll(): Observable<CountryResponseModel[]> {

    return this.http.get<CountryResponseModel[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get Country By Id
  // ==========================

  getById(id: number): Observable<CountryResponseModel> {

    return this.http.get<CountryResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Update Country
  // ==========================

  update(
    id: number,
    country: CountryRequestModel
  ): Observable<CountryResponseModel> {

    return this.http.put<CountryResponseModel>(
      `${this.apiUrl}${id}`,
      country
    );

  }

  // ==========================
  // Delete Country
  // ==========================

  delete(id: number): Observable<string> {

    return this.http.delete(
      `${this.apiUrl}${id}`,
      {
        responseType: 'text'
      }
    );

  }

}
