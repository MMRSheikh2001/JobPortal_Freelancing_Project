import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CategoryRequestModel, CategoryResponseModel } from '../models/category.model';
import { environment } from '../../../../enviroments/environment';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {


  private apiUrl = environment.apiUrl+'categories/';

  constructor(
    private http: HttpClient
  ) { }

  // ==========================
  // Save Category
  // ==========================

  save(
    category: CategoryRequestModel
  ): Observable<CategoryResponseModel> {

    return this.http.post<CategoryResponseModel>(
      this.apiUrl,
      category
    );

  }

  // ==========================
  // Get All Categories
  // ==========================

  getAll(): Observable<CategoryResponseModel[]> {

    return this.http.get<CategoryResponseModel[]>(
      this.apiUrl
    );

  }

  // ==========================
  // Get Category By Id
  // ==========================

  getById(
    id: number
  ): Observable<CategoryResponseModel> {

    return this.http.get<CategoryResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // ==========================
  // Update Category
  // ==========================

  update(
    id: number,
    category: CategoryRequestModel
  ): Observable<CategoryResponseModel> {

    return this.http.put<CategoryResponseModel>(
      `${this.apiUrl}${id}`,
      category
    );

  }

  // ==========================
  // Delete Category
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


}
