import { CanActivateFn } from '@angular/router';
import { inject } from '@angular/core';
import { Router } from '@angular/router';

import { StorageService } from '../services/storage.service';
export const roleGuard = (
  allowedRoles: string[]
): CanActivateFn => {

  return (route, state) => {

    const storage = inject(StorageService);
    const router = inject(Router);

    const role = storage.getRole();

    if (role && allowedRoles.includes(role)) {
      return true;
    }

    switch (role) {

      case 'USER':
        router.navigate(['/user/dashboard']);
        break;

      case 'COMPANY':
        router.navigate(['/company/dashboard']);
        break;

      case 'ADMIN':
        router.navigate(['/admin/dashboard']);
        break;

      default:
        router.navigate(['/login']);
    }

    return false;
  };

};