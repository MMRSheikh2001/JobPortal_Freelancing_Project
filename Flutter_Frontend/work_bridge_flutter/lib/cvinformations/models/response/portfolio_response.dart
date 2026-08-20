class PortfolioResponseDTO {
  final int? id;
  final String? title;
  final String? description;
  final String? projectUrl;
  final String? fileUrl;
  final String? technologies;
  final DateTime? createdAt;
  final DateTime? updatedAt;
  final int? userProfileId;
  final String? userName;
  final int? userId;
  final String? userEmail;

  const PortfolioResponseDTO({
    this.id,
    this.title,
    this.description,
    this.projectUrl,
    this.fileUrl,
    this.technologies,
    this.createdAt,
    this.updatedAt,
    this.userProfileId,
    this.userName,
    this.userId,
    this.userEmail,
  });

  /// Factory constructor to create [PortfolioResponseDTO] from a JSON map.
  factory PortfolioResponseDTO.fromJson(Map<String, dynamic> json) {
    return PortfolioResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      title: json['title'] as String?,
      description: json['description'] as String?,
      projectUrl: json['projectUrl'] as String?,
      fileUrl: json['fileUrl'] as String?,
      technologies: json['technologies'] as String?,
      createdAt: json['createdAt'] != null
          ? DateTime.tryParse(json['createdAt'] as String)
          : null,
      updatedAt: json['updatedAt'] != null
          ? DateTime.tryParse(json['updatedAt'] as String)
          : null,
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
      userName: json['userName'] as String?,
      userId: (json['userId'] as num?)?.toInt(),
      userEmail: json['userEmail'] as String?,
    );
  }

  /// Converts this [PortfolioResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (title != null) 'title': title,
      if (description != null) 'description': description,
      if (projectUrl != null) 'projectUrl': projectUrl,
      if (fileUrl != null) 'fileUrl': fileUrl,
      if (technologies != null) 'technologies': technologies,
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
      if (updatedAt != null) 'updatedAt': updatedAt!.toIso8601String(),
      if (userProfileId != null) 'userProfileId': userProfileId,
      if (userName != null) 'userName': userName,
      if (userId != null) 'userId': userId,
      if (userEmail != null) 'userEmail': userEmail,
    };
  }

  /// Helper method to create a modified copy of this object.
  PortfolioResponseDTO copyWith({
    int? id,
    String? title,
    String? description,
    String? projectUrl,
    String? fileUrl,
    String? technologies,
    DateTime? createdAt,
    DateTime? updatedAt,
    int? userProfileId,
    String? userName,
    int? userId,
    String? userEmail,
  }) {
    return PortfolioResponseDTO(
      id: id ?? this.id,
      title: title ?? this.title,
      description: description ?? this.description,
      projectUrl: projectUrl ?? this.projectUrl,
      fileUrl: fileUrl ?? this.fileUrl,
      technologies: technologies ?? this.technologies,
      createdAt: createdAt ?? this.createdAt,
      updatedAt: updatedAt ?? this.updatedAt,
      userProfileId: userProfileId ?? this.userProfileId,
      userName: userName ?? this.userName,
      userId: userId ?? this.userId,
      userEmail: userEmail ?? this.userEmail,
    );
  }
}