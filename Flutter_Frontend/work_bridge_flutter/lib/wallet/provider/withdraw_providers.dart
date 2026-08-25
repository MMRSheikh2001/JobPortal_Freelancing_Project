import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/providers.dart';
import 'package:work_bridge_flutter/wallet/data/withdraw_repository.dart';
import 'package:work_bridge_flutter/wallet/models/response/withdraw_response.dart';

// Repository Provider
final withdrawRepositoryProvider = Provider<WithdrawRepository>((ref) {
  final apiClient = ref.watch(apiClientProvider);
  return WithdrawRepository(apiClient);
});

// User Withdraws List Provider
final userWithdrawsProvider = FutureProvider.family
    .autoDispose<List<WithdrawResponseDTO>, int>((ref, userId) async {
      return ref.watch(withdrawRepositoryProvider).getUserWithdraws(userId);
    });

// Single Withdraw Item Provider
final withdrawByIdProvider = FutureProvider.family
    .autoDispose<WithdrawResponseDTO, ({int withdrawId, int userId})>((
      ref,
      params,
    ) async {
      return ref
          .watch(withdrawRepositoryProvider)
          .getWithdrawById(
            withdrawId: params.withdrawId,
            userId: params.userId,
          );
    });
