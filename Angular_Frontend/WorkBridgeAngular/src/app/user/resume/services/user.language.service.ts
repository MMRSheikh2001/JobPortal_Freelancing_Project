import { Injectable } from '@angular/core';
import { environment } from '../../../../enviroments/environment';
import { HttpClient } from '@angular/common/http';
import { UserLanguageRequestModel, UserLanguageResponseModel } from '../models/user-language';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserLanguageService {



  private apiUrl = environment.apiUrl + 'userlanguages/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Save User Language
  // ==========================

  save(
    userLanguage: UserLanguageRequestModel
  ): Observable<UserLanguageResponseModel> {

    return this.http.post<UserLanguageResponseModel>(
      this.apiUrl,
      userLanguage
    );

  }

  // ==========================
  // Get All User Languages
  // ==========================

  getAll(): Observable<UserLanguageResponseModel[]> {

    return this.http.get<UserLanguageResponseModel[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get User Language By Id
  // ==========================

  getById(
    id: number
  ): Observable<UserLanguageResponseModel> {

    return this.http.get<UserLanguageResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Update User Language
  // ==========================

  update(
    id: number,
    userLanguage: UserLanguageRequestModel
  ): Observable<UserLanguageResponseModel> {

    return this.http.put<UserLanguageResponseModel>(
      `${this.apiUrl}${id}`,
      userLanguage
    );

  }

  // ==========================
  // Delete User Language
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
  // Get By User Profile Id
  // ==========================

  getByUserProfileId(
    userProfileId: number
  ): Observable<UserLanguageResponseModel[]> {

    return this.http.get<UserLanguageResponseModel[]>(
      `${this.apiUrl}userprofile/${userProfileId}`
    );

  }

  // ==========================
  // Get By Language Id
  // ==========================

  getByLanguageId(
    languageId: number
  ): Observable<UserLanguageResponseModel[]> {

    return this.http.get<UserLanguageResponseModel[]>(
      `${this.apiUrl}language/${languageId}`
    );

  }

  // ==========================
  // Get By User Profile Id And Language Id
  // ==========================

  getByUserProfileIdAndLanguageId(
    userProfileId: number,
    languageId: number
  ): Observable<UserLanguageResponseModel> {

    return this.http.get<UserLanguageResponseModel>(
      `${this.apiUrl}userprofile/${userProfileId}/language/${languageId}`
    );

  }

  // ==========================
  // Delete By User Profile Id And Language Id
  // ==========================

  deleteByUserProfileIdAndLanguageId(
    userProfileId: number,
    languageId: number
  ): Observable<string> {

    return this.http.delete(
      `${this.apiUrl}userprofile/${userProfileId}/language/${languageId}`,
      {
        responseType: 'text'
      }
    );

  }

  // ==========================
  // Count Languages By User Profile Id
  // ==========================

  countLanguagesByUserProfileId(
    userProfileId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}userprofile/count/${userProfileId}`
    );

  }


}
