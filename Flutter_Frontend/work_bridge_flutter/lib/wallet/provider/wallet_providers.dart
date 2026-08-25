import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/providers.dart';
import 'package:work_bridge_flutter/wallet/data/wallet_repository.dart';
import 'package:work_bridge_flutter/wallet/models/response/wallet_response.dart';

// Repository Provider
final walletRepositoryProvider = Provider<WalletRepository>((ref) {
  final apiClient = ref.watch(apiClientProvider);
  return WalletRepository(apiClient);
});

// Wallet Response by User ID Provider
final walletByUserIdProvider = FutureProvider.family
    .autoDispose<WalletResponseDTO, int>((ref, userId) async {
  return ref.watch(walletRepositoryProvider).getByUserId(userId);
});

// Balance Provider
final walletBalanceProvider = FutureProvider.family
    .autoDispose<double, int>((ref, userId) async {
  return ref.watch(walletRepositoryProvider).getBalance(userId);
});

// Frozen Balance Provider
final walletFrozenBalanceProvider = FutureProvider.family
    .autoDispose<double, int>((ref, userId) async {
  return ref.watch(walletRepositoryProvider).getFrozenBalance(userId);
});