import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { JobResponseModel } from '../../models/job.model';
import { JobApplicationRequestModel, JobApplicationResponseModel } from '../../../../jobapplication/models/job-application.model';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { JobService } from '../../services/job.service';
import { JobApplicationService } from '../../../../jobapplication/services/job-application.service';
import { StorageService } from '../../../../auth/services/storage.service';
import { ToastService } from '../../../../services/toast.service';
import { FileResourceHandleService } from '../../../../services/file-resource-handle.service';
import { SavedJobService } from '../../../../saved/services/saved-job.service';

@Component({
  selector: 'app-job-details',
  imports: [CommonModule],
  templateUrl: './job-details.html',
  styleUrl: './job-details.css',
})
export class JobDetails implements OnInit {


  jobId!: number;

  job?: JobResponseModel;

  application?: JobApplicationResponseModel;

  loading = false;

  applying = false;

  alreadyApplied = false;

  role: string | null = null;

  profileId: number | null = null;

  companyLogoUrl = '';

  saved = false;

  saving = false;

  userId = 0;

  aiMatchScore?: number;

  aiMatchFeedback = '';

  checkingMatch = false;



  constructor(

    private route: ActivatedRoute,

    private router: Router,

    private jobService: JobService,

    private applicationService: JobApplicationService,

    private storageService: StorageService,

    private toast: ToastService,

    private cdr: ChangeDetectorRef,
    private fileService: FileResourceHandleService,

    private savedJobService: SavedJobService,

  ) { }

  ngOnInit(): void {

    this.role = this.storageService.getRole();

    this.profileId = this.storageService.getProfileId();

    this.userId =
      this.storageService.getUserId() ?? 0;

    this.jobId = Number(
      this.route.snapshot.paramMap.get('id')
    );

    this.loadJob();

  }

  //=========================================
  // Load Job
  //=========================================

  loadJob(): void {

    this.loading = true;

    this.cdr.markForCheck();

    this.jobService.getById(this.jobId).subscribe({

      next: (data) => {

        this.job = data;

        this.companyLogoUrl =
          this.fileService.getCompanyProfileImage(
            data.companyLogo
          );
        this.checkSaved();

        this.loading = false;

        this.cdr.markForCheck();

        if (this.role === 'USER' && this.profileId) {

          this.loadUserApplication();

        }

      },

      error: () => {

        this.loading = false;

        this.toast.show(
          'Failed to load job.',
          'danger'
        );

        this.cdr.markForCheck();

      }

    });

  }

  //=========================================
  // Load Existing Application
  //=========================================

  loadUserApplication(): void {

    if (!this.profileId) {

      return;

    }

    this.applicationService
      .getUserApplicationForJob(
        this.jobId,
        this.profileId
      )
      .subscribe({

        next: (data) => {

          this.application = data;

          this.alreadyApplied = true;

          this.cdr.markForCheck();

        },

        error: () => {

          this.application = undefined;

          this.alreadyApplied = false;

          this.cdr.markForCheck();

        }

      });

  }

  //=========================================
  // Apply
  //=========================================

  apply(): void {

    if (!this.profileId) {

      this.toast.show(
        'Please login first.',
        'warning'
      );

      return;

    }

    if (this.alreadyApplied) {

      return;

    }

    const request: JobApplicationRequestModel = {

      jobId: this.jobId,

      userProfileId: this.profileId

    };

    this.applying = true;

    this.cdr.markForCheck();

    this.applicationService
      .apply(request)
      .subscribe({

        next: (data) => {

          this.application = data;

          this.alreadyApplied = true;

          this.applying = false;

          this.toast.show(
            'Application submitted successfully.'
          );
          this.router.navigate(['/user/my-applications']);

          this.cdr.markForCheck();

        },

        error: () => {

          this.applying = false;

          this.toast.show(
            'Failed to apply.',
            'danger'
          );

          this.cdr.markForCheck();

        }

      });

  }

  //=========================================
  // Withdraw
  //=========================================

