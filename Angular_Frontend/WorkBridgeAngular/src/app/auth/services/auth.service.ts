import { Injectable } from '@angular/core';
import { environment } from '../../../enviroments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { LoginRequestModel } from '../models/login-request.model';
import { LoginResponseModel } from '../models/login-response.model';
import { RegisterRequestModel } from '../models/register.model';
import { ForgotPasswordRequestModel } from '../models/forgot-password-request.model';
import { ResetPasswordRequestModel } from '../models/reset-password-request.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {


  private api = environment.apiUrl;

  constructor(
    private http: HttpClient
  ) { }

  // ---------------- Register ----------------

  register(data: RegisterRequestModel): Observable<any> {
    return this.http.post(
      `${this.api}users/register`,
      data
    );
  }

  // ---------------- Login ----------------

  login(data: LoginRequestModel): Observable<LoginResponseModel> {
    return this.http.post<LoginResponseModel>(
      `${this.api}auth/login`,
      data
    );
  }

  // ---------------- Verify Email ----------------

  verifyEmail(token: string): Observable<string> {
    return this.http.get(
      `${this.api}auth/verifyemail?token=${token}`,
      { responseType: 'text' }
    );
  }

  // ---------------- Forgot Password ----------------

  forgotPassword(data: ForgotPasswordRequestModel): Observable<string> {
    return this.http.post(
      `${this.api}auth/forgot-password`,
      data,
      { responseType: 'text' }
    );
  }

  // ---------------- Reset Password ----------------

  resetPassword(data: ResetPasswordRequestModel): Observable<string> {
    return this.http.post(
      `${this.api}auth/reset-password`,
      data,
      { responseType: 'text' }
    );
  }

  

}
