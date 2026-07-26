import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AIInterviewSessionResponseDTO, JobApplicationResponseModel } from '../../models/job-application.model';
import { ActivatedRoute, Router } from '@angular/router';
import { JobApplicationService } from '../../services/job-application.service';
import { ToastService } from '../../../services/toast.service';

@Component({
  selector: 'app-ai-evaluation',
  imports: [CommonModule],
  templateUrl: './ai-evaluation.html',
  styleUrl: './ai-evaluation.css',
})
export class AiEvaluation implements OnInit {





  //=========================================
  // Properties
  //=========================================

  applicationId!: number;

  loading = false;

  application?: JobApplicationResponseModel;

  interview?: AIInterviewSessionResponseDTO;

  //=========================================
  // Constructor
  //=========================================

  constructor(

    private route: ActivatedRoute,

    private router: Router,

    private applicationService: JobApplicationService,

    private toast: ToastService,

    private cdr: ChangeDetectorRef

  ) { }

  //=========================================
  // Init
  //=========================================

  ngOnInit(): void {

    this.applicationId = Number(

      this.route.snapshot.paramMap.get(
        'applicationId'
      )

    );

    this.loadData();

  }

  //=========================================
  // Load
  //=========================================

  loadData(): void {

    this.loading = true;

    this.applicationService
      .getById(this.applicationId)
      .subscribe({

        next: application => {

          this.application = application;

          if (application.aiInterviewEnabled) {

            this.loadInterview();

          }
          else {

            this.loading = false;

            this.cdr.markForCheck();

          }

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Failed to load application.',
            'danger'
          );

        }

      });

  }

  //=========================================
  // Interview
  //=========================================

  loadInterview(): void {

    this.applicationService
      .getInterviewByApplicationId(
        this.applicationId
      )
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

        }

      });

  }

  //=========================================
  // Helpers
  //=========================================

  hasInterview(): boolean {

    return this.application?.aiInterviewEnabled === true;

  }

  hasQuestions(): boolean {

    return (this.interview?.questions?.length ?? 0) > 0;

  }

  getScoreClass(score?: number | null): string {

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

  back(): void {

    this.router.navigate([
      '/user/my-applications'
    ]);

  }




}
