import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { LoginResponseModel } from '../../../auth/models/login-response.model';
import { FileResourceHandleService } from '../../../services/file-resource-handle.service';
import { Router } from '@angular/router';
import { StorageService } from '../../../auth/services/storage.service';
import { NotificationService } from '../../../notification/services/notification.service';

@Component({
  selector: 'app-user-navbar',
  imports: [CommonModule],
  templateUrl: './user-navbar.html',
  styleUrl: './user-navbar.css',
})
export class UserNavbar implements OnInit {






  user?: LoginResponseModel;

  imageUrl = '';

  unreadCount = 0;

  constructor(
    private storage: StorageService,
    private router: Router,
    public fileService: FileResourceHandleService,

    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {

    this.user = this.storage.getUser() ?? undefined;

    this.imageUrl = this.fileService.getUserProfileImage(
      this.user?.image
    );
    this.loadUnreadCount();

  }

  logout(): void {

    this.storage.clearSession();

    this.router.navigate(['/login']);

  }

  loadUnreadCount(): void {

    const userId = this.storage.getUserId();

    if (!userId) {
      return;
    }

    this.notificationService
      .getUnreadCount(userId)
      .subscribe({

        next: count => {

          this.unreadCount = count;

        }

      });

  }

  goToNotifications(): void {

    const role = this.storage.getRole();

    if (role === 'ADMIN') {

      this.router.navigate(['/admin/notifications']);

    }

    else if (role === 'COMPANY') {

      this.router.navigate(['/company/notifications']);

    }

    else {

      this.router.navigate(['/user/notifications']);

    }

  }



}
