class ExtracurricularResponseDTO {
  final int? id;
  final String? title;
  final String? description;
  final String? organization;
  final String? role;
  final int? userProfileId;
  final int? userId;
  final String? userName;
  final String? userEmail;

  const ExtracurricularResponseDTO({
    this.id,
    this.title,
    this.description,
    this.organization,
    this.role,
    this.userProfileId,
    this.userId,
    this.userName,
    this.userEmail,
  });

  /// Factory constructor to create [ExtracurricularResponseDTO] from a JSON map.
  factory ExtracurricularResponseDTO.fromJson(Map<String, dynamic> json) {
    return ExtracurricularResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      title: json['title'] as String?,
      description: json['description'] as String?,
      organization: json['organization'] as String?,
      role: json['role'] as String?,
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
      userId: (json['userId'] as num?)?.toInt(),
      userName: json['userName'] as String?,
      userEmail: json['userEmail'] as String?,
    );
  }

  /// Converts this [ExtracurricularResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (title != null) 'title': title,
      if (description != null) 'description': description,
      if (organization != null) 'organization': organization,
      if (role != null) 'role': role,
      if (userProfileId != null) 'userProfileId': userProfileId,
      if (userId != null) 'userId': userId,
      if (userName != null) 'userName': userName,
      if (userEmail != null) 'userEmail': userEmail,
    };
  }

  /// Helper method to create a modified copy of this object.
  ExtracurricularResponseDTO copyWith({
    int? id,
    String? title,
    String? description,
    String? organization,
    String? role,
    int? userProfileId,
    int? userId,
    String? userName,
    String? userEmail,
  }) {
    return ExtracurricularResponseDTO(
      id: id ?? this.id,
      title: title ?? this.title,
      description: description ?? this.description,
      organization: organization ?? this.organization,
      role: role ?? this.role,
      userProfileId: userProfileId ?? this.userProfileId,
      userId: userId ?? this.userId,
      userName: userName ?? this.userName,
      userEmail: userEmail ?? this.userEmail,
    );
  }
}