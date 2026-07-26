import { Injectable } from '@angular/core';
import { environment } from '../../../../enviroments/environment';
import { HttpClient } from '@angular/common/http';
import { CompanyProfileRequestModel, CompanyProfileResponseModel, CompanySearchRequestDTO } from '../models/company-profile.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CompanyProfileService {




  private apiUrl =
    environment.apiUrl + 'companies/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Save Company Profile
  // ==========================

  save(
    company: CompanyProfileRequestModel,
    image?: File
  ): Observable<CompanyProfileResponseModel> {

    const formData = new FormData();

    formData.append(
      'companyprofile',
      new Blob(
        [JSON.stringify(company)],
        { type: 'application/json' }
      )
    );

    if (image) {

      formData.append(
        'image',
        image
      );

    }

    return this.http.post<CompanyProfileResponseModel>(
      this.apiUrl,
      formData
    );

  }

  // ==========================
  // Update Company Profile
  // ==========================

  update(
    id: number,
    company: CompanyProfileRequestModel,
    image?: File
  ): Observable<CompanyProfileResponseModel> {

    const formData = new FormData();

    formData.append(
      'companyprofile',
      new Blob(
        [JSON.stringify(company)],
        { type: 'application/json' }
      )
    );

    if (image) {

      formData.append(
        'image',
        image
      );

    }

    return this.http.put<CompanyProfileResponseModel>(
      `${this.apiUrl}${id}`,
      formData
    );

  }

  // ==========================
  // Get All Companies
  // ==========================

  getAll(): Observable<CompanyProfileResponseModel[]> {

    return this.http.get<CompanyProfileResponseModel[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get By Id
  // ==========================

  getById(
    id: number
  ): Observable<CompanyProfileResponseModel> {

    return this.http.get<CompanyProfileResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Get By User Id
  // ==========================

  getByUserId(
    userId: number
  ): Observable<CompanyProfileResponseModel> {

    return this.http.get<CompanyProfileResponseModel>(
      `${this.apiUrl}user/${userId}`
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
  // Delete Image
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
  // Exists By User Id
  // ==========================

  existsByUserId(
    userId: number
  ): Observable<boolean> {

    return this.http.get<boolean>(
      `${this.apiUrl}exists/user/${userId}`
    );

  }

  // ==========================
  // Count Companies
  // ==========================

  count(): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}count`
    );

  }

  // ==========================
  // Search Companies
  // ==========================

  search(
    keyword: string
  ): Observable<CompanyProfileResponseModel[]> {

    return this.http.get<CompanyProfileResponseModel[]>(
      `${this.apiUrl}search`,
      {
        params: {
          keyword
        }
      }
    );

  }

  // ==========================
  // Find By Police Station
  // ==========================

  findByPoliceStation(
    policeStationId: number
  ): Observable<CompanyProfileResponseModel[]> {

    return this.http.get<CompanyProfileResponseModel[]>(
      `${this.apiUrl}location/policestation/${policeStationId}`
    );

  }

  // ==========================
  // Find By District
  // ==========================

  findByDistrict(
    districtId: number
  ): Observable<CompanyProfileResponseModel[]> {

    return this.http.get<CompanyProfileResponseModel[]>(
      `${this.apiUrl}location/policestation/district/${districtId}`
    );

  }


  // ==========================
  // Filter Companies
  // ==========================

  filter(
    request: CompanySearchRequestDTO
  ): Observable<CompanyProfileResponseModel[]> {

    return this.http.post<CompanyProfileResponseModel[]>(
      `${this.apiUrl}filter`,
      request
    );

  }

}
