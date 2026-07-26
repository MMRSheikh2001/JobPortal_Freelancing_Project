import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { StorageService } from '../services/storage.service';
import { inject } from '@angular/core';
import { ToastService } from '../../services/toast.service';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';

export const authInterceptorInterceptor: HttpInterceptorFn = (req, next) => {


  const storage = inject(StorageService);

  const router = inject(Router);
  const toast = inject(ToastService);

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

        toast.show(
          'Your session has expired. Please login again.',
          'warning'
        );

        router.navigate(['/login']);

        return throwError(() => error);

      }

      // Other backend errors
      if (!req.url.includes('/auth/login')) {

        toast.show(
          error.error?.message ??
          'Something went wrong.',
          'danger'
        );

      }

      return throwError(() => error);

    })

  );
};
