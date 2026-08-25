import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/wallet/models/response/transaction_response.dart';
import 'package:work_bridge_flutter/wallet/provider/transaction_providers.dart';

class TransactionScreen extends ConsumerWidget {
  const TransactionScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final currentUser = ref.watch(currentUserProvider);
    final userId = currentUser?.userId;

    if (userId == null) {
      return Scaffold(
        appBar: AppBar(title: const Text('Transactions')),
        body: const Center(child: Text('Unable to identify the current user.')),
      );
    }

    final transactionsAsync = ref.watch(userTransactionHistoryProvider(userId));

    return Scaffold(
      appBar: AppBar(title: const Text('Transactions')),
      body: transactionsAsync.when(
        loading: () => const Center(child: CircularProgressIndicator()),
        error: (error, stack) => _buildError(context, ref, userId, error),
        data: (transactions) {
          if (transactions.isEmpty) {
            return _buildEmpty();
          }

          return _buildTransactionList(context, transactions, userId);
        },
      ),
    );
  }

  // ===========================================================================
  // TRANSACTION LIST
  // ===========================================================================

  Widget _buildTransactionList(
    BuildContext context,
    List<TransactionResponseDTO> transactions,
    int userId,
  ) {
    return RefreshIndicator(
      onRefresh: () async {
        // The provider will be invalidated by the caller if needed.
        // Keeping this screen simple and relying on Riverpod's provider.
      },
      child: ListView.separated(
        physics: const AlwaysScrollableScrollPhysics(),
        padding: const EdgeInsets.all(16),
        itemCount: transactions.length,
        separatorBuilder: (_, __) => const SizedBox(height: 10),
        itemBuilder: (context, index) {
          final transaction = transactions[index];

          return _buildTransactionCard(context, transaction, userId);
        },
      ),
    );
  }

  // ===========================================================================
  // TRANSACTION CARD
  // ===========================================================================

  Widget _buildTransactionCard(
    BuildContext context,
    TransactionResponseDTO transaction,
    int userId,
  ) {
    final isIncoming = transaction.toUserId == userId;

    final amount = transaction.amount ?? 0.0;

    final typeName = transaction.type?.name ?? 'Transaction';

    return Card(
      elevation: 0,
      margin: EdgeInsets.zero,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(12),
        side: BorderSide(color: Colors.grey.shade300),
      ),
      child: Padding(
        padding: const EdgeInsets.all(14),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildTransactionIcon(isIncoming),

            const SizedBox(width: 12),

            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Row(
                    children: [
                      Expanded(
                        child: Text(
                          _getTransactionTitle(transaction, userId),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                          style: const TextStyle(
                            fontSize: 15,
                            fontWeight: FontWeight.w600,
                          ),
                        ),
                      ),

                      const SizedBox(width: 8),

                      Text(
                        '${isIncoming ? '+' : '-'}৳${amount.toStringAsFixed(2)}',
                        style: TextStyle(
                          fontSize: 15,
                          fontWeight: FontWeight.bold,
                          color: isIncoming ? Colors.green : Colors.red,
                        ),
                      ),
                    ],
                  ),

                  const SizedBox(height: 5),

                  Text(
                    _getTransactionType(typeName),
                    style: TextStyle(fontSize: 12, color: Colors.grey.shade600),
                  ),

                  if (transaction.description != null &&
                      transaction.description!.trim().isNotEmpty) ...[
                    const SizedBox(height: 5),
                    Text(
                      transaction.description!,
                      maxLines: 2,
                      overflow: TextOverflow.ellipsis,
                      style: TextStyle(
                        fontSize: 13,
                        color: Colors.grey.shade700,
                      ),
                    ),
                  ],

                  if (transaction.createdAt != null) ...[
                    const SizedBox(height: 7),
                    Text(
                      _formatDateTime(transaction.createdAt),
                      style: TextStyle(
                        fontSize: 11,
                        color: Colors.grey.shade500,
                      ),
                    ),
                  ],
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  // ===========================================================================
  // TRANSACTION ICON
  // ===========================================================================

  Widget _buildTransactionIcon(bool isIncoming) {
    return Container(
      width: 45,
      height: 45,
      decoration: BoxDecoration(
        color: isIncoming
            ? Colors.green.withValues(alpha: 0.1)
            : Colors.red.withValues(alpha: 0.1),
        shape: BoxShape.circle,
      ),
      child: Icon(
        isIncoming ? Icons.arrow_downward : Icons.arrow_upward,
        color: isIncoming ? Colors.green : Colors.red,
        size: 22,
      ),
    );
  }

  // ===========================================================================
  // TRANSACTION TITLE
  // ===========================================================================

  String _getTransactionTitle(TransactionResponseDTO transaction, int userId) {
    if (transaction.toUserId == userId) {
      return transaction.fromUserName ?? 'Received';
    }

    if (transaction.fromUserId == userId) {
      return transaction.toUserName ?? 'Sent';
    }

    return transaction.type?.name ?? 'Transaction';
  }

  // ===========================================================================
  // TRANSACTION TYPE
  // ===========================================================================

  String _getTransactionType(String type) {
    if (type.isEmpty) {
      return 'Transaction';
    }

    // Convert enum-style names such as GIG_PAYMENT
    // into readable text.
    return type
        .replaceAll('_', ' ')
        .split(' ')
        .map(
          (word) => word.isEmpty
              ? word
              : '${word[0].toUpperCase()}${word.substring(1).toLowerCase()}',
        )
        .join(' ');
  }

  // ===========================================================================
  // DATE / TIME
  // ===========================================================================

  String _formatDateTime(DateTime? dateTime) {
    if (dateTime == null) {
      return '';
    }

    final local = dateTime.toLocal();

    final day = local.day.toString().padLeft(2, '0');
    final month = local.month.toString().padLeft(2, '0');
    final year = local.year;

    final hour = local.hour == 0
        ? 12
        : local.hour > 12
        ? local.hour - 12
        : local.hour;

    final minute = local.minute.toString().padLeft(2, '0');

    final period = local.hour >= 12 ? 'PM' : 'AM';

    return '$day/$month/$year • $hour:$minute $period';
  }

  // ===========================================================================
  // EMPTY
  // ===========================================================================

  Widget _buildEmpty() {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(
              Icons.receipt_long_outlined,
              size: 65,
              color: Colors.grey.shade400,
            ),
            const SizedBox(height: 16),
            Text(
              'No transactions yet',
              style: TextStyle(
                fontSize: 19,
                fontWeight: FontWeight.w600,
                color: Colors.grey.shade700,
              ),
            ),
            const SizedBox(height: 7),
            Text(
              'Your wallet transactions will appear here.',
              textAlign: TextAlign.center,
              style: TextStyle(fontSize: 14, color: Colors.grey.shade600),
            ),
          ],
        ),
      ),
    );
  }

  // ===========================================================================
  // ERROR
  // ===========================================================================

  Widget _buildError(
    BuildContext context,
    WidgetRef ref,
    int userId,
    Object error,
  ) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.error_outline, size: 55, color: Colors.red.shade400),
            const SizedBox(height: 15),
            const Text(
              'Could not load transactions.',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.w600),
            ),
            const SizedBox(height: 8),
            Text(
              error.toString(),
              textAlign: TextAlign.center,
              style: TextStyle(color: Colors.grey.shade600),
            ),
            const SizedBox(height: 20),
            ElevatedButton.icon(
              onPressed: () {
                ref.invalidate(userTransactionHistoryProvider(userId));
              },
              icon: const Icon(Icons.refresh),
              label: const Text('Retry'),
            ),
          ],
        ),
      ),
    );
  }
}
