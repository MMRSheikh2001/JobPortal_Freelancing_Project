import { Injectable } from '@angular/core';
import { NotificationFilterDTO, NotificationResponseDTO } from '../models/notification.model';
import { Observable } from 'rxjs';
import { NotificationType } from '../../enums/notification-type.enum';
import { environment } from '../../../enviroments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class NotificationService {




  private apiUrl =
    environment.apiUrl + 'notifications/';

  constructor(
    private http: HttpClient
  ) { }

  // =====================================
  // User Notifications
  // =====================================

  getUserNotifications(
    userId: number
  ): Observable<NotificationResponseDTO[]> {

    return this.http.get<NotificationResponseDTO[]>(
      `${this.apiUrl}user/${userId}`
    );

  }


  // =====================================
  // Get By Id
  // =====================================

  getById(
    notificationId: number
  ): Observable<NotificationResponseDTO> {

    return this.http.get<NotificationResponseDTO>(
      `${this.apiUrl}${notificationId}`
    );

  }

  // =====================================
  // Unread Notifications
  // =====================================

  getUnreadNotifications(
    userId: number
  ): Observable<NotificationResponseDTO[]> {

    return this.http.get<NotificationResponseDTO[]>(
      `${this.apiUrl}user/${userId}/unread`
    );

  }

  // =====================================
  // Unread Count
  // =====================================

  getUnreadCount(
    userId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}user/${userId}/count`
    );

  }

  // =====================================
  // Mark One As Read
  // =====================================

  markAsRead(
    notificationId: number,
    userId: number
  ): Observable<NotificationResponseDTO> {

    return this.http.put<NotificationResponseDTO>(
      `${this.apiUrl}${notificationId}/read?userId=${userId}`,
      {}
    );

  }

  // =====================================
  // Mark All As Read
  // =====================================

  markAllAsRead(
    userId: number
  ): Observable<void> {

    return this.http.put<void>(
      `${this.apiUrl}user/${userId}/read-all`,
      {}
    );

  }

  // =====================================
  // Filter By Type
  // =====================================

  getByType(
    userId: number,
    type: NotificationType
  ): Observable<NotificationResponseDTO[]> {

    return this.http.get<NotificationResponseDTO[]>(
      `${this.apiUrl}user/${userId}/type/${type}`
    );

  }

  // =====================================
  // Admin - All Notifications
  // =====================================

  getAllNotifications(): Observable<NotificationResponseDTO[]> {

    return this.http.get<NotificationResponseDTO[]>(
      `${this.apiUrl}admin`
    );

  }

  // =====================================
  // Delete One
  // =====================================

  delete(
    notificationId: number,
    userId: number
  ): Observable<void> {

    return this.http.delete<void>(
      `${this.apiUrl}${notificationId}/clear?userId=${userId}`
    );

  }

  // =====================================
  // Delete All
  // =====================================

  deleteAll(
    userId: number
  ): Observable<void> {

    return this.http.delete<void>(
      `${this.apiUrl}user/${userId}/clear`
    );

  }

  // =====================================
  // Search
  // =====================================

  search(
    filter: NotificationFilterDTO
  ): Observable<NotificationResponseDTO[]> {

    return this.http.post<NotificationResponseDTO[]>(
      `${this.apiUrl}search`,
      filter
    );

  }


}
