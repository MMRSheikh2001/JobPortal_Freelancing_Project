import 'dart:convert';

class PortfolioRequestDTO {
  final String? title;
  final String? description;
  final String? projectUrl;
  final String? technologies;
  final int? userProfileId;

  const PortfolioRequestDTO({
    this.title,
    this.description,
    this.projectUrl,
    this.technologies,
    this.userProfileId,
  });

  /// Factory constructor to create [PortfolioRequestDTO] from a JSON map.
  factory PortfolioRequestDTO.fromJson(Map<String, dynamic> json) {
    return PortfolioRequestDTO(
      title: json['title'] as String?,
      description: json['description'] as String?,
      projectUrl: json['projectUrl'] as String?,
      technologies: json['technologies'] as String?,
      userProfileId: (json['userProfileId'] as num?)?.toInt(),
    );
  }

  /// Converts this [PortfolioRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (title != null) 'title': title,
      if (description != null) 'description': description,
      if (projectUrl != null) 'projectUrl': projectUrl,
      if (technologies != null) 'technologies': technologies,
      if (userProfileId != null) 'userProfileId': userProfileId,
    };
  }

  /// Helper method to create a modified copy of this object.
  PortfolioRequestDTO copyWith({
    String? title,
    String? description,
    String? projectUrl,
    String? technologies,
    int? userProfileId,
  }) {
    return PortfolioRequestDTO(
      title: title ?? this.title,
      description: description ?? this.description,
      projectUrl: projectUrl ?? this.projectUrl,
      technologies: technologies ?? this.technologies,
      userProfileId: userProfileId ?? this.userProfileId,
    );
  }

  String toJsonString() => jsonEncode(toJson());


}