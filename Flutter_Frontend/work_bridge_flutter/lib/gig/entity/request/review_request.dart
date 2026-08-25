import 'dart:convert';

class ReviewRequestDTO {
  final int rating;
  final String? comment;
  final int? gigOrderId;

  ReviewRequestDTO({
    required this.rating,
    this.comment,
    this.gigOrderId,
  }) {
    // Mimics Java @NotNull, @Min(1), @Max(5) annotations
    assert(rating >= 1 && rating <= 5, 'Rating must be between 1 and 5');
  }

  /// Converts the DTO instance into a Map for HTTP requests
  Map<String, dynamic> toJson() {
    return {
      'rating': rating,
      'comment': comment,
      'gigOrderId': gigOrderId,
    };
  }

  /// Optional helper to convert directly to a JSON string
  String toRawJson() => json.encode(toJson());

  /// Creates a copy with modified fields (convenience method)
  ReviewRequestDTO copyWith({
    int? rating,
    String? comment,
    int? gigOrderId,
  }) {
    return ReviewRequestDTO(
      rating: rating ?? this.rating,
      comment: comment ?? this.comment,
      gigOrderId: gigOrderId ?? this.gigOrderId,
    );
  }
}