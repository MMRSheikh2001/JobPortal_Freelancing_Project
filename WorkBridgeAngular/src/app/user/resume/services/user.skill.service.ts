import { Injectable } from '@angular/core';
import { environment } from '../../../../enviroments/environment';
import { HttpClient } from '@angular/common/http';
import { UserSkillRequestModel, UserSkillResponseModel } from '../models/user-skill.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserSkillService {



  private apiUrl = environment.apiUrl + 'userskills/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Save User Skill
  // ==========================

  save(
    userSkill: UserSkillRequestModel
  ): Observable<UserSkillResponseModel> {

    return this.http.post<UserSkillResponseModel>(
      this.apiUrl,
      userSkill
    );

  }

  // ==========================
  // Get All User Skills
  // ==========================

  getAll(): Observable<UserSkillResponseModel[]> {

    return this.http.get<UserSkillResponseModel[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get User Skill By Id
  // ==========================

  getById(
    id: number
  ): Observable<UserSkillResponseModel> {

    return this.http.get<UserSkillResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Update User Skill
  // ==========================

  update(
    id: number,
    userSkill: UserSkillRequestModel
  ): Observable<UserSkillResponseModel> {

    return this.http.put<UserSkillResponseModel>(
      `${this.apiUrl}${id}`,
      userSkill
    );

  }

  // ==========================
  // Delete User Skill
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
  // Get By User Profile Id
  // ==========================

  getByUserProfileId(
    userProfileId: number
  ): Observable<UserSkillResponseModel[]> {

    return this.http.get<UserSkillResponseModel[]>(
      `${this.apiUrl}userprofile/${userProfileId}`
    );

  }

  // ==========================
  // Get By Skill Id
  // ==========================

  getBySkillId(
    skillId: number
  ): Observable<UserSkillResponseModel[]> {

    return this.http.get<UserSkillResponseModel[]>(
      `${this.apiUrl}skill/${skillId}`
    );

  }

  // ==========================
  // Get By Category Id
  // ==========================

  getByCategoryId(
    categoryId: number
  ): Observable<UserSkillResponseModel[]> {

    return this.http.get<UserSkillResponseModel[]>(
      `${this.apiUrl}skill/category/${categoryId}`
    );

  }

  // ==========================
  // Get One User Skill
  // ==========================

  getByUserProfileIdAndSkillId(
    userProfileId: number,
    skillId: number
  ): Observable<UserSkillResponseModel> {

    return this.http.get<UserSkillResponseModel>(
      `${this.apiUrl}userprofile/${userProfileId}/skill/${skillId}`
    );

  }

  // ==========================
  // Delete User Skill
  // ==========================

  deleteByUserProfileIdAndSkillId(
    userProfileId: number,
    skillId: number
  ): Observable<string> {

    return this.http.delete(
      `${this.apiUrl}userprofile/${userProfileId}/skill/${skillId}`,
      {
        responseType: 'text'
      }
    );

  }

  // ==========================
  // Count User Skills
  // ==========================

  countSkillsByUserProfileId(
    userProfileId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}userprofile/count/${userProfileId}`
    );

  }



}
