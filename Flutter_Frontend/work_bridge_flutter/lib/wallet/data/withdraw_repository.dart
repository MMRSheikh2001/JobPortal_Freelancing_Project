import 'package:dio/dio.dart';
import 'package:work_bridge_flutter/wallet/models/request/withdraw_request.dart';
import 'package:work_bridge_flutter/wallet/models/response/withdraw_response.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';

class WithdrawRepository {
  WithdrawRepository(this._apiClient);

  final ApiClient _apiClient;

  Dio get _dio => _apiClient.dio;

  Future<WithdrawResponseDTO> createWithdraw(WithdrawRequestDTO request) async {
    final response = await _dio.post(
      ApiConstants.createWithdraw,
      data: request.toJson(),
    );
    return WithdrawResponseDTO.fromJson(response.data);
  }

  Future<List<WithdrawResponseDTO>> getUserWithdraws(int userId) async {
    final response = await _dio.get(ApiConstants.userWithdraws(userId));
    return (response.data as List)
        .map((json) => WithdrawResponseDTO.fromJson(json))
        .toList();
  }

  Future<WithdrawResponseDTO> getWithdrawById({
    required int withdrawId,
    required int userId,
  }) async {
    final response = await _dio.get(
      ApiConstants.withdrawById(withdrawId, userId),
    );
    return WithdrawResponseDTO.fromJson(response.data);
  }
}
