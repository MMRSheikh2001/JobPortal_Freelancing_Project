import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ResetPasswordRequestModel } from '../models/reset-password-request.model';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-reset-password',
  imports: [CommonModule, FormsModule],
  templateUrl: './reset-password.html',
  styleUrl: './reset-password.css',
})
export class ResetPassword implements OnInit {




  resetData: ResetPasswordRequestModel = {
    token: '',
    newPassword: ''
  };

  confirmPassword = '';

  message = '';
  error = '';

  loading = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) { }

  ngOnInit(): void {

    const token = this.route.snapshot.queryParamMap.get('token');

    if (token) {
      this.resetData.token = token;
    }

  }

  resetPassword(): void {

    this.message = '';
    this.error = '';

    if (!this.resetData.token) {
      this.error = 'Invalid reset link.';
      return;
    }

    if (!this.resetData.newPassword) {
      this.error = 'Please enter a new password.';
      return;
    }

    if (this.resetData.newPassword !== this.confirmPassword) {
      this.error = 'Passwords do not match.';
      return;
    }

    this.loading = true;

    this.authService.resetPassword(this.resetData).subscribe({

      next: (response) => {

        this.loading = false;

        this.message = response;

        setTimeout(() => {

          this.router.navigate(['/login']);

        }, 3000);

      },

      error: (err) => {

        this.loading = false;

        this.error =
          typeof err.error === 'string'
            ? err.error
            : 'Password reset failed.';

      }

    });

  }


}
