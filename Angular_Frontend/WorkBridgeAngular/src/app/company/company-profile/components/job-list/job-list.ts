import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { JobResponseModel } from '../../models/job.model';
import { JobService } from '../../services/job.service';
import { StorageService } from '../../../../auth/services/storage.service';
import { ToastService } from '../../../../services/toast.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-job-list',
  imports: [CommonModule],
  templateUrl: './job-list.html',
  styleUrl: './job-list.css',
})
export class JobList implements OnInit {





  // =====================================
  // Properties
  // =====================================

  companyProfileId = 0;

  jobs: JobResponseModel[] = [];

  loading = false;

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private jobService: JobService,

    private storage: StorageService,

    private toast: ToastService,

    private router: Router,
    private cdr: ChangeDetectorRef

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.companyProfileId =
      this.storage.getProfileId() ?? 0;

    this.loadJobs();

  }

  // =====================================
  // Load Jobs
  // =====================================

  loadJobs(): void {

    this.loading = true;

    this.jobService
      .getByCompanyProfileId(this.companyProfileId)
      .subscribe({

        next: (data) => {

          this.jobs = data;

          this.loading = false;
          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Failed to load jobs.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Create Job
  // =====================================

  createJob(): void {

    this.router.navigate([
      '/company/manage-jobs'
    ]);

  }

  // =====================================
  // View Job
  // =====================================

  viewJob(id: number): void {

    this.router.navigate([
      '/job-details',
      id
    ]);

  }

  // =====================================
  // Edit Job
  // =====================================

  editJob(id: number): void {

    this.router.navigate([
      '/company/manage-jobs',
      id
    ]);

  }

  // =====================================
  // Toggle Status
  // =====================================

  toggleStatus(job: JobResponseModel): void {

    this.jobService
      .toggleStatus(job.id)
      .subscribe({

        next: (updatedJob) => {

          job.isActive =
            updatedJob.isActive;
            console.log('deactivate  sdhhkjdchkdjshk');

          this.toast.show(

            updatedJob.isActive
              ? 'Job Activated Successfully.'
              : 'Job Deactivated Successfully.'

          );
          this.loadJobs();

        },

        error: () => {

          this.toast.show(
            'Unable to change job status.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Delete Job
  // =====================================

  deleteJob(job: JobResponseModel): void {

    const ok = confirm(

      `Delete "${job.title}" ?`

    );

    if (!ok) {

      return;

    }

    this.jobService
      .delete(job.id)
      .subscribe({

        next: () => {

          this.toast.show(
            'Job Deleted Successfully.'
          );

          this.loadJobs();

        },

        error: () => {

          this.toast.show(
            'Unable to delete job.',
            'danger'
          );

        }

      });

  }


  // =====================================
// View Applications
// =====================================

viewApplications(jobId: number): void {

  this.router.navigate([
    '/company/job-applications',
    jobId
  ]);

}



}
