import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NotificationFilterDTO, NotificationResponseDTO } from '../models/notification.model';
import { Router } from '@angular/router';
import { ToastService } from '../../services/toast.service';
import { StorageService } from '../../auth/services/storage.service';
import { NotificationService } from '../services/notification.service';
import { NotificationType } from '../../enums/notification-type.enum';

@Component({
  selector: 'app-notifications.component',
  imports: [CommonModule, FormsModule],
  templateUrl: './notifications.component.html',
  styleUrl: './notifications.component.css',
})
export class NotificationsComponent implements OnInit {





  // =====================================
  // Properties
  // =====================================

  loading = false;

  notifications: NotificationResponseDTO[] = [];

  readonly notificationTypes =
    Object.values(NotificationType);

  role = '';

  userId = 0;

  filter: NotificationFilterDTO = {

    type: undefined,

    isRead: undefined,

    keyword: '',

    userId: undefined

  };

  // =====================================
  // Constructor
  // =====================================

  constructor(

    private notificationService: NotificationService,

    private storage: StorageService,

    private toast: ToastService,

    private router: Router,

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

    if (this.role !== 'ADMIN') {

      this.filter.userId = this.userId;

    }

    this.search();

  }

  // =====================================
  // Search
  // =====================================

  search(): void {

    this.loading = true;

    const request: NotificationFilterDTO = {

      type:
        this.filter.type || undefined,

      isRead:
        this.filter.isRead,

      keyword:
        this.filter.keyword?.trim() || undefined,

      userId:
        this.role === 'ADMIN'
          ? this.filter.userId || undefined
          : this.userId

    };

    this.notificationService
      .search(request)
      .subscribe({

        next: res => {

          this.notifications = res;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load notifications.',
            'danger'
          );

        }

      });

  }

  // =====================================
  // Reset
  // =====================================

  reset(): void {

    this.filter = {

      type: undefined,

      isRead: undefined,

      keyword: '',

      userId:
        this.role === 'ADMIN'
          ? undefined
          : this.userId

    };

    this.search();

  }

  // =====================================
  // Mark Read
  // =====================================

  markAsRead(
    notification: NotificationResponseDTO
  ): void {

    if (notification.isRead) {

      this.open(notification);

      return;

    }

    this.notificationService
      .markAsRead(
        notification.id,
        notification.userId
      )
      .subscribe({

        next: () => {

          notification.isRead = true;

          this.open(notification);

        }

      });

  }

  // =====================================
  // Delete
  // =====================================

  delete(
    notification: NotificationResponseDTO
  ): void {

    if (!confirm(
      'Delete this notification?'
    )) {

      return;

    }

    this.notificationService
      .delete(
        notification.id,
        notification.userId
      )
      .subscribe({

        next: () => {

          this.toast.show(
            'Notification deleted.'
          );

          this.search();

        }

      });

  }

  // =====================================
  // Mark All
  // =====================================

  markAllRead(): void {

    this.notificationService
      .markAllAsRead(this.userId)
      .subscribe({

        next: () => {

          this.toast.show(
            'All notifications marked as read.'
          );

          this.search();

        }

      });

  }

  // =====================================
  // Delete All
  // =====================================

  clearAll(): void {

    if (!confirm(
      'Delete all notifications?'
    )) {

      return;

    }

    this.notificationService
      .deleteAll(this.userId)
      .subscribe({

        next: () => {

          this.toast.show(
            'All notifications removed.'
          );

          this.search();

        }

      });

  }

  // =====================================
  // Details
  // =====================================

  open(
    notification: NotificationResponseDTO
  ): void {

    if (this.role === 'ADMIN') {

      this.router.navigate([
        '/admin/notification-details',
        notification.id
      ]);

      return;

    }

    if (this.role === 'COMPANY') {

      this.router.navigate([
        '/company/notification-details',
        notification.id
      ]);

      return;

    }

    this.router.navigate([
      '/user/notification-details',
      notification.id
    ]);

  }




}
