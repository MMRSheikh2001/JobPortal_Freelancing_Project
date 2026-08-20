import 'package:work_bridge_flutter/enums/language_proficiency.dart';

class UserLanguageResponseDTO {
  final int? id;
  final LanguageProficiency? proficiency;
  final int? languageId;
  final String? languageName;
  final int? userProfileId;
  final String? userName;
  final String? userEmail;

  const UserLanguageResponseDTO({
    this.id,
    this.proficiency,
    this.languageId,
    this.languageName,
    this.userProfileId,
    this.userName,
    this.userEmail,
  });

  /// Factory constructor to create [UserLanguageResponseDTO] from a JSON map.
  factory UserLanguageResponseDTO.fromJson(Map<String, dynamic> json) {
    return UserLanguageResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      proficiency: json['proficiency'] != null
          ? LanguageProficiency.fromJson(json['proficiency'] as String)
          : null,
      languageId: (json['languageId'] as num?)?.toInt(),
      languageName: json['languageName'] as String?,
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
      userName: json['userName'] as String?,
      userEmail: json['userEmail'] as String?,
    );
  }

  /// Converts this [UserLanguageResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (proficiency != null) 'proficiency': proficiency!.toJson(),
      if (languageId != null) 'languageId': languageId,
      if (languageName != null) 'languageName': languageName,
      if (userProfileId != null) 'userProfileId': userProfileId,
      if (userName != null) 'userName': userName,
      if (userEmail != null) 'userEmail': userEmail,
    };
  }

  /// Helper method to create a modified copy of this object.
  UserLanguageResponseDTO copyWith({
    int? id,
    LanguageProficiency? proficiency,
    int? languageId,
    String? languageName,
    int? userProfileId,
    String? userName,
    String? userEmail,
  }) {
    return UserLanguageResponseDTO(
      id: id ?? this.id,
      proficiency: proficiency ?? this.proficiency,
      languageId: languageId ?? this.languageId,
      languageName: languageName ?? this.languageName,
      userProfileId: userProfileId ?? this.userProfileId,
      userName: userName ?? this.userName,
      userEmail: userEmail ?? this.userEmail,
    );
  }
}