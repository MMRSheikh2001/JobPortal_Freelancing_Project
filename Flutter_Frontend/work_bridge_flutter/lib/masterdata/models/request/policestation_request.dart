class PoliceStationRequestDTO {
  final String? policeStationName;
  final int? districtId;

  const PoliceStationRequestDTO({
    this.policeStationName,
    this.districtId,
  });

  /// Factory constructor to create [PoliceStationRequestDTO] from a JSON map.
  factory PoliceStationRequestDTO.fromJson(Map<String, dynamic> json) {
    return PoliceStationRequestDTO(
      policeStationName: json['policeStationName'] as String?,
      districtId: (json['districtId'] as num?)?.toInt(),
    );
  }

  /// Converts this [PoliceStationRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (policeStationName != null) 'policeStationName': policeStationName,
      if (districtId != null) 'districtId': districtId,
    };
  }

  /// Helper method to create a modified copy of this object.
  PoliceStationRequestDTO copyWith({
    String? policeStationName,
    int? districtId,
  }) {
    return PoliceStationRequestDTO(
      policeStationName: policeStationName ?? this.policeStationName,
      districtId: districtId ?? this.districtId,
    );
  }
}