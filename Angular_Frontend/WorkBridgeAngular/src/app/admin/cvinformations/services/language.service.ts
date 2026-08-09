import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LanguageRequestModel, LanguageResponseModel } from '../models/language.model';
import { Observable } from 'rxjs';
import { environment } from '../../../../enviroments/environment';

@Injectable({
  providedIn: 'root',
})
export class LanguageService {



  private apiUrl = environment.apiUrl+'languages/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Save Language
  // ==========================

  save(
    language: LanguageRequestModel
  ): Observable<LanguageResponseModel> {

    return this.http.post<LanguageResponseModel>(
      this.apiUrl,
      language
    );

  }

  // ==========================
  // Get All Languages
  // ==========================

  getAll(): Observable<LanguageResponseModel[]> {

    return this.http.get<LanguageResponseModel[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get Language By Id
  // ==========================

  getById(
    id: number
  ): Observable<LanguageResponseModel> {

    return this.http.get<LanguageResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Update Language
  // ==========================

  update(
    id: number,
    language: LanguageRequestModel
  ): Observable<LanguageResponseModel> {

    return this.http.put<LanguageResponseModel>(
      `${this.apiUrl}${id}`,
      language
    );

  }

  // ==========================
  // Delete Language
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


}
