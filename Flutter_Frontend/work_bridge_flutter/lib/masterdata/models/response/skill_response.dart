class SkillResponseDTO {
  final int? skillId;
  final String? skillName;
  final int? categoryId;
  final String? categoryName;
  final String? categoryDescription;

  const SkillResponseDTO({
    this.skillId,
    this.skillName,
    this.categoryId,
    this.categoryName,
    this.categoryDescription,
  });

  /// Factory constructor to create [SkillResponseDTO] from a JSON map.
  factory SkillResponseDTO.fromJson(Map<String, dynamic> json) {
    return SkillResponseDTO(
      skillId: (json['skillId'] as num?)?.toInt(),
      skillName: json['skillName'] as String?,
      categoryId: (json['categoryId'] as num?)?.toInt(),
      categoryName: json['categoryName'] as String?,
      categoryDescription: json['categoryDescription'] as String?,
    );
  }

  /// Converts this [SkillResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (skillId != null) 'skillId': skillId,
      if (skillName != null) 'skillName': skillName,
      if (categoryId != null) 'categoryId': categoryId,
      if (categoryName != null) 'categoryName': categoryName,
      if (categoryDescription != null)
        'categoryDescription': categoryDescription,
    };
  }

  /// Helper method to create a modified copy of this object.
  SkillResponseDTO copyWith({
    int? skillId,
    String? skillName,
    int? categoryId,
    String? categoryName,
    String? categoryDescription,
  }) {
    return SkillResponseDTO(
      skillId: skillId ?? this.skillId,
      skillName: skillName ?? this.skillName,
      categoryId: categoryId ?? this.categoryId,
      categoryName: categoryName ?? this.categoryName,
      categoryDescription: categoryDescription ?? this.categoryDescription,
    );
  }
}