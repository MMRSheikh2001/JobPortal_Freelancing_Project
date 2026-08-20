class DistrictRequestDTO {
  final String? districtName;
  final int? divisionId;

  const DistrictRequestDTO({
    this.districtName,
    this.divisionId,
  });

  /// Factory constructor to create [DistrictRequestDTO] from a JSON map.
  factory DistrictRequestDTO.fromJson(Map<String, dynamic> json) {
    return DistrictRequestDTO(
      districtName: json['districtName'] as String?,
      divisionId: (json['divisionId'] as num?)?.toInt(),
    );
  }

  /// Converts this [DistrictRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (districtName != null) 'districtName': districtName,
      if (divisionId != null) 'divisionId': divisionId,
    };
  }

  /// Helper method to create a modified copy of this object.
  DistrictRequestDTO copyWith({
    String? districtName,
    int? divisionId,
  }) {
    return DistrictRequestDTO(
      districtName: districtName ?? this.districtName,
      divisionId: divisionId ?? this.divisionId,
    );
  }
}