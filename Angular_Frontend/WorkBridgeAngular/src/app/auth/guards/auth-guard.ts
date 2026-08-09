import { CanActivateFn, Router } from '@angular/router';
import { StorageService } from '../services/storage.service';
import { inject } from '@angular/core';

export const authGuard: CanActivateFn = (route, state) => {
  const storage = inject(StorageService);

  const router = inject(Router);

  // No token
  if (!storage.isLoggedIn()) {

    router.navigate(['/login']);

    return false;

  }

  // Token exists but expired
  if (storage.isTokenExpired()) {

    storage.clearSession();

    router.navigate(['/login']);

    return false;

  }

  return true;


};
