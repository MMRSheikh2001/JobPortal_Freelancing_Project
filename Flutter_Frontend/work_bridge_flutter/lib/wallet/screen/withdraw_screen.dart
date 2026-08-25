import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/enums/withdraw_method.dart';
import 'package:work_bridge_flutter/enums/withdraw_status.dart';
import 'package:work_bridge_flutter/wallet/models/request/withdraw_request.dart';
import 'package:work_bridge_flutter/wallet/models/response/withdraw_response.dart';
import 'package:work_bridge_flutter/wallet/provider/withdraw_providers.dart';

class WithdrawScreen extends ConsumerStatefulWidget {
  const WithdrawScreen({super.key});

  @override
  ConsumerState<WithdrawScreen> createState() => _WithdrawScreenState();
}

class _WithdrawScreenState extends ConsumerState<WithdrawScreen> {
  final _formKey = GlobalKey<FormState>();

  final TextEditingController _amountController = TextEditingController();
  final TextEditingController _accountNumberController =
      TextEditingController();
  final TextEditingController _accountNameController = TextEditingController();

  WithdrawMethod _selectedMethod = WithdrawMethod.bkash;

  bool _submitting = false;

  @override
  void dispose() {
    _amountController.dispose();
    _accountNumberController.dispose();
    _accountNameController.dispose();
    super.dispose();
  }

  // ===========================================================================
  // CREATE WITHDRAW REQUEST
  // ===========================================================================

  Future<void> _submitWithdraw() async {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    final userId = ref.read(currentUserProvider)?.userId;

    if (userId == null) {
      if (!mounted) return;

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Unable to identify the current user.')),
      );

      return;
    }

    final amount = double.tryParse(_amountController.text.trim());

    if (amount == null || amount <= 0) {
      return;
    }

    setState(() {
      _submitting = true;
    });

