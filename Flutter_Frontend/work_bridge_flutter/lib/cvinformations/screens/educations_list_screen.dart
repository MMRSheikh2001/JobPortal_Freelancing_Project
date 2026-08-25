import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/education_response.dart';
import 'package:work_bridge_flutter/enums/education_level.dart';
import 'package:work_bridge_flutter/router/app_router.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

/// Education list — mirrors WorkBridgeAndroid's EducationActivity: a
/// count header, a card per entry with Edit/Delete, an empty state, and
/// a FAB to add a new one.
class EducationsListScreen extends ConsumerStatefulWidget {
  const EducationsListScreen({super.key});

  @override
  ConsumerState<EducationsListScreen> createState() =>
      _EducationsListScreenState();
}

class _EducationsListScreenState extends ConsumerState<EducationsListScreen> {
  List<EducationResponseDTO> _educations = [];
  bool _loading = true;
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) => _load());
  }

  Future<void> _load() async {
    final profileId = ref.read(currentUserProvider)?.profileId;
    if (profileId == null) return;

    setState(() {
      _loading = true;
      _errorMessage = null;
    });
    try {
      final list = await ref
          .read(cvRepositoryProvider)
          .getEducationsByUserProfileId(profileId);
      if (!mounted) return;
      setState(() => _educations = list);
    } catch (e) {
      if (!mounted) return;
      setState(() => _errorMessage = e.toString());
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  Future<void> _goToAdd() async {
    final refreshed = await Navigator.of(context)
        .pushNamed(AppRouter.addEditEducation);
    if (refreshed == true) _load();
  }

  Future<void> _goToEdit(int educationId) async {
    final refreshed = await Navigator.of(context).pushNamed(
      AppRouter.addEditEducation,
      arguments: educationId,
    );
    if (refreshed == true) _load();
  }

  Future<void> _delete(EducationResponseDTO education) async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Delete education?'),
        content: Text(
          "This will permanently remove ${education.institution ?? 'this entry'} "
              'from your profile.',
        ),
        actions: [
          TextButton(
            onPressed: () => Navigator.of(ctx).pop(false),
            child: const Text('Cancel'),
          ),
          FilledButton(
            onPressed: () => Navigator.of(ctx).pop(true),
            style: FilledButton.styleFrom(backgroundColor: Colors.red),
            child: const Text('Delete'),
          ),
        ],
      ),
    );
    if (confirmed != true || education.id == null) return;

    try {
      await ref.read(cvRepositoryProvider).deleteEducation(education.id!);
      if (!mounted) return;
      setState(() => _educations.removeWhere((e) => e.id == education.id));
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Education deleted.')),
      );
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(context).showSnackBar(
        SnackBar(content: Text('Could not delete: $e')),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Education')),
      floatingActionButton: FloatingActionButton(
        onPressed: _goToAdd,
        child: const Icon(Icons.add),
      ),
      body: _loading
          ? const Center(child: CircularProgressIndicator())
          : _errorMessage != null
          ? Center(
        child: Padding(
          padding: const EdgeInsets.all(24),
          child: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              const Icon(Icons.error_outline,
                  size: 40, color: Colors.red),
              const SizedBox(height: 12),
              Text('Failed to load: $_errorMessage',
                  textAlign: TextAlign.center),
              const SizedBox(height: 12),
              OutlinedButton(
                onPressed: _load,
                child: const Text('Retry'),
              ),
            ],
          ),
        ),
      )
          : RefreshIndicator(
        onRefresh: _load,
        child: _educations.isEmpty
            ? ListView(
          // ListView (not just Center) so pull-to-refresh
          // still works on an empty list.
          children: const [
            SizedBox(height: 120),
            Icon(Icons.school_outlined,
                size: 56, color: Colors.black26),
            SizedBox(height: 12),
            Center(
              child: Text(
                'No education added yet.\nTap + to add your first one.',
                textAlign: TextAlign.center,
                style: TextStyle(color: Colors.black54),
              ),
            ),
          ],
        )
            : ListView(
          padding: const EdgeInsets.fromLTRB(16, 16, 16, 80),
          children: [
            Text(
              'Total Education: ${_educations.length}',
              style: const TextStyle(
                fontSize: 14,
                fontWeight: FontWeight.w600,
                color: Colors.black54,
              ),
            ),
            const SizedBox(height: 12),
            ..._educations.map(
                  (e) => _EducationCard(
                education: e,
                onEdit: () => _goToEdit(e.id!),
                onDelete: () => _delete(e),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _EducationCard extends StatelessWidget {
  const _EducationCard({
    required this.education,
    required this.onEdit,
    required this.onDelete,
  });

  final EducationResponseDTO education;
  final VoidCallback onEdit;
  final VoidCallback onDelete;

  String get _duration {
    final start = education.startDate;
    final end = education.endDate;
    final startLabel = start != null ? '${start.year}' : '?';
    final endLabel =
    education.currentlyStudying == true ? 'Present' : (end != null ? '${end.year}' : '?');
    return '$startLabel - $endLabel';
  }

  String get _resultLabel {
    if (education.result == null) return '';
    final result = education.result!;
    final outOf = education.outOf;
    return outOf != null ? '$result / $outOf' : '$result';
  }

  String _levelLabel(EducationLevel? e) => switch (e) {
    EducationLevel.ssc => 'SSC',
    EducationLevel.hsc => 'HSC',
    EducationLevel.diploma => 'Diploma',
    EducationLevel.bachelor => "Bachelor's",
    EducationLevel.pgd => 'PGD',
    EducationLevel.masters => "Master's",
    EducationLevel.mphil => 'MPhil',
    EducationLevel.phd => 'PhD',
    null => '',
  };

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.only(bottom: 10),
      clipBehavior: Clip.antiAlias,
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        education.institution ?? 'Untitled',
                        style: const TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const SizedBox(height: 2),
                      Text(
                        [
                          _levelLabel(education.educationLevel),
                          if (education.fieldOfStudy != null)
                            education.fieldOfStudy!,
                        ].where((s) => s.isNotEmpty).join(' • '),
                        style: const TextStyle(color: Colors.black54),
                      ),
                    ],
                  ),
                ),
                PopupMenuButton<String>(
                  onSelected: (value) {
                    if (value == 'edit') onEdit();
                    if (value == 'delete') onDelete();
                  },
                  itemBuilder: (_) => const [
                    PopupMenuItem(value: 'edit', child: Text('Edit')),
                    PopupMenuItem(value: 'delete', child: Text('Delete')),
                  ],
                ),
              ],
            ),
            const SizedBox(height: 8),
            Wrap(
              spacing: 8,
              runSpacing: 6,
              children: [
                _Tag(icon: Icons.calendar_today_outlined, label: _duration),
                if (_resultLabel.isNotEmpty)
                  _Tag(icon: Icons.grade_outlined, label: _resultLabel),
                if (education.gradeOrDivision != null)
                  _Tag(
                    icon: Icons.workspace_premium_outlined,
                    label: education.gradeOrDivision!,
                  ),
              ],
            ),
          ],
        ),
      ),
    );
  }
}

class _Tag extends StatelessWidget {
  const _Tag({required this.icon, required this.label});

  final IconData icon;
  final String label;

  @override
  Widget build(BuildContext context) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 8, vertical: 4),
      decoration: BoxDecoration(
        color: Colors.grey.shade100,
        borderRadius: BorderRadius.circular(8),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 13, color: Colors.black54),
          const SizedBox(width: 4),
          Text(label, style: const TextStyle(fontSize: 12, color: Colors.black87)),
        ],
      ),
    );
  }
}