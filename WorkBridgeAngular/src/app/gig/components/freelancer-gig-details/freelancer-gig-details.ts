import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GigOrderResponseDTO } from '../../models/gig-order.model';
import { ActivatedRoute } from '@angular/router';
import { GigOrderService } from '../../services/gig-order.service';
import { ToastService } from '../../../services/toast.service';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';

@Component({
  selector: 'app-freelancer-gig-details',
  imports: [CommonModule, FormsModule],
  templateUrl: './freelancer-gig-details.html',
  styleUrl: './freelancer-gig-details.css',
})
export class FreelancerGigDetails implements OnInit {






  gigOrderId = 0;

  order!: GigOrderResponseDTO;

  loading = false;
  saving = false;

  quotedPrice = 0;

  deliveryMessage = '';

  deliveryFile?: File;

  constructor(
    private route: ActivatedRoute,
    private gigOrderService: GigOrderService,
    private toast: ToastService,
    public fileService: FileResourceHandleService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {

    this.route.paramMap.subscribe(params => {

      this.gigOrderId = Number(
        params.get('gigOrderId')
      );

      this.loadOrder();

    });

  }

  // =====================================
  // Load Order
  // =====================================

  loadOrder(): void {

    console.log("Loading order:", this.gigOrderId);

    this.loading = true;

    this.gigOrderService.getById(this.gigOrderId).subscribe({

      next: res => {

        console.log("Order loaded", res);

        this.order = res;

        this.loading = false;
        this.cdr.markForCheck();

      },

      error: err => {

        console.log(err);

        this.loading = false;

      }

    });

  }
  // =====================================
  // Quote
  // =====================================

  sendQuote(): void {

    if (this.quotedPrice <= 0) {

      this.toast.show(
        'Enter a valid quote.',
        'warning'
      );

      return;

    }

    this.saving = true;

    this.gigOrderService
      .sendQuote(
        this.order.id,
        this.quotedPrice
      )
      .subscribe({

        next: res => {

          this.order = res;

          this.saving = false;

          this.toast.show(
            'Quote sent successfully.',
            'success'
          );
          this.loadOrder();

        },

        error: () => {

          this.saving = false;

          this.toast.show(
            'Unable to send quote.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // File
  // =====================================

  onFileSelected(event: Event): void {

    const input =
      event.target as HTMLInputElement;

    if (
      input.files &&
      input.files.length > 0
    ) {

      this.deliveryFile =
        input.files[0];

    }

  }

  // =====================================
  // Deliver
  // =====================================

  deliverOrder(): void {

    if (!this.deliveryFile) {

      this.toast.show(
        'Please select delivery file.',
        'warning'
      );

      return;

    }

    this.saving = true;

    this.gigOrderService
      .deliverOrder(
        this.order.id,
        this.deliveryMessage,
        this.deliveryFile
      )
      .subscribe({

        next: res => {

          this.order = res;

          this.deliveryFile = undefined;

          this.deliveryMessage = '';

          this.saving = false;

          this.toast.show(
            'Order delivered.',
            'success'
          );
          this.loadOrder();

        },

        error: () => {

          this.saving = false;

          this.toast.show(
            'Unable to deliver order.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Seller Cancel
  // =====================================

  cancelOrder(): void {

    if (!confirm(
      'Cancel this order? Buyer will be refunded immediately.'
    )) {
      return;
    }

    this.saving = true;

    this.gigOrderService
      .sellerCancel(
        this.order.id
      )
      .subscribe({

        next: res => {

          this.order = res;

          this.saving = false;

          this.toast.show(
            'Order cancelled.',
            'success'
          );
          this.loadOrder();

        },

        error: () => {

          this.saving = false;

          this.toast.show(
            'Unable to cancel order.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Raise Dispute
  // =====================================

  raiseDispute(): void {

    if (!confirm(
      'Raise dispute?'
    )) {
      return;
    }

    this.saving = true;

    this.gigOrderService
      .raiseDispute(
        this.order.id
      )
      .subscribe({

        next: res => {

          this.order = res;

          this.saving = false;

          this.toast.show(
            'Dispute raised.',
            'success'
          );
          this.loadOrder();

        },

        error: () => {

          this.saving = false;

          this.toast.show(
            'Unable to raise dispute.',
            'danger'
          );

        }

      });

  }



}
