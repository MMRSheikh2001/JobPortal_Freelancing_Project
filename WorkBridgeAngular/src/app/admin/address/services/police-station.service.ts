import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PoliceStationRequestModel, PoliceStationResponseModel } from '../models/police-station.model';
import { Observable } from 'rxjs';
import { environment } from '../../../../enviroments/environment';

@Injectable({
  providedIn: 'root',
})
export class PoliceStationService {



  private apiUrl = environment.apiUrl+'policestations/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Save Police Station
  // ==========================

  save(
    policeStation: PoliceStationRequestModel
  ): Observable<PoliceStationResponseModel> {

    return this.http.post<PoliceStationResponseModel>(
      this.apiUrl,
      policeStation
    );

  }

  // ==========================
  // Get All Police Stations
  // ==========================

  getAll(): Observable<PoliceStationResponseModel[]> {

    return this.http.get<PoliceStationResponseModel[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get Police Station By Id
  // ==========================

  getById(
    id: number
  ): Observable<PoliceStationResponseModel> {

    return this.http.get<PoliceStationResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Update Police Station
  // ==========================

  update(
    id: number,
    policeStation: PoliceStationRequestModel
  ): Observable<PoliceStationResponseModel> {

    return this.http.put<PoliceStationResponseModel>(
      `${this.apiUrl}${id}`,
      policeStation
    );

  }

  // ==========================
  // Delete Police Station
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
  // Get Police Stations By District Id
  // ==========================

  getByDistrictId(
    districtId: number
  ): Observable<PoliceStationResponseModel[]> {

    return this.http.get<PoliceStationResponseModel[]>(
      `${this.apiUrl}district/${districtId}`
    );

  }

  // ==========================
  // Get Police Stations By District Name
  // ==========================

  getByDistrictName(
    districtName: string
  ): Observable<PoliceStationResponseModel[]> {

    return this.http.get<PoliceStationResponseModel[]>(
      `${this.apiUrl}district/name/${districtName}`
    );

  }



}
