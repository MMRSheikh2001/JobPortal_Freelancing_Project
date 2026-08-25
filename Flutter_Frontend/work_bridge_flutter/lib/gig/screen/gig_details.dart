import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/auth/providers.dart';
import 'package:work_bridge_flutter/enums/gig_order_status.dart';
import 'package:work_bridge_flutter/gig/data/gig_repository.dart';
import 'package:work_bridge_flutter/gig/entity/response/gig_order_response.dart';
import 'package:work_bridge_flutter/gig/entity/response/gig_response.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

class GigDetails extends ConsumerStatefulWidget {
  const GigDetails({super.key, required this.gigId});

  final int gigId;

  @override
  ConsumerState<GigDetails> createState() => _GigDetailsState();
}

class _GigDetailsState extends ConsumerState<GigDetails> {
  GigResponseDTO? _gig;
  GigOrderResponseDTO? _activeOrder;

  bool _loading = true;
  bool _checkingOrder = false;
  bool _ordering = false;

  String? _error;
  int? _buyerId;

  @override
  void initState() {
    super.initState();
    _loadGigDetails();
  }

  // =====================================================
  // Load Gig
  // =====================================================

  Future<void> _loadGigDetails() async {
    setState(() {
      _loading = true;
      _error = null;
    });

    try {
      final loginResponse = ref.read(currentUserProvider);

      if (loginResponse == null) {
        throw Exception('User session not found.');
      }

      final buyerId = loginResponse.userId;

      if (buyerId == null) {
        throw Exception('Your user ID could not be found.');
      }

      final gig = await ref
          .read(gigRepositoryProvider)
          .getGigById(widget.gigId);

      if (!mounted) return;

      setState(() {
        _buyerId = buyerId;
        _gig = gig;
        _loading = false;
      });

      await _loadOrderStatus();
    } catch (e) {
      if (!mounted) return;

      setState(() {
        _loading = false;
        _error = _cleanError(e);
      });
    }
  }

  // =====================================================
  // Check Existing Order
  // =====================================================

  Future<void> _loadOrderStatus() async {
    final buyerId = _buyerId;

    if (buyerId == null) return;

    setState(() {
      _checkingOrder = true;
    });

    try {
      final repository = ref.read(gigRepositoryProvider);

      final exists = await repository.existsGigOrder(widget.gigId, buyerId);

      if (!exists) {
        if (!mounted) return;

        setState(() {
          _activeOrder = null;
          _checkingOrder = false;
        });

        return;
      }

      try {
        final order = await repository.findActiveOrder(widget.gigId, buyerId);

        if (!mounted) return;

        setState(() {
          _activeOrder = order;
          _checkingOrder = false;
        });
      } catch (_) {
        // An order exists, but there may not be an active order.
        if (!mounted) return;

        setState(() {
          _activeOrder = null;
          _checkingOrder = false;
        });
      }
    } catch (_) {
      if (!mounted) return;

      setState(() {
        _checkingOrder = false;
      });
    }
  }

  // =====================================================
  // Place Order
  // =====================================================

