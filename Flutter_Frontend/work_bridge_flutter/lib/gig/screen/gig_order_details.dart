import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:work_bridge_flutter/auth/request/user_request.dart';
import 'package:work_bridge_flutter/auth/providers.dart';
import 'package:work_bridge_flutter/gig/entity/response/gig_order_response.dart';
import 'package:work_bridge_flutter/router/app_router.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

import '../../enums/gig_order_status.dart';

class GigOrderDetailsScreen extends ConsumerStatefulWidget {
  const GigOrderDetailsScreen({super.key, required this.gigOrderId});

  final int gigOrderId;

  @override
  ConsumerState<GigOrderDetailsScreen> createState() =>
      _GigOrderDetailsScreenState();
}

class _GigOrderDetailsScreenState extends ConsumerState<GigOrderDetailsScreen> {
  GigOrderResponseDTO? order;

  bool loading = false;
  bool saving = false;

  // Will be connected to ReviewRepository later.
  bool reviewExists = false;

  @override
  void initState() {
    super.initState();
    loadOrder();
  }

  // =====================================================
  // Load Order
  // =====================================================

  Future<void> loadOrder() async {
    setState(() {
      loading = true;
    });

    try {
      final result = await ref
          .read(gigRepositoryProvider)
          .getOrderById(widget.gigOrderId);

      if (!mounted) return;

      setState(() {
        order = result;
        loading = false;
      });
    } catch (e) {
      if (!mounted) return;

      setState(() {
        loading = false;
      });

      showMessage(apiErrorMessage(e), isError: true);
    }
  }

  // =====================================================
  // Accept Quote
  // =====================================================

  Future<void> acceptQuote() async {
    if (order?.id == null) return;

    setState(() {
      saving = true;
    });

    try {
      await ref.read(gigRepositoryProvider).acceptQuote(order!.id!);

      if (!mounted) return;

      showMessage('Quote accepted.');

      await loadOrder();
    } catch (e) {
      if (!mounted) return;

      showMessage('Unable to accept quote.', isError: true);
    } finally {
      if (mounted) {
        setState(() {
          saving = false;
        });
      }
    }
  }

  // =====================================================
  // Reject Quote
  // =====================================================

  Future<void> rejectQuote() async {
    if (order?.id == null) return;

    final confirmed = await showConfirmationDialog(
      title: 'Reject Quote',
      message: 'Reject this quote?',
      confirmText: 'Reject',
      isDanger: true,
    );

    if (!confirmed) return;

    setState(() {
      saving = true;
    });

    try {
      await ref.read(gigRepositoryProvider).rejectQuote(order!.id!);

      if (!mounted) return;

      showMessage('Quote rejected.');

      await loadOrder();
    } catch (e) {
      if (!mounted) return;

      showMessage('Unable to reject quote.', isError: true);
    } finally {
      if (mounted) {
        setState(() {
          saving = false;
        });
      }
    }
  }


  Future<void> downloadDeliveryFile() async {
    final fileName = order?.deliveryFileUrl;

    if (fileName == null || fileName.isEmpty) {
      showMessage(
        'No delivery file available.',
        isError: true,
      );
      return;
    }

    final url = getDeliveryFileUrl(fileName);

    if (url == null || url.isEmpty) {
      showMessage(
        'Invalid delivery file URL.',
        isError: true,
      );
      return;
    }

    debugPrint('Delivery URL: $url');

    try {
      final uri = Uri.parse(url);

      if (kIsWeb) {
        // ============================
        // Flutter Web
        // ============================

        final launched = await launchUrl(
          uri,
          webOnlyWindowName: '_blank',
        );

        if (!launched && mounted) {
          showMessage(
            'Unable to open delivery file.',
            isError: true,
          );
        }
      } else {
        // ============================
        // Android / iOS
        // ============================

        final launched = await launchUrl(
          uri,
          mode: LaunchMode.externalApplication,
        );

        if (!launched && mounted) {
          showMessage(
            'Unable to open delivery file.',
            isError: true,
          );
        }
      }
    } catch (e) {
      debugPrint('Download error: $e');

      if (!mounted) return;

      showMessage(
        'Unable to open delivery file.',
        isError: true,
      );
    }
  }

