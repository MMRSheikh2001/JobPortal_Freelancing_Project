class CountryRequestDTO {
  final String? countryName;
  final String? countryCode;

  const CountryRequestDTO({
    this.countryName,
    this.countryCode,
  });

  /// Factory constructor to create [CountryRequestDTO] from a JSON map.
  factory CountryRequestDTO.fromJson(Map<String, dynamic> json) {
    return CountryRequestDTO(
      countryName: json['countryName'] as String?,
      countryCode: json['countryCode'] as String?,
    );
  }

  /// Converts this [CountryRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (countryName != null) 'countryName': countryName,
      if (countryCode != null) 'countryCode': countryCode,
    };
  }

  /// Helper method to create a modified copy of this object.
  CountryRequestDTO copyWith({
    String? countryName,
    String? countryCode,
  }) {
    return CountryRequestDTO(
      countryName: countryName ?? this.countryName,
      countryCode: countryCode ?? this.countryCode,
    );
  }
}