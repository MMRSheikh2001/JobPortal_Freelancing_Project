import 'dart:convert';

class ReviewResponseDTO {
  final int? id;
  final int? rating;
  final String? comment;
  final DateTime? createdAt;
  final int? gigOrderId;
  final int? reviewerId;
  final String? reviewerName;
  final int? sellerUserProfileId;
  final String? sellerName;
  final int? gigId;
  final String? gigTitle;

  const ReviewResponseDTO({
    this.id,
    this.rating,
    this.comment,
    this.createdAt,
    this.gigOrderId,
    this.reviewerId,
    this.reviewerName,
    this.sellerUserProfileId,
    this.sellerName,
    this.gigId,
    this.gigTitle,
  });

  /// Factory constructor to parse JSON data received from the backend
  factory ReviewResponseDTO.fromJson(Map<String, dynamic> json) {
    return ReviewResponseDTO(
      id: json['id'] as int?,
      rating: json['rating'] as int?,
      comment: json['comment'] as String?,
      createdAt: json['createdAt'] != null
          ? DateTime.tryParse(json['createdAt'].toString())
          : null,
      gigOrderId: json['gigOrderId'] as int?,
      reviewerId: json['reviewerId'] as int?,
      reviewerName: json['reviewerName'] as String?,
      sellerUserProfileId: json['sellerUserProfileId'] as int?,
      sellerName: json['sellerName'] as String?,
      gigId: json['gigId'] as int?,
      gigTitle: json['gigTitle'] as String?,
    );
  }

  /// Converts object back to JSON map if needed
  Map<String, dynamic> toJson() {
    return {
      'id': id,
      'rating': rating,
      'comment': comment,
      'createdAt': createdAt?.toIso8601String(),
      'gigOrderId': gigOrderId,
      'reviewerId': reviewerId,
      'reviewerName': reviewerName,
      'sellerUserProfileId': sellerUserProfileId,
      'sellerName': sellerName,
      'gigId': gigId,
      'gigTitle': gigTitle,
    };
  }

  /// Helper method for parsing from raw JSON string
  factory ReviewResponseDTO.fromRawJson(String str) =>
      ReviewResponseDTO.fromJson(json.decode(str) as Map<String, dynamic>);
}
