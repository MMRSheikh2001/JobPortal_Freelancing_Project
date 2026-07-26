import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HomeStatisticsDTO } from '../../../dashboard/models/home-statistics.model';
import { JobResponseModel } from '../../../company/company-profile/models/job.model';
import { GigResponseModel } from '../../../gig/models/gig.model';
import { DashboardService } from '../../../dashboard/services/dashboard.service';
import { JobService } from '../../../company/company-profile/services/job.service';
import { GigService } from '../../../gig/services/gig.service';
import { ToastService } from '../../../services/toast.service';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [CommonModule, FormsModule],
  templateUrl: './home.html',
  styleUrl: './home.css',
})
export class Home implements OnInit {






  //=====================================
  // Properties
  //=====================================

  loading = false;

  searchKeyword = '';

  statistics!: HomeStatisticsDTO;

  latestJobs: JobResponseModel[] = [];

  popularGigs: GigResponseModel[] = [];

  //=====================================
  // Constructor
  //=====================================

  constructor(

    private dashboardService: DashboardService,

    private jobService: JobService,

    private gigService: GigService,

    private toast: ToastService,

    public fileService: FileResourceHandleService,

    private router: Router,

    private cdr: ChangeDetectorRef

  ) { }

  //=====================================
  // Init
  //=====================================

  ngOnInit(): void {

    this.loadHome();

  }

  //=====================================
  // Load Home
  //=====================================

  loadHome(): void {

    this.loading = true;

    this.dashboardService
      .getHomeStatistics()
      .subscribe({

        next: (res) => {

          this.statistics = res;

          this.loadLatestJobs();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load home statistics.',
            'danger'
          );

        }

      });

  }

  //=====================================
  // Latest Jobs
  //=====================================

  loadLatestJobs(): void {

    this.jobService
      .getTop10ActiveJobs()
      .subscribe({

        next: (jobs) => {

          this.latestJobs = jobs;

          this.loadPopularGigs();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load latest jobs.',
            'warning'
          );

        }

      });

  }

  //=====================================
  // Popular Gigs
  //=====================================

  loadPopularGigs(): void {

    this.gigService
      .getPopular()
      .subscribe({

        next: (gigs) => {

          this.popularGigs = gigs;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load popular gigs.',
            'warning'
          );

        }

      });

  }

  //=====================================
  // Search
  //=====================================

  searchJobs(): void {

    if (!this.searchKeyword.trim()) {

      this.router.navigate([
        '/job-list'
      ]);

      return;

    }

    this.router.navigate(
      ['/job-list'],
      {
        queryParams: {
          keyword: this.searchKeyword
        }
      }
    );

  }

  //=====================================
  // Navigation
  //=====================================

  browseJobs(): void {

    this.router.navigate([
      '/job-list'
    ]);

  }

  browseGigs(): void {

    this.router.navigate([
      '/gig-list'
    ]);

  }

  openJob(jobId: number): void {

    this.router.navigate([
      '/job-details',
      jobId
    ]);

  }

  openGig(gigId: number): void {

    this.router.navigate([
      '/gig-details',
      gigId
    ]);

  }

  register(): void {

    this.router.navigate([
      '/register'
    ]);

  }

  login(): void {

    this.router.navigate([
      '/login'
    ]);

  }

  //=====================================
  // Helpers
  //=====================================

  getGigImage(fileName?: string): string {

    return this.fileService.getGigImage(fileName);

  }




}
