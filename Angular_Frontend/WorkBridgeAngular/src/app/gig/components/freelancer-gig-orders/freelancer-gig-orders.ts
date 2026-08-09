import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { GigOrderResponseDTO } from '../../models/gig-order.model';
import { ActivatedRoute, Router } from '@angular/router';
import { GigOrderService } from '../../services/gig-order.service';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';
import { ToastService } from '../../../services/toast.service';

@Component({
  selector: 'app-freelancer-gig-orders',
  imports: [CommonModule],
  templateUrl: './freelancer-gig-orders.html',
  styleUrl: './freelancer-gig-orders.css',
})
export class FreelancerGigOrders implements OnInit {





  // =====================================
  // Properties
  // =====================================

  gigId = 0;

  loading = false;

  orders: GigOrderResponseDTO[] = [];

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private route: ActivatedRoute,

    private router: Router,

    private gigOrderService: GigOrderService,

    public fileService: FileResourceHandleService,

    private toast: ToastService,

    private cdr: ChangeDetectorRef

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.gigId =
      Number(
        this.route.snapshot.paramMap.get('gigId')
      );

    this.loadOrders();

  }

  // =====================================
  // Load Orders
  // =====================================

  loadOrders(): void {

    this.loading = true;

    this.gigOrderService
      .getByGigId(this.gigId)
      .subscribe({

        next: (data) => {

          this.orders = data;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load gig orders.',
            'danger'
          );

          this.cdr.markForCheck();

        }

      });

  }

  // =====================================
  // View Order
  // =====================================

  viewOrder(
    order: GigOrderResponseDTO
  ): void {

    this.router.navigate([
      '/user/freelancer-gig-details',
      order.id
    ]);

  }

  // =====================================
  // Back
  // =====================================

  goBack(): void {

    this.router.navigate([
      '/user/freelancer-gigs'
    ]);

  }

  // =====================================
  // Gig Image
  // =====================================

  getGigImage(
    fileName?: string
  ): string {

    return this.fileService.getGigImage(fileName);

  }



}
