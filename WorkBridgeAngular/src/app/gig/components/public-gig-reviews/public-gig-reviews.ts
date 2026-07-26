import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { GigResponseModel } from '../../models/gig.model';
import { ReviewResponseModel } from '../../models/review.model';
import { ActivatedRoute, Router } from '@angular/router';
import { GigService } from '../../services/gig.service';
import { ReviewService } from '../../services/review.service';
import { ToastService } from '../../../services/toast.service';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';

@Component({
  selector: 'app-public-gig-reviews',
  imports: [CommonModule],
  templateUrl: './public-gig-reviews.html',
  styleUrl: './public-gig-reviews.css',
})
export class PublicGigReviews implements OnInit {





  // =====================================
  // Properties
  // =====================================

  gigId = 0;

  loading = false;

  gig?: GigResponseModel;

  reviews: ReviewResponseModel[] = [];

  averageRating = 0;

  totalReviews = 0;

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private route: ActivatedRoute,

    private router: Router,

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

      this.gigId = Number(
        params.get('id')
      );

      this.loadGig();

    });

  }

  // =====================================
  // Load Gig
  // =====================================

  loadGig(): void {

    this.loading = true;

    this.gigService
      .getById(this.gigId)
      .subscribe({

        next: res => {

          this.gig = res;

          this.averageRating =
            res.averageRating;

          this.totalReviews =
            res.totalReviews;

          this.loadReviews();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load gig.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Load Reviews
  // =====================================

  loadReviews(): void {

    this.reviewService
      .getGigReviews(this.gigId)
      .subscribe({

        next: res => {

          this.reviews = res;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load reviews.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Stars
  // =====================================

  getStars(
    rating: number
  ): number[] {

    return Array(rating).fill(0);

  }

  getEmptyStars(
    rating: number
  ): number[] {

    return Array(5 - rating).fill(0);

  }

  // =====================================
  // Back
  // =====================================

  backToGig(): void {

    this.router.navigate([
      '/gig-details',
      this.gigId
    ]);

  }



}
