import { Injectable } from '@angular/core';
import { environment } from '../../../../enviroments/environment';
import { HttpClient } from '@angular/common/http';
import { EducationRequestModel, EducationResponseModel } from '../models/education.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EducationService {



  private apiUrl = environment.apiUrl + 'educations/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Save Education
  // ==========================

  save(
    education: EducationRequestModel
  ): Observable<EducationResponseModel> {

    return this.http.post<EducationResponseModel>(
      this.apiUrl,
      education
    );

  }

  // ==========================
  // Get All Educations
  // ==========================

  getAll(): Observable<EducationResponseModel[]> {

    return this.http.get<EducationResponseModel[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get Education By Id
  // ==========================

  getById(
    id: number
  ): Observable<EducationResponseModel> {

    return this.http.get<EducationResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Update Education
  // ==========================

  update(
    id: number,
    education: EducationRequestModel
  ): Observable<EducationResponseModel> {

    return this.http.put<EducationResponseModel>(
      `${this.apiUrl}${id}`,
      education
    );

  }

  // ==========================
  // Delete Education
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
  ): Observable<EducationResponseModel[]> {

    return this.http.get<EducationResponseModel[]>(
      `${this.apiUrl}userprofile/${userProfileId}`
    );

  }

  // ==========================
  // Count By User Profile Id
  // ==========================

  countByUserProfileId(
    userProfileId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}userprofile/count/${userProfileId}`
    );

  }


}
