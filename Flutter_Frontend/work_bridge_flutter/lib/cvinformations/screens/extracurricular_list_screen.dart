import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/extracurricular_response.dart';
import 'package:work_bridge_flutter/router/app_router.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

/// Extracurricular list — same shape as EducationsListScreen /
/// ExperienceListScreen: count header, a card per entry with Edit/Delete,
/// empty state, FAB to add a new one.
class ExtracurricularListScreen extends ConsumerStatefulWidget {
  const ExtracurricularListScreen({super.key});

  @override
  ConsumerState<ExtracurricularListScreen> createState() =>
      _ExtracurricularListScreenState();
}

class _ExtracurricularListScreenState
    extends ConsumerState<ExtracurricularListScreen> {
  List<ExtracurricularResponseDTO> _extracurriculars = [];
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
          .getExtracurricularsByUserProfileId(profileId);
      if (!mounted) return;
      setState(() => _extracurriculars = list);
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
    ).pushNamed(AppRouter.addEditExtracurricular);
    if (refreshed == true) _load();
  }

  Future<void> _goToEdit(int extracurricularId) async {
    final refreshed = await Navigator.of(
      context,
    ).pushNamed(AppRouter.addEditExtracurricular, arguments: extracurricularId);
    if (refreshed == true) _load();
  }

  Future<void> _delete(ExtracurricularResponseDTO extracurricular) async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Delete extracurricular?'),
        content: Text(
          "This will permanently remove ${extracurricular.title ?? 'this entry'} "
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
    if (confirmed != true || extracurricular.id == null) return;

    try {
      await ref
          .read(cvRepositoryProvider)
          .deleteExtracurricular(extracurricular.id!);
      if (!mounted) return;
      setState(
        () => _extracurriculars.removeWhere((e) => e.id == extracurricular.id),
      );
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('Extracurricular deleted.')));
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
      appBar: AppBar(title: const Text('Extracurricular')),
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
              child: _extracurriculars.isEmpty
                  ? ListView(
                      children: const [
                        SizedBox(height: 120),
                        Icon(
                          Icons.star_outline,
                          size: 56,
                          color: Colors.black26,
                        ),
                        SizedBox(height: 12),
                        Center(
                          child: Text(
                            'No extracurricular activities added yet.\n'
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
                          'Total Extracurricular: ${_extracurriculars.length}',
                          style: const TextStyle(
                            fontSize: 14,
                            fontWeight: FontWeight.w600,
                            color: Colors.black54,
                          ),
                        ),
                        const SizedBox(height: 12),
                        ..._extracurriculars.map(
                          (e) => _ExtracurricularCard(
                            extracurricular: e,
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

class _ExtracurricularCard extends StatelessWidget {
  const _ExtracurricularCard({
    required this.extracurricular,
    required this.onEdit,
    required this.onDelete,
  });

  final ExtracurricularResponseDTO extracurricular;
  final VoidCallback onEdit;
  final VoidCallback onDelete;

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
                        extracurricular.title ?? 'Untitled',
                        style: const TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const SizedBox(height: 2),
                      Text(
                        [extracurricular.role, extracurricular.organization]
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
            if (extracurricular.description != null &&
                extracurricular.description!.isNotEmpty) ...[
              const SizedBox(height: 8),
              Text(
                extracurricular.description!,
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
