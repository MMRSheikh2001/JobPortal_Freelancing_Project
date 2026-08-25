import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/public_pages/provider/home_dashboard_provider.dart';
import 'package:work_bridge_flutter/router/app_router.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';

/// Job seeker's "Profile Center" — mirrors WorkBridgeAndroid's
/// ProfileCenterActivity: photo + name up top, a completion bar, then
/// one card per CV section, each navigating to that section's own
/// screen. This screen only orchestrates navigation + shows a summary;
/// the actual editing happens in each destination screen.
class UserProfileScreen extends ConsumerStatefulWidget {
  const UserProfileScreen({super.key});

  @override
  ConsumerState<UserProfileScreen> createState() => _UserProfileScreenState();
}

class _UserProfileScreenState extends ConsumerState<UserProfileScreen> {
  @override
  void initState() {
    super.initState();
    WidgetsBinding.instance.addPostFrameCallback((_) => _load());
  }

  void _load() {
    final userId = ref.read(currentUserProvider)?.userId;
    if (userId != null) {
      ref.read(dashboardControllerProvider.notifier).loadUserDashboard(userId);
    }
  }

  @override
  Widget build(BuildContext context) {
    final dashboardState = ref.watch(dashboardControllerProvider);
    final dashboard = dashboardState.value;
    final currentUser = ref.watch(currentUserProvider);

    // Prefer live dashboard data (reflects the latest saved profile);
    // fall back to what's cached from login while the dashboard loads.
    final name =
        dashboard?.userName ?? currentUser?.displayName ?? 'Your Profile';
    final rawImage = dashboard?.profileImage ?? currentUser?.image;
    final photoUrl = (rawImage != null && rawImage.isNotEmpty)
        ? '${ApiConstants.userProfileImageUrl}$rawImage'
        : null;
    final completion = dashboard?.profileCompletion ?? 0;

    return Scaffold(
      appBar: AppBar(title: const Text('My Profile')),
      body: RefreshIndicator(
        onRefresh: () async {
          final userId = ref.read(currentUserProvider)?.userId;
          if (userId != null) {
            await ref
                .read(dashboardControllerProvider.notifier)
                .refresh(userId);
          }
        },
        child: ListView(
          padding: const EdgeInsets.all(16),
          children: [
            // ── Photo + name ─────────────────────────────
            Center(
              child: Column(
                children: [
                  CircleAvatar(
                    radius: 44,
                    backgroundColor: Colors.blue.shade50,
                    backgroundImage: photoUrl != null
                        ? NetworkImage(photoUrl)
                        : null,
                    child: photoUrl == null
                        ? const Icon(Icons.person, size: 44, color: Colors.blue)
                        : null,
                  ),
                  const SizedBox(height: 12),
                  Text(
                    name,
                    style: const TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ],
              ),
            ),
            const SizedBox(height: 24),

            // ── Completion ────────────────────────────────
            Text(
              'Profile Completion: $completion%',
              style: const TextStyle(fontSize: 15, fontWeight: FontWeight.w600),
            ),
            const SizedBox(height: 8),
            ClipRRect(
              borderRadius: BorderRadius.circular(6),
              child: LinearProgressIndicator(
                value: completion / 100,
                minHeight: 10,
                backgroundColor: Colors.grey.shade200,
              ),
            ),
            const SizedBox(height: 24),

            // ── Sections ──────────────────────────────────
            _ProfileSectionCard(
              emoji: '👤',
              label: 'Personal Information',
              onTap: () =>
                  Navigator.of(context).pushNamed(AppRouter.personalInfo),
            ),
            _ProfileSectionCard(
              emoji: '🎓',
              label: 'Education',
              onTap: () =>
                  Navigator.of(context).pushNamed(AppRouter.educations),
            ),
            _ProfileSectionCard(
              emoji: '💼',
              label: 'Experience',
              onTap: () =>
                  Navigator.of(context).pushNamed(AppRouter.experiences),
            ),
            _ProfileSectionCard(
              emoji: '🏆',
              label: 'Training',
              onTap: () => Navigator.of(context).pushNamed(AppRouter.trainings),
            ),
            _ProfileSectionCard(
              emoji: '🛠',
              label: 'Skills',
              onTap: () =>
                  Navigator.of(context).pushNamed(AppRouter.userSkills),
            ),
            _ProfileSectionCard(
              emoji: '🌐',
              label: 'Languages',
              onTap: () =>
                  Navigator.of(context).pushNamed(AppRouter.userLanguages),
            ),
            _ProfileSectionCard(
              emoji: '📂',
              label: 'Portfolio',
              onTap: () =>
                  Navigator.of(context).pushNamed(AppRouter.portfolios),
            ),
            _ProfileSectionCard(
              emoji: '🤝',
              label: 'References',
              onTap: () =>
                  Navigator.of(context).pushNamed(AppRouter.references),
            ),
            _ProfileSectionCard(
              emoji: '⭐',
              label: 'Extracurricular',
              onTap: () =>
                  Navigator.of(context).pushNamed(AppRouter.extracurriculars),
            ),
            _ProfileSectionCard(
              emoji: '📄',
              label: 'Upload Resume',
              onTap: () =>
                  Navigator.of(context).pushNamed(AppRouter.resumeFile),
            ),
            _ProfileSectionCard(
              emoji: '👁',
              label: 'Resume Preview',
              onTap: () =>
                  Navigator.of(context).pushNamed(AppRouter.fullResumeScreen),
            ),
          ],
        ),
      ),
    );
  }
}

class _ProfileSectionCard extends StatelessWidget {
  const _ProfileSectionCard({
    required this.emoji,
    required this.label,
    required this.onTap,
  });

  final String emoji;
  final String label;
  final VoidCallback onTap;

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.only(bottom: 10),
      clipBehavior: Clip.antiAlias,
      child: InkWell(
        onTap: onTap,
        child: Padding(
          padding: const EdgeInsets.all(18),
          child: Row(
            children: [
              Text(emoji, style: const TextStyle(fontSize: 20)),
              const SizedBox(width: 12),
              Expanded(
                child: Text(
                  label,
                  style: const TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ),
              const Icon(Icons.chevron_right, color: Colors.black38),
            ],
          ),
        ),
      ),
    );
  }
}
