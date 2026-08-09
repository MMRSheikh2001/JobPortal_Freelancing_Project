import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { GigResponseModel } from '../../models/gig.model';
import { GigOrderResponseDTO } from '../../models/gig-order.model';
import { ActivatedRoute, Router } from '@angular/router';
import { GigService } from '../../services/gig.service';
import { GigOrderService } from '../../services/gig-order.service';
import { StorageService } from '../../../auth/services/storage.service';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';
import { ToastService } from '../../../services/toast.service';
import { SavedGigService } from '../../../saved/services/saved-gig.service';

@Component({
  selector: 'app-gig-details',
  imports: [CommonModule],
  templateUrl: './gig-details.html',
  styleUrl: './gig-details.css',
})
export class GigDetails implements OnInit {




  // =====================================
  // Properties
  // =====================================

  gigId = 0;

  loading = false;

  gig?: GigResponseModel;

  relatedGigs: GigResponseModel[] = [];

  imageUrl = '';

  loggedIn = false;

  role: string | null = null;

  buyerId = 0;

  activeOrder?: GigOrderResponseDTO;

  hasActiveOrder = false;

  isOwnGig = false;

  isSaved = false;

  savingGig = false;

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private route: ActivatedRoute,

    private router: Router,

    private gigService: GigService,

    private gigOrderService: GigOrderService,

    private storage: StorageService,

    public fileService: FileResourceHandleService,

    private toast: ToastService,

    private cdr: ChangeDetectorRef,
    private savedGigService: SavedGigService

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.loggedIn = this.storage.isLoggedIn();

    this.role = this.storage.getRole();

    this.buyerId = this.storage.getUserId() ?? 0;

    this.route.paramMap.subscribe(params => {

      this.gigId = Number(params.get('id'));

      this.loadGig();

    });

  }

  // =====================================
  // Load Gig
  // =====================================

  loadGig(): void {

    this.loading = true;

    this.gig = undefined;

    this.relatedGigs = [];

    this.activeOrder = undefined;

    this.hasActiveOrder = false;

    this.gigService
      .getById(this.gigId)
      .subscribe({

        next: (gig) => {

          this.gig = gig;

          this.isOwnGig =
            this.role === 'USER' &&
            gig.userProfileId === (this.storage.getProfileId() ?? 0);

          this.imageUrl =
            this.fileService.getGigImage(
              gig.gigImage
            );

          this.loadRelated();

          this.checkActiveOrder();

          this.checkSavedGig();

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Gig not found.',
            'danger'
          );

          this.router.navigate(['/']);

          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // Related Gigs
  // =====================================

  loadRelated(): void {

    this.gigService
      .getRelated(this.gigId)
      .subscribe({

        next: (data) => {

          this.relatedGigs = data;

          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // Check Active Order
  // =====================================

  checkActiveOrder(): void {

    if (!this.loggedIn) {

      return;

    }

    if (
      !this.canOrder()
    ) {

      return;

    }
    if (this.isOwnGig) {

      return;

    }

    this.gigOrderService
      .getActiveOrder(
        this.gigId,
        this.buyerId
      )
      .subscribe({

        next: (order) => {

          this.activeOrder = order;

          this.hasActiveOrder = true;

          this.cdr.markForCheck();

        },

        error: () => {

          this.hasActiveOrder = false;

          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // Order Gig
  // =====================================

  orderGig(): void {



    if (!this.loggedIn) {

      this.router.navigate(['/login']);

      return;

    }

    if (
      !this.canOrder()
    ) {

      this.toast.show(
        'Only users and companies can order gigs.',
        'warning'
      );

      return;

    }

    if (this.isOwnGig) {

      this.toast.show(
        'You cannot order your own gig.',
        'warning'
      );

      return;

    }

    if (this.hasActiveOrder) {

      this.continueOrder();

      return;

    }

    this.gigOrderService
      .placeOrder(
        this.gigId,
        this.buyerId
      )
      .subscribe({

        next: (order) => {

          this.activeOrder = order;

          this.hasActiveOrder = true;

          this.toast.show(
            'Order placed successfully.'
          );

          this.router.navigate([
            this.getConversationBaseRoute(),
            'conversation-list',
            order.conversationId
          ]);

          this.cdr.markForCheck();

        },

        error: () => {

          this.toast.show(
            'Unable to place order.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Continue Order
  // =====================================

  continueOrder(): void {

    if (!this.activeOrder) {
      return;
    }

    this.router.navigate([
      this.getConversationBaseRoute(),
      'conversation-list',
      this.activeOrder.conversationId
    ]);

  }

  // =====================================
  // View Related Gig
  // =====================================

  openGig(id: number): void {

    this.router.navigate([
      '/gig-details',
      id
    ]);

  }

  canOrder(): boolean {

    return this.loggedIn &&
      (this.role === 'USER' ||
        this.role === 'COMPANY');

  }


  // =====================================
  // Gig Reviews
  // =====================================

  viewReviews(): void {

    this.router.navigate([
      '/gig-reviews',
      this.gigId
    ]);

  }

  // =====================================
  // Seller Profile
  // =====================================

  viewSellerProfile(): void {

    if (!this.gig) {
      return;
    }

    this.router.navigate([
      '/seller-profile-view',
      this.gig.userProfileId
    ]);

  }

  private getConversationBaseRoute(): string {

    if (this.role === 'COMPANY') {
      return '/company';
    }

    return '/user';

  }

  private checkSavedGig(): void {

    if (!this.loggedIn) {
      return;
    }

    if (!this.canOrder()) {
      return;
    }

    this.savedGigService
      .isGigSaved(
        this.buyerId,
        this.gigId
      )
      .subscribe({

        next: res => {

          this.isSaved = res;

          this.cdr.markForCheck();

        }

      });

  }


  toggleSaveGig(): void {

    if (!this.loggedIn) {

      this.router.navigate(['/login']);

      return;

    }

    if (!this.canOrder()) {

      this.toast.show(
        'Only users and companies can save gigs.',
        'warning'
      );

      return;

    }

    this.savingGig = true;

    if (this.isSaved) {

      this.savedGigService
        .unsaveGig(
          this.buyerId,
          this.gigId
        )
        .subscribe({

          next: () => {

            this.isSaved = false;

            this.savingGig = false;

            this.toast.show(
              'Gig removed from saved list.',
              'success'
            );

            this.cdr.markForCheck();

          },

          error: () => {

            this.savingGig = false;

            this.toast.show(
              'Unable to remove saved gig.',
              'danger'
            );

          }

        });

    }

    else {

      this.savedGigService
        .saveGig(
          this.buyerId,
          this.gigId
        )
        .subscribe({

          next: () => {

            this.isSaved = true;

            this.savingGig = false;

            this.toast.show(
              'Gig saved successfully.',
              'success'
            );

            this.cdr.markForCheck();

          },

          error: () => {

            this.savingGig = false;

            this.toast.show(
              'Unable to save gig.',
              'danger'
            );

          }

        });

    }

  }
}
