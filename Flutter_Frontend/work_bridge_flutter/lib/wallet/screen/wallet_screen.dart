import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/router/app_router.dart';
import 'package:work_bridge_flutter/wallet/models/response/wallet_response.dart';
import 'package:work_bridge_flutter/wallet/provider/wallet_providers.dart';

class WalletScreen extends ConsumerWidget {
  const WalletScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final user = ref.watch(currentUserProvider);
    final userId = user?.userId;

    if (userId == null) {
      return Scaffold(
        appBar: AppBar(
          title: const Text('My Wallet'),
        ),
        body: const Center(
          child: Text('Unable to identify the current user.'),
        ),
      );
    }

    final walletAsync = ref.watch(walletByUserIdProvider(userId));

    return Scaffold(
      appBar: AppBar(
        title: const Text('My Wallet'),
      ),
      body: walletAsync.when(
        loading: () => const Center(
          child: CircularProgressIndicator(),
        ),
        error: (error, stack) => _buildError(
          context,
          ref,
          userId,
          error,
        ),
        data: (wallet) => _buildWalletContent(
          context,
          wallet,
        ),
      ),
    );
  }

  // ===========================================================================
  // WALLET CONTENT
  // ===========================================================================

  Widget _buildWalletContent(
      BuildContext context,
      WalletResponseDTO wallet,
      ) {
    final balance = wallet.balance ?? 0.0;
    final frozenBalance = wallet.frozenBalance ?? 0.0;

    return RefreshIndicator(
      onRefresh: () async {
        // Refresh is handled by the provider from the parent widget.
        // This callback intentionally does nothing because this widget
        // doesn't have direct access to WidgetRef.
      },
      child: SingleChildScrollView(
        physics: const AlwaysScrollableScrollPhysics(),
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _buildBalanceCard(
              context,
              wallet,
            ),

            const SizedBox(height: 24),

            _buildSectionTitle(
              'Wallet Actions',
              Icons.account_balance_wallet_outlined,
            ),

            const SizedBox(height: 12),

            _buildActionButton(
              context: context,
              icon: Icons.add_circle_outline,
              title: 'Deposit',
              subtitle: 'Add money to your wallet',
              onPressed: () {
                Navigator.pushNamed(
                  context,
                  AppRouter.deposit,
                );
              },
            ),

            const SizedBox(height: 12),

            _buildActionButton(
              context: context,
              icon: Icons.arrow_upward,
              title: 'Withdraw',
              subtitle: 'Withdraw money from your wallet',
              onPressed: () {
                Navigator.pushNamed(
                  context,
                  AppRouter.withdraw,
                );
              },
            ),

            const SizedBox(height: 12),

            _buildActionButton(
              context: context,
              icon: Icons.receipt_long_outlined,
              title: 'Transactions',
              subtitle: 'View your transaction history',
              onPressed: () {
                Navigator.pushNamed(
                  context,
                  AppRouter.transaction,
                );
              },
            ),

            const SizedBox(height: 24),

            _buildSectionTitle(
              'Wallet Information',
              Icons.info_outline,
            ),

            const SizedBox(height: 12),

            _buildInfoCard(
              icon: Icons.account_balance_wallet_outlined,
              title: 'Available Balance',
              value: _formatAmount(balance),
            ),

            const SizedBox(height: 10),

            _buildInfoCard(
              icon: Icons.lock_outline,
              title: 'Frozen Balance',
              value: _formatAmount(frozenBalance),
            ),
          ],
        ),
      ),
    );
  }

  // ===========================================================================
  // BALANCE CARD
  // ===========================================================================

  Widget _buildBalanceCard(
      BuildContext context,
      WalletResponseDTO wallet,
      ) {
    final balance = wallet.balance ?? 0.0;

    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(24),
      decoration: BoxDecoration(
        color: Theme.of(context).primaryColor,
        borderRadius: BorderRadius.circular(18),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Row(
            children: [
              Container(
                padding: const EdgeInsets.all(10),
                decoration: BoxDecoration(
                  color: Colors.white.withValues(alpha: 0.15),
                  shape: BoxShape.circle,
                ),
                child: const Icon(
                  Icons.account_balance_wallet,
                  color: Colors.white,
                  size: 24,
                ),
              ),
              const SizedBox(width: 12),
              const Text(
                'Available Balance',
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 15,
                  fontWeight: FontWeight.w500,
                ),
              ),
            ],
          ),

          const SizedBox(height: 22),

          Text(
            _formatAmount(balance),
            style: const TextStyle(
              color: Colors.white,
              fontSize: 32,
              fontWeight: FontWeight.bold,
            ),
          ),

          if (wallet.userName != null) ...[
            const SizedBox(height: 8),
            Text(
              wallet.userName!,
              style: const TextStyle(
                color: Colors.white70,
                fontSize: 13,
              ),
            ),
          ],
        ],
      ),
    );
  }

  // ===========================================================================
  // ACTION BUTTON
  // ===========================================================================

  Widget _buildActionButton({
    required BuildContext context,
    required IconData icon,
    required String title,
    required String subtitle,
    required VoidCallback onPressed,
  }) {
    return Card(
      elevation: 0,
      margin: EdgeInsets.zero,
      child: InkWell(
        onTap: onPressed,
        borderRadius: BorderRadius.circular(12),
        child: Padding(
          padding: const EdgeInsets.all(16),
          child: Row(
            children: [
              Container(
                width: 48,
                height: 48,
                decoration: BoxDecoration(
                  color: Theme.of(context)
                      .primaryColor
                      .withValues(alpha: 0.1),
                  borderRadius: BorderRadius.circular(12),
                ),
                child: Icon(
                  icon,
                  color: Theme.of(context).primaryColor,
                ),
              ),

              const SizedBox(width: 14),

              Expanded(
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text(
                      title,
                      style: const TextStyle(
                        fontSize: 16,
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                    const SizedBox(height: 3),
                    Text(
                      subtitle,
                      style: TextStyle(
                        fontSize: 13,
                        color: Colors.grey.shade600,
                      ),
                    ),
                  ],
                ),
              ),

              Icon(
                Icons.chevron_right,
                color: Colors.grey.shade500,
              ),
            ],
          ),
        ),
      ),
    );
  }

  // ===========================================================================
  // SECTION TITLE
  // ===========================================================================

  Widget _buildSectionTitle(
      String title,
      IconData icon,
      ) {
    return Row(
      children: [
        Icon(
          icon,
          size: 19,
          color: Colors.blue,
        ),
        const SizedBox(width: 8),
        Text(
          title.toUpperCase(),
          style: const TextStyle(
            color: Colors.blue,
            fontWeight: FontWeight.bold,
            letterSpacing: 0.5,
          ),
        ),
      ],
    );
  }

  // ===========================================================================
  // INFO CARD
  // ===========================================================================

  Widget _buildInfoCard({
    required IconData icon,
    required String title,
    required String value,
  }) {
    return Container(
      width: double.infinity,
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        border: Border.all(
          color: Colors.grey.shade300,
        ),
        borderRadius: BorderRadius.circular(10),
      ),
      child: Row(
        children: [
          Icon(
            icon,
            size: 21,
            color: Colors.grey.shade700,
          ),
          const SizedBox(width: 12),
          Expanded(
            child: Text(
              title,
              style: const TextStyle(
                fontSize: 14,
              ),
            ),
          ),
          Text(
            value,
            style: const TextStyle(
              fontSize: 15,
              fontWeight: FontWeight.w600,
            ),
          ),
        ],
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
            Icon(
              Icons.account_balance_wallet_outlined,
              size: 55,
              color: Colors.grey.shade400,
            ),
            const SizedBox(height: 16),
            const Text(
              'Could not load wallet.',
              style: TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.w600,
              ),
            ),
            const SizedBox(height: 8),
            Text(
              error.toString(),
              textAlign: TextAlign.center,
              style: TextStyle(
                color: Colors.grey.shade600,
              ),
            ),
            const SizedBox(height: 20),
            ElevatedButton.icon(
              onPressed: () {
                ref.invalidate(
                  walletByUserIdProvider(userId),
                );
              },
              icon: const Icon(Icons.refresh),
              label: const Text('Retry'),
            ),
          ],
        ),
      ),
    );
  }

  // ===========================================================================
  // AMOUNT FORMAT
  // ===========================================================================

  String _formatAmount(double amount) {
    return '৳${amount.toStringAsFixed(2)}';
  }
}