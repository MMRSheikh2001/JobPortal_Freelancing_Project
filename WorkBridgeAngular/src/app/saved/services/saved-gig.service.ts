import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SavedGigResponseDTO } from '../models/saved-gig.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../enviroments/environment';

@Injectable({
  providedIn: 'root',
})
export class SavedGigService {




  private apiUrl =
    environment.apiUrl + 'savedgigs/';

  constructor(
    private http: HttpClient
  ) { }

  // =====================================
  // Save Gig
  // =====================================

  saveGig(
    userId: number,
    gigId: number
  ): Observable<SavedGigResponseDTO> {

    return this.http.post<SavedGigResponseDTO>(
      `${this.apiUrl}?userId=${userId}&gigId=${gigId}`,
      {}
    );

  }

  // =====================================
  // Unsave Gig
  // =====================================

  unsaveGig(
    userId: number,
    gigId: number
  ): Observable<void> {

    return this.http.delete<void>(
      `${this.apiUrl}?userId=${userId}&gigId=${gigId}`
    );

  }

  // =====================================
  // Get Saved Gigs
  // =====================================

  getSavedGigs(
    userId: number
  ): Observable<SavedGigResponseDTO[]> {

    return this.http.get<SavedGigResponseDTO[]>(
      `${this.apiUrl}${userId}`
    );

  }

  // =====================================
  // Check Saved
  // =====================================

  isGigSaved(
    userId: number,
    gigId: number
  ): Observable<boolean> {

    return this.http.get<boolean>(
      `${this.apiUrl}check?userId=${userId}&gigId=${gigId}`
    );

  }

  // =====================================
  // Count Saved Gigs
  // =====================================

  countByUserId(
    userId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}${userId}/count`
    );

  }



}
