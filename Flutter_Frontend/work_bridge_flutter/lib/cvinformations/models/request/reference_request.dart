class ReferenceRequestDTO {
  final String? name;
  final String? organization;
  final String? designation;
  final String? phone;
  final String? email;
  final String? address;
  final String? relation;
  final int? userProfileId;

  const ReferenceRequestDTO({
    this.name,
    this.organization,
    this.designation,
    this.phone,
    this.email,
    this.address,
    this.relation,
    this.userProfileId,
  });

  /// Factory constructor to create [ReferenceRequestDTO] from a JSON map.
  factory ReferenceRequestDTO.fromJson(Map<String, dynamic> json) {
    return ReferenceRequestDTO(
      name: json['name'] as String?,
      organization: json['organization'] as String?,
      designation: json['designation'] as String?,
      phone: json['phone'] as String?,
      email: json['email'] as String?,
      address: json['address'] as String?,
      relation: json['relation'] as String?,
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
    );
  }

  /// Converts this [ReferenceRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (name != null) 'name': name,
      if (organization != null) 'organization': organization,
      if (designation != null) 'designation': designation,
      if (phone != null) 'phone': phone,
      if (email != null) 'email': email,
      if (address != null) 'address': address,
      if (relation != null) 'relation': relation,
      if (userProfileId != null) 'userProfileId': userProfileId,
    };
  }

  /// Helper method to create a modified copy of this object.
  ReferenceRequestDTO copyWith({
    String? name,
    String? organization,
    String? designation,
    String? phone,
    String? email,
    String? address,
    String? relation,
    int? userProfileId,
  }) {
    return ReferenceRequestDTO(
      name: name ?? this.name,
      organization: organization ?? this.organization,
      designation: designation ?? this.designation,
      phone: phone ?? this.phone,
      email: email ?? this.email,
      address: address ?? this.address,
      relation: relation ?? this.relation,
      userProfileId: userProfileId ?? this.userProfileId,
    );
  }
}