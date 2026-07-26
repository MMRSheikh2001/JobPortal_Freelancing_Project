import { Injectable } from '@angular/core';
import { environment } from '../../../../enviroments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { UserProfileRequestModel, UserProfileResponseModel } from '../models/user.profile.model';
import { Observable } from 'rxjs';
import { WorkPlaceType } from '../../../enums/work-place-type.enum';
import { Gender } from '../../../enums/gender.enum';
import { JobType } from '../../../enums/job-type.enum';

@Injectable({
  providedIn: 'root',
})
export class UserProfileService {



  private apiUrl = environment.apiUrl + 'userprofiles/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Save User Profile
  // ==========================

  save(
    userProfile: UserProfileRequestModel,
    image?: File
  ): Observable<UserProfileResponseModel> {

    const formData = new FormData();

    formData.append(
      'userprofile',
      new Blob(
        [JSON.stringify(userProfile)],
        { type: 'application/json' }
      )
    );

    if (image) {
      formData.append('image', image);
    }

    return this.http.post<UserProfileResponseModel>(
      this.apiUrl,
      formData
    );

  }

  // ==========================
  // Get All User Profiles
  // ==========================

  getAll(): Observable<UserProfileResponseModel[]> {

    return this.http.get<UserProfileResponseModel[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get User Profile By Id
  // ==========================

  getById(
    id: number
  ): Observable<UserProfileResponseModel> {

    return this.http.get<UserProfileResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Get User Profile By User Id
  // ==========================

  getByUserId(
    userId: number
  ): Observable<UserProfileResponseModel> {

    return this.http.get<UserProfileResponseModel>(
      `${this.apiUrl}user/${userId}`
    );

  }

  // ==========================
  // Update User Profile
  // ==========================

  update(
    id: number,
    userProfile: UserProfileRequestModel,
    image?: File
  ): Observable<UserProfileResponseModel> {

    const formData = new FormData();

    formData.append(
      'userprofile',
      new Blob(
        [JSON.stringify(userProfile)],
        { type: 'application/json' }
      )
    );

    if (image) {
      formData.append('image', image);
    }

    return this.http.put<UserProfileResponseModel>(
      `${this.apiUrl}${id}`,
      formData
    );

  }

  // ==========================
  // Delete User Profile
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
  // Delete Profile Image
  // ==========================

  deleteImage(
    id: number
  ): Observable<string> {

    return this.http.delete(
      `${this.apiUrl}${id}/image`,
      {
        responseType: 'text'
      }
    );

  }

  // ==========================
  // Filter User Profiles
  // ==========================

  filterUsers(
    keyword?: string,
    countryId?: number,
    divisionId?: number,
    districtId?: number,
    policeStationId?: number,
    jobType?: JobType,
    workPlaceType?: WorkPlaceType,
    gender?: Gender
  ): Observable<UserProfileResponseModel[]> {

    let params = new HttpParams();

    if (keyword) {
      params = params.set('keyword', keyword);
    }

    if (countryId) {
      params = params.set('countryId', countryId);
    }

    if (divisionId) {
      params = params.set('divisionId', divisionId);
    }

    if (districtId) {
      params = params.set('districtId', districtId);
    }

    if (policeStationId) {
      params = params.set('policeStationId', policeStationId);
    }

    if (jobType) {
      params = params.set('jobType', jobType);
    }

    if (workPlaceType) {
      params = params.set('workPlaceType', workPlaceType);
    }

    if (gender) {
      params = params.set('gender', gender);
    }

    return this.http.get<UserProfileResponseModel[]>(
      `${this.apiUrl}filter`,
      { params }
    );

  }


}
