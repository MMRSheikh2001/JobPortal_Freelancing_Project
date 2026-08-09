import { Injectable } from '@angular/core';
import { ReportFilterRequestDTO, ReportResponseDTO } from '../models/report.model';
import { Observable } from 'rxjs';
import { ReportStatus } from '../../enums/report-status.enum';
import { ReportType } from '../../enums/report-type.enum';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../enviroments/environment';

@Injectable({
  providedIn: 'root',
})
export class ReportService {



  private apiUrl = `${environment.apiUrl}reports`;

  constructor(
    private http: HttpClient
  ) { }

  //=====================================
  // Create Report
  //=====================================

  createReport(
    userId: number,
    subject: string,
    description: string,
    type: ReportType,
    attachment?: File
  ): Observable<ReportResponseDTO> {

    const formData = new FormData();

    formData.append('userId', userId.toString());
    formData.append('subject', subject);
    formData.append('description', description);
    formData.append('type', type);

    if (attachment) {
      formData.append(
        'attachment',
        attachment
      );
    }

    return this.http.post<ReportResponseDTO>(
      this.apiUrl,
      formData
    );

  }

  //=====================================
  // Resolve Report
  //=====================================

  resolveReport(
    reportId: number,
    adminReply: string
  ): Observable<ReportResponseDTO> {

    return this.http.patch<ReportResponseDTO>(
      `${this.apiUrl}/${reportId}/resolve?adminReply=${encodeURIComponent(adminReply)}`,
      {}
    );

  }

  //=====================================
  // Reject Report
  //=====================================

  rejectReport(
    reportId: number,
    adminReply: string
  ): Observable<ReportResponseDTO> {

    return this.http.patch<ReportResponseDTO>(
      `${this.apiUrl}/${reportId}/reject?adminReply=${encodeURIComponent(adminReply)}`,
      {}
    );

  }

  //=====================================
  // Get All
  //=====================================

  getAll(): Observable<ReportResponseDTO[]> {

    return this.http.get<ReportResponseDTO[]>(
      this.apiUrl
    );

  }

  //=====================================
  // Get By Id
  //=====================================

  getById(
    reportId: number
  ): Observable<ReportResponseDTO> {

    return this.http.get<ReportResponseDTO>(
      `${this.apiUrl}/${reportId}`
    );

  }

  //=====================================
  // Get By User
  //=====================================

  getByUserId(
    userId: number
  ): Observable<ReportResponseDTO[]> {

    return this.http.get<ReportResponseDTO[]>(
      `${this.apiUrl}/user/${userId}`
    );

  }

  //=====================================
  // Get By Status
  //=====================================

  getByStatus(
    status: ReportStatus
  ): Observable<ReportResponseDTO[]> {

    return this.http.get<ReportResponseDTO[]>(
      `${this.apiUrl}/status/${status}`
    );

  }

  //=====================================
  // Count By Status
  //=====================================

  countByStatus(
    status: ReportStatus
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}/status/${status}/count`
    );

  }

  //=====================================
  // Search
  //=====================================

  search(
    filter: ReportFilterRequestDTO
  ): Observable<ReportResponseDTO[]> {

    return this.http.post<ReportResponseDTO[]>(
      `${this.apiUrl}/search`,
      filter
    );

  }



}
