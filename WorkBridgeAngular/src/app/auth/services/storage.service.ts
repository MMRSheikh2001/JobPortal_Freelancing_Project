import { Injectable } from '@angular/core';
import { LoginResponseModel } from '../models/login-response.model';
import { CryptoUtil } from '../../utils/crypto.util';


export const KEYS = {
  TOKEN: 'wb_token',
  LOGIN_USER: 'wb_login_user',

  USER_PROFILE: 'wb_user_profile',
  COMPANY_PROFILE: 'wb_company_profile'
};

@Injectable({
  providedIn: 'root',
})
export class StorageService {


  constructor() { }

  // ==========================
  // Login Session
  // ==========================

  saveSession(data: LoginResponseModel): void {

    localStorage.setItem(
      KEYS.TOKEN,
      CryptoUtil.encrypt(data.token)
    );

    localStorage.setItem(
      KEYS.LOGIN_USER,
      CryptoUtil.encrypt(JSON.stringify(data))
    );

  }

  getToken(): string | null {

    const raw = localStorage.getItem(KEYS.TOKEN);

    return raw ? CryptoUtil.decrypt(raw) : null;

  }

  getUser(): LoginResponseModel | null {

    const raw = localStorage.getItem(KEYS.LOGIN_USER);

    if (!raw) return null;

    try {

      const json = CryptoUtil.decrypt(raw);

      return json ? JSON.parse(json) : null;

    } catch {

      return null;

    }

  }

  getRole(): string | null {

    return this.getUser()?.role ?? null;

  }

  getUserId(): number | null {

    return this.getUser()?.userId ?? null;

  }

  getProfileId(): number | null {

    return this.getUser()?.profileId ?? null;

  }

  isLoggedIn(): boolean {

    return !!this.getToken();

  }

  clearSession(): void {

    Object.values(KEYS).forEach(key =>
      localStorage.removeItem(key)
    );

  }

  // ==========================
  // Generic Methods
  // ==========================

  saveData(key: string, data: any): void {

    localStorage.setItem(
      key,
      CryptoUtil.encrypt(JSON.stringify(data))
    );

  }

  getData<T>(key: string): T | null {

    const raw = localStorage.getItem(key);

    if (!raw) return null;

    try {

      const json = CryptoUtil.decrypt(raw);

      return json ? JSON.parse(json) : null;

    } catch {

      return null;

    }

  }

  removeData(key: string): void {

    localStorage.removeItem(key);

  }

  // ==========================
  // User Profile
  // ==========================

  saveUserProfile(profile: any): void {

    this.saveData(KEYS.USER_PROFILE, profile);

  }

  getUserProfile<T>(): T | null {

    return this.getData<T>(KEYS.USER_PROFILE);

  }

  // ==========================
  // Company Profile
  // ==========================

  saveCompanyProfile(profile: any): void {

    this.saveData(KEYS.COMPANY_PROFILE, profile);

  }

  getCompanyProfile<T>(): T | null {

    return this.getData<T>(KEYS.COMPANY_PROFILE);

  }


  // ==========================
  // JWT Expiration
  // ==========================

  isTokenExpired(): boolean {

    const token = this.getToken();

    if (!token) {
      return true;
    }

    try {

      const payload = JSON.parse(
        atob(token.split('.')[1])
      );

      const exp = payload.exp;

      if (!exp) {
        return false;
      }

      return Date.now() >= exp * 1000;

    } catch {

      return true;

    }

  }



}
