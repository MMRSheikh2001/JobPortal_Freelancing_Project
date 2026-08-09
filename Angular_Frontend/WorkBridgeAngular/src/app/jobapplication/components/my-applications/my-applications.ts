import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { JobApplicationResponseModel } from '../../models/job-application.model';
import { JobApplicationService } from '../../services/job-application.service';
import { StorageService } from '../../../auth/services/storage.service';
import { ToastService } from '../../../services/toast.service';
import { Router } from '@angular/router';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';

@Component({
  selector: 'app-my-applications',
  imports: [CommonModule],
  templateUrl: './my-applications.html',
  styleUrl: './my-applications.css',
})
export class MyApplications implements OnInit {





  //=========================================
  // Properties
  //=========================================

  applications: JobApplicationResponseModel[] = [];

  loading = false;

  profileId: number | null = null;

  totalApplications = 0;
  aiPendingCount = 0;
  shortlistedCount = 0;
  hiredCount = 0;

  //=========================================
  // Constructor
  //=========================================

  constructor(

    private applicationService: JobApplicationService,

    private storageService: StorageService,

    private toast: ToastService,

    private router: Router,

    private cdr: ChangeDetectorRef,

    private fileService: FileResourceHandleService

  ) { }

  //=========================================
  // Init
  //=========================================

  ngOnInit(): void {

    this.profileId =
      this.storageService.getProfileId();

    this.loadApplications();

  }

  //=========================================
  // Load Applications
  //=========================================

  loadApplications(): void {

    if (!this.profileId) {

      this.toast.show(
        'User profile not found.',
        'danger'
      );

      return;

    }

    this.loading = true;

    this.cdr.markForCheck();

    this.applicationService
      .getByUserProfileId(
        this.profileId
      )
      .subscribe({

        next: (data) => {

          this.applications = data;

          this.calculateSummary();

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Failed to load applications.',
            'danger'
          );

          this.cdr.markForCheck();

        }

      });

  }

  //=========================================
  // Withdraw
  //=========================================

  withdraw(
    application: JobApplicationResponseModel
  ): void {

    if (!this.profileId) {

      return;

    }

    if (!confirm(
      'Withdraw this application?'
    )) {

      return;

    }

    this.applicationService
      .withdraw(
        application.id,
        this.profileId
      )
      .subscribe({

        next: (data) => {

          const index =
            this.applications.findIndex(
              x => x.id === data.id
            );

          if (index >= 0) {

            this.applications[index] = data;

          }

          this.toast.show(
            'Application withdrawn.'
          );

          this.cdr.markForCheck();

        },

        error: () => {

          this.toast.show(
            'Withdraw failed.',
            'danger'
          );

          this.cdr.markForCheck();

        }

      });

  }

  //=========================================
  // View Job
  //=========================================

  viewJob(
    application: JobApplicationResponseModel
  ): void {

    this.router.navigate([
      '/job-details',
      application.jobId
    ]);

  }

  //=========================================
  // Start AI Interview
  //=========================================

  startInterview(
    application: JobApplicationResponseModel
  ): void {

    this.router.navigate([
      '/user/ai-interview',
      application.id
    ]);

  }

  //=========================================
  // Helpers
  //=========================================

  getCompanyLogo(
    fileName?: string
  ): string {

    return this.fileService
      .getCompanyProfileImage(fileName);

  }

  canWithdraw(
    application: JobApplicationResponseModel
  ): boolean {

    return application.status !== 'WITHDRAWN'
      && application.status !== 'HIRED'
      && application.status !== 'REJECTED';

  }

  canStartInterview(
    application: JobApplicationResponseModel
  ): boolean {

    return application.status === 'AI_PENDING';

  }

  hasAiResult(
    application: JobApplicationResponseModel
  ): boolean {

    return application.aiMatchScore != null;

  }


  calculateSummary(): void {

    this.totalApplications = this.applications.length;

    this.aiPendingCount =
      this.applications.filter(
        x => x.status === 'AI_PENDING'
      ).length;

    this.shortlistedCount =
      this.applications.filter(
        x =>
          x.status === 'AUTOMATIC_QUALIFIED' ||
          x.status === 'COMPANY_SHORTLISTED'
      ).length;

    this.hiredCount =
      this.applications.filter(
        x => x.status === 'HIRED'
      ).length;

  }

  getStatusClass(status: string): string {

    switch (status) {

      case 'APPLIED':
        return 'bg-primary';

      case 'AI_PENDING':
        return 'bg-warning text-dark';

      case 'AI_COMPLETED':
        return 'bg-info';

      case 'AUTOMATIC_QUALIFIED':
      case 'HIRED':
        return 'bg-success';

      case 'COMPANY_SHORTLISTED':
        return 'bg-dark';

      case 'REJECTED':
        return 'bg-danger';

      case 'WITHDRAWN':
        return 'bg-secondary';

      default:
        return 'bg-light text-dark';

    }

  }


  //=========================================
  // View AI Evaluation
  //=========================================

  viewAiEvaluation(
    application: JobApplicationResponseModel
  ): void {

    this.router.navigate([
      '/user/ai-evaluation',
      application.id
    ]);

  }


}
