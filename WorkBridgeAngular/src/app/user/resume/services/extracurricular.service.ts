import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ExtracurricularRequestModel, ExtracurricularResponseModel } from '../models/extracurricular.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../enviroments/environment';

@Injectable({
  providedIn: 'root',
})
export class ExtracurricularService {



  private apiUrl = environment.apiUrl + 'extracurriculars/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Save Extracurricular
  // ==========================

  save(
    extracurricular: ExtracurricularRequestModel
  ): Observable<ExtracurricularResponseModel> {

    return this.http.post<ExtracurricularResponseModel>(
      this.apiUrl,
      extracurricular
    );

  }

  // ==========================
  // Get All Extracurriculars
  // ==========================

  getAll(): Observable<ExtracurricularResponseModel[]> {

    return this.http.get<ExtracurricularResponseModel[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get Extracurricular By Id
  // ==========================

  getById(
    id: number
  ): Observable<ExtracurricularResponseModel> {

    return this.http.get<ExtracurricularResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Update Extracurricular
  // ==========================

  update(
    id: number,
    extracurricular: ExtracurricularRequestModel
  ): Observable<ExtracurricularResponseModel> {

    return this.http.put<ExtracurricularResponseModel>(
      `${this.apiUrl}${id}`,
      extracurricular
    );

  }

  // ==========================
  // Delete Extracurricular
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
  // Get Extracurriculars By User Profile Id
  // ==========================

  getByUserProfileId(
    userProfileId: number
  ): Observable<ExtracurricularResponseModel[]> {

    return this.http.get<ExtracurricularResponseModel[]>(
      `${this.apiUrl}userprofile/${userProfileId}`
    );

  }

  // ==========================
  // Count Extracurriculars By User Profile Id
  // ==========================

  countByUserProfileId(
    userProfileId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}userprofile/count/${userProfileId}`
    );

  }


}
