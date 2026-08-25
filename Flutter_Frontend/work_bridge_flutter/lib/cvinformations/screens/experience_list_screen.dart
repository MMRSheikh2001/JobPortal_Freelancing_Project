import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/experience_response.dart';
import 'package:work_bridge_flutter/enums/employment_type.dart';
import 'package:work_bridge_flutter/router/app_router.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

/// Experience list — same shape as EducationsListScreen: count header, a
/// card per entry with Edit/Delete, empty state, FAB to add a new one.
class ExperienceListScreen extends ConsumerStatefulWidget {
  const ExperienceListScreen({super.key});

  @override
  ConsumerState<ExperienceListScreen> createState() =>
      _ExperienceListScreenState();
}

class _ExperienceListScreenState extends ConsumerState<ExperienceListScreen> {
  List<ExperienceResponseDTO> _experiences = [];
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
          .getExperiencesByUserProfileId(profileId);
      if (!mounted) return;
      setState(() => _experiences = list);
    } catch (e) {
      if (!mounted) return;
      setState(() => _errorMessage = e.toString());
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  Future<void> _goToAdd() async {
    final refreshed = await Navigator.of(
      context,
    ).pushNamed(AppRouter.addEditExperience);
    if (refreshed == true) _load();
  }

  Future<void> _goToEdit(int experienceId) async {
    final refreshed = await Navigator.of(
      context,
    ).pushNamed(AppRouter.addEditExperience, arguments: experienceId);
    if (refreshed == true) _load();
  }

  Future<void> _delete(ExperienceResponseDTO experience) async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Delete experience?'),
        content: Text(
          "This will permanently remove ${experience.position ?? 'this entry'} "
          'at ${experience.companyName ?? 'this company'} from your profile.',
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
    if (confirmed != true || experience.id == null) return;

    try {
      await ref.read(cvRepositoryProvider).deleteExperience(experience.id!);
      if (!mounted) return;
      setState(() => _experiences.removeWhere((e) => e.id == experience.id));
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('Experience deleted.')));
    } catch (e) {
      if (!mounted) return;
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Could not delete: $e')));
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Experience')),
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
                    const Icon(
                      Icons.error_outline,
                      size: 40,
                      color: Colors.red,
                    ),
                    const SizedBox(height: 12),
                    Text(
                      'Failed to load: $_errorMessage',
                      textAlign: TextAlign.center,
                    ),
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
              child: _experiences.isEmpty
                  ? ListView(
                      children: const [
                        SizedBox(height: 120),
                        Icon(
                          Icons.work_outline,
                          size: 56,
                          color: Colors.black26,
                        ),
                        SizedBox(height: 12),
                        Center(
                          child: Text(
                            'No experience added yet.\nTap + to add your first one.',
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
                          'Total Experience: ${_experiences.length}',
                          style: const TextStyle(
                            fontSize: 14,
                            fontWeight: FontWeight.w600,
                            color: Colors.black54,
                          ),
                        ),
                        const SizedBox(height: 12),
                        ..._experiences.map(
                          (e) => _ExperienceCard(
                            experience: e,
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

class _ExperienceCard extends StatelessWidget {
  const _ExperienceCard({
    required this.experience,
    required this.onEdit,
    required this.onDelete,
  });

  final ExperienceResponseDTO experience;
  final VoidCallback onEdit;
  final VoidCallback onDelete;

  String get _duration {
    final start = experience.startDate;
    final end = experience.endDate;
    final startLabel = start != null ? '${start.year}' : '?';
    final endLabel = experience.currentlyWorking == true
        ? 'Present'
        : (end != null ? '${end.year}' : '?');
    return '$startLabel - $endLabel';
  }

  String _employmentTypeLabel(EmploymentType? e) => switch (e) {
    EmploymentType.fullTime => 'Full Time',
    EmploymentType.partTime => 'Part Time',
    EmploymentType.contract => 'Contract',
    EmploymentType.internship => 'Internship',
    EmploymentType.freelance => 'Freelance',
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
                        experience.position ?? 'Untitled',
                        style: const TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const SizedBox(height: 2),
                      Text(
                        [
                              experience.companyName,
                              _employmentTypeLabel(experience.employmentType),
                            ]
                            .whereType<String>()
                            .where((s) => s.isNotEmpty)
                            .join(' • '),
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
                if (experience.currentlyWorking == true)
                  const _Tag(
                    icon: Icons.check_circle_outline,
                    label: 'Current',
                  ),
              ],
            ),
            if (experience.responsibilities != null &&
                experience.responsibilities!.isNotEmpty) ...[
              const SizedBox(height: 8),
              Text(
                experience.responsibilities!,
                maxLines: 2,
                overflow: TextOverflow.ellipsis,
                style: const TextStyle(fontSize: 13, color: Colors.black87),
              ),
            ],
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
          Text(
            label,
            style: const TextStyle(fontSize: 12, color: Colors.black87),
          ),
        ],
      ),
    );
  }
}
