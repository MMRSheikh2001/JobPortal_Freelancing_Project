class CategoryRequestDTO {
  final String? name;
  final String? description;

  const CategoryRequestDTO({
    this.name,
    this.description,
  });

  /// Factory constructor to create [CategoryRequestDTO] from a JSON map.
  factory CategoryRequestDTO.fromJson(Map<String, dynamic> json) {
    return CategoryRequestDTO(
      name: json['name'] as String?,
      description: json['description'] as String?,
    );
  }

  /// Converts this [CategoryRequestDTO] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (name != null) 'name': name,
      if (description != null) 'description': description,
    };
  }

  /// Helper method to create a modified copy of this object.
  CategoryRequestDTO copyWith({
    String? name,
    String? description,
  }) {
    return CategoryRequestDTO(
      name: name ?? this.name,
      description: description ?? this.description,
    );
  }
}