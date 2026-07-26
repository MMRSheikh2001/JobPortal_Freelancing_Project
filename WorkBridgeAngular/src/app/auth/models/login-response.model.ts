export type UserRole = 'USER' | 'COMPANY' | 'ADMIN';

export interface LoginResponseModel {
  token: string;
  tokenType: string;

  userId: number;
  email: string;
  role: UserRole;

  profileId?: number;
  displayName: string;
  image?: string;
}