import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { GigOrderResponseDTO } from '../../models/gig-order.model';
import { ReviewRequestDTO, ReviewResponseModel } from '../../models/review.model';
import { ActivatedRoute, Router } from '@angular/router';
import { GigOrderService } from '../../services/gig-order.service';
import { ReviewService } from '../../services/review.service';
import { ToastService } from '../../../services/toast.service';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';
import { StorageService } from '../../../auth/services/storage.service';
import { GigOrderStatus } from '../../../enums/gig-order-status.enum';

@Component({
  selector: 'app-buyer-review',
  imports: [CommonModule, FormsModule],
  templateUrl: './buyer-review.html',
  styleUrl: './buyer-review.css',
})
export class BuyerReview implements OnInit {






  // =====================================
  // Properties
  // =====================================

  gigOrderId = 0;

  stars = [1, 2, 3, 4, 5];

  loading = false;

  saving = false;

  order!: GigOrderResponseDTO;

  review?: ReviewResponseModel;

  reviewExists = false;

  canReview = false;

  request: ReviewRequestDTO = {

    rating: 0,

    comment: '',

    gigOrderId: 0

  };

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private route: ActivatedRoute,

    private router: Router,

    private gigOrderService: GigOrderService,

    private reviewService: ReviewService,

    private toast: ToastService,

    public fileService: FileResourceHandleService,

    private storage: StorageService,

    private cdr: ChangeDetectorRef

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.route.paramMap.subscribe(params => {

      this.gigOrderId =
        Number(params.get('gigOrderId'));

      this.loadOrder();

    });

  }

  // =====================================
  // Load Order
  // =====================================

  loadOrder(): void {

    this.loading = true;

    this.gigOrderService
      .getById(this.gigOrderId)
      .subscribe({

        next: res => {

          this.order = res;

          this.request.gigOrderId =
            this.order.id;

          this.canReview =
            this.order.status ===
            GigOrderStatus.BUYER_ACCEPTED;

          this.checkReview();

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
  // Check Review
  // =====================================

  checkReview(): void {

    this.reviewService
      .existsByGigOrderId(this.order.id)
      .subscribe({

        next: exists => {

          this.reviewExists = exists;

          if (exists) {

            this.loadReview();

            return;

          }

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // Load Review
  // =====================================

  loadReview(): void {

    this.reviewService
      .getByGigOrderId(this.order.id)
      .subscribe({

        next: res => {

          this.review = res;

          this.request.rating =
            res.rating;

          this.request.comment =
            res.comment;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load review.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Set Rating
  // =====================================

  setRating(
    rating: number
  ): void {

    this.request.rating = rating;

    this.cdr.markForCheck();

  }

  // =====================================
  // Save
  // =====================================

  save(): void {

    if (!this.canReview) {

      this.toast.show(
        'This order cannot be reviewed.',
        'warning'
      );

      return;

    }

    if (this.request.rating < 1) {

      this.toast.show(
        'Please select a rating.',
        'warning'
      );

      return;

    }

    this.saving = true;

    if (this.reviewExists && this.review) {

      this.reviewService
        .update(
          this.review.id,
          this.request
        )
        .subscribe({

          next: () => {

            this.toast.show(
              'Review updated.',
              'success'
            );

            this.saving = false;

            this.goBack();

          },

          error: () => {

            this.saving = false;

            this.toast.show(
              'Unable to update review.',
              'danger'
            );

          }

        });

      return;

    }

    this.reviewService
      .create(this.request)
      .subscribe({

        next: () => {

          this.toast.show(
            'Review submitted.',
            'success'
          );

          this.saving = false;

          this.goBack();

        },

        error: () => {

          this.saving = false;

          this.toast.show(
            'Unable to submit review.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Back
  // =====================================

  goBack(): void {

    const role =
      this.storage.getRole();

    if (role === 'COMPANY') {

      this.router.navigate([
        '/company/buyer-gig-details',
        this.order.id
      ]);

      return;

    }

    this.router.navigate([
      '/user/buyer-gig-details',
      this.order.id
    ]);

  }




}
