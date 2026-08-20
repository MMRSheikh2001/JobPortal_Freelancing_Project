class PoliceStationResponseDTO {
  final int? policeStationId;
  final String? policeStationName;
  final int? districtId;
  final String? districtName;
  final int? divisionId;
  final String? divisionName;
  final int? countryId;
  final String? countryName;
  final String? countryCode;

  const PoliceStationResponseDTO({
    this.policeStationId,
    this.policeStationName,
    this.districtId,
    this.districtName,
    this.divisionId,
    this.divisionName,
    this.countryId,
    this.countryName,
    this.countryCode,
  });

  /// Factory constructor to create [PoliceStationResponseDTO] from a JSON map.
  factory PoliceStationResponseDTO.fromJson(Map<String, dynamic> json) {
    return PoliceStationResponseDTO(
      policeStationId: (json['policeStationId'] as num?)?.toInt(),
      policeStationName: json['policeStationName'] as String?,
      districtId: (json['districtId'] as num?)?.toInt(),
      districtName: json['districtName'] as String?,
      divisionId: (json['divisionId'] as num?)?.toInt(),
      divisionName: json['divisionName'] as String?,
      countryId: (json['countryId'] as num?)?.toInt(),
      countryName: json['countryName'] as String?,
      countryCode: json['countryCode'] as String?,
    );
  }

  /// Converts this [PoliceStationResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (policeStationId != null) 'policeStationId': policeStationId,
      if (policeStationName != null) 'policeStationName': policeStationName,
      if (districtId != null) 'districtId': districtId,
      if (districtName != null) 'districtName': districtName,
      if (divisionId != null) 'divisionId': divisionId,
      if (divisionName != null) 'divisionName': divisionName,
      if (countryId != null) 'countryId': countryId,
      if (countryName != null) 'countryName': countryName,
      if (countryCode != null) 'countryCode': countryCode,
    };
  }

  /// Helper method to create a modified copy of this object.
  PoliceStationResponseDTO copyWith({
    int? policeStationId,
    String? policeStationName,
    int? districtId,
    String? districtName,
    int? divisionId,
    String? divisionName,
    int? countryId,
    String? countryName,
    String? countryCode,
  }) {
    return PoliceStationResponseDTO(
      policeStationId: policeStationId ?? this.policeStationId,
      policeStationName: policeStationName ?? this.policeStationName,
      districtId: districtId ?? this.districtId,
      districtName: districtName ?? this.districtName,
      divisionId: divisionId ?? this.divisionId,
      divisionName: divisionName ?? this.divisionName,
      countryId: countryId ?? this.countryId,
      countryName: countryName ?? this.countryName,
      countryCode: countryCode ?? this.countryCode,
    );
  }
}