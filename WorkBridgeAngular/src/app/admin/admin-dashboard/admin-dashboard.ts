import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { AdminDashboardDTO } from '../../dashboard/models/admin-dashboard.model';
import { DashboardService } from '../../dashboard/services/dashboard.service';
import { StorageService } from '../../auth/services/storage.service';
import { ToastService } from '../../services/toast.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-admin-dashboard',
  imports: [CommonModule],
  templateUrl: './admin-dashboard.html',
  styleUrl: './admin-dashboard.css',
})
export class AdminDashboard implements OnInit {






  //=====================================
  // Properties
  //=====================================

  loading = false;

  userId = 0;

  dashboard!: AdminDashboardDTO;

  //=====================================
  // Constructor
  //=====================================

  constructor(

    private dashboardService: DashboardService,

    private storage: StorageService,

    private toast: ToastService,

    private router: Router,

    private cdr: ChangeDetectorRef

  ) { }

  //=====================================
  // Init
  //=====================================

  ngOnInit(): void {

    this.userId = this.storage.getUserId() ?? 0;

    this.loadDashboard();

  }

  //=====================================
  // Load Dashboard
  //=====================================

  loadDashboard(): void {

    this.loading = true;

    this.dashboardService
      .getAdminDashboard(this.userId)
      .subscribe({

        next: (res) => {

          this.dashboard = res;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load admin dashboard.',
            'danger'
          );

        }

      });

  }

  //=====================================
  // Navigation
  //=====================================

  openDisputes(): void {

    this.router.navigate([
      '/admin/gig-order-disputes'
    ]);

  }

  openWallet(): void {

    this.router.navigate([
      '/admin/wallet'
    ]);

  }

  openTransactions(): void {

    this.router.navigate([
      '/admin/wallet/all-transactions'
    ]);

  }

  openNotifications(): void {

    this.router.navigate([
      '/admin/notifications'
    ]);

  }




}
