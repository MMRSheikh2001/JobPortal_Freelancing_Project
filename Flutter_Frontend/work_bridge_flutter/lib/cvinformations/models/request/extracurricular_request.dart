class ExtracurricularRequestDTO {
  final String? title;
  final String? description;
  final String? organization;
  final String? role;
  final int? userProfileId;

  const ExtracurricularRequestDTO({
    this.title,
    this.description,
    this.organization,
    this.role,
    this.userProfileId,
  });

  /// Factory constructor to create [ExtracurricularRequestDTO] from a JSON map.
  factory ExtracurricularRequestDTO.fromJson(Map<String, dynamic> json) {
    return ExtracurricularRequestDTO(
      title: json['title'] as String?,
      description: json['description'] as String?,
      organization: json['organization'] as String?,
      role: json['role'] as String?,
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
    );
  }

  /// Converts this [ExtracurricularRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (title != null) 'title': title,
      if (description != null) 'description': description,
      if (organization != null) 'organization': organization,
      if (role != null) 'role': role,
      if (userProfileId != null) 'userProfileId': userProfileId,
    };
  }

  /// Helper method to create a modified copy of this object.
  ExtracurricularRequestDTO copyWith({
    String? title,
    String? description,
    String? organization,
    String? role,
    int? userProfileId,
  }) {
    return ExtracurricularRequestDTO(
      title: title ?? this.title,
      description: description ?? this.description,
      organization: organization ?? this.organization,
      role: role ?? this.role,
      userProfileId: userProfileId ?? this.userProfileId,
    );
  }
}