  // =====================================================
  // Accept Delivery
  // =====================================================

  Future<void> acceptDelivery() async {
    if (order?.id == null) return;

    setState(() {
      saving = true;
    });

    try {
      await ref.read(gigRepositoryProvider).acceptDelivery(order!.id!);

      if (!mounted) return;

      showMessage('Delivery accepted.');

      await loadOrder();
    } catch (e) {
      if (!mounted) return;

      showMessage('Unable to accept delivery.', isError: true);
    } finally {
      if (mounted) {
        setState(() {
          saving = false;
        });
      }
    }
  }

  // =====================================================
  // Reject Delivery
  // =====================================================

  Future<void> rejectDelivery() async {
    if (order?.id == null) return;

    final confirmed = await showConfirmationDialog(
      title: 'Reject Delivery',
      message: 'Reject delivery?',
      confirmText: 'Reject',
      isDanger: true,
    );

    if (!confirmed) return;

    setState(() {
      saving = true;
    });

    try {
      await ref.read(gigRepositoryProvider).rejectDelivery(order!.id!);

      if (!mounted) return;

      showMessage('Delivery rejected.');

      await loadOrder();
    } catch (e) {
      if (!mounted) return;

      showMessage('Unable to reject delivery.', isError: true);
    } finally {
      if (mounted) {
        setState(() {
          saving = false;
        });
      }
    }
  }

  // =====================================================
  // Cancel Order
  // =====================================================

  Future<void> cancelOrder() async {
    if (order?.id == null) return;

    final confirmed = await showConfirmationDialog(
      title: 'Cancel Order',
      message:
          'Cancel this order?\n\nSeller will have 7 days to dispute if applicable.',
      confirmText: 'Cancel Order',
      isDanger: true,
    );

    if (!confirmed) return;

    setState(() {
      saving = true;
    });

    try {
      await ref.read(gigRepositoryProvider).buyerCancelOrder(order!.id!);

      if (!mounted) return;

      showMessage('Order cancelled.');

      await loadOrder();
    } catch (e) {
      if (!mounted) return;

      showMessage('Unable to cancel order.', isError: true);
    } finally {
      if (mounted) {
        setState(() {
          saving = false;
        });
      }
    }
  }

  // =====================================================
  // Review
  // =====================================================

  Future<void> goToReview() async {
    if (order?.id == null) return;

    Navigator.pushNamed(
      context,
      AppRouter.review,
      arguments: order!.id,
    );
  }

  // =====================================================
  // Helpers
  // =====================================================

  void showMessage(String message, {bool isError = false}) {
    if (!mounted) return;

    ScaffoldMessenger.of(context)
      ..hideCurrentSnackBar()
      ..showSnackBar(
        SnackBar(
          content: Text(message),
          backgroundColor: isError ? Colors.red : Colors.green,
        ),
      );
  }

