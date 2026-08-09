import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { TransactionType } from '../../../enums/transaction-type.enum';
import { TransactionResponseDTO } from '../../models/transaction.model';
import { TransactionService } from '../../services/transaction.service';
import { StorageService } from '../../../auth/services/storage.service';
import { ToastService } from '../../../services/toast.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-transaction-history.component',
  imports: [CommonModule],
  templateUrl: './transaction-history.component.html',
  styleUrl: './transaction-history.component.css',
})
export class TransactionHistoryComponent implements OnInit {





  // =====================================
  // Properties
  // =====================================

  loading = false;

  userId = 0;

  transactions: TransactionResponseDTO[] = [];

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private transactionService: TransactionService,

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

    this.loadTransactions();

  }

  // =====================================
  // Load Transactions
  // =====================================

  loadTransactions(): void {

    this.loading = true;

    this.transactionService
      .getUserHistory(this.userId)
      .subscribe({

        next: res => {

          this.transactions = res.sort(
            (a, b) =>
              new Date(b.createdAt).getTime() -
              new Date(a.createdAt).getTime()
          );

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load transaction history.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Badge Class
  // =====================================

  getBadgeClass(
    type: TransactionType
  ): string {

    switch (type) {

      case TransactionType.DEPOSIT:
        return 'bg-success';

      case TransactionType.WITHDRAW:
        return 'bg-danger';

      case TransactionType.FREEZE:
        return 'bg-warning text-dark';

      case TransactionType.REFUND:
        return 'bg-info text-dark';

      case TransactionType.SELLER_PAYOUT:
        return 'bg-primary';

      case TransactionType.PLATFORM_COMMISSION:
        return 'bg-secondary';

      case TransactionType.JOB_POST_PAYMENT:
        return 'bg-dark';

      default:
        return 'bg-light text-dark';

    }

  }

  // =====================================
  // Amount Color
  // =====================================

  getAmountClass(
    type: TransactionType
  ): string {

    switch (type) {

      case TransactionType.DEPOSIT:
      case TransactionType.REFUND:
      case TransactionType.SELLER_PAYOUT:
        return 'text-success fw-bold';

      case TransactionType.WITHDRAW:
      case TransactionType.FREEZE:
      case TransactionType.JOB_POST_PAYMENT:
      case TransactionType.PLATFORM_COMMISSION:
        return 'text-danger fw-bold';

      default:
        return '';

    }

  }

  // =====================================
  // Amount Prefix
  // =====================================

  getAmountPrefix(
    type: TransactionType
  ): string {

    switch (type) {

      case TransactionType.DEPOSIT:
      case TransactionType.REFUND:
      case TransactionType.SELLER_PAYOUT:
        return '+';

      default:
        return '-';

    }

  }

  // =====================================
  // Back
  // =====================================

  back(): void {

    this.router.navigate([
      this.getWalletBaseRoute(),
      'wallet'
    ]);

  }

  private getWalletBaseRoute(): string {

    const role = this.storageService.getRole();

    if (role === 'COMPANY') {
      return '/company';
    }

    if (role === 'ADMIN') {
      return '/admin';
    }

    return '/user';

  }




}
