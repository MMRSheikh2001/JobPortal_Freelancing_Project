class LanguageResponseDTO {
  final int? id;
  final String? name;

  const LanguageResponseDTO({
    this.id,
    this.name,
  });

  /// Factory constructor to create [LanguageResponseDTO] from a JSON map.
  factory LanguageResponseDTO.fromJson(Map<String, dynamic> json) {
    return LanguageResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      name: json['name'] as String?,
    );
  }

  /// Converts this [LanguageResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (name != null) 'name': name,
    };
  }

  /// Helper method to create a modified copy of this object.
  LanguageResponseDTO copyWith({
    int? id,
    String? name,
  }) {
    return LanguageResponseDTO(
      id: id ?? this.id,
      name: name ?? this.name,
    );
  }
}