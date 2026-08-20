import 'package:work_bridge_flutter/enums/proficiency_level.dart';

class UserSkillResponseDTO {
  final int? id;
  final ProficiencyLevel? proficiencyLevel;
  final int? yearsOfExperience;
  final DateTime? createdAt;
  final int? userProfileId;
  final String? userFullName;
  final String? userHeadline;
  final int? userId;
  final String? userEmail;
  final int? skillId;
  final String? skillName;
  final int? categoryId;
  final String? categoryName;
  final String? categoryDescription;

  const UserSkillResponseDTO({
    this.id,
    this.proficiencyLevel,
    this.yearsOfExperience,
    this.createdAt,
    this.userProfileId,
    this.userFullName,
    this.userHeadline,
    this.userId,
    this.userEmail,
    this.skillId,
    this.skillName,
    this.categoryId,
    this.categoryName,
    this.categoryDescription,
  });

  /// Factory constructor to create [UserSkillResponseDTO] from a JSON map.
  factory UserSkillResponseDTO.fromJson(Map<String, dynamic> json) {
    return UserSkillResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      proficiencyLevel: json['proficiencyLevel'] != null
          ? ProficiencyLevel.fromJson(json['proficiencyLevel'] as String)
          : null,
      yearsOfExperience: (json['yearsOfExperience'] as num?)?.toInt(),
      createdAt: json['createdAt'] != null
          ? DateTime.tryParse(json['createdAt'] as String)
          : null,
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
      userFullName: json['userFullName'] as String?,
      userHeadline: json['userHeadline'] as String?,
      userId: (json['userId'] as num?)?.toInt(),
      userEmail: json['userEmail'] as String?,
      skillId: (json['skillId'] as num?)?.toInt(),
      skillName: json['skillName'] as String?,
      categoryId: (json['categoryId'] as num?)?.toInt(),
      categoryName: json['categoryName'] as String?,
      categoryDescription: json['categoryDescription'] as String?,
    );
  }

  /// Converts this [UserSkillResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (proficiencyLevel != null) 'proficiencyLevel': proficiencyLevel!.toJson(),
      if (yearsOfExperience != null) 'yearsOfExperience': yearsOfExperience,
      if (createdAt != null) 'createdAt': createdAt!.toIso8601String(),
      if (userProfileId != null) 'userProfileId': userProfileId,
      if (userFullName != null) 'userFullName': userFullName,
      if (userHeadline != null) 'userHeadline': userHeadline,
      if (userId != null) 'userId': userId,
      if (userEmail != null) 'userEmail': userEmail,
      if (skillId != null) 'skillId': skillId,
      if (skillName != null) 'skillName': skillName,
      if (categoryId != null) 'categoryId': categoryId,
      if (categoryName != null) 'categoryName': categoryName,
      if (categoryDescription != null) 'categoryDescription': categoryDescription,
    };
  }

  /// Helper method to create a modified copy of this object.
  UserSkillResponseDTO copyWith({
    int? id,
    ProficiencyLevel? proficiencyLevel,
    int? yearsOfExperience,
    DateTime? createdAt,
    int? userProfileId,
    String? userFullName,
    String? userHeadline,
    int? userId,
    String? userEmail,
    int? skillId,
    String? skillName,
    int? categoryId,
    String? categoryName,
    String? categoryDescription,
  }) {
    return UserSkillResponseDTO(
      id: id ?? this.id,
      proficiencyLevel: proficiencyLevel ?? this.proficiencyLevel,
      yearsOfExperience: yearsOfExperience ?? this.yearsOfExperience,
      createdAt: createdAt ?? this.createdAt,
      userProfileId: userProfileId ?? this.userProfileId,
      userFullName: userFullName ?? this.userFullName,
      userHeadline: userHeadline ?? this.userHeadline,
      userId: userId ?? this.userId,
      userEmail: userEmail ?? this.userEmail,
      skillId: skillId ?? this.skillId,
      skillName: skillName ?? this.skillName,
      categoryId: categoryId ?? this.categoryId,
      categoryName: categoryName ?? this.categoryName,
      categoryDescription: categoryDescription ?? this.categoryDescription,
    );
  }
}