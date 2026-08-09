import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { GigOrderResponseDTO } from '../../models/gig-order.model';
import { GigOrderService } from '../../services/gig-order.service';
import { ToastService } from '../../../services/toast.service';
import { Router } from '@angular/router';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';
import { GigOrderStatus } from '../../../enums/gig-order-status.enum';

@Component({
  selector: 'app-gig-order-dispute-list',
  imports: [CommonModule],
  templateUrl: './gig-order-dispute-list.html',
  styleUrl: './gig-order-dispute-list.css',
})
export class GigOrderDisputeList implements OnInit {






  disputes: GigOrderResponseDTO[] = [];

  loading = false;

  constructor(
    private gigOrderService: GigOrderService,
    private toast: ToastService,
    private router: Router,
    public fileService: FileResourceHandleService,
    private cdr: ChangeDetectorRef

  ) { }

  ngOnInit(): void {

    this.loadDisputes();

  }

  // =====================================
  // Load Disputes
  // =====================================

  loadDisputes(): void {

    this.loading = true;

    this.gigOrderService
      .getByStatus(GigOrderStatus.SELLER_DISPUTED)
      .subscribe({

        next: res => {

          this.disputes = res;

          this.loading = false;
          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load disputes.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // View
  // =====================================

  view(order: GigOrderResponseDTO): void {

    this.router.navigate([
      '/admin/gig-order-details',
      order.id
    ]);

  }




}
