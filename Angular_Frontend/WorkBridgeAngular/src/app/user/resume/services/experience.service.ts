import { Injectable } from '@angular/core';
import { environment } from '../../../../enviroments/environment';
import { HttpClient } from '@angular/common/http';
import { ExperienceRequestModel, ExperienceResponseModel } from '../models/experience.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ExperienceService {



  private apiUrl = environment.apiUrl + 'experiences/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Save Experience
  // ==========================

  save(
    experience: ExperienceRequestModel
  ): Observable<ExperienceResponseModel> {

    return this.http.post<ExperienceResponseModel>(
      this.apiUrl,
      experience
    );

  }

  // ==========================
  // Get All Experiences
  // ==========================

  getAll(): Observable<ExperienceResponseModel[]> {

    return this.http.get<ExperienceResponseModel[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get Experience By Id
  // ==========================

  getById(
    id: number
  ): Observable<ExperienceResponseModel> {

    return this.http.get<ExperienceResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Update Experience
  // ==========================

  update(
    id: number,
    experience: ExperienceRequestModel
  ): Observable<ExperienceResponseModel> {

    return this.http.put<ExperienceResponseModel>(
      `${this.apiUrl}${id}`,
      experience
    );

  }

  // ==========================
  // Delete Experience
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
  // Get Experiences By User Profile Id
  // ==========================

  getByUserProfileId(
    userProfileId: number
  ): Observable<ExperienceResponseModel[]> {

    return this.http.get<ExperienceResponseModel[]>(
      `${this.apiUrl}userprofile/${userProfileId}`
    );

  }

  // ==========================
  // Count Experiences By User Profile Id
  // ==========================

  countByUserProfileId(
    userProfileId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}userprofile/count/${userProfileId}`
    );

  }



}
