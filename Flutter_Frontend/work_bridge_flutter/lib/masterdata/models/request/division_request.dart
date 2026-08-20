class DivisionRequestDTO {
  final String? divisionName;
  final int? countryId;

  const DivisionRequestDTO({
    this.divisionName,
    this.countryId,
  });

  /// Factory constructor to create [DivisionRequestDTO] from a JSON map.
  factory DivisionRequestDTO.fromJson(Map<String, dynamic> json) {
    return DivisionRequestDTO(
      divisionName: json['divisionName'] as String?,
      countryId: (json['countryId'] as num?)?.toInt(),
    );
  }

  /// Converts this [DivisionRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (divisionName != null) 'divisionName': divisionName,
      if (countryId != null) 'countryId': countryId,
    };
  }

  /// Helper method to create a modified copy of this object.
  DivisionRequestDTO copyWith({
    String? divisionName,
    int? countryId,
  }) {
    return DivisionRequestDTO(
      divisionName: divisionName ?? this.divisionName,
      countryId: countryId ?? this.countryId,
    );
  }
}