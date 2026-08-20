class DivisionResponseDTO {
  final int? divisionId;
  final String? divisionName;
  final int? countryId;
  final String? countryName;
  final String? countryCode;

  const DivisionResponseDTO({
    this.divisionId,
    this.divisionName,
    this.countryId,
    this.countryName,
    this.countryCode,
  });

  /// Factory constructor to create [DivisionResponseDTO] from a JSON map.
  factory DivisionResponseDTO.fromJson(Map<String, dynamic> json) {
    return DivisionResponseDTO(
      divisionId: (json['divisionId'] as num?)?.toInt(),
      divisionName: json['divisionName'] as String?,
      countryId: (json['countryId'] as num?)?.toInt(),
      countryName: json['countryName'] as String?,
      countryCode: json['countryCode'] as String?,
    );
  }

  /// Converts this [DivisionResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (divisionId != null) 'divisionId': divisionId,
      if (divisionName != null) 'divisionName': divisionName,
      if (countryId != null) 'countryId': countryId,
      if (countryName != null) 'countryName': countryName,
      if (countryCode != null) 'countryCode': countryCode,
    };
  }

  /// Helper method to create a modified copy of this object.
  DivisionResponseDTO copyWith({
    int? divisionId,
    String? divisionName,
    int? countryId,
    String? countryName,
    String? countryCode,
  }) {
    return DivisionResponseDTO(
      divisionId: divisionId ?? this.divisionId,
      divisionName: divisionName ?? this.divisionName,
      countryId: countryId ?? this.countryId,
      countryName: countryName ?? this.countryName,
      countryCode: countryCode ?? this.countryCode,
    );
  }
}