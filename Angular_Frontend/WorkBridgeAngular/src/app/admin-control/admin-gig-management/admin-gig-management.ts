import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { ToastService } from '../../services/toast.service';
import { CategoryService } from '../../admin/cvinformations/services/category.service';
import { GigService } from '../../gig/services/gig.service';
import { CategoryResponseModel } from '../../admin/cvinformations/models/category.model';
import { GigResponseModel } from '../../gig/models/gig.model';

@Component({
  selector: 'app-admin-gig-management',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-gig-management.html',
  styleUrl: './admin-gig-management.css',
})
export class AdminGigManagement implements OnInit {






  //-----------------------------------
  // Properties
  //-----------------------------------

  loading = false;

  gigs: GigResponseModel[] = [];

  filteredGigs: GigResponseModel[] = [];

  categories: CategoryResponseModel[] = [];

  keyword = '';

  categoryId = 0;

  active: string = 'ALL';

  //-----------------------------------
  // Constructor
  //-----------------------------------

  constructor(

    private gigService: GigService,

    private categoryService: CategoryService,

    private toast: ToastService,

    private router: Router,

    private cdr: ChangeDetectorRef

  ) { }

  //-----------------------------------
  // Init
  //-----------------------------------

  ngOnInit(): void {

    this.loadCategories();

    this.loadGigs();

  }

  //-----------------------------------
  // Categories
  //-----------------------------------

  loadCategories(): void {

    this.categoryService
      .getAll()
      .subscribe({

        next: res => {

          this.categories = res;

        }

      });

  }

  //-----------------------------------
  // Load Gigs
  //-----------------------------------

  loadGigs(): void {

    this.loading = true;

    this.gigService
      .getAll()
      .subscribe({

        next: res => {

          this.gigs = res;

          this.applyFilter();

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

  //-----------------------------------
  // Filter
  //-----------------------------------

  applyFilter(): void {

    this.filteredGigs = this.gigs.filter(gig => {

      const keywordMatch =

        this.keyword === '' ||

        gig.title
          .toLowerCase()
          .includes(this.keyword.toLowerCase()) ||

        gig.userName
          .toLowerCase()
          .includes(this.keyword.toLowerCase());

      const categoryMatch =

        this.categoryId === 0 ||

        gig.categoryId === this.categoryId;

      const statusMatch =

        this.active === 'ALL' ||

        (this.active === 'ACTIVE' && gig.isActive) ||

        (this.active === 'INACTIVE' && !gig.isActive);

      return keywordMatch &&
        categoryMatch &&
        statusMatch;

    });

  }

  //-----------------------------------
  // View Gig
  //-----------------------------------

  viewGig(id: number): void {

    this.router.navigate([
      '/gig-details',
      id
    ]);

  }

  //-----------------------------------
  // Seller
  //-----------------------------------

  viewSeller(userProfileId: number): void {

    this.router.navigate([
      '/admin/user-profile-review',
      userProfileId
    ]);

  }

  //-----------------------------------
  // Activate
  //-----------------------------------

  toggleStatus(gig: GigResponseModel): void {

    this.gigService
      .changeStatus(gig.id)
      .subscribe({

        next: updated => {

          gig.isActive = updated.isActive;

          this.toast.show(

            updated.isActive
              ? 'Gig Activated.'
              : 'Gig Deactivated.'

          );

          this.applyFilter();

        },

        error: () => {

          this.toast.show(
            'Unable to change status.',
            'danger'
          );

        }

      });

  }

  //-----------------------------------
  // Delete
  //-----------------------------------

  deleteGig(gig: GigResponseModel): void {

    if (!confirm(`Delete "${gig.title}"?`)) {

      return;

    }

    this.gigService
      .delete(gig.id)
      .subscribe({

        next: () => {

          this.toast.show(
            'Gig deleted successfully.'
          );

          this.gigs =
            this.gigs.filter(x => x.id !== gig.id);

          this.applyFilter();

        },

        error: () => {

          this.toast.show(
            'Unable to delete gig.',
            'danger'
          );

        }

      });

  }




}
