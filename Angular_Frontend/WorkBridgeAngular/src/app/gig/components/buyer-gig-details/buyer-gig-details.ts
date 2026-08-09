import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GigOrderResponseDTO } from '../../models/gig-order.model';
import { ActivatedRoute, Router } from '@angular/router';
import { GigOrderService } from '../../services/gig-order.service';
import { ToastService } from '../../../services/toast.service';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';
import { ReviewService } from '../../services/review.service';
import { StorageService } from '../../../auth/services/storage.service';



@Component({
  selector: 'app-buyer-gig-details',
  imports: [CommonModule, FormsModule],
  templateUrl: './buyer-gig-details.html',
  styleUrl: './buyer-gig-details.css',
})
export class BuyerGigDetails implements OnInit {






  gigOrderId = 0;

  order!: GigOrderResponseDTO;

  loading = false;

  saving = false;

  reviewExists = false;
  role = '';


  constructor(
    private route: ActivatedRoute,
    private gigOrderService: GigOrderService,
    private toast: ToastService,
    public fileService: FileResourceHandleService,
    private cdr: ChangeDetectorRef,
    private reviewService: ReviewService,
    private router: Router,
    private storage: StorageService
  ) { }

  ngOnInit(): void {


    this.route.paramMap.subscribe(params => {

      this.gigOrderId = Number(
        params.get('gigOrderId')
      );

      this.loadOrder();
      this.role = this.storage.getRole() ?? '';

    });

  }

  // =====================================
  // Load
  // =====================================

  loadOrder(): void {

    this.loading = true;

    this.gigOrderService
      .getById(this.gigOrderId)
      .subscribe({

        next: res => {

          this.order = res;

          if (this.order.status === 'BUYER_ACCEPTED') {

            this.reviewService
              .existsByGigOrderId(this.order.id)
              .subscribe({

                next: exists => {

                  this.reviewExists = exists;

                  this.cdr.markForCheck();

                }

              });

          }

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load order.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Accept Quote
  // =====================================

  acceptQuote(): void {

    this.saving = true;

    this.gigOrderService
      .acceptQuote(this.order.id)
      .subscribe({

        next: () => {

          this.toast.show(
            'Quote accepted.',
            'success'
          );

          this.saving = false;

          this.loadOrder();

        },

        error: () => {

          this.toast.show(
            'Unable to accept quote.',
            'danger'
          );

          this.saving = false;

        }

      });

  }

  // =====================================
  // Reject Quote
  // =====================================

  rejectQuote(): void {

    if (!confirm('Reject this quote?')) {
      return;
    }

    this.saving = true;

    this.gigOrderService
      .rejectQuote(this.order.id)
      .subscribe({

        next: () => {

          this.toast.show(
            'Quote rejected.',
            'success'
          );

          this.saving = false;

          this.loadOrder();

        },

        error: () => {

          this.toast.show(
            'Unable to reject quote.',
            'danger'
          );

          this.saving = false;

        }

      });

  }

  // =====================================
  // Accept Delivery
  // =====================================

  acceptDelivery(): void {

    this.saving = true;

    this.gigOrderService
      .acceptDelivery(this.order.id)
      .subscribe({

        next: () => {

          this.toast.show(
            'Delivery accepted.',
            'success'
          );

          this.saving = false;

          this.loadOrder();

        },

        error: () => {

          this.toast.show(
            'Unable to accept delivery.',
            'danger'
          );

          this.saving = false;

        }

      });

  }

  // =====================================
  // Reject Delivery
  // =====================================

  rejectDelivery(): void {

    if (!confirm('Reject delivery?')) {
      return;
    }

    this.saving = true;

    this.gigOrderService
      .rejectDelivery(this.order.id)
      .subscribe({

        next: () => {

          this.toast.show(
            'Delivery rejected.',
            'success'
          );

          this.saving = false;

          this.loadOrder();

        },

        error: () => {

          this.toast.show(
            'Unable to reject delivery.',
            'danger'
          );

          this.saving = false;

        }

      });

  }

  // =====================================
  // Cancel Order
  // =====================================

  cancelOrder(): void {

    if (!confirm(
      'Cancel this order? Seller will have 7 days to dispute if applicable.'
    )) {
      return;
    }

    this.saving = true;

    this.gigOrderService
      .buyerCancel(this.order.id)
      .subscribe({

        next: () => {

          this.toast.show(
            'Order cancelled.',
            'success'
          );

          this.saving = false;

          this.loadOrder();

        },

        error: () => {

          this.toast.show(
            'Unable to cancel order.',
            'danger'
          );

          this.saving = false;

        }

      });

  }

  goToReview(): void {
    if (this.role == 'USER') {

      this.router.navigate([
        '/user/buyer-review',
        this.order.id
      ]);
    } else {

      this.router.navigate([
        '/company/buyer-review',
        this.order.id
      ]);
    }




  }





}
