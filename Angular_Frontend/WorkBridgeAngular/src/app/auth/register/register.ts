import { Component } from '@angular/core';
import { RegisterRequestModel, UserRole } from '../models/register.model';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-register',
  imports: [CommonModule,FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css',
})
export class Register {


  registerData: RegisterRequestModel = {
    fullName: '',
    email: '',
    password: '',
    role: 'USER'
  };

  confirmPassword = '';

  message = '';
  error = '';

  roles: UserRole[] = ['USER', 'COMPANY'];

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  register(): void {

    this.message = '';
    this.error = '';

    if (this.registerData.password !== this.confirmPassword) {
      this.error = 'Passwords do not match';
      return;
    }

    this.authService.register(this.registerData).subscribe({

      next: () => {

        this.message =
          this.registerData.role === 'COMPANY'
            ? 'Company account created. Please verify your email.'
            : 'User account created. Please verify your email.';

        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 4000);

      },

      error: (err) => {

        this.error =
          typeof err.error === 'string'
            ? err.error
            : 'Registration Failed';

      }

    });

  }

}