  withdraw(): void {

    if (!this.application) {

      return;

    }

    this.applicationService
      .withdraw(
        this.application.id,
        this.profileId!
      )
      .subscribe({

        next: (data) => {

          this.application = data;

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
  // AI Interview
  //=========================================

  startInterview(): void {

    if (!this.application) {

      return;

    }

    this.router.navigate([
      '/user/ai-interview',
      this.application.id
    ]);

  }

  //=========================================
  // Admin
  //=========================================

  toggleStatus(): void {

    if (!this.job) {

      return;

    }

    this.jobService
      .toggleStatus(this.job.id)
      .subscribe({

        next: (data) => {

          this.job = data;

          this.toast.show(
            'Job updated.'
          );

          this.cdr.markForCheck();

        },

        error: () => {

          this.toast.show(
            'Update failed.',
            'danger'
          );

          this.cdr.markForCheck();

        }

      });

  }

  deleteJob(): void {

    if (!this.job) {

      return;

    }

    if (!confirm('Delete this job?')) {

      return;

    }

    this.jobService
      .delete(this.job.id)
      .subscribe({

        next: () => {

          this.toast.show(
            'Job deleted.'
          );

          this.router.navigate([
            '/'
          ]);

        },

        error: () => {

          this.toast.show(
            'Delete failed.',
            'danger'
          );

          this.cdr.markForCheck();

        }

      });

  }

  //=========================================
  // Helpers
  //=========================================

  isUser(): boolean {

    return this.role === 'USER';

  }

  isCompany(): boolean {

    return this.role === 'COMPANY';

  }

  isAdmin(): boolean {

    return this.role === 'ADMIN';

  }

  isCompanyOwner(): boolean {

    return this.storageService.getRole() === 'COMPANY'
      && this.storageService.getProfileId() === this.job?.companyProfileId;

  }

  getCompanyLogo(): string {

    return this.fileService.getCompanyProfileImage(
      this.job?.companyLogo
    );

  }


  getCompanyWebsite(): string {

    if (!this.job?.companyWebsite) {
      return '';
    }

    let website = this.job.companyWebsite.trim();

    if (
      !website.startsWith('http://') &&
      !website.startsWith('https://')
    ) {
      website = 'https://' + website;
    }

    return website;
  }


  //=========================================
  // Check Saved
  //=========================================

  checkSaved(): void {

    if (this.role !== 'USER') {

      return;

    }

    this.savedJobService
      .isJobSaved(
        this.userId,
        this.jobId
      )
      .subscribe({

        next: res => {

          this.saved = res;

          this.cdr.markForCheck();

        }

      });

  }

  //=========================================
  // Save / Unsave
  //=========================================

  toggleSave(): void {

    if (!this.storageService.isLoggedIn()) {

      this.toast.show(
        'Please login first.',
        'warning'
      );

      this.router.navigate(['/login']);

      return;

    }

    if (this.role !== 'USER') {

      this.toast.show(
        'Only users can save jobs.',
        'warning'
      );

      return;

    }

    this.saving = true;

    if (this.saved) {

      this.savedJobService
        .unsaveJob(
          this.userId,
          this.jobId
        )
        .subscribe({

          next: () => {

            this.saved = false;

            this.saving = false;

            this.toast.show(
              'Removed from saved jobs.'
            );

            this.cdr.markForCheck();

          },

          error: () => {

            this.saving = false;

            this.toast.show(
              'Unable to remove.',
              'danger'
            );

          }

        });

      return;

    }

    this.savedJobService
      .saveJob(
        this.userId,
        this.jobId
      )
      .subscribe({

        next: () => {

          this.saved = true;

          this.saving = false;

          this.toast.show(
            'Job saved.'
          );

          this.cdr.markForCheck();

        },

        error: () => {

          this.saving = false;

          this.toast.show(
            'Unable to save.',
            'danger'
          );

        }

      });

  }


  checkJobMatch(): void {

    if (!this.profileId) {

      this.toast.show(
        'Please login first.',
        'warning'
      );

      return;

    }

    this.checkingMatch = true;

    this.applicationService
      .getJobMatchScore(
        this.jobId,
        this.profileId
      )
      .subscribe({

        next: (data) => {

          this.aiMatchScore = data.matchScore;

          this.aiMatchFeedback = data.feedback;

          this.checkingMatch = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.checkingMatch = false;

          this.toast.show(
            'Unable to calculate job match.',
            'danger'
          );

          this.cdr.markForCheck();

        }

      });

  }


}
