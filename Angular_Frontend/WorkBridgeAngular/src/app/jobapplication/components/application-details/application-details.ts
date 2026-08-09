import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AIInterviewSessionResponseDTO, JobApplicationResponseModel } from '../../models/job-application.model';
import { ResumeResponseModel } from '../../../user/resume/models/resume-response.model';
import { ResumeFileResponseModel } from '../../../user/resume/models/resume-file.model';
import { ActivatedRoute, Router } from '@angular/router';
import { JobApplicationService } from '../../services/job-application.service';
import { ResumeResponseService } from '../../../user/resume/services/resume-response.service';
import { ResumeUploadedFileService } from '../../../user/resume/services/resume-uploaded-file.service';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';
import { ToastService } from '../../../services/toast.service';
import { catchError, forkJoin, of } from 'rxjs';
import { StorageService } from '../../../auth/services/storage.service';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-application-details',
  imports: [CommonModule, FormsModule],
  templateUrl: './application-details.html',
  styleUrl: './application-details.css',
})
export class ApplicationDetails implements OnInit {// =====================================
  // IDs
  // =====================================

  applicationId = 0;

  loading = false;



  // =====================================
  // Models
  // =====================================

  application?: JobApplicationResponseModel;

  resume?: ResumeResponseModel;

  interview?: AIInterviewSessionResponseDTO | null;

  uploadedResume?: ResumeFileResponseModel | null;

  // =====================================
  // Company Notes
  // =====================================

  companyNotes = '';

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private route: ActivatedRoute,

    private router: Router,

    private applicationService: JobApplicationService,

    private resumeService: ResumeResponseService,

    private uploadedResumeService: ResumeUploadedFileService,

    private toast: ToastService,

    private fileService: FileResourceHandleService,

    private cdr: ChangeDetectorRef

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.applicationId = Number(

      this.route.snapshot.paramMap.get(
        'applicationId'
      )

    );

