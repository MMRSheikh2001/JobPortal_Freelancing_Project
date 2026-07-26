import { Component } from '@angular/core';
import { AdminSidebar } from '../admin-sidebar/admin-sidebar';
import { AdminNavbar } from '../admin-navbar/admin-navbar';
import { RouterOutlet } from '@angular/router';
import { Footer } from '../../shared/footer/footer';

@Component({
  selector: 'app-admin-layout',
  imports: [
    RouterOutlet,
    AdminNavbar,
    AdminSidebar],
  templateUrl: './admin-layout.html',
  styleUrl: './admin-layout.css',
})
export class AdminLayout { }
