import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { SkillRequestModel, SkillResponseModel } from '../models/skill.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../../enviroments/environment';

@Injectable({
  providedIn: 'root',
})
export class SkillService {



  private apiUrl = environment.apiUrl+'skills/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Save Skill
  // ==========================

  save(
    skill: SkillRequestModel
  ): Observable<SkillResponseModel> {

    return this.http.post<SkillResponseModel>(
      this.apiUrl,
      skill
    );

  }

  // ==========================
  // Get All Skills
  // ==========================

  getAll(): Observable<SkillResponseModel[]> {

    return this.http.get<SkillResponseModel[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get Skill By Id
  // ==========================

  getById(
    id: number
  ): Observable<SkillResponseModel> {

    return this.http.get<SkillResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Update Skill
  // ==========================

  update(
    id: number,
    skill: SkillRequestModel
  ): Observable<SkillResponseModel> {

    return this.http.put<SkillResponseModel>(
      `${this.apiUrl}${id}`,
      skill
    );

  }

  // ==========================
  // Delete Skill
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
  // Get Skills By Category Id
  // ==========================

  getByCategoryId(
    categoryId: number
  ): Observable<SkillResponseModel[]> {

    return this.http.get<SkillResponseModel[]>(
      `${this.apiUrl}category/${categoryId}`
    );

  }

  // ==========================
  // Get Skills By Category Name
  // ==========================

  getByCategoryName(
    categoryName: string
  ): Observable<SkillResponseModel[]> {

    return this.http.get<SkillResponseModel[]>(
      `${this.apiUrl}category/name/${categoryName}`
    );

  }



}
