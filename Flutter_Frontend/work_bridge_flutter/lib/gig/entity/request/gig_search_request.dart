class GigSearchRequestDTO {
  final String? keyword;
  final int? categoryId;
  final double? minPrice;
  final double? maxPrice;
  final int? maxDeliveryDays;
  final bool? active;
  final int? minimumRating;
  final int? minimumOrders;

  const GigSearchRequestDTO({
    this.keyword,
    this.categoryId,
    this.minPrice,
    this.maxPrice,
    this.maxDeliveryDays,
    this.active,
    this.minimumRating,
    this.minimumOrders,
  });

  /// Factory constructor to create [GigSearchRequestDTO] from a JSON map.
  factory GigSearchRequestDTO.fromJson(Map<String, dynamic> json) {
    return GigSearchRequestDTO(
      keyword: json['keyword'] as String?,
      categoryId: (json['categoryId'] as num?)?.toInt(),
      minPrice: (json['minPrice'] as num?)?.toDouble(),
      maxPrice: (json['maxPrice'] as num?)?.toDouble(),
      maxDeliveryDays: (json['maxDeliveryDays'] as num?)?.toInt(),
      active: json['active'] as bool?,
      minimumRating: (json['minimumRating'] as num?)?.toInt(),
      minimumOrders: (json['minimumOrders'] as num?)?.toInt(),
    );
  }

  /// Converts this [GigSearchRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (keyword != null) 'keyword': keyword,
      if (categoryId != null) 'categoryId': categoryId,
      if (minPrice != null) 'minPrice': minPrice,
      if (maxPrice != null) 'maxPrice': maxPrice,
      if (maxDeliveryDays != null) 'maxDeliveryDays': maxDeliveryDays,
      if (active != null) 'active': active,
      if (minimumRating != null) 'minimumRating': minimumRating,
      if (minimumOrders != null) 'minimumOrders': minimumOrders,
    };
  }

  /// Helper method to create a modified copy of this object.
  GigSearchRequestDTO copyWith({
    String? keyword,
    int? categoryId,
    double? minPrice,
    double? maxPrice,
    int? maxDeliveryDays,
    bool? active,
    int? minimumRating,
    int? minimumOrders,
  }) {
    return GigSearchRequestDTO(
      keyword: keyword ?? this.keyword,
      categoryId: categoryId ?? this.categoryId,
      minPrice: minPrice ?? this.minPrice,
      maxPrice: maxPrice ?? this.maxPrice,
      maxDeliveryDays: maxDeliveryDays ?? this.maxDeliveryDays,
      active: active ?? this.active,
      minimumRating: minimumRating ?? this.minimumRating,
      minimumOrders: minimumOrders ?? this.minimumOrders,
    );
  }
}