    try {
      final request = WithdrawRequestDTO(
        userId: userId,
        amount: amount,
        withdrawMethod: _selectedMethod,
        accountNumber: _accountNumberController.text.trim(),
        accountName: _accountNameController.text.trim(),
      );

      await ref.read(withdrawRepositoryProvider).createWithdraw(request);

      if (!mounted) return;

      _amountController.clear();
      _accountNumberController.clear();
      _accountNameController.clear();

      setState(() {
        _selectedMethod = WithdrawMethod.bkash;
      });

      ref.invalidate(userWithdrawsProvider(userId));

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(
          content: Text('Withdrawal request submitted successfully.'),
          backgroundColor: Colors.green,
        ),
      );
    } catch (e) {
      if (!mounted) return;

      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(
          content: Text('Failed to submit withdrawal request: $e'),
          backgroundColor: Colors.red,
        ),
      );
    } finally {
      if (mounted) {
        setState(() {
          _submitting = false;
        });
      }
    }
  }

  // ===========================================================================
  // BUILD
  // ===========================================================================

  @override
  Widget build(BuildContext context) {
    final userId = ref.watch(currentUserProvider)?.userId;

    return Scaffold(
      appBar: AppBar(title: const Text('Withdraw')),
      body: userId == null
          ? const Center(child: Text('Unable to identify the current user.'))
          : RefreshIndicator(
              onRefresh: () async {
                ref.invalidate(userWithdrawsProvider(userId));
                await ref.read(userWithdrawsProvider(userId).future);
              },
              child: ListView(
                padding: const EdgeInsets.all(16),
                children: [
                  _buildWithdrawForm(),
                  const SizedBox(height: 28),
                  _buildWithdrawHistory(userId),
                ],
              ),
            ),
    );
  }

  // ===========================================================================
  // WITHDRAW FORM
  // ===========================================================================

  Widget _buildWithdrawForm() {
    return Card(
      elevation: 1,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              const Row(
                children: [
                  Icon(
                    Icons.account_balance_wallet_outlined,
                    color: Colors.blue,
                  ),
                  SizedBox(width: 8),
                  Text(
                    'Withdraw Money',
                    style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                  ),
                ],
              ),

              const SizedBox(height: 6),

              Text(
                'Submit a withdrawal request for admin approval.',
                style: TextStyle(color: Colors.grey.shade600),
              ),

              const SizedBox(height: 20),

              // ----------------------------------------------------------------
              // AMOUNT
              // ----------------------------------------------------------------
              TextFormField(
                controller: _amountController,
                keyboardType: const TextInputType.numberWithOptions(
                  decimal: true,
                ),
                decoration: const InputDecoration(
                  labelText: 'Amount',
                  hintText: 'Enter withdrawal amount',
                  prefixIcon: Icon(Icons.attach_money),
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.trim().isEmpty) {
                    return 'Please enter an amount';
                  }

                  final amount = double.tryParse(value.trim());

                  if (amount == null) {
                    return 'Please enter a valid amount';
                  }

                  if (amount <= 0) {
                    return 'Amount must be greater than 0';
                  }

                  return null;
                },
              ),

              const SizedBox(height: 16),

              // ----------------------------------------------------------------
              // WITHDRAW METHOD
              // ----------------------------------------------------------------
              DropdownButtonFormField<WithdrawMethod>(
                initialValue: _selectedMethod,
                decoration: const InputDecoration(
                  labelText: 'Withdrawal Method',
                  prefixIcon: Icon(Icons.payment),
                  border: OutlineInputBorder(),
                ),
                items: WithdrawMethod.values.map((method) {
                  return DropdownMenuItem<WithdrawMethod>(
                    value: method,
                    child: Text(_getMethodName(method)),
                  );
                }).toList(),
                onChanged: _submitting
                    ? null
                    : (method) {
                        if (method == null) return;

                        setState(() {
                          _selectedMethod = method;
                        });
                      },
              ),

              const SizedBox(height: 16),

              // ----------------------------------------------------------------
              // ACCOUNT NUMBER
              // ----------------------------------------------------------------
              TextFormField(
                controller: _accountNumberController,
                keyboardType: TextInputType.phone,
                decoration: InputDecoration(
                  labelText: _selectedMethod == WithdrawMethod.bank
                      ? 'Bank Account Number'
                      : 'Mobile Account Number',
                  hintText: _selectedMethod == WithdrawMethod.bank
                      ? 'Enter bank account number'
                      : 'Enter mobile number',
                  prefixIcon: const Icon(Icons.account_circle_outlined),
                  border: const OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.trim().isEmpty) {
                    return 'Please enter the account number';
                  }

                  return null;
                },
              ),

              const SizedBox(height: 16),

              // ----------------------------------------------------------------
              // ACCOUNT NAME
              // ----------------------------------------------------------------
              TextFormField(
                controller: _accountNameController,
                textCapitalization: TextCapitalization.words,
                decoration: const InputDecoration(
                  labelText: 'Account Name',
                  hintText: 'Enter account holder name',
                  prefixIcon: Icon(Icons.person_outline),
                  border: OutlineInputBorder(),
                ),
                validator: (value) {
                  if (value == null || value.trim().isEmpty) {
                    return 'Please enter the account name';
                  }

                  return null;
                },
              ),

              const SizedBox(height: 20),

              // ----------------------------------------------------------------
              // SUBMIT BUTTON
              // ----------------------------------------------------------------
              SizedBox(
                width: double.infinity,
                height: 50,
                child: ElevatedButton.icon(
                  onPressed: _submitting ? null : _submitWithdraw,
                  icon: _submitting
                      ? const SizedBox(
                          width: 18,
                          height: 18,
                          child: CircularProgressIndicator(
                            strokeWidth: 2,
                            color: Colors.white,
                          ),
                        )
                      : const Icon(Icons.send),
                  label: Text(
                    _submitting ? 'Submitting...' : 'Submit Withdrawal Request',
                  ),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  // ===========================================================================
  // WITHDRAW HISTORY
  // ===========================================================================

  Widget _buildWithdrawHistory(int userId) {
    final withdrawsAsync = ref.watch(userWithdrawsProvider(userId));

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        const Text(
          'Withdrawal Requests',
          style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
        ),

        const SizedBox(height: 12),

        withdrawsAsync.when(
          loading: () => const Center(
            child: Padding(
              padding: EdgeInsets.all(30),
              child: CircularProgressIndicator(),
            ),
          ),
          error: (error, stack) => _buildHistoryError(userId, error),
          data: (withdraws) {
            if (withdraws.isEmpty) {
              return _buildEmptyHistory();
            }

            return Column(
              children: withdraws.map((withdraw) {
                return _buildWithdrawCard(withdraw);
              }).toList(),
            );
          },
        ),
      ],
    );
  }

  // ===========================================================================
  // WITHDRAW CARD
  // ===========================================================================

  Widget _buildWithdrawCard(WithdrawResponseDTO withdraw) {
    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      elevation: 1,
      shape: RoundedRectangleBorder(borderRadius: BorderRadius.circular(14)),
      child: Padding(
        padding: const EdgeInsets.all(14),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // ---------------------------------------------------------------
            // TOP ROW
            // ---------------------------------------------------------------
            Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                _buildMethodIcon(withdraw.withdrawMethod),

                const SizedBox(width: 12),

                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        _getMethodName(withdraw.withdrawMethod),
                        style: const TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.w600,
                        ),
                      ),
                      const SizedBox(height: 3),
                      Text(
                        withdraw.accountNumber ?? 'N/A',
                        style: TextStyle(color: Colors.grey.shade600),
                      ),
                    ],
                  ),
                ),

                _buildStatusBadge(withdraw.withdrawStatus),
              ],
            ),

            const SizedBox(height: 14),

            const Divider(),

            const SizedBox(height: 8),

            // ---------------------------------------------------------------
            // AMOUNT
            // ---------------------------------------------------------------
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text('Amount', style: TextStyle(color: Colors.grey.shade600)),
                Text(
                  _formatAmount(withdraw.amount),
                  style: const TextStyle(
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ),

            const SizedBox(height: 8),

            // ---------------------------------------------------------------
            // ACCOUNT NAME
            // ---------------------------------------------------------------
            _buildInfoRow('Account Name', withdraw.accountName ?? 'N/A'),

            const SizedBox(height: 8),

            // ---------------------------------------------------------------
            // REQUEST DATE
            // ---------------------------------------------------------------
            _buildInfoRow('Requested', _formatDateTime(withdraw.createdAt)),

            // ---------------------------------------------------------------
            // UPDATED DATE
            // ---------------------------------------------------------------
            if (withdraw.updatedAt != null) ...[
              const SizedBox(height: 8),
              _buildInfoRow('Updated', _formatDateTime(withdraw.updatedAt)),
            ],

            // ---------------------------------------------------------------
            // TRANSACTION REFERENCE
            // ---------------------------------------------------------------
            if (withdraw.transactionReference != null &&
                withdraw.transactionReference!.trim().isNotEmpty) ...[
              const SizedBox(height: 8),
              _buildInfoRow(
                'Transaction Reference',
                withdraw.transactionReference!,
              ),
            ],

            // ---------------------------------------------------------------
            // ADMIN REMARKS
            // ---------------------------------------------------------------
            if (withdraw.adminRemarks != null &&
                withdraw.adminRemarks!.trim().isNotEmpty) ...[
              const SizedBox(height: 12),
              _buildAdminRemarks(withdraw.adminRemarks!),
            ],
          ],
        ),
      ),
    );
  }

  // ===========================================================================
  // STATUS
  // ===========================================================================

  Widget _buildStatusBadge(WithdrawStatus? status) {
    final statusText = _getStatusName(status);

    Color backgroundColor;
    Color textColor;
    IconData icon;

    switch (status) {
      case WithdrawStatus.approved:
        backgroundColor = Colors.green.shade50;
        textColor = Colors.green.shade700;
        icon = Icons.check_circle_outline;
        break;

      case WithdrawStatus.rejected:
        backgroundColor = Colors.red.shade50;
        textColor = Colors.red.shade700;
        icon = Icons.cancel_outlined;
        break;

      case WithdrawStatus.pending:
      case null:
        backgroundColor = Colors.orange.shade50;
        textColor = Colors.orange.shade800;
        icon = Icons.hourglass_empty;
        break;
    }

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 9, vertical: 6),
      decoration: BoxDecoration(
        color: backgroundColor,
        borderRadius: BorderRadius.circular(20),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 14, color: textColor),
          const SizedBox(width: 4),
          Text(
            statusText,
            style: TextStyle(
              fontSize: 12,
              fontWeight: FontWeight.w600,
              color: textColor,
            ),
          ),
        ],
      ),
    );
  }

  // ===========================================================================
  // ADMIN REMARKS
  // ===========================================================================

  Widget _buildAdminRemarks(String remarks) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: Colors.grey.shade100,
        borderRadius: BorderRadius.circular(10),
        border: Border.all(color: Colors.grey.shade300),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Icon(
                Icons.admin_panel_settings_outlined,
                size: 18,
                color: Colors.grey.shade700,
              ),
              const SizedBox(width: 6),
              Text(
                'Admin Remarks',
                style: TextStyle(
                  fontWeight: FontWeight.w600,
                  color: Colors.grey.shade800,
                ),
              ),
            ],
          ),
          const SizedBox(height: 7),
          Text(remarks, style: TextStyle(color: Colors.grey.shade700)),
        ],
      ),
    );
  }

  // ===========================================================================
  // METHOD ICON
  // ===========================================================================

  Widget _buildMethodIcon(WithdrawMethod? method) {
    IconData icon;

    switch (method) {
      case WithdrawMethod.bkash:
        icon = Icons.phone_android;
        break;
      case WithdrawMethod.nagad:
        icon = Icons.phone_android;
        break;
      case WithdrawMethod.bank:
        icon = Icons.account_balance;
        break;
      case null:
        icon = Icons.payment;
        break;
    }

    return Container(
      width: 46,
      height: 46,
      decoration: BoxDecoration(
        color: Colors.blue.shade50,
        borderRadius: BorderRadius.circular(12),
      ),
      child: Icon(icon, color: Colors.blue),
    );
  }

  // ===========================================================================
  // INFO ROW
  // ===========================================================================

  Widget _buildInfoRow(String title, String value) {
    return Row(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        SizedBox(
          width: 125,
          child: Text(title, style: TextStyle(color: Colors.grey.shade600)),
        ),
        Expanded(
          child: Text(
            value,
            textAlign: TextAlign.right,
            style: const TextStyle(fontWeight: FontWeight.w500),
          ),
        ),
      ],
    );
  }

  // ===========================================================================
  // EMPTY HISTORY
  // ===========================================================================

  Widget _buildEmptyHistory() {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.symmetric(horizontal: 20, vertical: 40),
      child: Column(
        children: [
          Icon(
            Icons.account_balance_wallet_outlined,
            size: 55,
            color: Colors.grey.shade400,
          ),
          const SizedBox(height: 12),
          Text(
            'No withdrawal requests',
            style: TextStyle(
              fontSize: 17,
              fontWeight: FontWeight.w600,
              color: Colors.grey.shade700,
            ),
          ),
          const SizedBox(height: 6),
          Text(
            'Your withdrawal requests will appear here.',
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
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(20),
      child: Column(
        children: [
          const Icon(Icons.error_outline, size: 45, color: Colors.red),
          const SizedBox(height: 10),
          const Text(
            'Could not load withdrawal requests.',
            textAlign: TextAlign.center,
            style: TextStyle(fontWeight: FontWeight.w600),
          ),
          const SizedBox(height: 12),
          ElevatedButton.icon(
            onPressed: () {
              ref.invalidate(userWithdrawsProvider(userId));
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

  String _getMethodName(WithdrawMethod? method) {
    switch (method) {
      case WithdrawMethod.bkash:
        return 'bKash';
      case WithdrawMethod.nagad:
        return 'Nagad';
      case WithdrawMethod.bank:
        return 'Bank';
      case null:
        return 'Unknown';
    }
  }

  String _getStatusName(WithdrawStatus? status) {
    switch (status) {
      case WithdrawStatus.pending:
        return 'Pending';
      case WithdrawStatus.approved:
        return 'Approved';
      case WithdrawStatus.rejected:
        return 'Rejected';
      case null:
        return 'Unknown';
    }
  }

  String _formatAmount(double? amount) {
    if (amount == null) return '৳0.00';

    return '৳${amount.toStringAsFixed(2)}';
  }

  String _formatDateTime(DateTime? dateTime) {
    if (dateTime == null) return 'N/A';

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

    return '$day/$month/$year $hour:$minute $period';
  }
}
