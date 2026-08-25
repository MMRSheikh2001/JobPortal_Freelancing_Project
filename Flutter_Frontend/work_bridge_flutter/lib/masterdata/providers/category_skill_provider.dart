import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/masterdata/models/response/category_response.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

final categoriesProvider = FutureProvider<List<CategoryResponseDTO>>((ref) {
  return ref.watch(masterDataRepositoryProvider).getAllCategories();
});