class LanguageRequestDTO {
  final String? name;

  const LanguageRequestDTO({
    this.name,
  });

  /// Factory constructor to create [LanguageRequestDTO] from a JSON map.
  factory LanguageRequestDTO.fromJson(Map<String, dynamic> json) {
    return LanguageRequestDTO(
      name: json['name'] as String?,
    );
  }

  /// Converts this [LanguageRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (name != null) 'name': name,
    };
  }

  /// Helper method to create a modified copy of this object.
  LanguageRequestDTO copyWith({
    String? name,
  }) {
    return LanguageRequestDTO(
      name: name ?? this.name,
    );
  }
}