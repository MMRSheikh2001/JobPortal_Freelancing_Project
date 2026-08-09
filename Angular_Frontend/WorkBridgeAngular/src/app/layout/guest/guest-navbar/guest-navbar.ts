import { Component } from '@angular/core';
import { RouterLink, RouterLinkActive } from '@angular/router';

@Component({
  selector: 'app-guest-navbar',
  imports: [
    RouterLink,
    RouterLinkActive],
  templateUrl: './guest-navbar.html',
  styleUrl: './guest-navbar.css',
})
export class GuestNavbar {}
