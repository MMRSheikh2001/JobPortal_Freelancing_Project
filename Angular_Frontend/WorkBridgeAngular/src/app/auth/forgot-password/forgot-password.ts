import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ForgotPasswordRequestModel } from '../models/forgot-password-request.model';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-forgot-password',
  imports: [CommonModule, FormsModule],
  templateUrl: './forgot-password.html',
  styleUrl: './forgot-password.css',
})
export class ForgotPassword {



  forgotData: ForgotPasswordRequestModel = {
    email: ''
  };

  message = '';
  error = '';
  loading = false;

  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  sendResetLink(): void {

    this.message = '';
    this.error = '';

    if (!this.forgotData.email) {
      this.error = 'Please enter your email address.';
      return;
    }

    this.loading = true;

    this.authService.forgotPassword(this.forgotData).subscribe({

      next: (response) => {

        this.loading = false;
        this.message = response;


        setTimeout(() => {

          this.router.navigate(['/']);

        }, 10000);

      },

      error: (err) => {

        this.loading = false;

        this.error =
          typeof err.error === 'string'
            ? err.error
            : 'Unable to send reset link. Please try again.';

      }

    });

  }


}
