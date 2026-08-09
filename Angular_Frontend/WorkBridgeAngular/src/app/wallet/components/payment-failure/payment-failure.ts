import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { StorageService } from '../../../auth/services/storage.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-payment-failure',
  imports: [CommonModule],
  templateUrl: './payment-failure.html',
  styleUrl: './payment-failure.css',
})
export class PaymentFailure implements OnInit {






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

    this.role =
      this.storageService.getRole() ?? '';

    this.loading = false;

    this.cdr.markForCheck();

  }

  // =====================================
  // Try Deposit Again
  // =====================================

  tryAgain(): void {

    this.router.navigate([
      this.getBaseRoute(),
      'wallet',
      'deposit'
    ]);

  }

  // =====================================
  // Wallet
  // =====================================

  goWallet(): void {

    this.router.navigate([
      this.getBaseRoute(),
      'wallet'
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
