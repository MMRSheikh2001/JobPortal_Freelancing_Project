import { Component } from '@angular/core';
import { Footer } from '../../shared/footer/footer';
import { GuestNavbar } from '../guest-navbar/guest-navbar';
import { RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-guest-layout',
  imports: [
    RouterOutlet,
    GuestNavbar,
    Footer],
  templateUrl: './guest-layout.html',
  styleUrl: './guest-layout.css',
})
export class GuestLayout { }
