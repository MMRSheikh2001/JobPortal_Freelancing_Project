import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { StorageService } from '../../../auth/services/storage.service';

@Component({
  selector: 'app-user-sidebar',
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive],
  templateUrl: './user-sidebar.html',
  styleUrl: './user-sidebar.css',
})
export class UserSidebar {




  constructor(
    private storageService: StorageService,
    private router: Router
  ) { }

  logout(): void {

    this.storageService.clearSession();

    this.router.navigate(['/login']);

  }


}
