class GigResponseDTO {
  final int? id;
  final String? title;
  final String? shortDescription;
  final String? description;
  final double? startingPrice;
  final int? deliveryDays;
  final int? revisions;
  final String? gigImage;
  final bool? isActive;
  final DateTime? createdAt;
  final DateTime? updatedAt;
  final int? categoryId;
  final String? categoryName;
  final int? userProfileId;
  final String? userName;
  final double? averageRating;
  final int? totalReviews;
  final int? completedOrders;

  const GigResponseDTO({
    this.id,
    this.title,
    this.shortDescription,
    this.description,
    this.startingPrice,
    this.deliveryDays,
    this.revisions,
    this.gigImage,
    this.isActive,
    this.createdAt,
    this.updatedAt,
    this.categoryId,
    this.categoryName,
    this.userProfileId,
    this.userName,
    this.averageRating,
    this.totalReviews,
    this.completedOrders,
  });

  /// Factory constructor to create [GigResponseDTO] from a JSON map.
  factory GigResponseDTO.fromJson(Map<String, dynamic> json) {
    return GigResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      title: json['title'] as String?,
      shortDescription: json['shortDescription'] as String?,
      description: json['description'] as String?,
      startingPrice: (json['startingPrice'] as num?)?.toDouble(),
      deliveryDays: (json['deliveryDays'] as num?)?.toInt(),
      revisions: (json['revisions'] as num?)?.toInt(),
      gigImage: json['gigImage'] as String?,
      isActive: json['isActive'] as bool?,
      createdAt: json['createdAt'] != null
          ? DateTime.tryParse(json['createdAt'] as String)
          : null,
      updatedAt: json['updatedAt'] != null
          ? DateTime.tryParse(json['updatedAt'] as String)
          : null,
      categoryId: (json['categoryId'] as num?)?.toInt(),
      categoryName: json['categoryName'] as String?,
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
      userName: json['userName'] as String?,
      averageRating: (json['averageRating'] as num?)?.toDouble(),
      totalReviews: (json['totalReviews'] as num?)?.toInt(),
      completedOrders: (json['completedOrders'] as num?)?.toInt(),
    );
  }

  /// Converts this [GigResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (title != null) 'title': title,
      if (shortDescription != null) 'shortDescription': shortDescription,
      if (description != null) 'description': description,
      if (startingPrice != null) 'startingPrice': startingPrice,
      if (deliveryDays != null) 'deliveryDays': deliveryDays,
      if (revisions != null) 'revisions': revisions,
      if (gigImage != null) 'gigImage': gigImage,
      if (isActive != null) 'isActive': isActive,
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
      if (updatedAt != null) 'updatedAt': updatedAt!.toIso8601String(),
      if (categoryId != null) 'categoryId': categoryId,
      if (categoryName != null) 'categoryName': categoryName,
      if (userProfileId != null) 'userProfileId': userProfileId,
      if (userName != null) 'userName': userName,
      if (averageRating != null) 'averageRating': averageRating,
      if (totalReviews != null) 'totalReviews': totalReviews,
      if (completedOrders != null) 'completedOrders': completedOrders,
    };
  }

  /// Helper method to create a modified copy of this object.
  GigResponseDTO copyWith({
    int? id,
    String? title,
    String? shortDescription,
    String? description,
    double? startingPrice,
    int? deliveryDays,
    int? revisions,
    String? gigImage,
    bool? isActive,
    DateTime? createdAt,
    DateTime? updatedAt,
    int? categoryId,
    String? categoryName,
    int? userProfileId,
    String? userName,
    double? averageRating,
    int? totalReviews,
    int? completedOrders,
  }) {
    return GigResponseDTO(
      id: id ?? this.id,
      title: title ?? this.title,
      shortDescription: shortDescription ?? this.shortDescription,
      description: description ?? this.description,
      startingPrice: startingPrice ?? this.startingPrice,
      deliveryDays: deliveryDays ?? this.deliveryDays,
      revisions: revisions ?? this.revisions,
      gigImage: gigImage ?? this.gigImage,
      isActive: isActive ?? this.isActive,
      createdAt: createdAt ?? this.createdAt,
      updatedAt: updatedAt ?? this.updatedAt,
      categoryId: categoryId ?? this.categoryId,
      categoryName: categoryName ?? this.categoryName,
      userProfileId: userProfileId ?? this.userProfileId,
      userName: userName ?? this.userName,
      averageRating: averageRating ?? this.averageRating,
      totalReviews: totalReviews ?? this.totalReviews,
      completedOrders: completedOrders ?? this.completedOrders,
    );
  }
}
