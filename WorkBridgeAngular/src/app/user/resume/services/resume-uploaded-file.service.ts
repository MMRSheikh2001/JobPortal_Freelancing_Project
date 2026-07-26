import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../enviroments/environment';
import { HttpClient } from '@angular/common/http';
import { ResumeFileResponseModel } from '../models/resume-file.model';

@Injectable({
  providedIn: 'root',
})
export class ResumeUploadedFileService {



  private apiUrl = environment.apiUrl + 'resumes/uploadedfile/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Upload Resume
  // ==========================

  uploadResume(
    userProfileId: number,
    cv: File
  ): Observable<ResumeFileResponseModel> {

    const formData = new FormData();

    formData.append(
      'userProfileId',
      userProfileId.toString()
    );

    formData.append(
      'cv',
      cv
    );

    return this.http.post<ResumeFileResponseModel>(
      this.apiUrl,
      formData
    );

  }

  // ==========================
  // Get All Uploaded Resumes
  // ==========================

  getAll(): Observable<ResumeFileResponseModel[]> {

    return this.http.get<ResumeFileResponseModel[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get Resume By Id
  // ==========================

  getById(
    id: number
  ): Observable<ResumeFileResponseModel> {

    return this.http.get<ResumeFileResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Get Resume By User Profile Id
  // ==========================

  getByUserProfileId(
    userProfileId: number
  ): Observable<ResumeFileResponseModel> {

    return this.http.get<ResumeFileResponseModel>(
      `${this.apiUrl}user/${userProfileId}`
    );

  }

  // ==========================
  // Delete Resume By Id
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
  // Delete Resume By User Profile Id
  // ==========================

  deleteByUserProfileId(
    userProfileId: number
  ): Observable<string> {

    return this.http.delete(
      `${this.apiUrl}user/${userProfileId}`,
      {
        responseType: 'text'
      }
    );

  }

  // ==========================
  // Check Resume Exists By User Profile Id
  // ==========================

  existsByUserProfileId(
    userProfileId: number
  ): Observable<boolean> {

    return this.http.get<boolean>(
      `${this.apiUrl}exists/${userProfileId}`
    );

  }


}
