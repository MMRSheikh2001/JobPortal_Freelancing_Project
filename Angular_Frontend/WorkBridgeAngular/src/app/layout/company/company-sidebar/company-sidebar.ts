import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { StorageService } from '../../../auth/services/storage.service';

@Component({
  selector: 'app-company-sidebar',
  imports: [
    CommonModule,
    RouterLink,
    RouterLinkActive],
  templateUrl: './company-sidebar.html',
  styleUrl: './company-sidebar.css',
})
export class CompanySidebar {



  constructor(
    private storageService: StorageService,
    private router: Router
  ) { }

  logout(): void {

    this.storageService.clearSession();

    this.router.navigate(['/login']);

  }


}
