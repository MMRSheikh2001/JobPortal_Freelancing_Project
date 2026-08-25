import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/enums/payment_status.dart';
import 'package:work_bridge_flutter/wallet/models/response/deposit_session_response.dart';
import 'package:work_bridge_flutter/wallet/models/response/payment_response.dart';
import 'package:work_bridge_flutter/wallet/provider/payment_providers.dart';

class DepositScreen extends ConsumerStatefulWidget {
  const DepositScreen({super.key});

  @override
  ConsumerState<DepositScreen> createState() => _DepositScreenState();
}

class _DepositScreenState extends ConsumerState<DepositScreen> {
  final TextEditingController _amountController = TextEditingController();

  bool _creatingDeposit = false;

  @override
  void dispose() {
    _amountController.dispose();
    super.dispose();
  }

  // ===========================================================================
  // CREATE DEPOSIT
  // ===========================================================================

  Future<void> _createDeposit() async {
    final amountText = _amountController.text.trim();

    if (amountText.isEmpty) {
      _showMessage('Please enter an amount.');
      return;
    }

    final amount = double.tryParse(amountText);

    if (amount == null || amount <= 0) {
      _showMessage('Please enter a valid amount greater than 0.');
      return;
    }

    final userId = ref.read(currentUserProvider)?.userId;

    if (userId == null) {
      _showMessage('Unable to identify the current user.');
      return;
    }

    setState(() {
      _creatingDeposit = true;
    });

    try {
      final session = await ref
          .read(paymentRepositoryProvider)
          .createDeposit(userId: userId, amount: amount);

      if (!mounted) return;

      if (session.gatewayPageUrl == null || session.gatewayPageUrl!.isEmpty) {
        _showMessage('Payment gateway URL was not returned.');
        return;
      }

      _amountController.clear();

      await _openPaymentGateway(session);

      // Refresh payment history after returning from the gateway.
      if (mounted) {
        ref.invalidate(userPaymentsProvider(userId));
      }
    } catch (e) {
      if (!mounted) return;

      _showMessage('Could not create deposit: ${_cleanError(e)}');
    } finally {
      if (mounted) {
        setState(() {
          _creatingDeposit = false;
        });
      }
    }
  }

  // ===========================================================================
  // OPEN SSL COMMERZ
  // ===========================================================================

  Future<void> _openPaymentGateway(DepositSessionResponseDTO session) async {
    final urlString = session.gatewayPageUrl;

    if (urlString == null || urlString.isEmpty) {
      _showMessage('Payment gateway URL is unavailable.');
      return;
    }

    final uri = Uri.tryParse(urlString);

    if (uri == null) {
      _showMessage('Invalid payment gateway URL.');
      return;
    }

    try {
      final launched = await launchUrl(
        uri,
        mode: LaunchMode.externalApplication,
      );

      if (!launched && mounted) {
        _showMessage('Could not open SSLCommerz payment page.');
      }
    } catch (e) {
      if (!mounted) return;

      _showMessage('Could not open payment gateway.');
    }
  }

  // ===========================================================================
  // PAYMENT ITEM
  // ===========================================================================

