import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { PaymentService } from '../../services/payment.service';
import { StorageService } from '../../../auth/services/storage.service';
import { ToastService } from '../../../services/toast.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-deposit.component',
  imports: [CommonModule, FormsModule],
  templateUrl: './deposit.component.html',
  styleUrl: './deposit.component.css',
})
export class DepositComponent implements OnInit {






  // =====================================
  // Properties
  // =====================================

  loading = false;

  userId = 0;

  amount: number | null = null;

  readonly minimumAmount = 100;

  readonly maximumAmount = 50000;

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private paymentService: PaymentService,

    private storageService: StorageService,

    private toast: ToastService,

    private router: Router,

    private cdr: ChangeDetectorRef

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    const id =
      this.storageService.getUserId();

    if (!id) {

      this.toast.show(
        'User not found.',
        'danger'
      );

      this.router.navigate([
        '/login'
      ]);

      return;

    }

    this.userId = id;

  }

  // =====================================
  // Deposit
  // =====================================

  deposit(): void {

    if (this.amount == null) {

      this.toast.show(
        'Please enter an amount.',
        'warning'
      );

      return;

    }

    if (this.amount < this.minimumAmount) {

      this.toast.show(
        `Minimum deposit is ৳${this.minimumAmount}.`,
        'warning'
      );

      return;

    }

    if (this.amount > this.maximumAmount) {

      this.toast.show(
        `Maximum deposit is ৳${this.maximumAmount}.`,
        'warning'
      );

      return;

    }

    this.loading = true;

    this.paymentService
      .createDeposit(
        this.userId,
        this.amount
      )
      .subscribe({

        next: res => {

          this.loading = false;

          this.cdr.markForCheck();

          // Redirect to SSLCommerz
          window.location.href =
            res.gatewayPageUrl;

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to initiate payment.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Cancel
  // =====================================

  cancel(): void {

    const role =
      this.storageService.getRole();

    if (role === 'USER') {

      this.router.navigate([
        '/user/wallet'
      ]);

    }
    else if (role === 'COMPANY') {

      this.router.navigate([
        '/company/wallet'
      ]);

    }
    else if (role === 'ADMIN') {

      this.router.navigate([
        '/admin/wallet'
      ]);

    }

  }





}
