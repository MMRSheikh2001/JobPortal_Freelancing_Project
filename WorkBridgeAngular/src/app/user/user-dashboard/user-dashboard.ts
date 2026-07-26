import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { UserDashboardDTO } from '../../dashboard/models/user-dashboard.model';
import { DashboardService } from '../../dashboard/services/dashboard.service';
import { StorageService } from '../../auth/services/storage.service';
import { ToastService } from '../../services/toast.service';
import { FileResourceHandleService } from '../../services/file-resource-handle.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-dashboard',
  imports: [CommonModule],
  templateUrl: './user-dashboard.html',
  styleUrl: './user-dashboard.css',
})
export class UserDashboard implements OnInit {





  //=====================================
  // Properties
  //=====================================

  loading = false;

  userId = 0;

  dashboard!: UserDashboardDTO;

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

    this.userId =
      this.storage.getUserId() ?? 0;

    this.loadDashboard();

    

  }

  //=====================================
  // Load Dashboard
  //=====================================

  loadDashboard(): void {

    this.loading = true;

    this.dashboardService
      .getUserDashboard(this.userId)
      .subscribe({

        next: (res) => {

          this.dashboard = {

            ...res,

            recentApplications:
              res.recentApplications ?? [],

            recentOrders:
              res.recentOrders ?? [],

            latestJobs:
              res.latestJobs ?? [],

            popularGigs:
              res.popularGigs ?? []

          };

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load dashboard.',
            'danger'
          );

        }

      });

  }

  //=====================================
  // Navigation
  //=====================================

  openApplication(applicationId: number): void {

    this.router.navigate([
      '/user/my-applications'
    ]);

  }

  openOrder(orderId: number): void {

    this.router.navigate([
      '/user/buyer-gig-details',
      orderId
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

  //=====================================
  // Helpers
  //=====================================

  get profileImage(): string {

    return this.fileService.getUserProfileImage(
      this.dashboard?.profileImage
    );

  }


  //=====================================
  // Application Status Badge
  //=====================================

  getApplicationBadge(status: string): string {

    switch (status) {

      case 'APPLIED':
        return 'bg-primary';

      case 'AI_PENDING':
        return 'bg-warning text-dark';

      case 'AI_COMPLETED':
        return 'bg-info';

      case 'AUTOMATIC_QUALIFIED':
        return 'bg-success';

      case 'COMPANY_SHORTLISTED':
        return 'bg-secondary';

      case 'HIRED':
        return 'bg-success';

      case 'REJECTED':
        return 'bg-danger';

      case 'WITHDRAWN':
        return 'bg-dark';

      default:
        return 'bg-light text-dark';

    }

  }



  //=====================================
  // Gig Order Badge
  //=====================================

  getOrderBadge(status: string): string {

    switch (status) {

      case 'ORDER_PLACED':
        return 'bg-primary';

      case 'QUOTED':
        return 'bg-info';

      case 'QUOTE_ACCEPTED':
        return 'bg-success';

      case 'QUOTE_REJECTED':
        return 'bg-danger';

      case 'DELIVERED':
        return 'bg-warning text-dark';

      case 'BUYER_ACCEPTED':
        return 'bg-success';

      case 'PAYMENT_RELEASED':
        return 'bg-success';

      case 'BUYER_REJECTED':
        return 'bg-danger';

      case 'BUYER_CANCELLED':
        return 'bg-secondary';

      case 'SELLER_CANCELLED':
        return 'bg-secondary';

      case 'SELLER_DISPUTED':
        return 'bg-dark';

      case 'REFUNDED':
        return 'bg-secondary';

      default:
        return 'bg-light text-dark';

    }

  }




}
