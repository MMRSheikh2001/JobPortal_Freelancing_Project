class ResumeFileResponseDTO {
  final int? id;
  final int? userProfileId;
  final String? userName;
  final String? fileName;
  final DateTime? uploadedAt;

  const ResumeFileResponseDTO({
    this.id,
    this.userProfileId,
    this.userName,
    this.fileName,
    this.uploadedAt,
  });

  /// Factory constructor to create [ResumeFileResponseDTO] from a JSON map.
  factory ResumeFileResponseDTO.fromJson(Map<String, dynamic> json) {
    return ResumeFileResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
      userName: json['userName'] as String?,
      fileName: json['fileName'] as String?,
      uploadedAt: json['uploadedAt'] != null
          ? DateTime.tryParse(json['uploadedAt'] as String)
          : null,
    );
  }

  /// Converts this [ResumeFileResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (userProfileId != null) 'userProfileId': userProfileId,
      if (userName != null) 'userName': userName,
      if (fileName != null) 'fileName': fileName,
      if (uploadedAt != null) 'uploadedAt': uploadedAt!.toIso8601String(),
    };
  }

  /// Helper method to create a modified copy of this object.
  ResumeFileResponseDTO copyWith({
    int? id,
    int? userProfileId,
    String? userName,
    String? fileName,
    DateTime? uploadedAt,
  }) {
    return ResumeFileResponseDTO(
      id: id ?? this.id,
      userProfileId: userProfileId ?? this.userProfileId,
      userName: userName ?? this.userName,
      fileName: fileName ?? this.fileName,
      uploadedAt: uploadedAt ?? this.uploadedAt,
    );
  }
}