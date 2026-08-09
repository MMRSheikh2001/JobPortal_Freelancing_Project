import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { StorageService } from '../../../auth/services/storage.service';

@Component({
  selector: 'app-admin-sidebar',
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive],
  templateUrl: './admin-sidebar.html',
  styleUrl: './admin-sidebar.css',
})
export class AdminSidebar {



  constructor(
    private storageService: StorageService,
    private router: Router
  ) { }

  logout(): void {

    this.storageService.clearSession();

    this.router.navigate(['/login']);

  }


}
