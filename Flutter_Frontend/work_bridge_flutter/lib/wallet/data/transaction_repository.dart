import 'package:dio/dio.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';
import 'package:work_bridge_flutter/wallet/models/response/transaction_response.dart';

class TransactionRepository {
  TransactionRepository(this._apiClient);

  final ApiClient _apiClient;

  Dio get _dio => _apiClient.dio;

  Future<TransactionResponseDTO> getById(int id) async {
    final response = await _dio.get(ApiConstants.transactionById(id));
    return TransactionResponseDTO.fromJson(response.data);
  }

  Future<List<TransactionResponseDTO>> getByFromUser(int userId) async {
    final response = await _dio.get(ApiConstants.transactionsFromUser(userId));
    return (response.data as List)
        .map((json) => TransactionResponseDTO.fromJson(json))
        .toList();
  }

  Future<List<TransactionResponseDTO>> getByToUser(int userId) async {
    final response = await _dio.get(ApiConstants.transactionsToUser(userId));
    return (response.data as List)
        .map((json) => TransactionResponseDTO.fromJson(json))
        .toList();
  }

  Future<List<TransactionResponseDTO>> getUserHistory(int userId) async {
    final response = await _dio.get(
      ApiConstants.userTransactionHistory(userId),
    );
    return (response.data as List)
        .map((json) => TransactionResponseDTO.fromJson(json))
        .toList();
  }
}
