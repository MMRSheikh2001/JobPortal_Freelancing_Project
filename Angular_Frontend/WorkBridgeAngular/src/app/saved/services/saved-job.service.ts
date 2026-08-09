import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SavedJobResponseDTO } from '../models/saved-job.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../enviroments/environment';

@Injectable({
  providedIn: 'root',
})
export class SavedJobService {





  private apiUrl =
    environment.apiUrl + 'savedjobs/';

  constructor(
    private http: HttpClient
  ) { }

  // =====================================
  // Save Job
  // =====================================

  saveJob(
    userId: number,
    jobId: number
  ): Observable<SavedJobResponseDTO> {

    return this.http.post<SavedJobResponseDTO>(
      `${this.apiUrl}?userId=${userId}&jobId=${jobId}`,
      {}
    );

  }

  // =====================================
  // Unsave Job
  // =====================================

  unsaveJob(
    userId: number,
    jobId: number
  ): Observable<void> {

    return this.http.delete<void>(
      `${this.apiUrl}?userId=${userId}&jobId=${jobId}`
    );

  }

  // =====================================
  // Get Saved Jobs
  // =====================================

  getSavedJobs(
    userId: number
  ): Observable<SavedJobResponseDTO[]> {

    return this.http.get<SavedJobResponseDTO[]>(
      `${this.apiUrl}${userId}`
    );

  }

  // =====================================
  // Check Saved
  // =====================================

  isJobSaved(
    userId: number,
    jobId: number
  ): Observable<boolean> {

    return this.http.get<boolean>(
      `${this.apiUrl}check?userId=${userId}&jobId=${jobId}`
    );

  }

  // =====================================
  // Count Saved Jobs
  // =====================================

  countByUserId(
    userId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}${userId}/count`
    );

  }




}
