class CompanyProfileResponseDTO {
  final int? id;
  final int? userId;
  final String? userEmail;
  final String? name;
  final String? phone;
  final String? companyEmail;
  final String? image;
  final String? companyDescription;
  final String? companyWebsite;
  final String? industry;
  final String? foundedYear;
  final String? tradeLicenseNumber;
  final DateTime? createdAt;
  final DateTime? updatedAt;

  // Location Details
  final int? locationId;
  final String? locationDetails;
  final String? locationPostCode;
  final int? locationCountryId;
  final String? locationCountryName;
  final String? locationCountryCode;
  final int? locationDivisionId;
  final String? locationDivisionName;
  final int? locationDistrictId;
  final String? locationDistrictName;
  final int? locationPoliceStationId;
  final String? locationPoliceStationName;

  const CompanyProfileResponseDTO({
    this.id,
    this.userId,
    this.userEmail,
    this.name,
    this.phone,
    this.companyEmail,
    this.image,
    this.companyDescription,
    this.companyWebsite,
    this.industry,
    this.foundedYear,
    this.tradeLicenseNumber,
    this.createdAt,
    this.updatedAt,
    this.locationId,
    this.locationDetails,
    this.locationPostCode,
    this.locationCountryId,
    this.locationCountryName,
    this.locationCountryCode,
    this.locationDivisionId,
    this.locationDivisionName,
    this.locationDistrictId,
    this.locationDistrictName,
    this.locationPoliceStationId,
    this.locationPoliceStationName,
  });

  /// Factory constructor to create [CompanyProfileResponseDTO] from a JSON map.
  factory CompanyProfileResponseDTO.fromJson(Map<String, dynamic> json) {
    return CompanyProfileResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      userId: (json['userId'] as num?)?.toInt(),
      userEmail: json['userEmail'] as String?,
      name: json['name'] as String?,
      phone: json['phone'] as String?,
      companyEmail: json['companyEmail'] as String?,
      image: json['image'] as String?,
      companyDescription: json['companyDescription'] as String?,
      companyWebsite: json['companyWebsite'] as String?,
      industry: json['industry'] as String?,
      foundedYear: json['foundedYear'] as String?,
      tradeLicenseNumber: json['tradeLicenseNumber'] as String?,
      createdAt: json['createdAt'] != null
          ? DateTime.tryParse(json['createdAt'] as String)
          : null,
      updatedAt: json['updatedAt'] != null
          ? DateTime.tryParse(json['updatedAt'] as String)
          : null,
      locationId: (json['locationId'] as num?)?.toInt(),
      locationDetails: json['locationDetails'] as String?,
      locationPostCode: json['locationPostCode'] as String?,
      locationCountryId: (json['locationCountryId'] as num?)?.toInt(),
      locationCountryName: json['locationCountryName'] as String?,
      locationCountryCode: json['locationCountryCode'] as String?,
      locationDivisionId: (json['locationDivisionId'] as num?)?.toInt(),
      locationDivisionName: json['locationDivisionName'] as String?,
      locationDistrictId: (json['locationDistrictId'] as num?)?.toInt(),
      locationDistrictName: json['locationDistrictName'] as String?,
      locationPoliceStationId:
      (json['locationPoliceStationId'] as num?)?.toInt(),
      locationPoliceStationName:
      json['locationPoliceStationName'] as String?,
    );
  }

  /// Converts this [CompanyProfileResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (userId != null) 'userId': userId,
      if (userEmail != null) 'userEmail': userEmail,
      if (name != null) 'name': name,
      if (phone != null) 'phone': phone,
      if (companyEmail != null) 'companyEmail': companyEmail,
      if (image != null) 'image': image,
      if (companyDescription != null) 'companyDescription': companyDescription,
      if (companyWebsite != null) 'companyWebsite': companyWebsite,
      if (industry != null) 'industry': industry,
      if (foundedYear != null) 'foundedYear': foundedYear,
      if (tradeLicenseNumber != null) 'tradeLicenseNumber': tradeLicenseNumber,
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
      if (updatedAt != null) 'updatedAt': updatedAt!.toIso8601String(),
      if (locationId != null) 'locationId': locationId,
      if (locationDetails != null) 'locationDetails': locationDetails,
      if (locationPostCode != null) 'locationPostCode': locationPostCode,
      if (locationCountryId != null) 'locationCountryId': locationCountryId,
      if (locationCountryName != null)
        'locationCountryName': locationCountryName,
      if (locationCountryCode != null)
        'locationCountryCode': locationCountryCode,
      if (locationDivisionId != null)
        'locationDivisionId': locationDivisionId,
      if (locationDivisionName != null)
        'locationDivisionName': locationDivisionName,
      if (locationDistrictId != null)
        'locationDistrictId': locationDistrictId,
      if (locationDistrictName != null)
        'locationDistrictName': locationDistrictName,
      if (locationPoliceStationId != null)
        'locationPoliceStationId': locationPoliceStationId,
      if (locationPoliceStationName != null)
        'locationPoliceStationName': locationPoliceStationName,
    };
  }

  /// Helper method to create a modified copy of this object.
  CompanyProfileResponseDTO copyWith({
    int? id,
    int? userId,
    String? userEmail,
    String? name,
    String? phone,
    String? companyEmail,
    String? image,
    String? companyDescription,
    String? companyWebsite,
    String? industry,
    String? foundedYear,
    String? tradeLicenseNumber,
    DateTime? createdAt,
    DateTime? updatedAt,
    int? locationId,
    String? locationDetails,
    String? locationPostCode,
    int? locationCountryId,
    String? locationCountryName,
    String? locationCountryCode,
    int? locationDivisionId,
    String? locationDivisionName,
    int? locationDistrictId,
    String? locationDistrictName,
    int? locationPoliceStationId,
    String? locationPoliceStationName,
  }) {
    return CompanyProfileResponseDTO(
      id: id ?? this.id,
      userId: userId ?? this.userId,
      userEmail: userEmail ?? this.userEmail,
      name: name ?? this.name,
      phone: phone ?? this.phone,
      companyEmail: companyEmail ?? this.companyEmail,
      image: image ?? this.image,
      companyDescription: companyDescription ?? this.companyDescription,
      companyWebsite: companyWebsite ?? this.companyWebsite,
      industry: industry ?? this.industry,
      foundedYear: foundedYear ?? this.foundedYear,
      tradeLicenseNumber: tradeLicenseNumber ?? this.tradeLicenseNumber,
      createdAt: createdAt ?? this.createdAt,
      updatedAt: updatedAt ?? this.updatedAt,
      locationId: locationId ?? this.locationId,
      locationDetails: locationDetails ?? this.locationDetails,
      locationPostCode: locationPostCode ?? this.locationPostCode,
      locationCountryId: locationCountryId ?? this.locationCountryId,
      locationCountryName: locationCountryName ?? this.locationCountryName,
      locationCountryCode: locationCountryCode ?? this.locationCountryCode,
      locationDivisionId: locationDivisionId ?? this.locationDivisionId,
      locationDivisionName:
      locationDivisionName ?? this.locationDivisionName,
      locationDistrictId: locationDistrictId ?? this.locationDistrictId,
      locationDistrictName:
      locationDistrictName ?? this.locationDistrictName,
      locationPoliceStationId:
      locationPoliceStationId ?? this.locationPoliceStationId,
      locationPoliceStationName:
      locationPoliceStationName ?? this.locationPoliceStationName,
    );
  }
}