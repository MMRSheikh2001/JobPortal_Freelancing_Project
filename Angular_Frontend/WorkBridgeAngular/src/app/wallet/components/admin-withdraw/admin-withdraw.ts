import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { WithdrawStatus } from '../../../enums/withdraw-status.enum';
import { WithdrawResponseModel } from '../../models/withdraw.model';
import { WithdrawService } from '../../services/withdraw.service';
import { ToastService } from '../../../services/toast.service';

@Component({
  selector: 'app-admin-withdraw',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-withdraw.html',
  styleUrl: './admin-withdraw.css',
})
export class AdminWithdraw implements OnInit {






  // =====================================
  // Properties
  // =====================================

  loading = false;

  processing = false;

  withdraws: WithdrawResponseModel[] = [];

  selectedWithdraw?: WithdrawResponseModel;

  adminRemarks = '';

  transactionReference = '';

  approvedWithdraws: WithdrawResponseModel[] = [];

  rejectedWithdraws: WithdrawResponseModel[] = [];
  // =====================================
  // Constructor
  // =====================================

  constructor(

    private withdrawService: WithdrawService,

    private toast: ToastService,

    private cdr: ChangeDetectorRef

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.loadAllWithdraws();

  }


  // =====================================
  // Load Everything
  // =====================================

  loadAllWithdraws(): void {

    this.loadPendingWithdraws();

    this.loadApprovedWithdraws();

    this.loadRejectedWithdraws();

  }



  // =====================================
  // Load Pending Requests
  // =====================================

  loadPendingWithdraws(): void {

    this.loading = true;

    this.withdrawService
      .getPendingWithdraws()
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
  // Load Approved Requests
  // =====================================

  loadApprovedWithdraws(): void {

    this.withdrawService
      .getApprovedWithdraws()
      .subscribe({

        next: res => {

          this.approvedWithdraws = res;

          this.cdr.markForCheck();

        },

        error: () => {

          this.toast.show(
            'Unable to load approved withdrawals.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Load Rejected Requests
  // =====================================

  loadRejectedWithdraws(): void {

    this.withdrawService
      .getRejectedWithdraws()
      .subscribe({

        next: res => {

          this.rejectedWithdraws = res;

          this.cdr.markForCheck();

        },

        error: () => {

          this.toast.show(
            'Unable to load rejected withdrawals.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Select Request
  // =====================================

  selectWithdraw(
    withdraw: WithdrawResponseModel
  ): void {

    this.selectedWithdraw = withdraw;

    this.adminRemarks =
      withdraw.adminRemarks ?? '';

    this.transactionReference =
      withdraw.transactionReference ?? '';

  }

  // =====================================
  // Approve
  // =====================================

  approve(): void {

    if (!this.selectedWithdraw) {

      return;

    }

    if (!this.transactionReference.trim()) {

      this.toast.show(
        'Transaction reference is required.',
        'warning'
      );

      return;

    }

    this.processing = true;

    this.withdrawService
      .approveWithdraw(
        this.selectedWithdraw.id,
        this.adminRemarks,
        this.transactionReference
      )
      .subscribe({

        next: () => {

          this.toast.show(
            'Withdrawal approved successfully.'
          );

          this.processing = false;

          this.clearSelection();

          this.loadAllWithdraws();

        },

        error: err => {

          this.processing = false;

          this.toast.show(
            err?.error?.message ??
            'Unable to approve withdrawal.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Reject
  // =====================================

  reject(): void {

    if (!this.selectedWithdraw) {

      return;

    }

    if (!this.adminRemarks.trim()) {

      this.toast.show(
        'Remarks are required.',
        'warning'
      );

      return;

    }

    this.processing = true;

    this.withdrawService
      .rejectWithdraw(
        this.selectedWithdraw.id,
        this.adminRemarks
      )
      .subscribe({

        next: () => {

          this.toast.show(
            'Withdrawal rejected.'
          );

          this.processing = false;

          this.clearSelection();

          this.loadAllWithdraws();

        },

        error: err => {

          this.processing = false;

          this.toast.show(
            err?.error?.message ??
            'Unable to reject withdrawal.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Clear Selection
  // =====================================

  clearSelection(): void {

    this.selectedWithdraw = undefined;

    this.adminRemarks = '';

    this.transactionReference = '';

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
