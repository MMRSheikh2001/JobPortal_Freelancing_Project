import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ReferenceRequestModel, ReferenceResponseModel } from '../models/reference.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../enviroments/environment';

@Injectable({
  providedIn: 'root',
})
export class ReferenceService {



  private apiUrl = environment.apiUrl + 'references/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Save Reference
  // ==========================

  save(
    reference: ReferenceRequestModel
  ): Observable<ReferenceResponseModel> {

    return this.http.post<ReferenceResponseModel>(
      this.apiUrl,
      reference
    );

  }

  // ==========================
  // Get All References
  // ==========================

  getAll(): Observable<ReferenceResponseModel[]> {

    return this.http.get<ReferenceResponseModel[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get Reference By Id
  // ==========================

  getById(
    id: number
  ): Observable<ReferenceResponseModel> {

    return this.http.get<ReferenceResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Update Reference
  // ==========================

  update(
    id: number,
    reference: ReferenceRequestModel
  ): Observable<ReferenceResponseModel> {

    return this.http.put<ReferenceResponseModel>(
      `${this.apiUrl}${id}`,
      reference
    );

  }

  // ==========================
  // Delete Reference
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
  // Get References By User Profile Id
  // ==========================

  getByUserProfileId(
    userProfileId: number
  ): Observable<ReferenceResponseModel[]> {

    return this.http.get<ReferenceResponseModel[]>(
      `${this.apiUrl}userprofile/${userProfileId}`
    );

  }

  // ==========================
  // Count References By User Profile Id
  // ==========================

  countByUserProfileId(
    userProfileId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}userprofile/count/${userProfileId}`
    );

  }

}
