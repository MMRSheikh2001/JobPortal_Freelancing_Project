export type UserRole = 'USER' | 'COMPANY' | 'ADMIN';

export interface RegisterRequestModel {
  fullName: string;
  email: string;
  password: string;
  role: UserRole;
}

