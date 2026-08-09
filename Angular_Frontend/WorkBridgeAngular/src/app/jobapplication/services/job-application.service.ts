import { Injectable } from '@angular/core';
import { environment } from '../../../enviroments/environment';
import { HttpClient } from '@angular/common/http';
import { AIInterviewSessionResponseDTO, JobApplicationFilterRequestDTO, JobApplicationRequestModel, JobApplicationResponseModel, ResumeScreeningResult } from '../models/job-application.model';
import { Observable } from 'rxjs';
import { ApplicationStatus } from '../../enums/application-status.enum';

@Injectable({
  providedIn: 'root',
})
export class JobApplicationService {



  // =====================================
  // API
  // =====================================

  private api =
    environment.apiUrl + 'jobapplications/';

  private aiApi =
    environment.apiUrl + 'ai/interview/';

  // =====================================
  // Constructor
  // =====================================

  constructor(
    private http: HttpClient
  ) { }

  // =====================================
  // Apply
  // =====================================

  apply(
    model: JobApplicationRequestModel
  ): Observable<JobApplicationResponseModel> {

    return this.http.post<JobApplicationResponseModel>(
      this.api,
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

  getAll(): Observable<JobApplicationResponseModel[]> {

    return this.http.get<JobApplicationResponseModel[]>(
      this.api
    );

  }

  // =====================================
  // Get By Id
  // =====================================

  getById(
    id: number
  ): Observable<JobApplicationResponseModel> {

    return this.http.get<JobApplicationResponseModel>(
      this.api + id
    );

  }

  // =====================================
  // User Applications
  // =====================================

  getByUserProfileId(
    userProfileId: number
  ): Observable<JobApplicationResponseModel[]> {

    return this.http.get<JobApplicationResponseModel[]>(
      this.api + 'userprofile/' + userProfileId
    );

  }

  getByUserProfileIdAndStatus(
    userProfileId: number,
    status: ApplicationStatus
  ): Observable<JobApplicationResponseModel[]> {

    return this.http.get<JobApplicationResponseModel[]>(
      this.api +
      'userprofile/' +
      userProfileId +
      '/status/' +
      status
    );

  }

  withdraw(
    applicationId: number,
    userProfileId: number
  ): Observable<JobApplicationResponseModel> {

    return this.http.patch<JobApplicationResponseModel>(
      this.api +
      'withdraw/' +
      applicationId +
      '/userprofile/' +
      userProfileId,
      {}
    );

  }

  // =====================================
  // Job Applications
  // =====================================

  getByJobId(
    jobId: number
  ): Observable<JobApplicationResponseModel[]> {

    return this.http.get<JobApplicationResponseModel[]>(
      this.api + 'job/' + jobId
    );

  }

  getByJobIdAndStatus(
    jobId: number,
    status: ApplicationStatus
  ): Observable<JobApplicationResponseModel[]> {

    return this.http.get<JobApplicationResponseModel[]>(
      this.api +
      'job/' +
      jobId +
      '/status/' +
      status
    );

  }

  getByJobIdOrderByAppliedAtDesc(
    jobId: number
  ): Observable<JobApplicationResponseModel[]> {

    return this.http.get<JobApplicationResponseModel[]>(
      this.api +
      'job/' +
      jobId +
      '/appliedat/desc'
    );

  }

  // =====================================
  // Company Applications
  // =====================================

  getByCompanyProfileId(
    companyProfileId: number
  ): Observable<JobApplicationResponseModel[]> {

    return this.http.get<JobApplicationResponseModel[]>(
      this.api +
      'companyprofile/' +
      companyProfileId
    );

  }

  getByCompanyProfileIdAndStatus(
    companyProfileId: number,
    status: ApplicationStatus
  ): Observable<JobApplicationResponseModel[]> {

    return this.http.get<JobApplicationResponseModel[]>(
      this.api +
      'companyprofile/' +
      companyProfileId +
      '/status/' +
      status
    );

  }

  getByCompanyProfileIdAndJobId(
    companyProfileId: number,
    jobId: number
  ): Observable<JobApplicationResponseModel[]> {

    return this.http.get<JobApplicationResponseModel[]>(
      this.api +
      'job/' +
      jobId +
      '/companyprofile/' +
      companyProfileId
    );

  }

  // =====================================
  // Company Actions
  // =====================================

  shortlist(
    applicationId: number
  ): Observable<JobApplicationResponseModel> {

    return this.http.patch<JobApplicationResponseModel>(
      this.api + 'shortlist/' + applicationId,
      {}
    );

  }

  reject(
    applicationId: number
  ): Observable<JobApplicationResponseModel> {

    return this.http.patch<JobApplicationResponseModel>(
      this.api + 'reject/' + applicationId,
      {}
    );

  }

  hire(
    applicationId: number
  ): Observable<JobApplicationResponseModel> {

    return this.http.patch<JobApplicationResponseModel>(
      this.api + 'hire/' + applicationId,
      {}
    );

  }

  updateCompanyNotes(
    applicationId: number,
    companyNotes: string
  ): Observable<JobApplicationResponseModel> {

    return this.http.patch<JobApplicationResponseModel>(
      this.api +
      'companynotes/' +
      applicationId +
      '?companyNotes=' +
      encodeURIComponent(companyNotes),
      {}
    );

  }

  // =====================================
  // AI
  // =====================================

  getPendingAIApplications(): Observable<JobApplicationResponseModel[]> {

    return this.http.get<JobApplicationResponseModel[]>(
      this.api + 'pendingai'
    );

  }

  getCompletedAIApplications(
    jobId: number
  ): Observable<JobApplicationResponseModel[]> {

    return this.http.get<JobApplicationResponseModel[]>(
      this.api +
      'job/' +
      jobId +
      '/aicompleted'
    );

  }

  selectTopQualifiedCandidates(
    jobId: number
  ): Observable<string> {

    return this.http.post(
      this.api +
      'job/' +
      jobId +
      '/select-top-qualified',
      {},
      {
        responseType: 'text'
      }
    );

  }

  startInterview(
    applicationId: number
  ): Observable<AIInterviewSessionResponseDTO> {

    return this.http.post<AIInterviewSessionResponseDTO>(
      this.aiApi +
      'start/' +
      applicationId,
      {}
    );

  }

  submitInterview(
    model: AIInterviewSessionResponseDTO
  ): Observable<AIInterviewSessionResponseDTO> {

    return this.http.post<AIInterviewSessionResponseDTO>(
      this.aiApi +
      'submit',
      model
    );

  }

  // =====================================
  // Counts
  // =====================================

  countByJobId(
    jobId: number
  ): Observable<number> {

    return this.http.get<number>(
      this.api + 'job/count/' + jobId
    );

  }

  countByJobIdAndStatus(
    jobId: number,
    status: ApplicationStatus
  ): Observable<number> {

    return this.http.get<number>(
      this.api +
      'count/job/' +
      jobId +
      '/status/' +
      status
    );

  }

  countByUserProfileId(
    userProfileId: number
  ): Observable<number> {

    return this.http.get<number>(
      this.api +
      'count/userprofile/' +
      userProfileId
    );

  }

  countByCompanyProfileId(
    companyProfileId: number
  ): Observable<number> {

    return this.http.get<number>(
      this.api +
      'count/companyprofile/' +
      companyProfileId
    );

  }

  countByCompanyProfileIdAndStatus(
    companyProfileId: number,
    status: ApplicationStatus
  ): Observable<number> {

    return this.http.get<number>(
      this.api +
      'count/companyprofile/' +
      companyProfileId +
      '/status/' +
      status
    );

  }

  countAIShortlisted(
    jobId: number
  ): Observable<number> {

    return this.http.get<number>(
      this.api +
      'job/' +
      jobId +
      '/count/aishortlisted'
    );

  }

  // =====================================
  // Utility
  // =====================================

  exists(
    jobId: number,
    userProfileId: number
  ): Observable<boolean> {

    return this.http.get<boolean>(
      this.api +
      'exist/job/' +
      jobId +
      '/userprofile/' +
      userProfileId
    );

  }

  getUserApplicationForJob(
    jobId: number,
    userProfileId: number
  ): Observable<JobApplicationResponseModel> {

    return this.http.get<JobApplicationResponseModel>(
      this.api +
      'job/' +
      jobId +
      '/userprofile/' +
      userProfileId
    );

  }

  //AI Job Match


  getJobMatchScore(
    jobId: number,
    userProfileId: number
  ): Observable<ResumeScreeningResult> {

    return this.http.get<ResumeScreeningResult>(
      this.aiApi + jobId + '/match/' + userProfileId
    );

  }

  // =====================================
  // AI Interview
  // =====================================

  getInterviewByApplicationId(
    applicationId: number
  ): Observable<AIInterviewSessionResponseDTO> {

    return this.http.get<AIInterviewSessionResponseDTO>(
      this.aiApi + applicationId
    );

  }


  // =====================================
  // Search
  // =====================================

  search(
    filter: JobApplicationFilterRequestDTO
  ): Observable<JobApplicationResponseModel[]> {

    return this.http.post<JobApplicationResponseModel[]>(
      this.api + 'search',
      filter
    );

  }


}
