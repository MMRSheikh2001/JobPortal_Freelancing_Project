import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { LoginRequestModel } from '../models/login-request.model';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { StorageService } from '../services/storage.service';

@Component({
  selector: 'app-login',
  imports: [CommonModule, FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css',
})
export class Login {


  loginData: LoginRequestModel = {
    email: '',
    password: ''
  };

  message = '';
  error = '';

  constructor(
    private authService: AuthService,
    private storage: StorageService,
    private router: Router
  ) { }

  login(): void {

    this.message = '';
    this.error = '';

    if (!this.loginData.email || !this.loginData.password) {
      this.error = 'Please enter email and password.';
      return;
    }

    this.authService.login(this.loginData).subscribe({

      next: (response) => {

        // Save JWT
        // Save logged in user information
        this.storage.saveSession(response);



        this.message = 'Login Successful';

        // Redirect according to role
        switch (response.role) {

          case 'USER':
            this.router.navigate(['/user/dashboard']);
            break;

          case 'COMPANY':
            this.router.navigate(['/company/dashboard']);
            break;

          case 'ADMIN':
            this.router.navigate(['/admin/dashboard']);
            break;

          default:
            this.router.navigate(['/']);
            break;
        }

      },

      error: (err) => {

        this.error =
          err.error?.message ??
          'Invalid Email or Password';
          alert("Invalid Email or Password");

      }

    });

  }


}
