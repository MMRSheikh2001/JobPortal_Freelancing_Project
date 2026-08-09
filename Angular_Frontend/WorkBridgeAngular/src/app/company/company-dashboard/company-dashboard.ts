import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { FileResourceHandleService } from '../../services/file-resource-handle.service';
import { ToastService } from '../../services/toast.service';
import { StorageService } from '../../auth/services/storage.service';
import { DashboardService } from '../../dashboard/services/dashboard.service';
import { CompanyDashboardDTO } from '../../dashboard/models/company-dashboard.model';
import { UserDashboardDTO } from '../../dashboard/models/user-dashboard.model';

@Component({
  selector: 'app-company-dashboard',
  imports: [CommonModule],
  templateUrl: './company-dashboard.html',
  styleUrl: './company-dashboard.css',
})
export class CompanyDashboard implements OnInit {





  //=====================================
  // Properties
  //=====================================

  loading = false;

  userId = 0;

  companyProfileId = 0;

  companyDashboard!: CompanyDashboardDTO;

  buyerDashboard!: UserDashboardDTO;

  //=====================================
  // Constructor
  //=====================================

  constructor(

    private dashboardService: DashboardService,

    private storage: StorageService,

    private toast: ToastService,

    public fileService: FileResourceHandleService,

    private router: Router,

    private cdr: ChangeDetectorRef

  ) { }

  //=====================================
  // Init
  //=====================================

  ngOnInit(): void {

    this.userId = this.storage.getUserId() ?? 0;

    this.companyProfileId =
      this.storage.getProfileId() ?? 0;

    this.loadDashboard();

  }

  //=====================================
  // Load Dashboard
  //=====================================

  loadDashboard(): void {

    this.loading = true;

    this.dashboardService
      .getCompanyDashboard(this.companyProfileId)
      .subscribe({

        next: (company) => {

          this.companyDashboard = company;

          this.loadBuyerDashboard();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load company dashboard.',
            'danger'
          );

        }

      });

  }

  //=====================================
  // Buyer Dashboard
  //=====================================

  loadBuyerDashboard(): void {

    this.dashboardService
      .getUserDashboard(this.userId)
      .subscribe({

        next: (buyer) => {

          this.buyerDashboard = {

            ...buyer,

            recentApplications:
              buyer.recentApplications ?? [],

            recentOrders:
              buyer.recentOrders ?? [],

            latestJobs:
              buyer.latestJobs ?? [],

            popularGigs:
              buyer.popularGigs ?? []

          };

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load buyer information.',
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

  openGig(gigId: number): void {

    this.router.navigate([
      '/gig-details',
      gigId
    ]);

  }

  openOrder(orderId: number): void {

    this.router.navigate([
      '/company/buyer-gig-details',
      orderId
    ]);

  }

  openApplications(): void {

    this.router.navigate([
      '/company/job-list'
    ]);

  }

  openWallet(): void {

    this.router.navigate([
      '/company/wallet'
    ]);

  }

  openMessages(): void {

    this.router.navigate([
      '/company/conversation-list'
    ]);

  }

  //=====================================
  // Helpers
  //=====================================

  get profileImage(): string {

    return this.fileService.getCompanyProfileImage(
      this.companyDashboard?.profileImage
    );

  }

  getGigImage(fileName?: string): string {

    return this.fileService.getGigImage(fileName);

  }




}
