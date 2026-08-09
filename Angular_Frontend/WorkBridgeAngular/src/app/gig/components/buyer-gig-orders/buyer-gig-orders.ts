import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { GigOrderResponseDTO } from '../../models/gig-order.model';
import { GigOrderService } from '../../services/gig-order.service';
import { StorageService } from '../../../auth/services/storage.service';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';
import { Router } from '@angular/router';
import { ToastService } from '../../../services/toast.service';
import { CommonModule } from '@angular/common';
import { GigOrderStatus } from '../../../enums/gig-order-status.enum';

@Component({
  selector: 'app-buyer-gig-orders',
  imports: [CommonModule],
  templateUrl: './buyer-gig-orders.html',
  styleUrl: './buyer-gig-orders.css',
})
export class BuyerGigOrders implements OnInit {





  // =====================================
  // Properties
  // =====================================

  buyerId = 0;

  role: string | null = null;

  loading = false;

  orders: GigOrderResponseDTO[] = [];

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private gigOrderService: GigOrderService,

    private storage: StorageService,

    public fileService: FileResourceHandleService,

    private router: Router,

    private toast: ToastService,

    private cdr: ChangeDetectorRef

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.role =
      this.storage.getRole();

    this.buyerId =
      this.storage.getUserId() ?? 0;

    this.loadOrders();


  }

  // =====================================
  // Load Orders
  // =====================================

  loadOrders(): void {

    this.loading = true;

    this.gigOrderService
      .getBuyerOrders(this.buyerId)
      .subscribe({

        next: (data) => {

          this.orders = data;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load your orders.',
            'danger'
          );

          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // View Order
  // =====================================

  viewOrder(
    order: GigOrderResponseDTO
  ): void {

    if (this.role === 'COMPANY') {

      this.router.navigate([
        '/company/buyer-gig-details',
        order.id
      ]);

      return;

    }

    this.router.navigate([
      '/user/buyer-gig-details',
      order.id
    ]);

  }

  // =====================================
  // Browse Gigs
  // =====================================

  browseGigs(): void {

    this.router.navigate([
      '/'
    ]);

  }



  getGigImage(fileName?: string): string {

    return this.fileService.getGigImage(fileName);

  }


  getStatusClass(status: GigOrderStatus): string {

    switch (status) {

      case GigOrderStatus.ORDER_PLACED:
        return 'bg-warning text-dark';

      case GigOrderStatus.QUOTED:
        return 'bg-info text-dark';

      case GigOrderStatus.QUOTE_ACCEPTED:
        return 'bg-primary';

      case GigOrderStatus.DELIVERED:
        return 'bg-success';

      case GigOrderStatus.BUYER_ACCEPTED:
        return 'bg-success';

      case GigOrderStatus.PAYMENT_RELEASED:
        return 'bg-success';

      case GigOrderStatus.BUYER_REJECTED:
        return 'bg-danger';

      case GigOrderStatus.QUOTE_REJECTED:
        return 'bg-danger';

      case GigOrderStatus.BUYER_CANCELLED:
        return 'bg-danger';

      case GigOrderStatus.SELLER_CANCELLED:
        return 'bg-danger';

      case GigOrderStatus.REFUNDED:
        return 'bg-danger';

      case GigOrderStatus.SELLER_DISPUTED:
        return 'bg-dark';

      default:
        return 'bg-secondary';

    }





  }



  getStatusText(status: GigOrderStatus): string {

    switch (status) {

      case GigOrderStatus.ORDER_PLACED:
        return 'Order Placed';

      case GigOrderStatus.QUOTED:
        return 'Quote Sent';

      case GigOrderStatus.QUOTE_ACCEPTED:
        return 'Quote Accepted';

      case GigOrderStatus.QUOTE_REJECTED:
        return 'Quote Rejected';

      case GigOrderStatus.DELIVERED:
        return 'Delivered';

      case GigOrderStatus.BUYER_ACCEPTED:
        return 'Completed';

      case GigOrderStatus.BUYER_REJECTED:
        return 'Delivery Rejected';

      case GigOrderStatus.BUYER_CANCELLED:
        return 'Cancelled';

      case GigOrderStatus.SELLER_CANCELLED:
        return 'Cancelled';

      case GigOrderStatus.SELLER_DISPUTED:
        return 'In Dispute';

      case GigOrderStatus.PAYMENT_RELEASED:
        return 'Payment Released';

      case GigOrderStatus.REFUNDED:
        return 'Refunded';

      default:
        return status;

    }

  }


}
