import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { TransactionType } from '../../../enums/transaction-type.enum';
import { TransactionFilterDTO, TransactionResponseDTO } from '../../models/transaction.model';
import { TransactionService } from '../../services/transaction.service';
import { ToastService } from '../../../services/toast.service';
import { UserRole } from '../../../enums/user-role.enum';

@Component({
  selector: 'app-admin-transaction-history.component',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-transaction-history.component.html',
  styleUrl: './admin-transaction-history.component.css',
})
export class AdminTransactionHistoryComponent implements OnInit {




  // =====================================
  // Properties
  // =====================================

  loading = false;

  transactions: TransactionResponseDTO[] = [];

  readonly transactionTypes = Object.values(TransactionType);

  readonly userRoles = Object.values(UserRole);

  filter: TransactionFilterDTO = {

    transactionType: undefined,

    userRole: undefined,

    keyword: '',

    fromDate: '',

    toDate: '',

    userId: undefined

  };

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private transactionService: TransactionService,

    private toast: ToastService,

    private cdr: ChangeDetectorRef

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.search();

  }

  // =====================================
  // Search
  // =====================================

  search(): void {

    this.loading = true;

    const request: TransactionFilterDTO = {

      transactionType:
        this.filter.transactionType || undefined,

      userRole:
        this.filter.userRole || undefined,

      keyword:
        this.filter.keyword?.trim() || undefined,

      fromDate:
        this.filter.fromDate || undefined,

      toDate:
        this.filter.toDate || undefined,

      userId:
        this.filter.userId && this.filter.userId > 0
          ? this.filter.userId
          : undefined

    };

    this.transactionService
      .search(request)
      .subscribe({

        next: res => {

          this.transactions = res;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load transactions.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Reset Filter
  // =====================================

  reset(): void {

    this.filter = {

      transactionType: undefined,

      userRole: undefined,

      keyword: '',

      fromDate: '',

      toDate: '',

      userId: undefined

    };

    this.search();

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

      default:

        return 'text-danger fw-bold';

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



}
