import 'package:dio/dio.dart';
import 'package:work_bridge_flutter/wallet/models/response/wallet_response.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';

class WalletRepository {
  WalletRepository(this._apiClient);

  final ApiClient _apiClient;

  Dio get _dio => _apiClient.dio;

  Future<WalletResponseDTO> getById(int id) async {
    final response = await _dio.get(ApiConstants.walletById(id));
    return WalletResponseDTO.fromJson(response.data);
  }

  Future<WalletResponseDTO> getByUserId(int userId) async {
    final response = await _dio.get(ApiConstants.walletByUserId(userId));
    return WalletResponseDTO.fromJson(response.data);
  }

  Future<double> getBalance(int userId) async {
    final response = await _dio.get(ApiConstants.walletBalance(userId));
    return (response.data as num).toDouble();
  }

  Future<double> getFrozenBalance(int userId) async {
    final response = await _dio.get(ApiConstants.walletFrozenBalance(userId));
    return (response.data as num).toDouble();
  }
}