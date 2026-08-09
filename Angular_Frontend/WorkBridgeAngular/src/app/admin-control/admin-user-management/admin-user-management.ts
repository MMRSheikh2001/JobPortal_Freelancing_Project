import { CommonModule } from '@angular/common';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { UserRole } from '../../enums/user-role.enum';
import { UserResponseDTO, UserSearchRequestDTO } from '../models/user.model';
import { UserService } from '../services/user.service';
import { ToastService } from '../../services/toast.service';

@Component({
  selector: 'app-admin-user-management',
  imports: [CommonModule, FormsModule],
  templateUrl: './admin-user-management.html',
  styleUrl: './admin-user-management.css',
})
export class AdminUserManagement implements OnInit {





  //=====================================
  // Properties
  //=====================================

  loading = false;

  users: UserResponseDTO[] = [];

  roles = Object.values(UserRole);

  filter: UserSearchRequestDTO = {

    keyword: '',

    role: undefined,

    isVerified: undefined,

    isActive: undefined,

    isSuspended: undefined

  };

  //=====================================
  // Constructor
  //=====================================

  constructor(

    private userService: UserService,

    private toast: ToastService,

    private cdr: ChangeDetectorRef

  ) { }

  //=====================================
  // Init
  //=====================================

  ngOnInit(): void {

    this.search();

  }

  //=====================================
  // Search
  //=====================================

  search(): void {

    this.loading = true;

    this.userService
      .filter(this.filter)
      .subscribe({

        next: res => {

          this.users = res;

          this.loading = false;

          this.cdr.markForCheck();

        },

        error: () => {

          this.loading = false;

          this.toast.show(
            'Unable to load users.',
            'danger'
          );

        }

      });

  }

  //=====================================
  // Reset Filter
  //=====================================

  reset(): void {

    this.filter = {

      keyword: '',

      role: undefined,

      isVerified: undefined,

      isActive: undefined,

      isSuspended: undefined

    };

    this.search();

  }

  //=====================================
  // Suspend / Unsuspend
  //=====================================

  toggleSuspend(user: UserResponseDTO): void {

    this.userService
      .toggleSuspendStatus(user.id)
      .subscribe({

        next: res => {

          user.isSuspended = res.isSuspended;

          this.toast.show(

            res.isSuspended
              ? 'User suspended successfully.'
              : 'User unsuspended successfully.',

            'success'

          );

        },

        error: () => {

          this.toast.show(
            'Unable to update user.',
            'danger'
          );

        }

      });

  }

  //=====================================
  // Delete User
  //=====================================

  delete(user: UserResponseDTO): void {

    if (!confirm(`Delete ${user.email}?`)) {

      return;

    }

    this.userService
      .delete(user.id)
      .subscribe({

        next: () => {

          this.toast.show(
            'User deleted successfully.',
            'success'
          );

          this.users =
            this.users.filter(
              x => x.id !== user.id
            );

          this.cdr.markForCheck();

        },

        error: () => {

          this.toast.show(
            'Unable to delete user.',
            'danger'
          );

        }

      });

  }

  //=====================================
  // Helpers
  //=====================================

  getRoleBadge(role: UserRole): string {

    switch (role) {

      case UserRole.ADMIN:
        return 'bg-danger';

      case UserRole.COMPANY:
        return 'bg-primary';

      default:
        return 'bg-success';

    }

  }


  //=====================================
  // Is Admin
  //=====================================

  isAdmin(user: UserResponseDTO): boolean {

    return user.role === UserRole.ADMIN;

  }

}