  Future<void> _placeOrder() async {
    final gig = _gig;
    final buyerId = _buyerId;

    if (gig?.id == null || buyerId == null) {
      _showMessage(
        'Unable to place the order. Required information is missing.',
        isError: true,
      );
      return;
    }

    if (gig!.isActive != true) {
      _showMessage('This gig is no longer active.', isError: true);
      return;
    }

    final confirmed = await showDialog<bool>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Order this gig?'),
          content: Text(
            'Are you sure you want to order "${gig.title ?? 'this gig'}"?',
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context, false),
              child: const Text('Cancel'),
            ),
            FilledButton(
              onPressed: () => Navigator.pop(context, true),
              child: const Text('Order'),
            ),
          ],
        );
      },
    );

    if (confirmed != true) return;

    setState(() {
      _ordering = true;
    });

    try {
      final order = await ref
          .read(gigRepositoryProvider)
          .placeOrder(gig.id!, buyerId);

      if (!mounted) return;

      setState(() {
        _activeOrder = order;
        _ordering = false;
      });

      _showMessage('Gig ordered successfully.');
    } catch (e) {
      if (!mounted) return;

      setState(() {
        _ordering = false;
      });

      _showMessage('Failed to place order: ${_cleanError(e)}', isError: true);
    }
  }

  // =====================================================
  // Helpers
  // =====================================================

  void _showMessage(String message, {bool isError = false}) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(message),
        backgroundColor: isError ? Colors.red : null,
      ),
    );
  }

  String _cleanError(Object error) {
    return error.toString().replaceFirst('Exception: ', '');
  }

  String _formatDate(DateTime date) {
    return '${date.day}/${date.month}/${date.year}';
  }

  String _formatPrice(double? price) {
    if (price == null) {
      return 'Price not specified';
    }

    return '৳${price.toStringAsFixed(0)}';
  }

  String _formatRating(double? rating) {
    if (rating == null) {
      return 'No rating';
    }

    return rating.toStringAsFixed(1);
  }

  // =====================================================
  // Build
  // =====================================================

  @override
  Widget build(BuildContext context) {
    if (_loading) {
      return const Scaffold(body: Center(child: CircularProgressIndicator()));
    }

    if (_error != null || _gig == null) {
      return Scaffold(
        appBar: AppBar(title: const Text('Gig Details')),
        body: Center(
          child: Padding(
            padding: const EdgeInsets.all(24),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                const Icon(Icons.error_outline, size: 48, color: Colors.red),
                const SizedBox(height: 12),
                Text(
                  _error ?? 'Unable to load gig.',
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 16),
                FilledButton(
                  onPressed: _loadGigDetails,
                  child: const Text('Retry'),
                ),
              ],
            ),
          ),
        ),
      );
    }

    final gig = _gig!;

    return Scaffold(
      appBar: AppBar(title: const Text('Gig Details')),
      bottomNavigationBar: _buildBottomAction(),
      body: RefreshIndicator(
        onRefresh: _loadGigDetails,
        child: ListView(
          padding: const EdgeInsets.fromLTRB(16, 16, 16, 100),
          children: [
            _buildGigImage(gig),

            const SizedBox(height: 16),

            _buildGigHeader(gig),

            const SizedBox(height: 16),

            _buildGigSummary(gig),

            const SizedBox(height: 16),

            _buildSection(
              title: 'Description',
              icon: Icons.description_outlined,
              child: _buildText(gig.description),
            ),

            _buildSection(
              title: 'Gig Information',
              icon: Icons.info_outline,
              child: _buildGigInformation(gig),
            ),

            _buildSellerSection(gig),
          ],
        ),
      ),
    );
  }

  // =====================================================
  // Gig Image
  // =====================================================

  Widget _buildGigImage(GigResponseDTO gig) {
    final imageName = gig.gigImage;

    if (imageName == null || imageName.trim().isEmpty) {
      return Container(
        height: 230,
        decoration: BoxDecoration(
          color: Colors.grey.shade100,
          borderRadius: BorderRadius.circular(14),
        ),
        child: const Center(
          child: Icon(Icons.image_outlined, size: 70, color: Colors.grey),
        ),
      );
    }

    return ClipRRect(
      borderRadius: BorderRadius.circular(14),
      child: Image.network(
        '${ApiConstants.gigImageUrl}$imageName',
        height: 230,
        width: double.infinity,
        fit: BoxFit.cover,
        errorBuilder: (context, error, stackTrace) {
          return Container(
            height: 230,
            width: double.infinity,
            color: Colors.grey.shade100,
            child: const Center(
              child: Icon(
                Icons.broken_image_outlined,
                size: 60,
                color: Colors.grey,
              ),
            ),
          );
        },
        loadingBuilder: (context, child, loadingProgress) {
          if (loadingProgress == null) {
            return child;
          }

          return Container(
            height: 230,
            width: double.infinity,
            color: Colors.grey.shade100,
            child: const Center(child: CircularProgressIndicator()),
          );
        },
      ),
    );
  }

  // =====================================================
  // Gig Header
  // =====================================================

  Widget _buildGigHeader(GigResponseDTO gig) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(18),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            if (gig.categoryName != null)
              Chip(
                avatar: const Icon(Icons.category_outlined, size: 17),
                label: Text(gig.categoryName!),
              ),

            const SizedBox(height: 8),

            Text(
              gig.title ?? 'Untitled gig',
              style: const TextStyle(fontSize: 22, fontWeight: FontWeight.bold),
            ),

            if (gig.shortDescription != null &&
                gig.shortDescription!.trim().isNotEmpty) ...[
              const SizedBox(height: 8),
              Text(
                gig.shortDescription!,
                style: const TextStyle(
                  fontSize: 15,
                  color: Colors.black54,
                  height: 1.4,
                ),
              ),
            ],

            const SizedBox(height: 14),

            Row(
              children: [
                const Icon(
                  Icons.person_outline,
                  size: 19,
                  color: Colors.black54,
                ),
                const SizedBox(width: 7),
                Expanded(
                  child: Text(
                    gig.userName ?? 'Unknown seller',
                    style: const TextStyle(fontWeight: FontWeight.w600),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  // =====================================================
  // Summary
  // =====================================================

  Widget _buildGigSummary(GigResponseDTO gig) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Wrap(
          spacing: 8,
          runSpacing: 8,
          children: [
            _infoChip(
              Icons.payments_outlined,
              'Starting ${_formatPrice(gig.startingPrice)}',
            ),

            if (gig.deliveryDays != null)
              _infoChip(
                Icons.schedule_outlined,
                '${gig.deliveryDays} day${gig.deliveryDays == 1 ? '' : 's'} delivery',
              ),

            if (gig.revisions != null)
              _infoChip(
                Icons.refresh_outlined,
                '${gig.revisions} revision${gig.revisions == 1 ? '' : 's'}',
              ),

            _infoChip(Icons.star_outline, _formatRating(gig.averageRating)),

            if (gig.totalReviews != null)
              _infoChip(
                Icons.rate_review_outlined,
                '${gig.totalReviews} review${gig.totalReviews == 1 ? '' : 's'}',
              ),

            if (gig.completedOrders != null)
              _infoChip(
                Icons.check_circle_outline,
                '${gig.completedOrders} completed',
              ),
          ],
        ),
      ),
    );
  }

  // =====================================================
  // Gig Information
  // =====================================================

  Widget _buildGigInformation(GigResponseDTO gig) {
    return Column(
      children: [
        if (gig.startingPrice != null)
          _detailRow(
            Icons.payments_outlined,
            'Starting Price',
            _formatPrice(gig.startingPrice),
          ),

        if (gig.deliveryDays != null)
          _detailRow(
            Icons.schedule_outlined,
            'Delivery',
            '${gig.deliveryDays} day${gig.deliveryDays == 1 ? '' : 's'}',
          ),

        if (gig.revisions != null)
          _detailRow(Icons.refresh_outlined, 'Revisions', '${gig.revisions}'),

        if (gig.categoryName != null)
          _detailRow(Icons.category_outlined, 'Category', gig.categoryName!),

        if (gig.createdAt != null)
          _detailRow(
            Icons.calendar_today_outlined,
            'Created',
            _formatDate(gig.createdAt!),
          ),

        if (gig.updatedAt != null)
          _detailRow(
            Icons.update_outlined,
            'Last Updated',
            _formatDate(gig.updatedAt!),
          ),

        _detailRow(
          gig.isActive == true
              ? Icons.check_circle_outline
              : Icons.cancel_outlined,
          'Status',
          gig.isActive == true ? 'Active' : 'Inactive',
        ),
      ],
    );
  }

  // =====================================================
  // Seller
  // =====================================================

  Widget _buildSellerSection(GigResponseDTO gig) {
    return Card(
      margin: const EdgeInsets.only(top: 4),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Row(
              children: [
                Icon(Icons.person_outline),
                SizedBox(width: 8),
                Text(
                  'About the Seller',
                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                ),
              ],
            ),

            const SizedBox(height: 14),

            if (gig.userName != null)
              _sellerInfo(Icons.person, 'Seller', gig.userName!),

            if (gig.averageRating != null)
              _sellerInfo(
                Icons.star_outline,
                'Rating',
                '${_formatRating(gig.averageRating)} / 5',
              ),

            if (gig.totalReviews != null)
              _sellerInfo(
                Icons.rate_review_outlined,
                'Reviews',
                '${gig.totalReviews}',
              ),

            if (gig.completedOrders != null)
              _sellerInfo(
                Icons.check_circle_outline,
                'Completed Orders',
                '${gig.completedOrders}',
              ),
          ],
        ),
      ),
    );
  }

  // =====================================================
  // Sections
  // =====================================================

  Widget _buildSection({
    required String title,
    required IconData icon,
    required Widget child,
  }) {
    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Icon(icon, size: 21),
                const SizedBox(width: 8),
                Text(
                  title,
                  style: const TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ),

            const SizedBox(height: 12),

            child,
          ],
        ),
      ),
    );
  }

  Widget _buildText(String? value) {
    if (value == null || value.trim().isEmpty) {
      return const Text(
        'Not specified.',
        style: TextStyle(color: Colors.black54),
      );
    }

    return Text(value, style: const TextStyle(height: 1.45));
  }

  // =====================================================
  // Bottom Action
  // =====================================================

  Widget _buildBottomAction() {
    if (_checkingOrder) {
      return const SafeArea(
        child: Padding(
          padding: EdgeInsets.all(12),
          child: SizedBox(
            height: 50,
            child: Center(child: CircularProgressIndicator()),
          ),
        ),
      );
    }

    if (_activeOrder != null) {
      return _buildExistingOrderAction();
    }

    final gig = _gig;

    if (gig?.isActive != true) {
      return const SafeArea(
        child: Padding(
          padding: EdgeInsets.all(12),
          child: SizedBox(
            height: 50,
            child: FilledButton(
              onPressed: null,
              child: Text('Gig is no longer active'),
            ),
          ),
        ),
      );
    }

    return SafeArea(
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: SizedBox(
          height: 50,
          child: FilledButton.icon(
            onPressed: _ordering ? null : _placeOrder,
            icon: _ordering
                ? const SizedBox(
                    width: 18,
                    height: 18,
                    child: CircularProgressIndicator(
                      strokeWidth: 2,
                      color: Colors.white,
                    ),
                  )
                : const Icon(Icons.shopping_cart_outlined),
            label: Text(_ordering ? 'Ordering...' : 'Order Now'),
          ),
        ),
      ),
    );
  }

  // =====================================================
  // Existing Order Action
  // =====================================================

  Widget _buildExistingOrderAction() {
    final order = _activeOrder;

    if (order == null) {
      return const SizedBox.shrink();
    }

    final status = order.status;

    String label;

    switch (status) {
      case GigOrderStatus.orderPlaced:
        label = 'Order Placed';
        break;

      case GigOrderStatus.quoted:
        label = 'Quote Received';
        break;

      case GigOrderStatus.quoteAccepted:
        label = 'Quote Accepted';
        break;

      case GigOrderStatus.quoteRejected:
        label = 'Quote Rejected';
        break;

      case GigOrderStatus.delivered:
        label = 'Delivery Received';
        break;

      case GigOrderStatus.buyerAccepted:
        label = 'Order Completed';
        break;

      case GigOrderStatus.buyerRejected:
        label = 'Delivery Rejected';
        break;

      case GigOrderStatus.buyerCancelled:
        label = 'Order Cancelled';
        break;

      case GigOrderStatus.sellerCancelled:
        label = 'Seller Cancelled';
        break;

      case GigOrderStatus.sellerDisputed:
        label = 'Under Dispute';
        break;

      case GigOrderStatus.paymentReleased:
        label = 'Payment Released';
        break;

      case GigOrderStatus.refunded:
        label = 'Refunded';
        break;

      case null:
        label = 'Order Exists';
        break;
    }

    return SafeArea(
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: SizedBox(
          height: 50,
          child: FilledButton(onPressed: null, child: Text(label)),
        ),
      ),
    );
  }

  // =====================================================
  // Small UI Helpers
  // =====================================================

  Widget _infoChip(IconData icon, String label) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 9, vertical: 7),
      decoration: BoxDecoration(
        color: Colors.grey.shade100,
        borderRadius: BorderRadius.circular(9),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 15, color: Colors.black54),
          const SizedBox(width: 5),
          Text(label, style: const TextStyle(fontSize: 12)),
        ],
      ),
    );
  }

  Widget _detailRow(IconData icon, String label, String value) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 12),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Icon(icon, size: 19, color: Colors.black54),
          const SizedBox(width: 10),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  label,
                  style: const TextStyle(fontSize: 11, color: Colors.black45),
                ),
                const SizedBox(height: 2),
                Text(value),
              ],
            ),
          ),
        ],
      ),
    );
  }

  Widget _sellerInfo(IconData icon, String label, String value) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 10),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Icon(icon, size: 19, color: Colors.black54),
          const SizedBox(width: 10),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  label,
                  style: const TextStyle(fontSize: 11, color: Colors.black45),
                ),
                const SizedBox(height: 2),
                Text(value),
              ],
            ),
          ),
        ],
      ),
    );
  }
}
