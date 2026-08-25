import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/portfolio_response.dart';
import 'package:work_bridge_flutter/router/app_router.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

/// Portfolio list — same shape as TrainingListScreen /
/// EducationsListScreen: count header, a card per entry with
/// Edit/Delete, empty state, FAB to add a new one. Cards also show a
/// small badge when a project file is attached.
class PortfolioListScreen extends ConsumerStatefulWidget {
  const PortfolioListScreen({super.key});

  @override
  ConsumerState<PortfolioListScreen> createState() =>
      _PortfolioListScreenState();
}

class _PortfolioListScreenState extends ConsumerState<PortfolioListScreen> {
  List<PortfolioResponseDTO> _portfolios = [];
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
          .getPortfoliosByUserProfileId(profileId);
      if (!mounted) return;
      setState(() => _portfolios = list);
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
    ).pushNamed(AppRouter.addEditPortfolio);
    if (refreshed == true) _load();
  }

  Future<void> _goToEdit(int portfolioId) async {
    final refreshed = await Navigator.of(
      context,
    ).pushNamed(AppRouter.addEditPortfolio, arguments: portfolioId);
    if (refreshed == true) _load();
  }

  Future<void> _delete(PortfolioResponseDTO portfolio) async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (ctx) => AlertDialog(
        title: const Text('Delete portfolio entry?'),
        content: Text(
          "This will permanently remove ${portfolio.title ?? 'this entry'} "
          '(including any uploaded file) from your profile.',
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
    if (confirmed != true || portfolio.id == null) return;

    try {
      await ref.read(cvRepositoryProvider).deletePortfolio(portfolio.id!);
      if (!mounted) return;
      setState(() => _portfolios.removeWhere((p) => p.id == portfolio.id));
      ScaffoldMessenger.of(
        context,
      ).showSnackBar(const SnackBar(content: Text('Portfolio entry deleted.')));
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
      appBar: AppBar(title: const Text('Portfolio')),
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
              child: _portfolios.isEmpty
                  ? ListView(
                      children: const [
                        SizedBox(height: 120),
                        Icon(
                          Icons.folder_outlined,
                          size: 56,
                          color: Colors.black26,
                        ),
                        SizedBox(height: 12),
                        Center(
                          child: Text(
                            'No portfolio entries added yet.\n'
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
                          'Total Portfolio: ${_portfolios.length}',
                          style: const TextStyle(
                            fontSize: 14,
                            fontWeight: FontWeight.w600,
                            color: Colors.black54,
                          ),
                        ),
                        const SizedBox(height: 12),
                        ..._portfolios.map(
                          (p) => _PortfolioCard(
                            portfolio: p,
                            onEdit: () => _goToEdit(p.id!),
                            onDelete: () => _delete(p),
                          ),
                        ),
                      ],
                    ),
            ),
    );
  }
}

class _PortfolioCard extends StatelessWidget {
  const _PortfolioCard({
    required this.portfolio,
    required this.onEdit,
    required this.onDelete,
  });

  final PortfolioResponseDTO portfolio;
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
                        portfolio.title ?? 'Untitled',
                        style: const TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      if (portfolio.technologies != null) ...[
                        const SizedBox(height: 2),
                        Text(
                          portfolio.technologies!,
                          style: const TextStyle(color: Colors.black54),
                        ),
                      ],
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
            if (portfolio.description != null &&
                portfolio.description!.isNotEmpty) ...[
              const SizedBox(height: 8),
              Text(
                portfolio.description!,
                maxLines: 2,
                overflow: TextOverflow.ellipsis,
                style: const TextStyle(fontSize: 13, color: Colors.black87),
              ),
            ],
            const SizedBox(height: 8),
            Wrap(
              spacing: 8,
              runSpacing: 6,
              children: [
                if (portfolio.projectUrl != null)
                  const _Tag(icon: Icons.link_outlined, label: 'Link attached'),
                if (portfolio.fileUrl != null)
                  const _Tag(icon: Icons.attach_file, label: 'File attached'),
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
