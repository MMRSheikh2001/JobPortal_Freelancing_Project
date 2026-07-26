import { Injectable } from '@angular/core';
import { AdminDashboardDTO } from '../models/admin-dashboard.model';
import { Observable } from 'rxjs';
import { FreelancerDashboardDTO } from '../models/freelancer-dashboard.model';
import { CompanyDashboardDTO } from '../models/company-dashboard.model';
import { UserDashboardDTO } from '../models/user-dashboard.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../enviroments/environment';
import { HomeStatisticsDTO } from '../models/home-statistics.model';

@Injectable({
  providedIn: 'root',
})
export class DashboardService {




  //=====================================
  // API
  //=====================================

  private api =
    environment.apiUrl + 'dashboards/';

  //=====================================
  // Constructor
  //=====================================

  constructor(
    private http: HttpClient
  ) { }

  //=====================================
  // User Dashboard
  //=====================================

  getUserDashboard(
    userId: number
  ): Observable<UserDashboardDTO> {

    return this.http.get<UserDashboardDTO>(
      `${this.api}user/${userId}`
    );

  }

  //=====================================
  // Company Dashboard
  //=====================================

  getCompanyDashboard(
    companyProfileId: number
  ): Observable<CompanyDashboardDTO> {

    return this.http.get<CompanyDashboardDTO>(
      `${this.api}company/${companyProfileId}`
    );

  }

  //=====================================
  // Freelancer Dashboard
  //=====================================

  getFreelancerDashboard(
    userProfileId: number
  ): Observable<FreelancerDashboardDTO> {

    return this.http.get<FreelancerDashboardDTO>(
      `${this.api}freelancer/${userProfileId}`
    );

  }

  //=====================================
  // Admin Dashboard
  //=====================================

  getAdminDashboard(
    userId: number
  ): Observable<AdminDashboardDTO> {

    return this.http.get<AdminDashboardDTO>(
      `${this.api}admin/${userId}`
    );

  }


  //=====================================
  // Home Statistics
  //=====================================

  getHomeStatistics(): Observable<HomeStatisticsDTO> {

    return this.http.get<HomeStatisticsDTO>(
      `${this.api}home-statistics`
    );

  }

}
