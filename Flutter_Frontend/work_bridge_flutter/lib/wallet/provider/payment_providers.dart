import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/providers.dart';
import 'package:work_bridge_flutter/wallet/data/payment_repository.dart';
import 'package:work_bridge_flutter/wallet/models/response/payment_response.dart';

// Repository Provider
final paymentRepositoryProvider = Provider<PaymentRepository>((ref) {
  final apiClient = ref.watch(apiClientProvider);
  return PaymentRepository(apiClient);
});

// Single Payment by ID Provider
final paymentByIdProvider = FutureProvider.family
    .autoDispose<PaymentResponseDTO, int>((ref, id) async {
      return ref.watch(paymentRepositoryProvider).getById(id);
    });

// Single Payment by Gateway Transaction ID Provider
final paymentByGatewayTxnIdProvider = FutureProvider.family
    .autoDispose<PaymentResponseDTO, String>((ref, gatewayTxnId) async {
      return ref
          .watch(paymentRepositoryProvider)
          .getByGatewayTransactionId(gatewayTxnId);
    });

// User Payment History Provider
final userPaymentsProvider = FutureProvider.family
    .autoDispose<List<PaymentResponseDTO>, int>((ref, userId) async {
      return ref.watch(paymentRepositoryProvider).getUserPayments(userId);
    });
