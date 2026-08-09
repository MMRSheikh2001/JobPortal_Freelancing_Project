import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { DashboardService } from '../services/dashboard.service';
import { StorageService } from '../../auth/services/storage.service';
import { ToastService } from '../../services/toast.service';
import { FileResourceHandleService } from '../../services/file-resource-handle.service';
import { Router } from '@angular/router';
import { FreelancerDashboardDTO } from '../models/freelancer-dashboard.model';

@Component({
  selector: 'app-freelancer-dashboard',
  imports: [CommonModule],
  templateUrl: './freelancer-dashboard.html',
  styleUrl: './freelancer-dashboard.css',
})
export class FreelancerDashboard implements OnInit {




  //=====================================
  // Properties
  //=====================================

  loading = false;

  profileId = 0;

  dashboard!: FreelancerDashboardDTO;

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

    this.profileId =
      this.storage.getProfileId() ?? 0;

    this.loadDashboard();

  }

  //=====================================
  // Load Dashboard
  //=====================================

  loadDashboard(): void {

    this.loading = true;

    this.dashboardService
      .getFreelancerDashboard(this.profileId)
      .subscribe({

        next: (res) => {

          this.dashboard = {

            ...res,

            recentOrders:
              res.recentOrders ?? [],

            myPopularGigs:
              res.myPopularGigs ?? []

          };

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load freelancer dashboard.',
            'danger'
          );

        }

      });

  }

  //=====================================
  // Navigation
  //=====================================

  openOrder(order: any): void {

    this.router.navigate([
      '/user/freelancer-gig-details',
      order.id
    ]);

  }

  openGig(gig: any): void {

    this.router.navigate([
      '/gig-details',
      gig.id
    ]);

  }

  openManageGig(gig: any): void {

    this.router.navigate([
      '/user/manage-gig',
      gig.id
    ]);

  }

  openWallet(): void {

    this.router.navigate([
      '/user/wallet'
    ]);

  }

  openMessages(): void {

    this.router.navigate([
      '/user/conversation-list'
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

  getGigImage(fileName?: string): string {

    return this.fileService.getGigImage(fileName);

  }


}
