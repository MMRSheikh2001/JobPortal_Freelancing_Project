import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/reference_response.dart';
import 'package:work_bridge_flutter/router/app_router.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

/// Reference list — same shape as EducationsListScreen /
/// ExperienceListScreen / ExtracurricularListScreen: count header, a
/// card per entry with Edit/Delete, empty state, FAB to add a new one.
class ReferenceListScreen extends ConsumerStatefulWidget {
  const ReferenceListScreen({super.key});

  @override
  ConsumerState<ReferenceListScreen> createState() =>
      _ReferenceListScreenState();
}

class _ReferenceListScreenState extends ConsumerState<ReferenceListScreen> {
  List<ReferenceResponseDTO> _references = [];
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
          .getReferencesByUserProfileId(profileId);
      if (!mounted) return;
      setState(() => _references = list);
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
    ).pushNamed(AppRouter.addEditReference);
    if (refreshed == true) _load();
  }

  Future<void> _goToEdit(int referenceId) async {
    final refreshed = await Navigator.of(
      context,
    ).pushNamed(AppRouter.addEditReference, arguments: referenceId);
    if (refreshed == true) _load();
  }

  Future<void> _delete(ReferenceResponseDTO reference) async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Delete reference?'),
        content: Text(
          "This will permanently remove ${reference.name ?? 'this entry'} "
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
    if (confirmed != true || reference.id == null) return;

    try {
      await ref.read(cvRepositoryProvider).deleteReference(reference.id!);
      if (!mounted) return;
      setState(() => _references.removeWhere((r) => r.id == reference.id));
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('Reference deleted.')));
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
      appBar: AppBar(title: const Text('References')),
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
              child: _references.isEmpty
                  ? ListView(
                      children: const [
                        SizedBox(height: 120),
                        Icon(
                          Icons.people_outline,
                          size: 56,
                          color: Colors.black26,
                        ),
                        SizedBox(height: 12),
                        Center(
                          child: Text(
                            'No references added yet.\n'
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
                          'Total References: ${_references.length}',
                          style: const TextStyle(
                            fontSize: 14,
                            fontWeight: FontWeight.w600,
                            color: Colors.black54,
                          ),
                        ),
                        const SizedBox(height: 12),
                        ..._references.map(
                          (r) => _ReferenceCard(
                            reference: r,
                            onEdit: () => _goToEdit(r.id!),
                            onDelete: () => _delete(r),
                          ),
                        ),
                      ],
                    ),
            ),
    );
  }
}

class _ReferenceCard extends StatelessWidget {
  const _ReferenceCard({
    required this.reference,
    required this.onEdit,
    required this.onDelete,
  });

  final ReferenceResponseDTO reference;
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
                        reference.name ?? 'Untitled',
                        style: const TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const SizedBox(height: 2),
                      Text(
                        [reference.designation, reference.organization]
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
                if (reference.relation != null)
                  _Tag(icon: Icons.groups_outlined, label: reference.relation!),
                if (reference.phone != null)
                  _Tag(icon: Icons.phone_outlined, label: reference.phone!),
                if (reference.email != null)
                  _Tag(icon: Icons.email_outlined, label: reference.email!),
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
          Text(
            label,
            style: const TextStyle(fontSize: 12, color: Colors.black87),
          ),
        ],
      ),
    );
  }
}
