class ReferenceResponseDTO {
  final int? id;
  final String? name;
  final String? organization;
  final String? designation;
  final String? phone;
  final String? email;
  final String? address;
  final String? relation;
  final int? userProfileId;
  final String? userName;
  final int? userId;
  final String? userEmail;

  const ReferenceResponseDTO({
    this.id,
    this.name,
    this.organization,
    this.designation,
    this.phone,
    this.email,
    this.address,
    this.relation,
    this.userProfileId,
    this.userName,
    this.userId,
    this.userEmail,
  });

  /// Factory constructor to create [ReferenceResponseDTO] from a JSON map.
  factory ReferenceResponseDTO.fromJson(Map<String, dynamic> json) {
    return ReferenceResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      name: json['name'] as String?,
      organization: json['organization'] as String?,
      designation: json['designation'] as String?,
      phone: json['phone'] as String?,
      email: json['email'] as String?,
      address: json['address'] as String?,
      relation: json['relation'] as String?,
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
      userName: json['userName'] as String?,
      userId: (json['userId'] as num?)?.toInt(),
      userEmail: json['userEmail'] as String?,
    );
  }

  /// Converts this [ReferenceResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (name != null) 'name': name,
      if (organization != null) 'organization': organization,
      if (designation != null) 'designation': designation,
      if (phone != null) 'phone': phone,
      if (email != null) 'email': email,
      if (address != null) 'address': address,
      if (relation != null) 'relation': relation,
      if (userProfileId != null) 'userProfileId': userProfileId,
      if (userName != null) 'userName': userName,
      if (userId != null) 'userId': userId,
      if (userEmail != null) 'userEmail': userEmail,
    };
  }

  /// Helper method to create a modified copy of this object.
  ReferenceResponseDTO copyWith({
    int? id,
    String? name,
    String? organization,
    String? designation,
    String? phone,
    String? email,
    String? address,
    String? relation,
    int? userProfileId,
    String? userName,
    int? userId,
    String? userEmail,
  }) {
    return ReferenceResponseDTO(
      id: id ?? this.id,
      name: name ?? this.name,
      organization: organization ?? this.organization,
      designation: designation ?? this.designation,
      phone: phone ?? this.phone,
      email: email ?? this.email,
      address: address ?? this.address,
      relation: relation ?? this.relation,
      userProfileId: userProfileId ?? this.userProfileId,
      userName: userName ?? this.userName,
      userId: userId ?? this.userId,
      userEmail: userEmail ?? this.userEmail,
    );
  }
}