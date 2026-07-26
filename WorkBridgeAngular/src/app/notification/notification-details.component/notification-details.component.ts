import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { NotificationType } from '../../enums/notification-type.enum';
import { ToastService } from '../../services/toast.service';
import { StorageService } from '../../auth/services/storage.service';
import { NotificationService } from '../services/notification.service';
import { ActivatedRoute, Router } from '@angular/router';
import { NotificationResponseDTO } from '../models/notification.model';

@Component({
  selector: 'app-notification-details.component',
  imports: [CommonModule],
  templateUrl: './notification-details.component.html',
  styleUrl: './notification-details.component.css',
})
export class NotificationDetailsComponent implements OnInit {






  // =====================================
  // Properties
  // =====================================

  loading = false;

  notificationId = 0;

  notification?: NotificationResponseDTO;

  role = '';

  userId = 0;

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private route: ActivatedRoute,

    private router: Router,

    private notificationService: NotificationService,

    private storage: StorageService,

    private toast: ToastService,

    private cdr: ChangeDetectorRef

  ) { }

  // =====================================
  // Init
  // =====================================

  ngOnInit(): void {

    this.role =
      this.storage.getRole() ?? '';

    this.userId =
      this.storage.getUserId() ?? 0;

    this.route.paramMap.subscribe(params => {

      this.notificationId =
        Number(params.get('notificationId'));

      this.load();

    });

  }

  // =====================================
  // Load Notification
  // =====================================

  load(): void {

    this.loading = true;

    this.notificationService
      .getById(this.notificationId)
      .subscribe({

        next: res => {

          this.notification = res;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load notification.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Back
  // =====================================

  back(): void {

    if (this.role === 'ADMIN') {

      this.router.navigate([
        '/admin/notifications'
      ]);

      return;

    }

    if (this.role === 'COMPANY') {

      this.router.navigate([
        '/company/notifications'
      ]);

      return;

    }

    this.router.navigate([
      '/user/notifications'
    ]);

  }

  // =====================================
  // Open Related
  // =====================================

  openRelated(): void {

    if (!this.notification) {

      return;

    }

    switch (this.notification.type) {

      // =============================
      // Job
      // =============================

      case NotificationType.JOB_APPLIED:

        if (this.role === 'COMPANY') {

          this.router.navigate([
            '/company/job-applications',
            this.notification.referenceId
          ]);

        }

        break;

      case NotificationType.JOB_SHORTLISTED:

      case NotificationType.JOB_REJECTED:

      case NotificationType.JOB_HIRED:

        if (this.role === 'USER') {

          this.router.navigate([
            '/user/my-applications'
          ]);

        }

        break;

      // =============================
      // Gig
      // =============================

      case NotificationType.GIG_ORDER:

      case NotificationType.GIG_COMPLETED:

        if (this.role === 'USER') {

          this.router.navigate([
            '/user/buyer-gig-details',
            this.notification.referenceId
          ]);

        }

        if (this.role === 'COMPANY') {

          this.router.navigate([
            '/company/buyer-gig-details',
            this.notification.referenceId
          ]);

        }

        break;

      // =============================
      // Wallet
      // =============================

      case NotificationType.DEPOSIT_SUCCESS:

      case NotificationType.WITHDRAW_APPROVED:

      case NotificationType.WITHDRAW_REJECTED:

        if (this.role === 'ADMIN') {

          this.router.navigate([
            '/admin/wallet'
          ]);

        }
        else if (this.role === 'COMPANY') {

          this.router.navigate([
            '/company/wallet'
          ]);

        }
        else {

          this.router.navigate([
            '/user/wallet'
          ]);

        }

        break;

      default:

        this.toast.show(
          'No related page available.',
          'info'
        );

    }

  }




  getRelatedItemLabel(): string {

    if (!this.notification) {

      return '-';

    }

    switch (this.notification.type) {

      // ---------------------
      // Job
      // ---------------------

      case NotificationType.JOB_APPLIED:
        return `Job #${this.notification.referenceId}`;

      case NotificationType.JOB_SHORTLISTED:
      case NotificationType.JOB_REJECTED:
      case NotificationType.JOB_HIRED:
        return `Job Application #${this.notification.referenceId}`;

      // ---------------------
      // Gig
      // ---------------------

      case NotificationType.GIG_ORDER:
      case NotificationType.GIG_COMPLETED:
        return `Gig Order #${this.notification.referenceId}`;

      // ---------------------
      // Wallet
      // ---------------------

      case NotificationType.DEPOSIT_SUCCESS:
        return `Deposit #${this.notification.referenceId}`;

      case NotificationType.WITHDRAW_APPROVED:
      case NotificationType.WITHDRAW_REJECTED:
        return `Withdraw Request #${this.notification.referenceId}`;

      // ---------------------
      // General
      // ---------------------

      case NotificationType.SYSTEM:
        return 'System Notification';

      case NotificationType.ADMIN_MESSAGE:
        return 'Admin Message';

      default:
        return '-';

    }

  }



}
