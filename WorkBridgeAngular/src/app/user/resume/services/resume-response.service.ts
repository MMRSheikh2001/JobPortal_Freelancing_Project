import { Injectable } from '@angular/core';
import { environment } from '../../../../enviroments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ResumeResponseModel } from '../models/resume-response.model';

@Injectable({
  providedIn: 'root',
})
export class ResumeResponseService {



  private base = `${environment.apiUrl}resume`;

  constructor(private http: HttpClient) { }

  // ── GET /api/resume/{userProfileId}
  // Returns the full resume data as JSON object
  // Use this to build a custom preview UI in Angular
  getResume(userProfileId: number): Observable<ResumeResponseModel> {
    return this.http.get<ResumeResponseModel>(`${this.base}/${userProfileId}`);
  }

  // ── GET /api/resume/{userProfileId}/html
  // Returns the rendered HTML string from Thymeleaf
  // Display it in an <iframe [srcdoc]="htmlString"> in your template
  getHtml(userProfileId: number): Observable<string> {
    return this.http.get(`${this.base}/${userProfileId}/html`, {
      responseType: 'text'   // ← must be 'text' — server returns text/html not JSON
    });
  }

  // ── GET /api/resume/{userProfileId}/pdf
  // Returns raw PDF bytes
  // Triggers a file download in the browser
  downloadPdf(userProfileId: number): Observable<Blob> {
    return this.http.get(`${this.base}/${userProfileId}/pdf`, {
      responseType: 'blob'   // ← must be 'blob' — server returns binary application/pdf
    });
  }


}
