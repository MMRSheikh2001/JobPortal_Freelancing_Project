import { Injectable } from '@angular/core';
import { environment } from '../../../../enviroments/environment';
import { HttpClient } from '@angular/common/http';
import { ResumeImportPreviewDTO } from '../models/resume-preview.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ResumeImportService {


  private apiUrl =
    environment.apiUrl + 'resume-import/';

  constructor(
    private http: HttpClient
  ) { }

  getPreviewFromResume(
    resumeId: number
  ): Observable<ResumeImportPreviewDTO> {

    return this.http.get<ResumeImportPreviewDTO>(
      this.apiUrl + resumeId
    );

  }



  saveImportedResume(
    userProfileId: number,
    preview: ResumeImportPreviewDTO
  ): Observable<void> {

    return this.http.post<void>(
      this.apiUrl + 'save/' + userProfileId,
      preview
    );

  }



}