  Widget _buildPaymentItem(PaymentResponseDTO payment) {
    final status = payment.paymentStatus;

    return Card(
      margin: const EdgeInsets.only(bottom: 10),
      elevation: 1,
      child: Padding(
        padding: const EdgeInsets.all(14),
        child: Column(
          children: [
            Row(
              children: [
                CircleAvatar(
                  radius: 22,
                  backgroundColor: _getStatusColor(
                    status,
                  ).withValues(alpha: 0.12),
                  child: Icon(
                    _getStatusIcon(status),
                    color: _getStatusColor(status),
                  ),
                ),
                const SizedBox(width: 12),
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      const Text(
                        'Deposit',
                        style: TextStyle(
                          fontSize: 15,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                      const SizedBox(height: 3),
                      Text(
                        _formatDate(payment.createdAt),
                        style: TextStyle(
                          fontSize: 12,
                          color: Colors.grey.shade600,
                        ),
                      ),
                    ],
                  ),
                ),
                Column(
                  crossAxisAlignment: CrossAxisAlignment.end,
                  children: [
                    Text(
                      '+৳${(payment.amount ?? 0).toStringAsFixed(2)}',
                      style: const TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 4),
                    _buildStatusBadge(status),
                  ],
                ),
              ],
            ),

            if (payment.gatewayTransactionId != null) ...[
              const SizedBox(height: 12),
              const Divider(),
              Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    'Transaction ID',
                    style: TextStyle(fontSize: 12, color: Colors.grey.shade600),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: Text(
                      payment.gatewayTransactionId!,
                      textAlign: TextAlign.end,
                      style: const TextStyle(
                        fontSize: 12,
                        fontWeight: FontWeight.w500,
                      ),
                    ),
                  ),
                ],
              ),
            ],

            if (payment.paymentMethod != null) ...[
              const SizedBox(height: 8),
              Row(
                children: [
                  Text(
                    'Payment Method',
                    style: TextStyle(fontSize: 12, color: Colors.grey.shade600),
                  ),
                  const Spacer(),
                  Text(
                    payment.paymentMethod!,
                    style: const TextStyle(
                      fontSize: 12,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                ],
              ),
            ],

            if (payment.failureReason != null &&
                payment.failureReason!.isNotEmpty) ...[
              const SizedBox(height: 10),
              Container(
                width: double.infinity,
                padding: const EdgeInsets.all(10),
                decoration: BoxDecoration(
                  color: Colors.red.withValues(alpha: 0.08),
                  borderRadius: BorderRadius.circular(8),
                ),
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    const Icon(
                      Icons.error_outline,
                      size: 18,
                      color: Colors.red,
                    ),
                    const SizedBox(width: 8),
                    Expanded(
                      child: Text(
                        payment.failureReason!,
                        style: const TextStyle(fontSize: 12, color: Colors.red),
                      ),
                    ),
                  ],
                ),
              ),
            ],
          ],
        ),
      ),
    );
  }

  // ===========================================================================
  // STATUS BADGE
  // ===========================================================================

  Widget _buildStatusBadge(PaymentStatus? status) {
    final color = _getStatusColor(status);

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.12),
        borderRadius: BorderRadius.circular(20),
      ),
      child: Text(
        _getStatusText(status),
        style: TextStyle(
          color: color,
          fontSize: 11,
          fontWeight: FontWeight.w600,
        ),
      ),
    );
  }

  Color _getStatusColor(PaymentStatus? status) {
    switch (status) {
      case PaymentStatus.success:
        return Colors.green;

      case PaymentStatus.failed:
        return Colors.red;

      case PaymentStatus.cancelled:
        return Colors.orange;

      case PaymentStatus.pending:
      case null:
        return Colors.blue;
    }
  }

  IconData _getStatusIcon(PaymentStatus? status) {
    switch (status) {
      case PaymentStatus.success:
        return Icons.check_circle_outline;

      case PaymentStatus.failed:
        return Icons.error_outline;

      case PaymentStatus.cancelled:
        return Icons.cancel_outlined;

      case PaymentStatus.pending:
      case null:
        return Icons.hourglass_empty;
    }
  }

  String _getStatusText(PaymentStatus? status) {
    switch (status) {
      case PaymentStatus.success:
        return 'SUCCESS';

      case PaymentStatus.failed:
        return 'FAILED';

      case PaymentStatus.cancelled:
        return 'CANCELLED';

      case PaymentStatus.pending:
      case null:
        return 'PENDING';
    }
  }

  // ===========================================================================
  // FORM
  // ===========================================================================

  Widget _buildDepositForm() {
    return Card(
      elevation: 1,
      child: Padding(
        padding: const EdgeInsets.all(18),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Text(
              'Add Money',
              style: TextStyle(fontSize: 19, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 6),
            Text(
              'Enter the amount you want to add to your wallet.',
              style: TextStyle(color: Colors.grey.shade600, fontSize: 13),
            ),
            const SizedBox(height: 18),

            TextField(
              controller: _amountController,
              keyboardType: const TextInputType.numberWithOptions(
                decimal: true,
              ),
              enabled: !_creatingDeposit,
              decoration: InputDecoration(
                labelText: 'Amount',
                hintText: 'Enter amount',
                prefixText: '৳ ',
                prefixIcon: const Icon(Icons.account_balance_wallet_outlined),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(10),
                ),
              ),
            ),

            const SizedBox(height: 14),

            Container(
              width: double.infinity,
              padding: const EdgeInsets.all(12),
              decoration: BoxDecoration(
                color: Colors.blue.withValues(alpha: 0.07),
                borderRadius: BorderRadius.circular(8),
              ),
              child: const Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Icon(Icons.info_outline, size: 18, color: Colors.blue),
                  SizedBox(width: 8),
                  Expanded(
                    child: Text(
                      'You will be redirected to SSLCommerz to complete the payment securely.',
                      style: TextStyle(fontSize: 12, color: Colors.blue),
                    ),
                  ),
                ],
              ),
            ),

            const SizedBox(height: 18),

            SizedBox(
              width: double.infinity,
              height: 48,
              child: ElevatedButton.icon(
                onPressed: _creatingDeposit ? null : _createDeposit,
                icon: _creatingDeposit
                    ? const SizedBox(
                        width: 18,
                        height: 18,
                        child: CircularProgressIndicator(
                          strokeWidth: 2,
                          color: Colors.white,
                        ),
                      )
                    : const Icon(Icons.payment),
                label: Text(
                  _creatingDeposit
                      ? 'Creating Payment...'
                      : 'Proceed to Payment',
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  // ===========================================================================
  // PAGE
  // ===========================================================================

  @override
  Widget build(BuildContext context) {
    final userId = ref.watch(currentUserProvider)?.userId;

    if (userId == null) {
      return Scaffold(
        appBar: AppBar(title: const Text('Deposit')),
        body: const Center(child: Text('Unable to identify the current user.')),
      );
    }

    final paymentsAsync = ref.watch(userPaymentsProvider(userId));

    return Scaffold(
      appBar: AppBar(title: const Text('Deposit')),
      body: RefreshIndicator(
        onRefresh: () async {
          ref.invalidate(userPaymentsProvider(userId));
          await ref.read(userPaymentsProvider(userId).future);
        },
        child: ListView(
          padding: const EdgeInsets.all(16),
          children: [
            _buildDepositForm(),

            const SizedBox(height: 28),

            const Text(
              'Payment History',
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
            ),

            const SizedBox(height: 12),

            paymentsAsync.when(
              loading: () => const Padding(
                padding: EdgeInsets.all(30),
                child: Center(child: CircularProgressIndicator()),
              ),

              error: (error, stack) => _buildHistoryError(userId, error),

              data: (payments) {
                if (payments.isEmpty) {
                  return _buildEmptyHistory();
                }

                return Column(
                  children: payments.map(_buildPaymentItem).toList(),
                );
              },
            ),
          ],
        ),
      ),
    );
  }

  // ===========================================================================
  // EMPTY HISTORY
  // ===========================================================================

  Widget _buildEmptyHistory() {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 45, horizontal: 20),
      child: Column(
        children: [
          Icon(
            Icons.receipt_long_outlined,
            size: 55,
            color: Colors.grey.shade400,
          ),
          const SizedBox(height: 14),
          Text(
            'No deposits yet',
            style: TextStyle(
              fontSize: 17,
              fontWeight: FontWeight.w600,
              color: Colors.grey.shade700,
            ),
          ),
          const SizedBox(height: 6),
          Text(
            'Your payment history will appear here.',
            textAlign: TextAlign.center,
            style: TextStyle(color: Colors.grey.shade600),
          ),
        ],
      ),
    );
  }

  // ===========================================================================
  // ERROR
  // ===========================================================================

  Widget _buildHistoryError(int userId, Object error) {
    return Padding(
      padding: const EdgeInsets.symmetric(vertical: 30),
      child: Column(
        children: [
          const Icon(Icons.error_outline, size: 45, color: Colors.red),
          const SizedBox(height: 10),
          const Text(
            'Could not load payment history.',
            style: TextStyle(fontWeight: FontWeight.w600),
          ),
          const SizedBox(height: 12),
          ElevatedButton.icon(
            onPressed: () {
              ref.invalidate(userPaymentsProvider(userId));
            },
            icon: const Icon(Icons.refresh),
            label: const Text('Retry'),
          ),
        ],
      ),
    );
  }

  // ===========================================================================
  // HELPERS
  // ===========================================================================

  void _showMessage(String message) {
    if (!mounted) return;

    ScaffoldMessenger.of(
      context,
    ).showSnackBar(SnackBar(content: Text(message)));
  }

  String _formatDate(DateTime? dateTime) {
    if (dateTime == null) return '';

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

  String _cleanError(Object error) {
    final text = error.toString();

    if (text.startsWith('Exception: ')) {
      return text.substring(11);
    }

    return text;
  }
}
