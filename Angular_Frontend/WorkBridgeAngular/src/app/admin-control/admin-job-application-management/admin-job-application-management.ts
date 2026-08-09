import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { JobApplicationFilterRequestDTO, JobApplicationResponseModel } from '../../jobapplication/models/job-application.model';
import { ApplicationStatus } from '../../enums/application-status.enum';
import { JobApplicationService } from '../../jobapplication/services/job-application.service';
import { ToastService } from '../../services/toast.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-job-application-management',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-job-application-management.html',
  styleUrl: './admin-job-application-management.css',
})
export class AdminJobApplicationManagement implements OnInit {





  //-----------------------------------
  // Data
  //-----------------------------------

  loading = false;

  applications: JobApplicationResponseModel[] = [];

  //-----------------------------------
  // Filter
  //-----------------------------------

  filter: JobApplicationFilterRequestDTO = {
    keyword: '',
    companyProfileId: 0,
    jobId: 0,
    userProfileId: 0,
    categoryId: 0,
    status: undefined,
    aiCompleted: undefined,
    aiShortlisted: undefined,
    appliedFrom: '',
    appliedTo: ''
  };

  statuses = Object.values(ApplicationStatus);

  //-----------------------------------
  // Constructor
  //-----------------------------------

  constructor(
    private service: JobApplicationService,
    private toast: ToastService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) { }

  //-----------------------------------
  // Init
  //-----------------------------------

  ngOnInit(): void {
    this.search();
  }

  //-----------------------------------
  // Search
  //-----------------------------------

  search(): void {

    this.loading = true;

    this.service.search(this.filter)
      .subscribe({

        next: res => {

          this.applications = res;
          this.loading = false;
          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load applications.',
            'danger'
          );

        }

      });

  }

  //-----------------------------------
  // Clear Filter
  //-----------------------------------

  clearFilter(): void {

    this.filter = {
      keyword: '',
      companyProfileId: 0,
      jobId: 0,
      userProfileId: 0,
      categoryId: 0,
      status: undefined,
      aiCompleted: undefined,
      aiShortlisted: undefined,
      appliedFrom: '',
      appliedTo: ''
    };

    this.search();

  }

  //-----------------------------------
  // Applicant
  //-----------------------------------

  viewApplicant(
    userProfileId: number
  ): void {

    this.router.navigate([
      '/admin/user-profile-review',
      userProfileId
    ]);

  }

  //-----------------------------------
  // Company
  //-----------------------------------

  viewCompany(
    companyProfileId: number
  ): void {

    this.router.navigate([
      '/company-profile',
      companyProfileId
    ]);

  }

  //-----------------------------------
  // Job
  //-----------------------------------

  viewJob(
    jobId: number
  ): void {

    this.router.navigate([
      '/job-details',
      jobId
    ]);

  }

  //-----------------------------------
  // Shortlist
  //-----------------------------------

  shortlist(
    application: JobApplicationResponseModel
  ): void {

    this.service.shortlist(application.id)
      .subscribe({

        next: res => {

          application.status = res.status;

          this.toast.show(
            'Application shortlisted.'
          );

        },

        error: () => {

          this.toast.show(
            'Unable to shortlist.',
            'danger'
          );

        }

      });

  }

  //-----------------------------------
  // Hire
  //-----------------------------------

  hire(
    application: JobApplicationResponseModel
  ): void {

    this.service.hire(application.id)
      .subscribe({

        next: res => {

          application.status = res.status;

          this.toast.show(
            'Candidate hired.'
          );

        },

        error: () => {

          this.toast.show(
            'Unable to hire.',
            'danger'
          );

        }

      });

  }

  //-----------------------------------
  // Reject
  //-----------------------------------

  reject(
    application: JobApplicationResponseModel
  ): void {

    this.service.reject(application.id)
      .subscribe({

        next: res => {

          application.status = res.status;

          this.toast.show(
            'Application rejected.'
          );

        },

        error: () => {

          this.toast.show(
            'Unable to reject.',
            'danger'
          );

        }

      });

  }

  getStatusClass(status: ApplicationStatus): string {

    switch (status) {

      case ApplicationStatus.APPLIED:
        return 'bg-secondary';

      case ApplicationStatus.AI_PENDING:
        return 'bg-warning text-dark';

      case ApplicationStatus.AI_COMPLETED:
        return 'bg-info';

      case ApplicationStatus.AUTOMATIC_QUALIFIED:
        return 'bg-primary';

      case ApplicationStatus.COMPANY_SHORTLISTED:
        return 'bg-success';

      case ApplicationStatus.HIRED:
        return 'bg-success';

      case ApplicationStatus.REJECTED:
        return 'bg-danger';

      case ApplicationStatus.WITHDRAWN:
        return 'bg-dark';

      default:
        return 'bg-secondary';
    }

  }



}
