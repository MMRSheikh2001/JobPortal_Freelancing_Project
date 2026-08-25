import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/providers.dart';
import 'package:work_bridge_flutter/wallet/data/transaction_repository.dart';
import 'package:work_bridge_flutter/wallet/models/response/transaction_response.dart';

// Repository Provider
final transactionRepositoryProvider = Provider<TransactionRepository>((ref) {
  final apiClient = ref.watch(apiClientProvider);
  return TransactionRepository(apiClient);
});

// Single Transaction Provider
final transactionByIdProvider = FutureProvider.family
    .autoDispose<TransactionResponseDTO, int>((ref, id) async {
      return ref.watch(transactionRepositoryProvider).getById(id);
    });

// Outgoing Transactions Provider
final transactionsFromUserProvider = FutureProvider.family
    .autoDispose<List<TransactionResponseDTO>, int>((ref, userId) async {
      return ref.watch(transactionRepositoryProvider).getByFromUser(userId);
    });

// Incoming Transactions Provider
final transactionsToUserProvider = FutureProvider.family
    .autoDispose<List<TransactionResponseDTO>, int>((ref, userId) async {
      return ref.watch(transactionRepositoryProvider).getByToUser(userId);
    });

// Transaction History Provider
final userTransactionHistoryProvider = FutureProvider.family
    .autoDispose<List<TransactionResponseDTO>, int>((ref, userId) async {
      return ref.watch(transactionRepositoryProvider).getUserHistory(userId);
    });
