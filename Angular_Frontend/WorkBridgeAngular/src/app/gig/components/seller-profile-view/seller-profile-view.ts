import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { UserProfileResponseModel } from '../../../user/resume/models/user.profile.model';
import { GigResponseModel } from '../../models/gig.model';
import { ReviewResponseModel } from '../../models/review.model';
import { ActivatedRoute, Router } from '@angular/router';
import { UserProfileService } from '../../../user/resume/services/user.profile.service';
import { GigService } from '../../services/gig.service';
import { ReviewService } from '../../services/review.service';
import { ToastService } from '../../../services/toast.service';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';

@Component({
  selector: 'app-seller-profile-view',
  imports: [CommonModule],
  templateUrl: './seller-profile-view.html',
  styleUrl: './seller-profile-view.css',
})
export class SellerProfileView implements OnInit {





  // =====================================
  // Properties
  // =====================================

  userProfileId = 0;

  loading = false;

  seller?: UserProfileResponseModel;

  gigs: GigResponseModel[] = [];

  latestReviews: ReviewResponseModel[] = [];

  reviewCount = 0;

  averageRating = 0;

  completedOrders = 0;

  activeGigCount = 0;

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private route: ActivatedRoute,

    private router: Router,

    private userProfileService: UserProfileService,

    private gigService: GigService,

    private reviewService: ReviewService,

    private toast: ToastService,

    public fileService: FileResourceHandleService,

    private cdr: ChangeDetectorRef

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.route.paramMap.subscribe(params => {

      this.userProfileId = Number(
        params.get('userProfileId')
      );

      this.loadSeller();

    });

  }

  // =====================================
  // Load Seller
  // =====================================

  loadSeller(): void {

    this.loading = true;

    this.userProfileService
      .getById(this.userProfileId)
      .subscribe({

        next: res => {

          this.seller = res;

          this.loadSellerGigs();

          this.loadReviewCount();

          this.loadLatestReviews();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load seller profile.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Seller Gigs
  // =====================================

  loadSellerGigs(): void {

    this.gigService
      .getActiveByUserProfileId(this.userProfileId)
      .subscribe({

        next: res => {

          this.gigs = res;

          this.activeGigCount = res.length;

          this.completedOrders = 0;

          let ratingSum = 0;
          let ratedGigCount = 0;

          res.forEach(gig => {

            this.completedOrders +=
              gig.completedOrders;

            if (gig.totalReviews > 0) {

              ratingSum +=
                gig.averageRating * gig.totalReviews;

              ratedGigCount +=
                gig.totalReviews;

            }

          });

          this.averageRating =
            ratedGigCount > 0
              ? +(ratingSum / ratedGigCount).toFixed(1)
              : 0;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load gigs.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Review Count
  // =====================================

  loadReviewCount(): void {

    this.reviewService
      .countSellerReviews(this.userProfileId)
      .subscribe({

        next: count => {

          this.reviewCount = count;

          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // Latest Reviews
  // =====================================

  loadLatestReviews(): void {

    this.reviewService
      .getSellerReviews(this.userProfileId)
      .subscribe({

        next: res => {

          this.latestReviews = res
            .sort((a, b) =>
              new Date(b.createdAt).getTime() -
              new Date(a.createdAt).getTime())
            .slice(0, 3);

          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // Open Gig
  // =====================================

  openGig(
    gigId: number
  ): void {

    this.router.navigate([
      '/gig-details',
      gigId
    ]);

  }

  // =====================================
  // View Reviews
  // =====================================

  viewReviews(
    gigId: number
  ): void {

    this.router.navigate([
      '/gig-reviews',
      gigId
    ]);

  }




}
