
enum GigOrderStatus {
  orderPlaced,
  quoted,
  quoteAccepted,
  quoteRejected,
  delivered,
  buyerAccepted,
  buyerRejected,
  buyerCancelled,
  sellerCancelled,
  sellerDisputed,
  paymentReleased,
  refunded;

  /// Map Dart enum values back to backend JSON string representation.
  String toJson() {
    switch (this) {
      case GigOrderStatus.orderPlaced:
        return 'ORDER_PLACED';
      case GigOrderStatus.quoted:
        return 'QUOTED';
      case GigOrderStatus.quoteAccepted:
        return 'QUOTE_ACCEPTED';
      case GigOrderStatus.quoteRejected:
        return 'QUOTE_REJECTED';
      case GigOrderStatus.delivered:
        return 'DELIVERED';
      case GigOrderStatus.buyerAccepted:
        return 'BUYER_ACCEPTED';
      case GigOrderStatus.buyerRejected:
        return 'BUYER_REJECTED';
      case GigOrderStatus.buyerCancelled:
        return 'BUYER_CANCELLED';
      case GigOrderStatus.sellerCancelled:
        return 'SELLER_CANCELLED';
      case GigOrderStatus.sellerDisputed:
        return 'SELLER_DISPUTED';
      case GigOrderStatus.paymentReleased:
        return 'PAYMENT_RELEASED';
      case GigOrderStatus.refunded:
        return 'REFUNDED';
    }
  }

  /// Map JSON string value to [GigOrderStatus] enum safely.
  static GigOrderStatus? fromJson(String? value) {
    if (value == null) return null;
    switch (value.toUpperCase()) {
      case 'ORDER_PLACED':
        return GigOrderStatus.orderPlaced;
      case 'QUOTED':
        return GigOrderStatus.quoted;
      case 'QUOTE_ACCEPTED':
        return GigOrderStatus.quoteAccepted;
      case 'QUOTE_REJECTED':
        return GigOrderStatus.quoteRejected;
      case 'DELIVERED':
        return GigOrderStatus.delivered;
      case 'BUYER_ACCEPTED':
        return GigOrderStatus.buyerAccepted;
      case 'BUYER_REJECTED':
        return GigOrderStatus.buyerRejected;
      case 'BUYER_CANCELLED':
        return GigOrderStatus.buyerCancelled;
      case 'SELLER_CANCELLED':
        return GigOrderStatus.sellerCancelled;
      case 'SELLER_DISPUTED':
        return GigOrderStatus.sellerDisputed;
      case 'PAYMENT_RELEASED':
        return GigOrderStatus.paymentReleased;
      case 'REFUNDED':
        return GigOrderStatus.refunded;
      default:
        return null;
    }
  }
}