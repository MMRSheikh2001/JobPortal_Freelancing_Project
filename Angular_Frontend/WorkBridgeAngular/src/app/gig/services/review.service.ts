import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ReviewRequestDTO, ReviewResponseModel } from '../models/review.model';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../enviroments/environment';

@Injectable({
  providedIn: 'root',
})
export class ReviewService {




  private apiUrl =
    `${environment.apiUrl}reviews/`;

  constructor(
    private http: HttpClient
  ) { }

  // =====================================
  // Create Review
  // =====================================

  create(
    request: ReviewRequestDTO
  ): Observable<ReviewResponseModel> {

    return this.http.post<ReviewResponseModel>(
      this.apiUrl,
      request
    );

  }

  // =====================================
  // Get All
  // =====================================

  getAll(): Observable<ReviewResponseModel[]> {

    return this.http.get<ReviewResponseModel[]>(
      this.apiUrl
    );

  }

  // =====================================
  // Get By Id
  // =====================================

  getById(
    id: number
  ): Observable<ReviewResponseModel> {

    return this.http.get<ReviewResponseModel>(
      `${this.apiUrl}${id}`
    );

  }

  // =====================================
  // Update
  // =====================================

  update(
    id: number,
    request: ReviewRequestDTO
  ): Observable<ReviewResponseModel> {

    return this.http.put<ReviewResponseModel>(
      `${this.apiUrl}${id}`,
      request
    );

  }

  // =====================================
  // Delete
  // =====================================

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

  // =====================================
  // Find By Gig Order
  // =====================================

  getByGigOrderId(
    gigOrderId: number
  ): Observable<ReviewResponseModel> {

    return this.http.get<ReviewResponseModel>(
      `${this.apiUrl}gig-order/${gigOrderId}`
    );

  }

  // =====================================
  // Seller Reviews
  // =====================================

  getSellerReviews(
    sellerUserProfileId: number
  ): Observable<ReviewResponseModel[]> {

    return this.http.get<ReviewResponseModel[]>(
      `${this.apiUrl}seller/${sellerUserProfileId}`
    );

  }

  // =====================================
  // Buyer Reviews
  // =====================================

  getBuyerReviews(
    buyerId: number
  ): Observable<ReviewResponseModel[]> {

    return this.http.get<ReviewResponseModel[]>(
      `${this.apiUrl}buyer/${buyerId}`
    );

  }

  // =====================================
  // Gig Reviews
  // =====================================

  getGigReviews(
    gigId: number
  ): Observable<ReviewResponseModel[]> {

    return this.http.get<ReviewResponseModel[]>(
      `${this.apiUrl}gig/${gigId}`
    );

  }

  // =====================================
  // Reviews By Rating
  // =====================================

  getByRating(
    rating: number
  ): Observable<ReviewResponseModel[]> {

    return this.http.get<ReviewResponseModel[]>(
      `${this.apiUrl}rating/${rating}`
    );

  }

  // =====================================
  // Seller Review Count
  // =====================================

  countSellerReviews(
    sellerUserProfileId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}seller/${sellerUserProfileId}/count`
    );

  }

  // =====================================
  // Gig Review Count
  // =====================================

  countGigReviews(
    gigId: number
  ): Observable<number> {

    return this.http.get<number>(
      `${this.apiUrl}gig/${gigId}/count`
    );

  }

  // =====================================
  // Exists By Gig Order
  // =====================================

  existsByGigOrderId(
    gigOrderId: number
  ): Observable<boolean> {

    return this.http.get<boolean>(
      `${this.apiUrl}gig-order/${gigOrderId}/exists`
    );

  }


}
