import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { WalletResponseModel } from '../../models/wallet.model';
import { WalletService } from '../../services/wallet.service';
import { StorageService } from '../../../auth/services/storage.service';
import { ToastService } from '../../../services/toast.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-wallet-home.component',
  imports: [CommonModule],
  templateUrl: './wallet-home.component.html',
  styleUrl: './wallet-home.component.css',
})
export class WalletHomeComponent implements OnInit {





  // =====================================
  // Properties
  // =====================================

  loading = false;

  wallet?: WalletResponseModel;

  userId = 0;

  role = '';

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private walletService: WalletService,

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
      this.role = this.storageService.getRole() ?? '';

    if (!id) {

      this.toast.show(
        'User not found.',
        'danger'
      );

      this.router.navigate(['/login']);

      return;

    }

    this.userId = id;

    this.loadWallet();

  }

  // =====================================
  // Load Wallet
  // =====================================

  loadWallet(): void {

    this.loading = true;

    this.walletService
      .getByUserId(this.userId)
      .subscribe({

        next: res => {

          this.wallet = res;

          this.loading = false;

          this.cdr.markForCheck();

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
  // Deposit
  // =====================================
  deposit(): void {

    this.router.navigate([
      this.getWalletBaseRoute(),
      'wallet',
      'deposit'
    ]);

  }

  //Withdraw navigate

  withdraw(): void {

    this.router.navigate([
      this.getWalletBaseRoute(),
      'wallet',
      'withdraw'
    ]);

  }

  //Transaction history navigate

  transactionHistory(): void {

    this.router.navigate([
      this.getWalletBaseRoute(),
      'wallet',
      'transactions'
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



  // =====================================
// Admin All Transactions
// =====================================

allTransactions(): void {

  this.router.navigate([
    '/admin',
    'wallet',
    'all-transactions'
  ]);

}

}
