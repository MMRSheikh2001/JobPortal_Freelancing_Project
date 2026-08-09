import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GigOrderStatus } from '../../enums/gig-order-status.enum';
import { GigOrderFilterRequestDTO, GigOrderResponseDTO } from '../../gig/models/gig-order.model';
import { GigOrderService } from '../../gig/services/gig-order.service';
import { ToastService } from '../../services/toast.service';
import { Router } from '@angular/router';
import { UserRole } from '../../enums/user-role.enum';

@Component({
  selector: 'app-admin-gig-order-management',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-gig-order-management.html',
  styleUrl: './admin-gig-order-management.css',
})
export class AdminGigOrderManagement implements OnInit {





  //-----------------------------------
  // Data
  //-----------------------------------

  loading = false;

  orders: GigOrderResponseDTO[] = [];

  //-----------------------------------
  // Filter
  //-----------------------------------

  filter: GigOrderFilterRequestDTO = {

    keyword: '',

    buyerId: 0,

    sellerId: 0,

    gigId: 0,

    categoryId: 0,

    status: undefined,

    paymentLocked: undefined,

    createdFrom: '',

    createdTo: ''

  };

  statuses = Object.values(
    GigOrderStatus
  );

  //-----------------------------------
  // Constructor
  //-----------------------------------

  constructor(

    private service: GigOrderService,

    private toast: ToastService,

    private router: Router,

    private cdr: ChangeDetectorRef

  ) { }

  //-----------------------------------
  // Init
  //-----------------------------------

  ngOnInit(): void {

    this.search();

  }

  //-----------------------------------
  // Search
  //-----------------------------------

  search(): void {

    this.loading = true;

    this.service.search(this.filter)
      .subscribe({

        next: res => {

          this.orders = res;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load gig orders.',
            'danger'
          );

        }

      });

  }

  //-----------------------------------
  // Clear Filter
  //-----------------------------------

  clearFilter(): void {

    this.filter = {

      keyword: '',

      buyerId: 0,

      sellerId: 0,

      gigId: 0,

      categoryId: 0,

      status: undefined,

      paymentLocked: undefined,

      createdFrom: '',

      createdTo: ''

    };

    this.search();

  }

  //-----------------------------------
  // Buyer
  //-----------------------------------

  viewBuyer(
    order: GigOrderResponseDTO
  ): void {

    if (order.buyerRole === UserRole.USER) {

      this.router.navigate([
        '/admin/user-profile-review',
        order.buyerUserProfileId
      ]);

    }
    else {

      this.router.navigate([
        '/company-profile',
        order.buyerCompanyProfileId
      ]);

    }

  }

  //-----------------------------------
  // Seller
  //-----------------------------------

  viewSeller(
    sellerId: number
  ): void {

    this.router.navigate([
      '/admin/user-profile-review',
      sellerId
    ]);

  }

  //-----------------------------------
  // Gig
  //-----------------------------------

  viewGig(
    gigId: number
  ): void {

    this.router.navigate([
      '/gig-details',
      gigId
    ]);

  }

  //-----------------------------------
  // Order Details
  //-----------------------------------

  viewOrderDetails(
    gigOrderId: number
  ): void {

    this.router.navigate([
      '/admin/gig-order-details',
      gigOrderId
    ]);

  }

  //-----------------------------------
  // Release Payment
  //-----------------------------------

  releasePayment(
    order: GigOrderResponseDTO
  ): void {

    this.service.releasePayment(order.id)
      .subscribe({

        next: res => {

          order.status = res.status;

          order.paymentReleasedAt =
            res.paymentReleasedAt;

          this.toast.show(
            'Payment released successfully.'
          );

          this.cdr.markForCheck();

        },

        error: () => {

          this.toast.show(
            'Unable to release payment.',
            'danger'
          );

        }

      });

  }

  //-----------------------------------
  // Refund Buyer
  //-----------------------------------

  refundBuyer(
    order: GigOrderResponseDTO
  ): void {

    this.service.refundBuyer(order.id)
      .subscribe({

        next: res => {

          order.status = res.status;

          order.refundedAt =
            res.refundedAt;

          this.toast.show(
            'Buyer refunded successfully.'
          );

          this.cdr.markForCheck();

        },

        error: () => {

          this.toast.show(
            'Unable to refund buyer.',
            'danger'
          );

        }

      });

  }

  //-----------------------------------
  // Status Badge
  //-----------------------------------

  getStatusClass(
    status: GigOrderStatus
  ): string {

    switch (status) {

      case GigOrderStatus.ORDER_PLACED:
        return 'bg-secondary';

      case GigOrderStatus.QUOTED:
        return 'bg-warning text-dark';

      case GigOrderStatus.QUOTE_ACCEPTED:
        return 'bg-primary';

      case GigOrderStatus.QUOTE_REJECTED:
        return 'bg-dark';

      case GigOrderStatus.DELIVERED:
        return 'bg-info';

      case GigOrderStatus.BUYER_ACCEPTED:
        return 'bg-success';

      case GigOrderStatus.BUYER_REJECTED:
        return 'bg-danger';

      case GigOrderStatus.BUYER_CANCELLED:
        return 'bg-danger';

      case GigOrderStatus.SELLER_CANCELLED:
        return 'bg-secondary';

      case GigOrderStatus.SELLER_DISPUTED:
        return 'bg-warning text-dark';

      case GigOrderStatus.PAYMENT_RELEASED:
        return 'bg-success';

      case GigOrderStatus.REFUNDED:
        return 'bg-danger';

      default:
        return 'bg-secondary';

    }

  }




}
