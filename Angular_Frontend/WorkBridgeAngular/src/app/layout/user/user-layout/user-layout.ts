import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { UserNavbar } from '../user-navbar/user-navbar';
import { UserSidebar } from '../user-sidebar/user-sidebar';
import { Footer } from '../../shared/footer/footer';

@Component({
  selector: 'app-user-layout',
  imports: [RouterOutlet,UserNavbar,UserSidebar],
  templateUrl: './user-layout.html',
  styleUrl: './user-layout.css',
})
export class UserLayout {}
