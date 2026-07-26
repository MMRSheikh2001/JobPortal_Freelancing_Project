import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { JobApplicationResponseModel } from '../../models/job-application.model';
import { ActivatedRoute, Router } from '@angular/router';
import { JobApplicationService } from '../../services/job-application.service';
import { StorageService } from '../../../auth/services/storage.service';
import { ToastService } from '../../../services/toast.service';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';
import { CommonModule, DatePipe } from '@angular/common';

@Component({
  selector: 'app-company-job-applications',
  imports: [CommonModule, DatePipe],
  templateUrl: './company-job-applications.html',
  styleUrl: './company-job-applications.css',
})
export class CompanyJobApplications implements OnInit {





  //=========================================
  // Properties
  //=========================================

  applications: JobApplicationResponseModel[] = [];

  loading = false;

  companyProfileId: number | null = null;

  jobId!: number;

  //=========================================
  // Constructor
  //=========================================

  constructor(

    private route: ActivatedRoute,

    private router: Router,

    private applicationService: JobApplicationService,

    private storageService: StorageService,

    private toast: ToastService,

    private cdr: ChangeDetectorRef,

    private fileService: FileResourceHandleService

  ) { }

  //=========================================
  // Init
  //=========================================

  ngOnInit(): void {

    this.companyProfileId =
      this.storageService.getProfileId();

    this.jobId = Number(
      this.route.snapshot.paramMap.get('jobId')
    );

    this.loadApplications();

  }

  //=========================================
  // Load Applications
  //=========================================

  loadApplications(): void {

    if (!this.companyProfileId) {

      this.toast.show(
        'Company profile not found.',
        'danger'
      );

      return;

    }

    this.loading = true;

    this.cdr.markForCheck();

    this.applicationService
      .getByCompanyProfileIdAndJobId(
        this.companyProfileId,
        this.jobId
      )
      .subscribe({

        next: (data) => {

          this.applications = data;

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
  // Shortlist
  //=========================================

  shortlist(
    application: JobApplicationResponseModel
  ): void {

    this.applicationService
      .shortlist(application.id)
      .subscribe({

        next: (data) => {

          this.updateApplication(data);

          this.toast.show(
            'Applicant shortlisted.'
          );

        },

        error: () => {

          this.toast.show(
            'Shortlist failed.',
            'danger'
          );

        }

      });

  }

  //=========================================
  // Reject
  //=========================================

  reject(
    application: JobApplicationResponseModel
  ): void {

    this.applicationService
      .reject(application.id)
      .subscribe({

        next: (data) => {

          this.updateApplication(data);

          this.toast.show(
            'Applicant rejected.'
          );

        },

        error: () => {

          this.toast.show(
            'Reject failed.',
            'danger'
          );

        }

      });

  }

  //=========================================
  // Hire
  //=========================================

  hire(
    application: JobApplicationResponseModel
  ): void {

    this.applicationService
      .hire(application.id)
      .subscribe({

        next: (data) => {

          this.updateApplication(data);

          this.toast.show(
            'Applicant hired.'
          );

        },

        error: () => {

          this.toast.show(
            'Hire failed.',
            'danger'
          );

        }

      });

  }

  //=========================================
  // View Details
  //=========================================

  viewDetails(
    application: JobApplicationResponseModel
  ): void {

    this.router.navigate([
      '/company/application-details',
      application.id
    ]);

  }

  //=========================================
  // Helpers
  //=========================================

  private updateApplication(
    application: JobApplicationResponseModel
  ): void {

    const index =
      this.applications.findIndex(
        x => x.id === application.id
      );

    if (index >= 0) {

      this.applications[index] = application;

    }

    this.cdr.markForCheck();

  }

  getApplicantImage(
    fileName?: string
  ): string {

    return this.fileService.getUserProfileImage(
      fileName
    );

  }

  canShortlist(
    application: JobApplicationResponseModel
  ): boolean {

    return application.status === 'APPLIED'
      || application.status === 'AI_PENDING'
      || application.status === 'AI_COMPLETED'
      || application.status === 'AUTOMATIC_QUALIFIED';

  }

  canReject(
    application: JobApplicationResponseModel
  ): boolean {

    return application.status !== 'HIRED'
      && application.status !== 'REJECTED'
      && application.status !== 'WITHDRAWN';

  }

  canHire(
    application: JobApplicationResponseModel
  ): boolean {

    return application.status === 'COMPANY_SHORTLISTED';

  }

  getScoreClass(score: number | null | undefined): string {

    if (score == null) {
      return 'text-muted';
    }

    if (score >= 80) {
      return 'text-success fw-bold';
    }

    if (score >= 60) {
      return 'text-warning fw-bold';
    }

    return 'text-danger fw-bold';

  }


  //=========================================
  // Run AI Shortlisting
  //=========================================

  runAIShortlisting(): void {

    if (!confirm('Run AI shortlisting now?')) {
      return;
    }

    this.applicationService
      .selectTopQualifiedCandidates(this.jobId)
      .subscribe({

        next: (message) => {

          this.toast.show(message);

          // Reload list so new AI shortlist flags appear
          this.loadApplications();

        },

        error: () => {

          this.toast.show(
            'AI shortlisting failed.',
            'danger'
          );

        }

      });

  }

  get aiShortlistedApplications(): JobApplicationResponseModel[] {

    return this.applications.filter(

      application => application.aiShortlisted

    );

  }



}