    this.loadApplication();

  }

  // =====================================
  // Load Application
  // =====================================

  loadApplication(): void {

    this.loading = true;

    this.applicationService
      .getById(this.applicationId)
      .subscribe({

        next: application => {

          this.application = application;

          this.companyNotes =
            application.companyNotes ?? '';

          this.loadResume();

          this.loadInterview();

          this.loadUploadedResume();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Application not found.',
            'danger'
          );

          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // Load Resume
  // =====================================

  loadResume(): void {

    if (!this.application) {

      return;

    }

    this.resumeService
      .getResume(
        this.application.userProfileId
      )
      .subscribe({

        next: resume => {

          this.resume = resume;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Failed to load resume.',
            'danger'
          );

          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // Load Interview
  // =====================================

  loadInterview(): void {

    if (!this.application) {

      return;

    }

    this.applicationService
      .getInterviewByApplicationId(
        this.application.id
      )
      .subscribe({

        next: interview => {

          this.interview = interview;

          this.cdr.markForCheck();

        },

        error: () => {

          /*
           Applicant never started
           interview.

           Backend returns 404.

           This is NOT an error.
          */

          this.interview = null;

          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // Load Uploaded Resume
  // =====================================

  loadUploadedResume(): void {

    if (!this.application) {

      return;

    }

    this.uploadedResumeService
      .getByUserProfileId(
        this.application.userProfileId
      )
      .subscribe({

        next: resume => {

          this.uploadedResume = resume;

          this.cdr.markForCheck();

        },

        error: () => {

          /*
           Applicant has not uploaded
           a resume.

           Ignore.
          */

          this.uploadedResume = null;

          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // Angular Resume
  // =====================================

  viewAngularResume(): void {

    if (!this.application) {
      return;
    }

    this.router.navigate([
      '/company/resume-preview',
      this.application.userProfileId
    ]);

  }

  // =====================================
  // Spring Resume (HTML)
  // =====================================

  previewSpringResume(): void {

    if (!this.application) {
      return;
    }

    this.resumeService
      .getHtml(this.application.userProfileId)
      .subscribe({

        next: html => {

          const win = window.open('', '_blank');

          if (win) {

            win.document.open();
            win.document.write(html);
            win.document.close();

          }

        },

        error: () => {

          this.toast.show(
            'Unable to load Spring resume.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Spring Resume PDF
  // =====================================

  downloadSpringPdf(): void {

    if (!this.application) {
      return;
    }

    this.resumeService
      .downloadPdf(this.application.userProfileId)
      .subscribe(blob => {

        const url =
          window.URL.createObjectURL(blob);

        const a =
          document.createElement('a');

        a.href = url;

        a.download = 'Resume.pdf';

        a.click();

        window.URL.revokeObjectURL(url);

      });

  }

  // =====================================
  // Uploaded Resume
  // =====================================

  previewUploadedResume(): void {

    if (
      !this.uploadedResume ||
      !this.uploadedResume.fileName
    ) {

      this.toast.show(
        'Applicant has not uploaded a resume.',
        'warning'
      );

      return;

    }

    window.open(

      this.fileService.getResume(
        this.uploadedResume.fileName
      ),

      '_blank'

    );

  }

  // =====================================
  // Save Company Notes
  // =====================================

  saveCompanyNotes(): void {

    if (!this.application) {

      return;

    }

    this.applicationService
      .updateCompanyNotes(
        this.application.id,
        this.companyNotes
      )
      .subscribe({

        next: application => {

          this.application = application;

          this.toast.show(
            'Company notes updated.'
          );

          this.cdr.markForCheck();

        },

        error: () => {

          this.toast.show(
            'Failed to update notes.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Shortlist
  // =====================================

  shortlist(): void {

    if (!this.application) {
      return;
    }

    if (!confirm('Shortlist this applicant?')) {
      return;
    }

    this.applicationService
      .shortlist(this.application.id)
      .subscribe({

        next: application => {

          this.application = application;

          this.toast.show(
            'Applicant shortlisted.'
          );

          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // Reject
  // =====================================

  reject(): void {

    if (!this.application) {
      return;
    }

    if (!confirm('Reject this applicant?')) {
      return;
    }

    this.applicationService
      .reject(this.application.id)
      .subscribe({

        next: application => {

          this.application = application;

          this.toast.show(
            'Applicant rejected.'
          );

          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // Hire
  // =====================================

  hire(): void {

    if (!this.application) {
      return;
    }

    if (!confirm('Hire this applicant?')) {
      return;
    }

    this.applicationService
      .hire(this.application.id)
      .subscribe({

        next: application => {

          this.application = application;

          this.toast.show(
            'Applicant hired.'
          );

          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // Company Logo
  // =====================================

  getCompanyLogo(fileName?: string): string {

    return this.fileService.getCompanyProfileImage(fileName);

  }

  // =====================================
  // Applicant Image
  // =====================================

  getProfileImage(fileName?: string): string {

    return this.fileService.getUserProfileImage(fileName);

  }

  // =====================================
  // Status Badge
  // =====================================
  getStatusBadge(
    status?: string
  ): string {

    switch (status) {

      case 'HIRED':
        return 'bg-success';

      case 'REJECTED':
        return 'bg-danger';

      case 'COMPANY_SHORTLISTED':
        return 'bg-info';

      case 'AI_COMPLETED':
        return 'bg-primary';

      case 'AI_PENDING':
        return 'bg-warning text-dark';

      case 'WITHDRAWN':
        return 'bg-secondary';

      default:
        return 'bg-secondary';

    }

  }

  // =====================================
  // Back
  // =====================================

  goBack(): void {

    if (!this.application) {
      return;
    }

    this.router.navigate([
      '/company/job-applications',
      this.application.jobId
    ]);

  }

  // =====================================
  // Interview Helpers
  // =====================================

  hasInterview(): boolean {

    return this.interview != null;

  }

  interviewStarted(): boolean {

    return !!this.interview?.startedAt;

  }

  interviewCompleted(): boolean {

    return this.interview?.completed === true;

  }

  hasQuestions(): boolean {

    return (this.interview?.questions?.length ?? 0) > 0;

  }


  // =====================================
  // User Image
  // =====================================

  getUserImage(): string {

    return this.fileService.getUserProfileImage(
      this.application?.userImage
    );

  }


}
