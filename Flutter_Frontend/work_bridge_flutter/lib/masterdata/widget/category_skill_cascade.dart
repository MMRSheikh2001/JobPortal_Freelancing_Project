import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/masterdata/models/response/category_response.dart';
import 'package:work_bridge_flutter/masterdata/models/response/skill_response.dart';
import 'package:work_bridge_flutter/masterdata/providers/category_skill_provider.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

/// Selection reported back to the parent. categoryId alone is enough for
/// a job-search filter (matches JobSearchRequestDTO.categoryId); skillIds
/// is for flows that need actual skills, e.g. adding skills to a
/// UserProfile or posting a job with required skills.
class CategorySkillSelection {
  const CategorySkillSelection({
    this.categoryId,
    this.categoryName,
    this.skillIds = const [],
    this.skillNames = const [],
  });

  final int? categoryId;
  final String? categoryName;
  final List<int> skillIds;
  final List<String> skillNames;

  static const empty = CategorySkillSelection();
}

/// Category -> Skill(s) picker, backed by MasterDataRepository.
/// Category is single-select (a dropdown); skills within that category
/// are multi-select chips. Set [multiSelectSkills] to false for flows
/// that only need one skill (e.g. a single "primary skill" field).
class CategorySkillCascade extends ConsumerStatefulWidget {
  const CategorySkillCascade({
    super.key,
    required this.onChanged,
    this.categoryLabel = 'Category',
    this.skillsLabel = 'Skills',
    this.multiSelectSkills = true,
    this.showSkills = true,
  });

  final ValueChanged<CategorySkillSelection> onChanged;
  final String categoryLabel;
  final String skillsLabel;

  /// True: skills render as tappable multi-select chips.
  /// False: skills render as a single-select dropdown (one skill only).
  final bool multiSelectSkills;

  /// Job search filters only need categoryId — set false to hide the
  /// skill picker entirely and just emit the category.
  final bool showSkills;

  @override
  ConsumerState<CategorySkillCascade> createState() =>
      _CategorySkillCascadeState();
}

class _CategorySkillCascadeState extends ConsumerState<CategorySkillCascade> {
  List<SkillResponseDTO> _skills = [];
  CategoryResponseDTO? _category;
  final Set<SkillResponseDTO> _selectedSkills = {};

  bool _loadingSkills = false;

  // Guards against out-of-order async responses — if the user picks
  // Category A then quickly Category B, and A's skills response arrives
  // after B's, we must not let A's stale skills overwrite B's list.
  int _categoryRequestId = 0;

  void _emit() {
    widget.onChanged(
      CategorySkillSelection(
        categoryId: _category?.id,
        categoryName: _category?.name,
        skillIds: _selectedSkills
            .map((s) => s.skillId)
            .whereType<int>()
            .toList(),
        skillNames: _selectedSkills
            .map((s) => s.skillName)
            .whereType<String>()
            .toList(),
      ),
    );
  }

  Future<void> _onCategoryChanged(CategoryResponseDTO? category) async {
    final requestId = ++_categoryRequestId;

    setState(() {
      _category = category;
      _selectedSkills.clear();
      _skills = [];
    });
    _emit();
    if (!widget.showSkills || category?.id == null) return;

    setState(() => _loadingSkills = true);
    try {
      final list = await ref
          .read(masterDataRepositoryProvider)
          .getSkillsByCategoryId(category!.id!);
      if (mounted && requestId == _categoryRequestId) {
        setState(() => _skills = list);
      }
    } finally {
      if (mounted && requestId == _categoryRequestId) {
        setState(() => _loadingSkills = false);
      }
    }
  }

  void _toggleSkill(SkillResponseDTO skill) {
    setState(() {
      if (widget.multiSelectSkills) {
        if (_selectedSkills.contains(skill)) {
          _selectedSkills.remove(skill);
        } else {
          _selectedSkills.add(skill);
        }
      } else {
        _selectedSkills
          ..clear()
          ..add(skill);
      }
    });
    _emit();
  }

  @override
  Widget build(BuildContext context) {
    final categoriesAsync = ref.watch(categoriesProvider);

    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: [
        categoriesAsync.when(
          loading: () => const LinearProgressIndicator(),
          error: (e, _) => Text(
            'Failed to load categories: $e',
            style: const TextStyle(color: Colors.red),
          ),
          data: (categories) => DropdownButtonFormField<CategoryResponseDTO>(
            value: _category,
            isExpanded: true,
            decoration: InputDecoration(
              labelText: widget.categoryLabel,
              prefixIcon: const Icon(Icons.category_outlined),
            ),
            hint: Text('Select ${widget.categoryLabel}'),
            items: categories
                .map(
                  (c) => DropdownMenuItem(
                value: c,
                child: Text(c.name ?? ''),
              ),
            )
                .toList(),
            onChanged: _onCategoryChanged,
          ),
        ),
        if (widget.showSkills) ...[
          const SizedBox(height: 12),
          if (_loadingSkills)
            const LinearProgressIndicator()
          else if (_category != null && _skills.isEmpty)
            const Padding(
              padding: EdgeInsets.symmetric(vertical: 4),
              child: Text(
                'No skills found for this category.',
                style: TextStyle(color: Colors.black54),
              ),
            )
          else if (_skills.isNotEmpty) ...[
              Text(
                widget.skillsLabel,
                style: Theme.of(context).textTheme.labelLarge,
              ),
              const SizedBox(height: 6),
              Wrap(
                spacing: 8,
                runSpacing: 8,
                children: _skills.map((skill) {
                  final selected = _selectedSkills.contains(skill);
                  return FilterChip(
                    label: Text(skill.skillName ?? ''),
                    selected: selected,
                    onSelected: (_) => _toggleSkill(skill),
                  );
                }).toList(),
              ),
            ],
        ],
      ],
    );
  }
}