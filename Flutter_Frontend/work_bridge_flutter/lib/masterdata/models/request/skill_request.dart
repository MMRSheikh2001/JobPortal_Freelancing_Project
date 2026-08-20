class SkillRequestDTO {
  final String? skillName;
  final int? categoryId;

  const SkillRequestDTO({
    this.skillName,
    this.categoryId,
  });

  /// Factory constructor to create [SkillRequestDTO] from a JSON map.
  factory SkillRequestDTO.fromJson(Map<String, dynamic> json) {
    return SkillRequestDTO(
      skillName: json['skillName'] as String?,
      categoryId: (json['categoryId'] as num?)?.toInt(),
    );
  }

  /// Converts this [SkillRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (skillName != null) 'skillName': skillName,
      if (categoryId != null) 'categoryId': categoryId,
    };
  }

  /// Helper method to create a modified copy of this object.
  SkillRequestDTO copyWith({
    String? skillName,
    int? categoryId,
  }) {
    return SkillRequestDTO(
      skillName: skillName ?? this.skillName,
      categoryId: categoryId ?? this.categoryId,
    );
  }
}