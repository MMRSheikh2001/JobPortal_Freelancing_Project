import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CompanyProfileResponseModel } from '../company-profile/models/company-profile.model';
import { JobResponseModel } from '../company-profile/models/job.model';
import { ActivatedRoute, Router } from '@angular/router';
import { CompanyProfileService } from '../company-profile/services/company-profile.service';
import { JobService } from '../company-profile/services/job.service';
import { ToastService } from '../../services/toast.service';
import { FileResourceHandleService } from '../../services/file-resource-handle.service';

@Component({
  selector: 'app-company-public-page',
  imports: [CommonModule],
  templateUrl: './company-public-page.html',
  styleUrl: './company-public-page.css',
})
export class CompanyPublicPage implements OnInit {





  //=====================================
  // Properties
  //=====================================

  loading = false;

  companyId!: number;

  company!: CompanyProfileResponseModel;

  activeJobs: JobResponseModel[] = [];

  //=====================================
  // Constructor
  //=====================================

  constructor(

    private route: ActivatedRoute,

    private router: Router,

    private companyService: CompanyProfileService,

    private jobService: JobService,

    private toast: ToastService,

    public fileService: FileResourceHandleService,

    private cdr: ChangeDetectorRef

  ) { }

  //=====================================
  // Init
  //=====================================

  ngOnInit(): void {

    this.route.paramMap.subscribe({

      next: params => {

        this.companyId = Number(
          params.get('companyId')
        );

        this.loadCompany();

      }

    });

  }

  //=====================================
  // Load Company
  //=====================================

  loadCompany(): void {

    this.loading = true;

    this.companyService
      .getById(this.companyId)
      .subscribe({

        next: res => {

          this.company = res;

          this.loadJobs();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load company.',
            'danger'
          );

        }

      });

  }

  //=====================================
  // Load Active Jobs
  //=====================================

  loadJobs(): void {

    this.jobService
      .getActiveJobsByCompanyProfileId(this.companyId)
      .subscribe({

        next: res => {

          this.activeJobs = res;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load jobs.',
            'warning'
          );

        }

      });

  }

  //=====================================
  // Navigation
  //=====================================

  openJob(jobId: number): void {

    this.router.navigate([
      '/job-details',
      jobId
    ]);

  }

  //=====================================
  // Helpers
  //=====================================

  getLogo(): string {

    return this.fileService.getCompanyProfileImage(
      this.company?.image
    );

  }




}
