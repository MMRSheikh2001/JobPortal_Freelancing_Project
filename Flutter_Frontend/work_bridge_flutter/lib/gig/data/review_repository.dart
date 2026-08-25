import 'package:dio/dio.dart';
import 'package:work_bridge_flutter/gig/entity/request/review_request.dart';
import 'package:work_bridge_flutter/gig/entity/response/review_response.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';

class ReviewRepository {
  final ApiClient _apiClient;

  ReviewRepository(this._apiClient);

  // =====================================================
  // Create Review
  // =====================================================

  Future<ReviewResponseDTO> create(
      ReviewRequestDTO request,
      ) async {
    final response = await _apiClient.dio.post(
      'reviews/',
      data: request.toJson(),
    );

    return ReviewResponseDTO.fromJson(
      response.data,
    );
  }

  // =====================================================
  // Update Review
  // =====================================================

  Future<ReviewResponseDTO> update(
      int id,
      ReviewRequestDTO request,
      ) async {
    final response = await _apiClient.dio.put(
      'reviews/$id',
      data: request.toJson(),
    );

    return ReviewResponseDTO.fromJson(
      response.data,
    );
  }

  // =====================================================
  // Delete Review
  // =====================================================

  Future<String> delete(int id) async {
    final response = await _apiClient.dio.delete(
      'reviews/$id',
    );

    return response.data.toString();
  }

  // =====================================================
  // Get Review By ID
  // =====================================================

  Future<ReviewResponseDTO> getById(int id) async {
    final response = await _apiClient.dio.get(
      'reviews/$id',
    );

    return ReviewResponseDTO.fromJson(
      response.data,
    );
  }

  // =====================================================
  // Get Review By Gig Order ID
  // =====================================================

  Future<ReviewResponseDTO?> getByGigOrderId(
      int gigOrderId,
      ) async {
    try {
      final response = await _apiClient.dio.get(
        'reviews/gig-order/$gigOrderId',
      );

      if (response.data == null) {
        return null;
      }

      return ReviewResponseDTO.fromJson(
        response.data,
      );
    } on DioException catch (e) {
      // If your backend returns 404 when no review exists,
      // treat it as "no review".
      if (e.response?.statusCode == 404) {
        return null;
      }

      rethrow;
    }
  }

  //Exists

  Future<bool> existsByGigOrderId(int gigOrderId) async {
    final response = await _apiClient.dio.get(
      'reviews/gig-order/$gigOrderId/exists',
    );

    return response.data == true;
  }




}