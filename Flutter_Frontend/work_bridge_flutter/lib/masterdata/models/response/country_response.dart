class CountryResponseDTO {
  final int? countryId;
  final String? countryName;
  final String? countryCode;

  const CountryResponseDTO({
    this.countryId,
    this.countryName,
    this.countryCode,
  });

  /// Factory constructor to create [CountryResponseDTO] from a JSON map.
  factory CountryResponseDTO.fromJson(Map<String, dynamic> json) {
    return CountryResponseDTO(
      countryId: (json['countryId'] as num?)?.toInt(),
      countryName: json['countryName'] as String?,
      countryCode: json['countryCode'] as String?,
    );
  }

  /// Converts this [CountryResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (countryId != null) 'countryId': countryId,
      if (countryName != null) 'countryName': countryName,
      if (countryCode != null) 'countryCode': countryCode,
    };
  }

  /// Helper method to create a modified copy of this object.
  CountryResponseDTO copyWith({
    int? countryId,
    String? countryName,
    String? countryCode,
  }) {
    return CountryResponseDTO(
      countryId: countryId ?? this.countryId,
      countryName: countryName ?? this.countryName,
      countryCode: countryCode ?? this.countryCode,
    );
  }
}