import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-verify-email',
  imports: [CommonModule],
  templateUrl: './verify-email.html',
  styleUrl: './verify-email.css',
})
export class VerifyEmail implements OnInit {



  loading = true;

  success = false;

  message = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private cdr: ChangeDetectorRef
  ) { }

  ngOnInit(): void {

    const token = this.route.snapshot.queryParamMap.get('token');

    if (!token) {
      this.loading = false;
      this.success = false;
      this.message = 'Verification token not found.';
      return;
    }

    this.authService.verifyEmail(token).subscribe({

      next: (response) => {



        this.loading = false;


        this.success = true;


        this.message = response;
        this.cdr.markForCheck();




      },

      error: (err) => {

        this.loading = false;
        this.success = false;

        this.message =
          typeof err.error === 'string'
            ? err.error
            : 'Verification failed. The link may be invalid or expired.';

      }

    });

  }

  goToLogin(): void {

    this.router.navigate(['/login']);

  }

}
