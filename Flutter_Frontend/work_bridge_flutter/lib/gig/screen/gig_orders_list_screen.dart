import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/providers.dart';
import 'package:work_bridge_flutter/gig/entity/response/gig_order_response.dart';
import 'package:work_bridge_flutter/router/app_router.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

class GigOrdersListScreen extends ConsumerStatefulWidget {
  const GigOrdersListScreen({super.key});

  @override
  ConsumerState<GigOrdersListScreen> createState() => _GigOrdersListScreenState();
}

class _GigOrdersListScreenState extends ConsumerState<GigOrdersListScreen> {
  List<GigOrderResponseDTO> _orders = [];

  bool _isLoading = true;
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    _loadOrders();
  }

  Future<void> _loadOrders() async {
    setState(() {
      _isLoading = true;
      _errorMessage = null;
    });

    try {
      // Get logged-in user from secure storage via provider.
      final loginResponse = await ref.read(storageServiceProvider).getUser();

      if (loginResponse == null) {
        throw Exception('User session not found.');
      }

      // IMPORTANT:
      // GigOrder.buyer is User.
      // Therefore we use User.id, NOT profileId.
      final int? userId = loginResponse.userId;

      if (userId == null) {
        throw Exception('User ID not found.');
      }

      // Buyer-side orders only.
      final orders = await ref.read(gigRepositoryProvider).getBuyerOrders(userId);

      if (!mounted) return;

      setState(() {
        _orders = orders;
        _isLoading = false;
      });
    } catch (e) {
      if (!mounted) return;

      setState(() {
        _isLoading = false;
        _errorMessage = _getErrorMessage(e);
      });
    }
  }

  String _getErrorMessage(Object error) {
    final message = error.toString();

    if (message.startsWith('Exception: ')) {
      return message.substring(11);
    }

    return message;
  }

  Future<void> _refreshOrders() async {
    await _loadOrders();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('My Gig Orders')),
      body: _buildBody(),
    );
  }

  Widget _buildBody() {
    if (_isLoading) {
      return const Center(child: CircularProgressIndicator());
    }

    if (_errorMessage != null) {
      return _buildErrorView();
    }

    if (_orders.isEmpty) {
      return _buildEmptyView();
    }

    return RefreshIndicator(
      onRefresh: _refreshOrders,
      child: ListView.builder(
        padding: const EdgeInsets.all(16),
        itemCount: _orders.length,
        itemBuilder: (context, index) {
          final order = _orders[index];

          return _buildOrderCard(order);
        },
      ),
    );
  }

  Widget _buildOrderCard(GigOrderResponseDTO order) {
    return Card(
      margin: const EdgeInsets.only(bottom: 16),
      elevation: 2,
      child: InkWell(
        onTap: () {
          if (order.id != null) {
            Navigator.of(context).pushNamed(
              AppRouter.orderDetails,
              arguments: order.id,
            );
          }
        },
        borderRadius: BorderRadius.circular(12),
        child: Padding(
          padding: const EdgeInsets.all(14),
          child: Row(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              _buildGigImage(order),

              const SizedBox(width: 14),

              Expanded(child: _buildOrderInformation(order)),
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildGigImage(GigOrderResponseDTO order) {
    if (order.gigImage == null || order.gigImage!.isEmpty) {
      return Container(
        width: 90,
        height: 90,
        decoration: BoxDecoration(
          color: Colors.grey.shade200,
          borderRadius: BorderRadius.circular(10),
        ),
        child: Icon(Icons.work_outline, size: 38, color: Colors.grey.shade600),
      );
    }

    return ClipRRect(
      borderRadius: BorderRadius.circular(10),
      child: Image.network(
        order.gigImage!,
        width: 90,
        height: 90,
        fit: BoxFit.cover,
        errorBuilder: (context, error, stackTrace) {
          return Container(
            width: 90,
            height: 90,
            color: Colors.grey.shade200,
            child: Icon(
              Icons.broken_image_outlined,
              size: 36,
              color: Colors.grey.shade600,
            ),
          );
        },
        loadingBuilder: (context, child, loadingProgress) {
          if (loadingProgress == null) {
            return child;
          }

          return Container(
            width: 90,
            height: 90,
            color: Colors.grey.shade200,
            child: const Center(
              child: SizedBox(
                width: 22,
                height: 22,
                child: CircularProgressIndicator(strokeWidth: 2),
              ),
            ),
          );
        },
      ),
    );
  }

  Widget _buildOrderInformation(GigOrderResponseDTO order) {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        Text(
          order.gigTitle ?? 'Untitled Gig',
          maxLines: 2,
          overflow: TextOverflow.ellipsis,
          style: const TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
        ),

        const SizedBox(height: 6),

        if (order.sellerName != null)
          Row(
            children: [
              const Icon(Icons.person_outline, size: 16),
              const SizedBox(width: 4),
              Expanded(
                child: Text(
                  order.sellerName!,
                  maxLines: 1,
                  overflow: TextOverflow.ellipsis,
                  style: TextStyle(color: Colors.grey.shade700),
                ),
              ),
            ],
          ),

        const SizedBox(height: 8),

        Row(
          children: [
            _buildStatusBadge(order),
            const Spacer(),
            _buildPrice(order),
          ],
        ),

        if (order.createdAt != null) ...[
          const SizedBox(height: 8),
          Text(
            'Ordered ${_formatDate(order.createdAt!)}',
            style: TextStyle(fontSize: 12, color: Colors.grey.shade600),
          ),
        ],
      ],
    );
  }

  Widget _buildStatusBadge(GigOrderResponseDTO order) {
    final status = order.status;

    String text;
    Color color;

    switch (status?.toJson()) {
      case 'ORDER_PLACED':
        text = 'Order Placed';
        color = Colors.blue;

      case 'QUOTED':
        text = 'Quote Received';
        color = Colors.orange;

      case 'QUOTE_ACCEPTED':
        text = 'In Progress';
        color = Colors.indigo;

      case 'QUOTE_REJECTED':
        text = 'Quote Rejected';
        color = Colors.red;

      case 'DELIVERED':
        text = 'Delivered';
        color = Colors.purple;

      case 'BUYER_ACCEPTED':
        text = 'Completed';
        color = Colors.green;

      case 'BUYER_REJECTED':
        text = 'Rejected';
        color = Colors.red;

      case 'BUYER_CANCELLED':
        text = 'Cancelled';
        color = Colors.red;

      case 'SELLER_CANCELLED':
        text = 'Seller Cancelled';
        color = Colors.red;

      case 'SELLER_DISPUTED':
        text = 'Disputed';
        color = Colors.deepOrange;

      case 'PAYMENT_RELEASED':
        text = 'Payment Released';
        color = Colors.green;

      case 'REFUNDED':
        text = 'Refunded';
        color = Colors.orange;

      default:
        text = 'Unknown';
        color = Colors.grey;
    }

    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
      decoration: BoxDecoration(
        color: color.withValues(alpha: 0.12),
        borderRadius: BorderRadius.circular(6),
      ),
      child: Text(
        text,
        style: TextStyle(
          color: color,
          fontSize: 11,
          fontWeight: FontWeight.w600,
        ),
      ),
    );
  }

  Widget _buildPrice(GigOrderResponseDTO order) {
    final price = order.agreedPrice ?? order.quotedPrice ?? 0;

    return Text(
      '\$${price.toStringAsFixed(2)}',
      style: const TextStyle(fontSize: 15, fontWeight: FontWeight.bold),
    );
  }

  Widget _buildEmptyView() {
    return RefreshIndicator(
      onRefresh: _refreshOrders,
      child: ListView(
        physics: const AlwaysScrollableScrollPhysics(),
        children: [
          SizedBox(height: MediaQuery.of(context).size.height * 0.30),
          const Icon(Icons.shopping_bag_outlined, size: 70, color: Colors.grey),
          const SizedBox(height: 16),
          const Center(
            child: Text(
              'No gig orders yet',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.w600),
            ),
          ),
          const SizedBox(height: 8),
          Center(
            child: Text(
              'Your purchased gigs will appear here.',
              style: TextStyle(color: Colors.grey.shade600),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildErrorView() {
    return RefreshIndicator(
      onRefresh: _refreshOrders,
      child: ListView(
        physics: const AlwaysScrollableScrollPhysics(),
        children: [
          SizedBox(height: MediaQuery.of(context).size.height * 0.25),
          const Icon(Icons.error_outline, size: 60, color: Colors.red),
          const SizedBox(height: 16),
          const Center(
            child: Text(
              'Failed to load orders',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.w600),
            ),
          ),
          const SizedBox(height: 8),
          Padding(
            padding: const EdgeInsets.symmetric(horizontal: 30),
            child: Text(
              _errorMessage ?? 'Something went wrong.',
              textAlign: TextAlign.center,
              style: TextStyle(color: Colors.grey.shade700),
            ),
          ),
          const SizedBox(height: 16),
          Center(
            child: ElevatedButton(
              onPressed: _loadOrders,
              child: const Text('Try Again'),
            ),
          ),
        ],
      ),
    );
  }

  String _formatDate(DateTime date) {
    final localDate = date.toLocal();

    return '${localDate.day.toString().padLeft(2, '0')}/'
        '${localDate.month.toString().padLeft(2, '0')}/'
        '${localDate.year}';
  }
}