  Future<bool> showConfirmationDialog({
    required String title,
    required String message,
    required String confirmText,
    bool isDanger = false,
  }) async {
    final result = await showDialog<bool>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: Text(title),
          content: Text(message),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.pop(context, false);
              },
              child: const Text('No'),
            ),
            TextButton(
              onPressed: () {
                Navigator.pop(context, true);
              },
              child: Text(
                confirmText,
                style: TextStyle(color: isDanger ? Colors.red : null),
              ),
            ),
          ],
        );
      },
    );

    return result ?? false;
  }

  String formatDate(DateTime? date) {
    if (date == null) return '';

    final local = date.toLocal();

    String twoDigits(int value) {
      return value.toString().padLeft(2, '0');
    }

    return '${local.day}/${twoDigits(local.month)}/${local.year} '
        '${twoDigits(local.hour)}:${twoDigits(local.minute)}';
  }

  String formatPrice(double? price) {
    if (price == null) return '0.00';

    return price.toStringAsFixed(price.truncateToDouble() == price ? 0 : 2);
  }

  String? getGigImageUrl(String? fileName) {
    if (fileName == null || fileName.isEmpty) {
      return null;
    }

    return '${ApiConstants.gigImageUrl}$fileName';
  }

  String? getDeliveryFileUrl(String? fileName) {
    if (fileName == null || fileName.isEmpty) {
      return null;
    }

    // IMPORTANT:
    // deliveryFileUrl from backend contains ONLY the file name.
    return '${ApiConstants.gigDeliveryFileUrl}$fileName';
  }

  // =====================================================
  // Status UI
  // =====================================================

  Widget buildStatusContent() {
    if (order == null || order!.status == null) {
      return const SizedBox.shrink();
    }

    switch (order!.status!) {
      case GigOrderStatus.orderPlaced:
        return buildOrderPlaced();

      case GigOrderStatus.quoted:
        return buildQuoted();

      case GigOrderStatus.quoteAccepted:
        return buildQuoteAccepted();

      case GigOrderStatus.delivered:
        return buildDelivered();

      case GigOrderStatus.buyerCancelled:
        return buildBuyerCancelled();

      case GigOrderStatus.buyerRejected:
        return buildBuyerRejected();

      case GigOrderStatus.sellerDisputed:
        return buildSellerDisputed();

      case GigOrderStatus.buyerAccepted:
        return buildBuyerAccepted();

      case GigOrderStatus.paymentReleased:
        return buildPaymentReleased();

      case GigOrderStatus.refunded:
        return buildRefunded();

      case GigOrderStatus.sellerCancelled:
        return buildSellerCancelled();

      case GigOrderStatus.quoteRejected:
        return buildQuoteRejected();
    }
  }

  // =====================================================
  // ORDER PLACED
  // =====================================================

  Widget buildOrderPlaced() {
    return buildCard(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            'Waiting for Seller Quote',
            style: TextStyle(fontSize: 19, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 10),
          const Text('The seller has not sent a quotation yet.'),
          const SizedBox(height: 20),
          buildDangerButton(text: 'Cancel Order', onPressed: cancelOrder),
        ],
      ),
    );
  }

  // =====================================================
  // QUOTED
  // =====================================================

  Widget buildQuoted() {
    return buildCard(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            'Seller Quote',
            style: TextStyle(fontSize: 19, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 12),
          Text(
            formatPrice(order!.quotedPrice),
            style: const TextStyle(
              fontSize: 30,
              fontWeight: FontWeight.bold,
              color: Colors.green,
            ),
          ),
          const SizedBox(height: 8),
          const Text('Accept or reject the quotation.'),
          const SizedBox(height: 20),
          Row(
            children: [
              Expanded(
                child: buildSuccessButton(
                  text: 'Accept Quote',
                  onPressed: acceptQuote,
                ),
              ),
              const SizedBox(width: 10),
              Expanded(
                child: buildDangerButton(
                  text: 'Reject Quote',
                  onPressed: rejectQuote,
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  // =====================================================
  // QUOTE ACCEPTED
  // =====================================================

  Widget buildQuoteAccepted() {
    return buildCard(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            'Seller is working on your order.',
            style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 20),
          const Text(
            'Expected Delivery',
            style: TextStyle(fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 5),
          Text(
            formatDate(order!.expectedDeliveryAt),
            style: const TextStyle(fontSize: 16),
          ),
          const SizedBox(height: 20),
          buildDangerButton(text: 'Cancel Order', onPressed: cancelOrder),
        ],
      ),
    );
  }

  // =====================================================
  // DELIVERED
  // =====================================================

  Widget buildDelivered() {
    final deliveryUrl = getDeliveryFileUrl(order!.deliveryFileUrl);

    return buildCard(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Container(
            width: double.infinity,
            padding: const EdgeInsets.all(14),
            decoration: BoxDecoration(
              color: Colors.grey.shade100,
              borderRadius: BorderRadius.circular(8),
            ),
            child: const Text(
              'Delivery Received',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
          ),

          const SizedBox(height: 20),

          const Text(
            'Delivery Message',
            style: TextStyle(fontWeight: FontWeight.bold),
          ),

          const SizedBox(height: 8),

          Text(order!.deliveryMessage ?? ''),

          const SizedBox(height: 20),

          if (deliveryUrl != null)
            SizedBox(
              width: double.infinity,
              child: OutlinedButton.icon(
                onPressed: downloadDeliveryFile,
                icon: const Icon(Icons.download),
                label: const Text('Download Delivery'),
              ),
            ),

          const SizedBox(height: 10),

          Row(
            children: [
              Expanded(
                child: buildSuccessButton(
                  text: 'Accept Delivery',
                  onPressed: acceptDelivery,
                ),
              ),
              const SizedBox(width: 10),
              Expanded(
                child: buildDangerButton(
                  text: 'Reject Delivery',
                  onPressed: rejectDelivery,
                ),
              ),
            ],
          ),
        ],
      ),
    );
  }

  // =====================================================
  // BUYER CANCELLED
  // =====================================================

  Widget buildBuyerCancelled() {
    return buildWarningContainer(
      title: 'Order Cancelled',
      child: Text.rich(
        TextSpan(
          text: 'Waiting until ',
          children: [
            TextSpan(
              text: formatDate(order!.sellerDisputeDeadline),
              style: const TextStyle(fontWeight: FontWeight.bold),
            ),
            const TextSpan(text: ' for seller dispute.'),
          ],
        ),
      ),
    );
  }

  // =====================================================
  // BUYER REJECTED
  // =====================================================

  Widget buildBuyerRejected() {
    return buildWarningContainer(
      child: const Text(
        'Delivery rejected.\n\n'
        'Waiting for seller response.',
      ),
    );
  }

  // =====================================================
  // SELLER DISPUTED
  // =====================================================

  Widget buildSellerDisputed() {
    return buildInfoContainer(
      child: const Text(
        'Seller raised a dispute.\n\n'
        'Waiting for admin decision.',
      ),
    );
  }

  // =====================================================
  // BUYER ACCEPTED
  // =====================================================

  Widget buildBuyerAccepted() {
    return buildCard(
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          const Text(
            'Order Completed',
            style: TextStyle(fontSize: 19, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 10),
          const Text('Delivery accepted successfully.'),
          const SizedBox(height: 20),
          SizedBox(
            width: double.infinity,
            child: ElevatedButton(
              onPressed: goToReview,
              child: Text(reviewExists ? 'Edit Review' : 'Write Review'),
            ),
          ),
        ],
      ),
    );
  }

  // =====================================================
  // PAYMENT RELEASED
  // =====================================================

  Widget buildPaymentReleased() {
    return buildInfoContainer(
      child: const Text('Admin released payment to seller after dispute.'),
    );
  }

  // =====================================================
  // REFUNDED
  // =====================================================

  Widget buildRefunded() {
    return buildSuccessContainer(
      child: const Text('Your money has been refunded.'),
    );
  }

  // =====================================================
  // SELLER CANCELLED
  // =====================================================

  Widget buildSellerCancelled() {
    return buildSuccessContainer(
      child: const Text(
        'Seller cancelled the order.\n\n'
        'Your payment has been refunded.',
      ),
    );
  }

  // =====================================================
  // QUOTE REJECTED
  // =====================================================

  Widget buildQuoteRejected() {
    return buildInfoContainer(
      child: const Text('You rejected the seller\'s quotation.'),
    );
  }

  // =====================================================
  // Common UI
  // =====================================================

  Widget buildCard({required Widget child}) {
    return Card(
      margin: const EdgeInsets.only(bottom: 16),
      elevation: 1,
      child: Padding(padding: const EdgeInsets.all(16), child: child),
    );
  }

  Widget buildDangerButton({
    required String text,
    required VoidCallback onPressed,
  }) {
    return ElevatedButton(
      onPressed: saving ? null : onPressed,
      style: ElevatedButton.styleFrom(
        backgroundColor: Colors.red,
        foregroundColor: Colors.white,
      ),
      child: saving
          ? const SizedBox(
              height: 20,
              width: 20,
              child: CircularProgressIndicator(
                strokeWidth: 2,
                color: Colors.white,
              ),
            )
          : Text(text),
    );
  }

  Widget buildSuccessButton({
    required String text,
    required VoidCallback onPressed,
  }) {
    return ElevatedButton(
      onPressed: saving ? null : onPressed,
      style: ElevatedButton.styleFrom(
        backgroundColor: Colors.green,
        foregroundColor: Colors.white,
      ),
      child: saving
          ? const SizedBox(
              height: 20,
              width: 20,
              child: CircularProgressIndicator(
                strokeWidth: 2,
                color: Colors.white,
              ),
            )
          : Text(text),
    );
  }

  Widget buildWarningContainer({String? title, required Widget child}) {
    return Container(
      width: double.infinity,
      margin: const EdgeInsets.only(bottom: 16),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.amber.shade50,
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: Colors.amber.shade200),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          if (title != null) ...[
            Text(
              title,
              style: const TextStyle(fontWeight: FontWeight.bold, fontSize: 18),
            ),
            const SizedBox(height: 10),
          ],
          child,
        ],
      ),
    );
  }

  Widget buildInfoContainer({required Widget child}) {
    return Container(
      width: double.infinity,
      margin: const EdgeInsets.only(bottom: 16),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.blue.shade50,
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: Colors.blue.shade200),
      ),
      child: child,
    );
  }

  Widget buildSuccessContainer({required Widget child}) {
    return Container(
      width: double.infinity,
      margin: const EdgeInsets.only(bottom: 16),
      padding: const EdgeInsets.all(16),
      decoration: BoxDecoration(
        color: Colors.green.shade50,
        borderRadius: BorderRadius.circular(8),
        border: Border.all(color: Colors.green.shade200),
      ),
      child: child,
    );
  }

  // =====================================================
  // Build
  // =====================================================

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Gig Order Details')),
      body: loading && order == null
          ? const Center(child: CircularProgressIndicator())
          : order == null
          ? const Center(child: Text('Unable to load order.'))
          : RefreshIndicator(
              onRefresh: loadOrder,
              child: SingleChildScrollView(
                physics: const AlwaysScrollableScrollPhysics(),
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    // =================================
                    // Header
                    // =================================
                    buildCard(
                      child: Row(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          SizedBox(
                            width: 90,
                            height: 90,
                            child: ClipRRect(
                              borderRadius: BorderRadius.circular(8),
                              child: getGigImageUrl(order!.gigImage) != null
                                  ? Image.network(
                                      getGigImageUrl(order!.gigImage)!,
                                      fit: BoxFit.cover,
                                      errorBuilder:
                                          (context, error, stackTrace) {
                                            return Container(
                                              color: Colors.grey.shade200,
                                              child: const Icon(
                                                Icons.image,
                                                size: 40,
                                              ),
                                            );
                                          },
                                    )
                                  : Container(
                                      color: Colors.grey.shade200,
                                      child: const Icon(Icons.image, size: 40),
                                    ),
                            ),
                          ),

                          const SizedBox(width: 16),

                          Expanded(
                            child: Column(
                              crossAxisAlignment: CrossAxisAlignment.start,
                              children: [
                                Text(
                                  order!.gigTitle ?? '',
                                  style: const TextStyle(
                                    fontSize: 20,
                                    fontWeight: FontWeight.bold,
                                  ),
                                ),

                                const SizedBox(height: 8),

                                Text.rich(
                                  TextSpan(
                                    text: 'Seller: ',
                                    children: [
                                      TextSpan(
                                        text: order!.sellerName ?? '',
                                        style: const TextStyle(
                                          fontWeight: FontWeight.bold,
                                        ),
                                      ),
                                    ],
                                  ),
                                ),

                                const SizedBox(height: 6),

                                Row(
                                  children: [
                                    const Text('Status: '),
                                    Container(
                                      padding: const EdgeInsets.symmetric(
                                        horizontal: 8,
                                        vertical: 4,
                                      ),
                                      decoration: BoxDecoration(
                                        color: Colors.blue,
                                        borderRadius: BorderRadius.circular(12),
                                      ),
                                      child: Text(
                                        order!.status!.toJson(),
                                        style: const TextStyle(
                                          color: Colors.white,
                                          fontSize: 12,
                                          fontWeight: FontWeight.bold,
                                        ),
                                      ),
                                    ),
                                  ],
                                ),

                                const SizedBox(height: 6),

                                Text('Order #${order!.id}'),
                              ],
                            ),
                          ),
                        ],
                      ),
                    ),

                    // =================================
                    // Status-specific content
                    // =================================
                    buildStatusContent(),
                  ],
                ),
              ),
            ),
    );
  }
}
