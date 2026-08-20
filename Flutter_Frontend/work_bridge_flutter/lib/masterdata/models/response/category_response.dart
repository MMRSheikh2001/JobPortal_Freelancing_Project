class CategoryResponseDTO {
  final int? id;
  final String? name;
  final String? description;

  const CategoryResponseDTO({
    this.id,
    this.name,
    this.description,
  });

  /// Factory constructor to create [CategoryResponseDTO] from a JSON map.
  factory CategoryResponseDTO.fromJson(Map<String, dynamic> json) {
    return CategoryResponseDTO(
      id: (json['id'] as num?)?.toInt(),
      name: json['name'] as String?,
      description: json['description'] as String?,
    );
  }

  /// Converts this [CategoryResponseDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (id != null) 'id': id,
      if (name != null) 'name': name,
      if (description != null) 'description': description,
    };
  }

  /// Helper method to create a modified copy of this object.
  CategoryResponseDTO copyWith({
    int? id,
    String? name,
    String? description,
  }) {
    return CategoryResponseDTO(
      id: id ?? this.id,
      name: name ?? this.name,
      description: description ?? this.description,
    );
  }
}