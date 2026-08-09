import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CompanyNavbar } from '../company-navbar/company-navbar';
import { CompanySidebar } from '../company-sidebar/company-sidebar';
import { Footer } from '../../shared/footer/footer';

@Component({
  selector: 'app-company-layout',
  imports: [RouterOutlet,
    CompanyNavbar,
    CompanySidebar],
  templateUrl: './company-layout.html',
  styleUrl: './company-layout.css',
})
export class CompanyLayout { }
