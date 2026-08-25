import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/training_response.dart';
import 'package:work_bridge_flutter/router/app_router.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

/// Training list — same shape as EducationsListScreen /
/// ExperienceListScreen / ReferenceListScreen: count header, a card per
/// entry with Edit/Delete, empty state, FAB to add a new one. Cards also
/// show a small badge when a certificate file is attached.
class TrainingListScreen extends ConsumerStatefulWidget {
  const TrainingListScreen({super.key});

  @override
  ConsumerState<TrainingListScreen> createState() =>
      _TrainingListScreenState();
}

class _TrainingListScreenState extends ConsumerState<TrainingListScreen> {
  List<TrainingResponseDTO> _trainings = [];
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
          .getTrainingsByUserProfileId(profileId);
      if (!mounted) return;
      setState(() => _trainings = list);
    } catch (e) {
      if (!mounted) return;
      setState(() => _errorMessage = e.toString());
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  Future<void> _goToAdd() async {
    final refreshed =
    await Navigator.of(context).pushNamed(AppRouter.addEditTraining);
    if (refreshed == true) _load();
  }

  Future<void> _goToEdit(int trainingId) async {
    final refreshed = await Navigator.of(context).pushNamed(
      AppRouter.addEditTraining,
      arguments: trainingId,
    );
    if (refreshed == true) _load();
  }

  Future<void> _delete(TrainingResponseDTO training) async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Delete training?'),
        content: Text(
          "This will permanently remove ${training.name ?? 'this entry'} "
              '(including any uploaded certificate) from your profile.',
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
    if (confirmed != true || training.id == null) return;

    try {
      await ref.read(cvRepositoryProvider).deleteTraining(training.id!);
      if (!mounted) return;
      setState(() => _trainings.removeWhere((t) => t.id == training.id));
      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Training deleted.')),
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
      appBar: AppBar(title: const Text('Training')),
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
        child: _trainings.isEmpty
            ? ListView(
          children: const [
            SizedBox(height: 120),
            Icon(Icons.workspace_premium_outlined,
                size: 56, color: Colors.black26),
            SizedBox(height: 12),
            Center(
              child: Text(
                'No training added yet.\n'
                    'Tap + to add your first one.',
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
              'Total Training: ${_trainings.length}',
              style: const TextStyle(
                fontSize: 14,
                fontWeight: FontWeight.w600,
                color: Colors.black54,
              ),
            ),
            const SizedBox(height: 12),
            ..._trainings.map(
                  (t) => _TrainingCard(
                training: t,
                onEdit: () => _goToEdit(t.id!),
                onDelete: () => _delete(t),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class _TrainingCard extends StatelessWidget {
  const _TrainingCard({
    required this.training,
    required this.onEdit,
    required this.onDelete,
  });

  final TrainingResponseDTO training;
  final VoidCallback onEdit;
  final VoidCallback onDelete;

  String get _duration {
    final start = training.startDate;
    final end = training.endDate;
    if (start == null && end == null) return '';
    final startLabel = start != null ? '${start.year}' : '?';
    final endLabel = end != null ? '${end.year}' : '?';
    return '$startLabel - $endLabel';
  }

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
                        training.name ?? 'Untitled',
                        style: const TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const SizedBox(height: 2),
                      Text(
                        [
                          training.trainingType?.value,
                          training.institution,
                        ].whereType<String>().where((s) => s.isNotEmpty).join(
                            ' • '),
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
                if (_duration.isNotEmpty)
                  _Tag(icon: Icons.calendar_today_outlined, label: _duration),
                if (training.duration != null)
                  _Tag(icon: Icons.timelapse_outlined, label: training.duration!),
                if (training.certificateFile != null)
                  const _Tag(
                    icon: Icons.attach_file,
                    label: 'Certificate attached',
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