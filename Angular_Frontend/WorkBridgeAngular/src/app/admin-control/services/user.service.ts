import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { UserResponseDTO, UserSearchRequestDTO } from '../models/user.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../enviroments/environment';

@Injectable({
  providedIn: 'root',
})
export class UserService {



  //=====================================
  // API
  //=====================================

  private apiUrl =
    environment.apiUrl + 'users/';

  //=====================================
  // Constructor
  //=====================================

  constructor(
    private http: HttpClient
  ) { }

  //=====================================
  // Get All Users
  //=====================================

  getAll(): Observable<UserResponseDTO[]> {

    return this.http.get<UserResponseDTO[]>(
      this.apiUrl
    );

  }

  //=====================================
  // Get User By Id
  //=====================================

  getById(
    id: number
  ): Observable<UserResponseDTO> {

    return this.http.get<UserResponseDTO>(
      `${this.apiUrl}${id}`
    );

  }

  //=====================================
  // Delete User
  //=====================================

  delete(
    id: number
  ): Observable<string> {

    return this.http.delete(
      `${this.apiUrl}${id}`,
      {
        responseType: 'text'
      }
    );

  }

  //=====================================
  // Suspend / Unsuspend User
  //=====================================

  toggleSuspendStatus(
    id: number
  ): Observable<UserResponseDTO> {

    return this.http.patch<UserResponseDTO>(
      `${this.apiUrl}${id}/toggle-suspend-status`,
      {}
    );

  }


  //=====================================
  // Filter Users
  //=====================================

  filter(
    request: UserSearchRequestDTO
  ): Observable<UserResponseDTO[]> {

    return this.http.post<UserResponseDTO[]>(
      `${this.apiUrl}filter`,
      request
    );

  }


}
