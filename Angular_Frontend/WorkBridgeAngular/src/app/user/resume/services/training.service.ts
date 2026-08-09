import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { TrainingRequestModel, TrainingResponseModel } from '../models/training.model';
import { environment } from '../../../../enviroments/environment';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class TrainingService {



  private apiUrl = environment.apiUrl + 'trainings/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Save Training
  // ==========================

  save(
    training: TrainingRequestModel,
    file?: File
  ): Observable<TrainingResponseModel> {

    const formData = new FormData();

    formData.append(
      'training',
      new Blob(
        [JSON.stringify(training)],
        { type: 'application/json' }
      )
    );

    if (file) {
      formData.append('file', file);
    }

    return this.http.post<TrainingResponseModel>(
      this.apiUrl,
      formData
    );

  }

  // ==========================
  // Get All Trainings
  // ==========================

  getAll(): Observable<TrainingResponseModel[]> {

    return this.http.get<TrainingResponseModel[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get Training By Id
  // ==========================

  getById(
    id: number
  ): Observable<TrainingResponseModel> {

    return this.http.get<TrainingResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Update Training
  // ==========================

  update(
    id: number,
    training: TrainingRequestModel,
    file?: File
  ): Observable<TrainingResponseModel> {

    const formData = new FormData();

    formData.append(
      'training',
      new Blob(
        [JSON.stringify(training)],
        { type: 'application/json' }
      )
    );

    if (file) {
      formData.append('file', file);
    }

    return this.http.put<TrainingResponseModel>(
      `${this.apiUrl}${id}`,
      formData
    );

  }

  // ==========================
  // Delete Training
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
  // Delete Certificate File
  // ==========================

  deleteFile(
    id: number
  ): Observable<string> {

    return this.http.delete(
      `${this.apiUrl}${id}/file`,
      {
        responseType: 'text'
      }
    );

  }

  // ==========================
  // Get Trainings By User Profile Id
  // ==========================

  getByUserProfileId(
    userProfileId: number
  ): Observable<TrainingResponseModel[]> {

    return this.http.get<TrainingResponseModel[]>(
      `${this.apiUrl}userprofile/${userProfileId}`
    );

  }

  // ==========================
  // Count Trainings By User Profile Id
  // ==========================

  countByUserProfileId(
    userProfileId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}count/userprofile/${userProfileId}`
    );

  }


}
