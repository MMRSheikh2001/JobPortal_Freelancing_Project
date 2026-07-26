import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../../enviroments/environment';
import { HttpClient } from '@angular/common/http';
import { JobRequestModel, JobResponseModel, JobSearchRequestModel } from '../models/job.model';

@Injectable({
  providedIn: 'root',
})
export class JobService {




  // =====================================
  // API
  // =====================================

  private api =
    environment.apiUrl + 'jobs/';

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private http: HttpClient

  ) { }

  // =====================================
  // Save
  // =====================================

  save(
    model: JobRequestModel
  ): Observable<JobResponseModel> {

    return this.http.post<JobResponseModel>(
      this.api,
      model
    );

  }

  // =====================================
  // Update
  // =====================================

  update(
    id: number,
    model: JobRequestModel
  ): Observable<JobResponseModel> {

    return this.http.put<JobResponseModel>(
      this.api + id,
      model
    );

  }

  // =====================================
  // Delete
  // =====================================

  delete(
    id: number
  ): Observable<string> {

    return this.http.delete(
      this.api + id,
      {
        responseType: 'text'
      }
    );

  }

  // =====================================
  // Get All
  // =====================================

  getAll(): Observable<JobResponseModel[]> {

    return this.http.get<JobResponseModel[]>(
      this.api
    );

  }

  // =====================================
  // Get By Id
  // =====================================

  getById(
    id: number
  ): Observable<JobResponseModel> {

    return this.http.get<JobResponseModel>(
      this.api + id
    );

  }

  // =====================================
  // Search
  // =====================================

  search(
    model: JobSearchRequestModel
  ): Observable<JobResponseModel[]> {

    return this.http.post<JobResponseModel[]>(
      this.api + 'search',
      model
    );

  }

  // =====================================
  // Company Jobs
  // =====================================

  getByCompanyProfileId(
    companyProfileId: number
  ): Observable<JobResponseModel[]> {

    return this.http.get<JobResponseModel[]>(
      this.api + 'companyprofile/' + companyProfileId
    );

  }

  // =====================================
  // Company User Jobs
  // =====================================

  getByCompanyUserId(
    userId: number
  ): Observable<JobResponseModel[]> {

    return this.http.get<JobResponseModel[]>(
      this.api + 'user/' + userId
    );

  }

  // =====================================
  // Active Jobs
  // =====================================

  getActiveJobs(): Observable<JobResponseModel[]> {

    return this.http.get<JobResponseModel[]>(
      this.api + 'active=true'
    );

  }

  // =====================================
  // Company Active Jobs
  // =====================================

  getActiveJobsByCompanyProfileId(
    companyProfileId: number
  ): Observable<JobResponseModel[]> {

    return this.http.get<JobResponseModel[]>(
      this.api +
      'companyprofile/' +
      companyProfileId +
      '/active=true'
    );

  }

  // =====================================
  // Top 10 Active Jobs
  // =====================================

  getTop10ActiveJobs(): Observable<JobResponseModel[]> {

    return this.http.get<JobResponseModel[]>(
      this.api +
      'createdat/top10/active=true'
    );

  }

  // =====================================
  // Top 20 Active Jobs
  // =====================================

  getTop20ActiveJobs(): Observable<JobResponseModel[]> {

    return this.http.get<JobResponseModel[]>(
      this.api +
      'createdat/top20/active=true'
    );

  }

  // =====================================
  // Count Company Jobs
  // =====================================

  countByCompanyProfileId(
    companyProfileId: number
  ): Observable<number> {

    return this.http.get<number>(
      this.api +
      'companyprofile/count/' +
      companyProfileId
    );

  }

  // =====================================
  // Count Active Company Jobs
  // =====================================

  countActiveByCompanyProfileId(
    companyProfileId: number
  ): Observable<number> {

    return this.http.get<number>(
      this.api +
      'companyprofile/count/' +
      companyProfileId +
      '/active=true'
    );

  }

  // =====================================
  // Count Inactive Company Jobs
  // =====================================

  countInactiveByCompanyProfileId(
    companyProfileId: number
  ): Observable<number> {

    return this.http.get<number>(
      this.api +
      'companyprofile/count/' +
      companyProfileId +
      '/active=false'
    );

  }

  // =====================================
  // Delete Company Jobs
  // =====================================

  deleteByCompanyProfileId(
    companyProfileId: number
  ): Observable<void> {

    return this.http.delete<void>(
      this.api +
      'companyprofile/delete/' +
      companyProfileId
    );

  }


  // =====================================
  // Toggle Job Status
  // =====================================

  toggleStatus(
    id: number
  ): Observable<JobResponseModel> {

    return this.http.patch<JobResponseModel>(
      `${this.api}${id}/toggle-status`,
      {}
    );

  }


}
