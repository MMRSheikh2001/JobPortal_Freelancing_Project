import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { GigResponseModel } from '../../models/gig.model';
import { GigService } from '../../services/gig.service';
import { StorageService } from '../../../auth/services/storage.service';
import { ToastService } from '../../../services/toast.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-my-gigs',
  imports: [CommonModule],
  templateUrl: './my-gigs.html',
  styleUrl: './my-gigs.css',
})
export class MyGigs implements OnInit {






  // =====================================
  // Properties
  // =====================================

  userProfileId = 0;

  gigs: GigResponseModel[] = [];

  loading = false;

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private gigService: GigService,

    private storage: StorageService,

    private toast: ToastService,

    private router: Router,

    private cdr: ChangeDetectorRef

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.userProfileId =
      this.storage.getProfileId() ?? 0;

    this.loadGigs();

  }

  // =====================================
  // Load Gigs
  // =====================================

  loadGigs(): void {

    this.loading = true;

    this.gigService
      .getByUserProfileId(this.userProfileId)
      .subscribe({

        next: (data) => {

          this.gigs = data;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Failed to load gigs.',
            'danger'
          );

          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // Create Gig
  // =====================================

  createGig(): void {

    this.router.navigate([
      '/user/manage-gig'
    ]);

  }

  // =====================================
  // View Gig
  // =====================================

  viewGig(id: number): void {

    this.router.navigate([
      '/gig-details',
      id
    ]);

  }

  // =====================================
  // Edit Gig
  // =====================================

  editGig(id: number): void {

    this.router.navigate([
      '/user/manage-gig',
      id
    ]);

  }

  // =====================================
  // Gig Orders
  // =====================================

  gigOrders(id: number): void {

    this.router.navigate([
      '/user/freelancer-gig-orders',
      id
    ]);

  }

  // =====================================
  // Activate / Deactivate
  // =====================================

  toggleStatus(gig: GigResponseModel): void {

    this.gigService
      .changeStatus(gig.id)
      .subscribe({

        next: (updatedGig) => {

          gig.isActive =
            updatedGig.isActive;

          this.toast.show(

            updatedGig.isActive
              ? 'Gig Activated Successfully.'
              : 'Gig Deactivated Successfully.'

          );

          this.loadGigs();

        },

        error: () => {

          this.toast.show(
            'Unable to change gig status.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Delete Gig
  // =====================================

  deleteGig(gig: GigResponseModel): void {

    const ok = confirm(

      `Delete "${gig.title}" ?`

    );

    if (!ok) {

      return;

    }

    this.gigService
      .delete(gig.id)
      .subscribe({

        next: () => {

          this.toast.show(
            'Gig Deleted Successfully.'
          );

          this.loadGigs();

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
