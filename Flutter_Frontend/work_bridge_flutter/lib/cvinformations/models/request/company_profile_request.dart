class CompanyProfileRequestDTO {
  final int? userId;
  final String? name;
  final String? phone;
  final String? companyEmail;
  final String? companyDescription;
  final String? companyWebsite;
  final String? industry;
  final String? foundedYear;
  final String? tradeLicenseNumber;
  final int? locationId;
  final String? locationDetails;
  final String? locationPostCode;
  final int? locationPoliceStationId;

  const CompanyProfileRequestDTO({
    this.userId,
    this.name,
    this.phone,
    this.companyEmail,
    this.companyDescription,
    this.companyWebsite,
    this.industry,
    this.foundedYear,
    this.tradeLicenseNumber,
    this.locationId,
    this.locationDetails,
    this.locationPostCode,
    this.locationPoliceStationId,
  });

  /// Factory constructor to create [CompanyProfileRequestDTO] from a JSON map.
  factory CompanyProfileRequestDTO.fromJson(Map<String, dynamic> json) {
    return CompanyProfileRequestDTO(
      userId: (json['userId'] as num?)?.toInt(),
      name: json['name'] as String?,
      phone: json['phone'] as String?,
      companyEmail: json['companyEmail'] as String?,
      companyDescription: json['companyDescription'] as String?,
      companyWebsite: json['companyWebsite'] as String?,
      industry: json['industry'] as String?,
      foundedYear: json['foundedYear'] as String?,
      tradeLicenseNumber: json['tradeLicenseNumber'] as String?,
      locationId: (json['locationId'] as num?)?.toInt(),
      locationDetails: json['locationDetails'] as String?,
      locationPostCode: json['locationPostCode'] as String?,
      locationPoliceStationId:
      (json['locationPoliceStationId'] as num?)?.toInt(),
    );
  }

  /// Converts this [CompanyProfileRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (userId != null) 'userId': userId,
      if (name != null) 'name': name,
      if (phone != null) 'phone': phone,
      if (companyEmail != null) 'companyEmail': companyEmail,
      if (companyDescription != null) 'companyDescription': companyDescription,
      if (companyWebsite != null) 'companyWebsite': companyWebsite,
      if (industry != null) 'industry': industry,
      if (foundedYear != null) 'foundedYear': foundedYear,
      if (tradeLicenseNumber != null) 'tradeLicenseNumber': tradeLicenseNumber,
      if (locationId != null) 'locationId': locationId,
      if (locationDetails != null) 'locationDetails': locationDetails,
      if (locationPostCode != null) 'locationPostCode': locationPostCode,
      if (locationPoliceStationId != null)
        'locationPoliceStationId': locationPoliceStationId,
    };
  }

  /// Helper method to create a modified copy of this object.
  CompanyProfileRequestDTO copyWith({
    int? userId,
    String? name,
    String? phone,
    String? companyEmail,
    String? companyDescription,
    String? companyWebsite,
    String? industry,
    String? foundedYear,
    String? tradeLicenseNumber,
    int? locationId,
    String? locationDetails,
    String? locationPostCode,
    int? locationPoliceStationId,
  }) {
    return CompanyProfileRequestDTO(
      userId: userId ?? this.userId,
      name: name ?? this.name,
      phone: phone ?? this.phone,
      companyEmail: companyEmail ?? this.companyEmail,
      companyDescription: companyDescription ?? this.companyDescription,
      companyWebsite: companyWebsite ?? this.companyWebsite,
      industry: industry ?? this.industry,
      foundedYear: foundedYear ?? this.foundedYear,
      tradeLicenseNumber: tradeLicenseNumber ?? this.tradeLicenseNumber,
      locationId: locationId ?? this.locationId,
      locationDetails: locationDetails ?? this.locationDetails,
      locationPostCode: locationPostCode ?? this.locationPostCode,
      locationPoliceStationId:
      locationPoliceStationId ?? this.locationPoliceStationId,
    );
  }
}