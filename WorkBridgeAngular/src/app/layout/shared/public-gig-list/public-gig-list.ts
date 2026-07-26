import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { GigResponseModel, GigSearchRequestModel } from '../../../gig/models/gig.model';
import { CategoryResponseModel } from '../../../admin/cvinformations/models/category.model';
import { GigService } from '../../../gig/services/gig.service';
import { CategoryService } from '../../../admin/cvinformations/services/category.service';
import { ToastService } from '../../../services/toast.service';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-public-gig-list',
  imports: [CommonModule, FormsModule],
  templateUrl: './public-gig-list.html',
  styleUrl: './public-gig-list.css',
})
export class PublicGigList implements OnInit {





  // =====================================
  // Properties
  // =====================================

  gigs: GigResponseModel[] = [];

  categories: CategoryResponseModel[] = [];

  loading = false;

  searchRequest: GigSearchRequestModel = {

    keyword: '',

    categoryId: 0,

    minPrice: 0,

    maxPrice: 0,

    maxDeliveryDays: 0,

    active: true,

    minimumRating: 0,

    minimumOrders: 0

  };

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private gigService: GigService,

    private categoryService: CategoryService,

    private toast: ToastService,

    public fileService: FileResourceHandleService,

    private cdr: ChangeDetectorRef,
    private router: Router

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.loadCategories();

    this.searchGigs();

  }

  // =====================================
  // Categories
  // =====================================

  loadCategories(): void {

    this.categoryService
      .getAll()
      .subscribe({

        next: res => {

          this.categories = res;

          this.cdr.markForCheck();

        },

        error: () => {

          this.toast.show(
            'Unable to load categories.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Search
  // =====================================

  searchGigs(): void {

    this.loading = true;

    this.gigService
      .search(this.searchRequest)
      .subscribe({

        next: res => {

          this.gigs = res;

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
  // Reset Filter
  // =====================================

  resetFilters(): void {

    this.searchRequest = {

      keyword: '',

      categoryId: 0,

      minPrice: 0,

      maxPrice: 0,

      maxDeliveryDays: 0,

      active: true,

      minimumRating: 0,

      minimumOrders: 0

    };

    this.searchGigs();

  }

  // =====================================
  // View Gig
  // =====================================

  viewGig(
    gig: GigResponseModel
  ): void {

    this.router.navigate([
      '/gig-details',
      gig.id
    ]);

  }


}
