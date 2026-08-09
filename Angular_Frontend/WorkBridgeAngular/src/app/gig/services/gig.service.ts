import { Injectable } from '@angular/core';
import { GigRequestModel, GigResponseModel, GigSearchRequestModel } from '../models/gig.model';
import { environment } from '../../../enviroments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class GigService {





  private apiUrl = `${environment.apiUrl}gigs/`;

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Save
  // ==========================

  save(
    gig: GigRequestModel,
    image?: File
  ): Observable<GigResponseModel> {

    const formData = new FormData();

    formData.append(
      'gig',
      new Blob(
        [JSON.stringify(gig)],
        { type: 'application/json' }
      )
    );

    if (image) {
      formData.append('image', image);
    }

    return this.http.post<GigResponseModel>(
      this.apiUrl,
      formData
    );

  }

  // ==========================
  // Find All
  // ==========================

  getAll(): Observable<GigResponseModel[]> {

    return this.http.get<GigResponseModel[]>(this.apiUrl);

  }

  // ==========================
  // Get By Id
  // ==========================

  getById(
    id: number
  ): Observable<GigResponseModel> {

    return this.http.get<GigResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Update
  // ==========================

  update(
    id: number,
    gig: GigRequestModel,
    image?: File
  ): Observable<GigResponseModel> {

    const formData = new FormData();

    formData.append(
      'gig',
      new Blob(
        [JSON.stringify(gig)],
        { type: 'application/json' }
      )
    );

    if (image) {
      formData.append('image', image);
    }

    return this.http.put<GigResponseModel>(
      `${this.apiUrl}${id}`,
      formData
    );

  }

  // ==========================
  // Change Status
  // ==========================

  changeStatus(
    id: number
  ): Observable<GigResponseModel> {

    return this.http.patch<GigResponseModel>(
      `${this.apiUrl}${id}/change`,
      {}
    );

  }

  // ==========================
  // Delete
  // ==========================

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

  // ==========================
  // Active Gigs
  // ==========================

  getActive(): Observable<GigResponseModel[]> {

    return this.http.get<GigResponseModel[]>(
      `${this.apiUrl}active`
    );

  }

  // ==========================
  // User Gigs
  // ==========================

  getByUserProfileId(
    userProfileId: number
  ): Observable<GigResponseModel[]> {

    return this.http.get<GigResponseModel[]>(
      `${this.apiUrl}userprofile/${userProfileId}`
    );

  }

  // ==========================
  // User Gig Count
  // ==========================

  countByUserProfileId(
    userProfileId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}userprofile/${userProfileId}/count`
    );

  }

  // ==========================
  // User Active Gigs
  // ==========================

  getActiveByUserProfileId(
    userProfileId: number
  ): Observable<GigResponseModel[]> {

    return this.http.get<GigResponseModel[]>(
      `${this.apiUrl}userprofile/${userProfileId}/active`
    );

  }

  // ==========================
  // Search
  // ==========================

  search(
    request: GigSearchRequestModel
  ): Observable<GigResponseModel[]> {

    return this.http.post<GigResponseModel[]>(
      `${this.apiUrl}search`,
      request
    );

  }

  // ==========================
  // Latest Gigs
  // ==========================

  getLatest(): Observable<GigResponseModel[]> {

    return this.http.get<GigResponseModel[]>(
      `${this.apiUrl}latest`
    );

  }

  // ==========================
  // Top Rated Gigs
  // ==========================

  getTopRated(): Observable<GigResponseModel[]> {

    return this.http.get<GigResponseModel[]>(
      `${this.apiUrl}top-rated`
    );

  }

  // ==========================
  // Popular Gigs
  // ==========================

  getPopular(): Observable<GigResponseModel[]> {

    return this.http.get<GigResponseModel[]>(
      `${this.apiUrl}popular`
    );

  }

  // ==========================
  // Related Gigs
  // ==========================

  getRelated(
    gigId: number
  ): Observable<GigResponseModel[]> {

    return this.http.get<GigResponseModel[]>(
      `${this.apiUrl}${gigId}/related`
    );

  }




}
