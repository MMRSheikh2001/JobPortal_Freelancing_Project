import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { StorageService } from '../../../auth/services/storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-payment-success',
  imports: [CommonModule],
  templateUrl: './payment-success.html',
  styleUrl: './payment-success.css',
})
export class PaymentSuccess implements OnInit {





  // =====================================
  // Properties
  // =====================================

  loading = true;

  role = '';

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private storageService: StorageService,

    private router: Router,

    private cdr: ChangeDetectorRef

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.role = this.storageService.getRole() ?? '';

    this.loading = false;

    this.cdr.markForCheck();

  }

  // =====================================
  // Go Wallet
  // =====================================

  goWallet(): void {

    this.router.navigate([
      this.getBaseRoute(),
      'wallet'
    ]);

  }

  // =====================================
  // View Transactions
  // =====================================

  viewTransactions(): void {

    this.router.navigate([
      this.getBaseRoute(),
      'wallet',
      'transactions'
    ]);

  }

  // =====================================
  // Dashboard
  // =====================================

  goDashboard(): void {

    this.router.navigate([
      this.getBaseRoute(),
      'dashboard'
    ]);

  }

  // =====================================
  // Base Route
  // =====================================

  private getBaseRoute(): string {

    switch (this.role) {

      case 'COMPANY':
        return '/company';

      case 'ADMIN':
        return '/admin';

      default:
        return '/user';

    }

  }



}
