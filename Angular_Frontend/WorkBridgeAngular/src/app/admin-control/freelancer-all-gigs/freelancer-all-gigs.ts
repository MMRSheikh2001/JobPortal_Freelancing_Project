import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { GigResponseModel } from '../../gig/models/gig.model';
import { ActivatedRoute, Router } from '@angular/router';
import { GigService } from '../../gig/services/gig.service';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-freelancer-all-gigs',
  imports: [CommonModule],
  templateUrl: './freelancer-all-gigs.html',
  styleUrl: './freelancer-all-gigs.css',
})
export class FreelancerAllGigs implements OnInit {




 

  //---------------------------------
  // Properties
  //---------------------------------

  userProfileId = 0;

  gigs: GigResponseModel[] = [];

  loading = false;

  //---------------------------------
  // Constructor
  //---------------------------------

  constructor(

    private route: ActivatedRoute,

    private gigService: GigService,

    private toast: ToastService,

    private router: Router,

    private cdr: ChangeDetectorRef

  ) { }

  //---------------------------------
  // Init
  //---------------------------------

  ngOnInit(): void {

    this.userProfileId = Number(

      this.route.snapshot.paramMap.get('userProfileId')

    );

    this.loadGigs();

  }

  //---------------------------------
  // Load
  //---------------------------------

  loadGigs(): void {

    this.loading = true;

    this.gigService
      .getByUserProfileId(this.userProfileId)
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

  //---------------------------------
  // View
  //---------------------------------

  viewGig(id: number): void {

    this.router.navigate([
      '/gig-details',
      id
    ]);

  }

  //---------------------------------
  // Orders
  //---------------------------------

  gigOrders(id: number): void {

    this.router.navigate([
      '/admin/gig-order-details',
      id
    ]);

  }

  //---------------------------------
  // Activate / Deactivate
  //---------------------------------

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

        },

        error: () => {

          this.toast.show(
            'Unable to change status.',
            'danger'
          );

        }

      });

  }

  //---------------------------------
  // Delete
  //---------------------------------

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
