import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { StorageService } from '../services/storage.service';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';
import Swal from 'sweetalert2';

export const authInterceptorInterceptor: HttpInterceptorFn = (req, next) => {


  const storage = inject(StorageService);

  const router = inject(Router);


  const publicUrls = [
    '/auth/login',
    '/auth/verifyemail',
    '/users/register',
    '/auth/forgot-password',
    '/auth/reset-password'
  ];

  const isPublic = publicUrls.some(url =>
    req.url.includes(url)
  );

  if (isPublic) {
    return next(req);
  }

  const token = storage.getToken();

  if (token) {

    req = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });

  }
  return next(req).pipe(

    catchError((error: HttpErrorResponse) => {



      // JWT expired / invalid
      if (error.status === 401) {

        storage.clearSession();

        Swal.fire({
          icon: 'warning',
          title: 'Session Expired',
          text: 'Your session has expired. Please login again.',
          confirmButtonColor: '#dc3545'
        }).then(() => {

          router.navigate(['/login']);

        });

        router.navigate(['/login']);

        return throwError(() => error);

      }

      // Other backend errors
      if (!req.url.includes('/auth/login')) {

        const message =
          error.error?.message ||
          error.error?.error ||
          error.message ||
          'Something went wrong.';



        Swal.fire({
          icon: 'error',
          title: 'Oops!',
          text: message,
          confirmButtonColor: '#0d6efd'
        });

      }

      return throwError(() => error);

    })

  );
};
