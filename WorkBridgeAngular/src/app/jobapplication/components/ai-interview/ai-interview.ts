import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { AIInterviewSessionResponseDTO } from '../../models/job-application.model';
import { ActivatedRoute, Router } from '@angular/router';
import { JobApplicationService } from '../../services/job-application.service';
import { ToastService } from '../../../services/toast.service';

@Component({
  selector: 'app-ai-interview',
  imports: [CommonModule, FormsModule],
  templateUrl: './ai-interview.html',
  styleUrl: './ai-interview.css',
})
export class AiInterview implements OnInit {







  // =====================================
  // IDs
  // =====================================

  applicationId = 0;

  // =====================================
  // State
  // =====================================

  loading = false;

  submitting = false;

  // =====================================
  // Interview
  // =====================================

  interview?: AIInterviewSessionResponseDTO;

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private route: ActivatedRoute,

    private router: Router,

    private applicationService: JobApplicationService,

    private toast: ToastService,

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

    this.startInterview();

  }

  // =====================================
  // Start / Load Interview
  // =====================================

  startInterview(): void {

    this.loading = true;

    this.applicationService
      .startInterview(this.applicationId)
      .subscribe({

        next: interview => {

          this.interview = interview;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load interview.',
            'danger'
          );

          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // Submit Interview
  // =====================================

  submitInterview(): void {

    if (!this.interview) {
      return;
    }

    if (!this.canSubmit()) {

      this.toast.show(
        'Please answer every question before submitting.',
        'warning'
      );

      return;

    }

    if (!confirm(
      'Submit your interview? You cannot change your answers afterwards.'
    )) {
      return;
    }

    this.submitting = true;

    this.applicationService
      .submitInterview(this.interview)
      .subscribe({

        next: interview => {

          this.interview = interview;

          this.submitting = false;

          this.toast.show(
            'Interview submitted successfully.'
          );

          this.router.navigate([
            '/user/my-applications'
          ]);

        },

        error: () => {

          this.submitting = false;

          this.toast.show(
            'Failed to submit interview.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Can Submit
  // =====================================

  canSubmit(): boolean {

    if (!this.interview?.questions?.length) {
      return false;
    }

    return this.interview.questions.every(

      q => (q.answer ?? '').trim().length > 0

    );

  }

  // =====================================
  // Question Count
  // =====================================

  questionCount(): number {

    return this.interview?.questions?.length ?? 0;

  }

  // =====================================
  // Is Completed
  // =====================================

  isCompleted(): boolean {

    return this.interview?.completed === true;

  }

  // =====================================
  // Back
  // =====================================

  goBack(): void {

    this.router.navigate([
      '/user/my-applications'
    ]);

  }




}
