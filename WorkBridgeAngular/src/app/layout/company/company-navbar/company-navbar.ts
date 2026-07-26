import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { StorageService } from '../../../auth/services/storage.service';
import { Router } from '@angular/router';
import { NotificationService } from '../../../notification/services/notification.service';

@Component({
  selector: 'app-company-navbar',
  imports: [CommonModule],
  templateUrl: './company-navbar.html',
  styleUrl: './company-navbar.css',
})
export class CompanyNavbar implements OnInit {


  unreadCount = 0;

  constructor(
    private storage: StorageService,
    private router: Router,


    private notificationService: NotificationService
  ) { }

  ngOnInit(): void {
    this.loadUnreadCount();
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
