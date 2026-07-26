import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { WithdrawStatus } from '../../../enums/withdraw-status.enum';
import { WithdrawMethod } from '../../../enums/withdraw-method.enum';
import { WalletResponseModel } from '../../models/wallet.model';
import { WithdrawRequestModel, WithdrawResponseModel } from '../../models/withdraw.model';
import { WalletService } from '../../services/wallet.service';
import { WithdrawService } from '../../services/withdraw.service';
import { StorageService } from '../../../auth/services/storage.service';
import { ToastService } from '../../../services/toast.service';

@Component({
  selector: 'app-user-withdraw',
  imports: [CommonModule, FormsModule],
  templateUrl: './user-withdraw.html',
  styleUrl: './user-withdraw.css',
})
export class UserWithdraw implements OnInit {






  // =====================================
  // Properties
  // =====================================

  loading = false;

  submitting = false;

  userId = 0;

  wallet?: WalletResponseModel;

  withdraws: WithdrawResponseModel[] = [];

  readonly withdrawMethods =
    Object.values(WithdrawMethod);

  readonly minimumAmount = 100;

  request: WithdrawRequestModel = {

    userId: 0,

    amount: 0,

    withdrawMethod: WithdrawMethod.BKASH,

    accountNumber: '',

    accountName: ''

  };

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private walletService: WalletService,

    private withdrawService: WithdrawService,

    private storageService: StorageService,

    private toast: ToastService,

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

      return;

    }

    this.userId = id;

    this.request.userId = id;

    this.loadData();

  }

  // =====================================
  // Load Wallet + Requests
  // =====================================

  loadData(): void {

    this.loading = true;

    this.walletService
      .getByUserId(this.userId)
      .subscribe({

        next: wallet => {

          this.wallet = wallet;

          this.loadWithdraws();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load wallet.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Load Withdraw Requests
  // =====================================

  loadWithdraws(): void {

    this.withdrawService
      .getUserWithdraws(this.userId)
      .subscribe({

        next: res => {

          this.withdraws = res;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load withdrawal requests.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Submit Withdraw
  // =====================================

  submit(): void {

    if (this.request.amount <= 0) {

      this.toast.show(
        'Enter withdrawal amount.',
        'warning'
      );

      return;

    }

    if (this.request.amount < this.minimumAmount) {

      this.toast.show(
        `Minimum withdrawal is ৳${this.minimumAmount}.`,
        'warning'
      );

      return;

    }

    if (!this.wallet) {

      return;

    }

    if (this.request.amount > this.wallet.balance) {

      this.toast.show(
        'Insufficient wallet balance.',
        'warning'
      );

      return;

    }

    if (!this.request.accountNumber.trim()) {

      this.toast.show(
        'Enter account number.',
        'warning'
      );

      return;

    }

    if (!this.request.accountName.trim()) {

      this.toast.show(
        'Enter account holder name.',
        'warning'
      );

      return;

    }

    this.submitting = true;

    this.withdrawService
      .createWithdraw(this.request)
      .subscribe({

        next: () => {

          this.toast.show(
            'Withdrawal request submitted successfully.'
          );

          this.request.amount = 0;

          this.request.accountNumber = '';

          this.request.accountName = '';

          this.request.withdrawMethod =
            WithdrawMethod.BKASH;

          this.submitting = false;

          this.loadData();

        },

        error: err => {

          this.submitting = false;

          this.toast.show(
            err?.error?.message ??
            'Unable to submit withdrawal request.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Badge Class
  // =====================================

  getBadgeClass(
    status: WithdrawStatus
  ): string {

    switch (status) {

      case WithdrawStatus.APPROVED:
        return 'bg-success';

      case WithdrawStatus.REJECTED:
        return 'bg-danger';

      default:
        return 'bg-warning text-dark';

    }

  }



}
