import 'package:dio/dio.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';
import 'package:work_bridge_flutter/wallet/models/response/deposit_session_response.dart';
import 'package:work_bridge_flutter/wallet/models/response/payment_response.dart';

class PaymentRepository {
  PaymentRepository(this._apiClient);

  final ApiClient _apiClient;

  Dio get _dio => _apiClient.dio;

  Future<DepositSessionResponseDTO> createDeposit({
    required int userId,
    required double amount,
  }) async {
    final response = await _dio.post(
      ApiConstants.createDeposit(userId),
      queryParameters: {'amount': amount},
    );
    return DepositSessionResponseDTO.fromJson(response.data);
  }

  Future<PaymentResponseDTO> getById(int id) async {
    final response = await _dio.get(ApiConstants.paymentById(id));
    return PaymentResponseDTO.fromJson(response.data);
  }

  Future<PaymentResponseDTO> getByGatewayTransactionId(
      String gatewayTransactionId,
      ) async {
    final response = await _dio.get(
      ApiConstants.paymentByGatewayTxnId(gatewayTransactionId),
    );
    return PaymentResponseDTO.fromJson(response.data);
  }

  Future<List<PaymentResponseDTO>> getUserPayments(int userId) async {
    final response = await _dio.get(ApiConstants.userPayments(userId));
    return (response.data as List)
        .map((json) => PaymentResponseDTO.fromJson(json))
        .toList();
  }
}