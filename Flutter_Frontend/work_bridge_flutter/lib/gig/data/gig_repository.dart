import 'package:dio/dio.dart';
import 'package:work_bridge_flutter/gig/entity/request/gig_search_request.dart';
import 'package:work_bridge_flutter/gig/entity/response/gig_order_response.dart';
import 'package:work_bridge_flutter/gig/entity/response/gig_response.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';

class GigRepository {
  GigRepository(this._apiClient);

  final ApiClient _apiClient;

  Dio get _dio => _apiClient.dio;

  // =====================================================
  // Gigs
  // =====================================================

  Future<List<GigResponseDTO>> searchGigs(GigSearchRequestDTO request) async {
    final response = await _dio.post(
      ApiConstants.searchGigs,
      data: request.toJson(),
    );

    return (response.data as List)
        .map((json) => GigResponseDTO.fromJson(json))
        .toList();
  }

  Future<GigResponseDTO> getGigById(int id) async {
    final response = await _dio.get(ApiConstants.getGigUrl(id));

    return GigResponseDTO.fromJson(response.data);
  }

  // =====================================================
  // Gig Orders
  // =====================================================

  Future<GigOrderResponseDTO> placeOrder(int gigId, int buyerId) async {
    final response = await _dio.post(
      ApiConstants.placeGigOrder(gigId, buyerId),
    );

    return GigOrderResponseDTO.fromJson(response.data);
  }

  Future<GigOrderResponseDTO> acceptQuote(int orderId) async {
    final response = await _dio.patch(ApiConstants.acceptGigQuote(orderId));

    return GigOrderResponseDTO.fromJson(response.data);
  }

  Future<GigOrderResponseDTO> rejectQuote(int orderId) async {
    final response = await _dio.patch(ApiConstants.rejectGigQuote(orderId));

    return GigOrderResponseDTO.fromJson(response.data);
  }

  Future<GigOrderResponseDTO> acceptDelivery(int orderId) async {
    final response = await _dio.patch(ApiConstants.acceptGigDelivery(orderId));

    return GigOrderResponseDTO.fromJson(response.data);
  }

  Future<GigOrderResponseDTO> rejectDelivery(int orderId) async {
    final response = await _dio.patch(ApiConstants.rejectGigDelivery(orderId));

    return GigOrderResponseDTO.fromJson(response.data);
  }

  Future<GigOrderResponseDTO> buyerCancelOrder(int orderId) async {
    final response = await _dio.patch(
      ApiConstants.buyerCancelGigOrder(orderId),
    );

    return GigOrderResponseDTO.fromJson(response.data);
  }

  Future<List<GigOrderResponseDTO>> getBuyerOrders(int buyerId) async {
    final response = await _dio.get(ApiConstants.buyerGigOrders(buyerId));

    return (response.data as List)
        .map((json) => GigOrderResponseDTO.fromJson(json))
        .toList();
  }

  Future<List<GigOrderResponseDTO>> getBuyerOrdersByStatus(
    int buyerId,
    String status,
  ) async {
    final response = await _dio.get(
      ApiConstants.buyerGigOrdersByStatus(buyerId, status),
    );

    return (response.data as List)
        .map((json) => GigOrderResponseDTO.fromJson(json))
        .toList();
  }

  Future<int> countBuyerOrdersByStatus(int buyerId, String status) async {
    final response = await _dio.get(
      ApiConstants.buyerGigOrderCountByStatus(buyerId, status),
    );

    return (response.data as num).toInt();
  }

  Future<int> countBuyerOrders(int buyerId) async {
    final response = await _dio.get(ApiConstants.buyerGigOrderCount(buyerId));

    return (response.data as num).toInt();
  }

  Future<bool> existsGigOrder(int gigId, int buyerId) async {
    final response = await _dio.get(
      ApiConstants.gigOrderExists(gigId, buyerId),
    );

    return response.data as bool;
  }

  Future<GigOrderResponseDTO> findActiveOrder(int gigId, int buyerId) async {
    final response = await _dio.get(
      ApiConstants.activeGigOrder(gigId, buyerId),
    );

    return GigOrderResponseDTO.fromJson(response.data);
  }

  Future<GigOrderResponseDTO> getOrderById(int orderId) async {
    final response = await _dio.get(ApiConstants.gigOrderById(orderId));

    return GigOrderResponseDTO.fromJson(response.data);
  }
}
