import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:url_launcher/url_launcher.dart';

import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/education_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/experience_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/extracurricular_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/portfolio_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/reference_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/training_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/user_language_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/user_skill_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/user_profile_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/resume_response.dart';

import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

final fullResumeProvider = FutureProvider<ResumeResponseDTO?>((ref) async {
  final user = ref.watch(currentUserProvider);

  final profileId = user?.profileId;

  if (profileId == null) {
    return null;
  }

  return ref.watch(cvRepositoryProvider).getResume(profileId);
});

class FullResumeScreen extends ConsumerWidget {
  const FullResumeScreen({super.key});

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final resumeAsync = ref.watch(fullResumeProvider);

    return Scaffold(
      appBar: AppBar(
        title: const Text('My Resume'),
      ),
      body: resumeAsync.when(
        loading: () => const Center(
          child: CircularProgressIndicator(),
        ),
        error: (error, stackTrace) => _ErrorView(
          message: apiErrorMessage(error),
          onRetry: () {
            ref.invalidate(fullResumeProvider);
          },
        ),
        data: (resume) {
          if (resume == null) {
            return const Center(
              child: Text(
                'Please create your profile first.',
                style: TextStyle(fontSize: 16),
              ),
            );
          }

          return _ResumeContent(resume: resume);
        },
      ),
    );
  }
}

class _ResumeContent extends StatelessWidget {
  const _ResumeContent({
    required this.resume,
  });

  final ResumeResponseDTO resume;

  @override
  Widget build(BuildContext context) {
    final profile = resume.profile;

    return RefreshIndicator(
      onRefresh: () async {
        // Refresh is handled by the provider from the parent.
      },
      child: SingleChildScrollView(
        physics: const AlwaysScrollableScrollPhysics(),
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            _ProfileHeader(profile: profile),

            const SizedBox(height: 16),

            if (_hasText(profile?.professionalSummary))
              _ResumeSection(
                title: 'Professional Summary',
                icon: Icons.person_outline,
                child: Text(
                  profile!.professionalSummary!,
                  style: const TextStyle(height: 1.5),
                ),
              ),

            if (_hasText(profile?.careerObjective))
              _ResumeSection(
                title: 'Career Objective',
                icon: Icons.flag_outlined,
                child: Text(
                  profile!.careerObjective!,
                  style: const TextStyle(height: 1.5),
                ),
              ),

            _EducationSection(
              educations: resume.educations ?? [],
            ),

            _ExperienceSection(
              experiences: resume.experiences ?? [],
            ),

            _SkillsSection(
              skills: resume.skills ?? [],
            ),

            _LanguagesSection(
              languages: resume.languages ?? [],
            ),

            _TrainingSection(
              trainings: resume.trainings ?? [],
            ),

            _PortfolioSection(
              portfolios: resume.portfolios ?? [],
            ),

            _ExtracurricularSection(
              extracurriculars: resume.extracurriculars ?? [],
            ),

            _ReferenceSection(
              references: resume.references ?? [],
            ),

            const SizedBox(height: 24),

            _GeneratedCvButton(
              profileId: profile?.id,
            ),

            const SizedBox(height: 24),
          ],
        ),
      ),
    );
  }
}
class _ProfileHeader extends StatelessWidget {
  const _ProfileHeader({
    required this.profile,
  });

  final UserProfileResponseDTO? profile;

  @override
  Widget build(BuildContext context) {
    if (profile == null) {
      return const Card(
        child: Padding(
          padding: EdgeInsets.all(20),
          child: Text('Profile information is not available.'),
        ),
      );
    }

    return Card(
      child: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          children: [
            if (_hasText(profile!.image))
              CircleAvatar(
                radius: 50,
                backgroundImage: NetworkImage(
                  '${ApiConstants.userProfileImageUrl}${profile!.image}',
                ),
              )
            else
              const CircleAvatar(
                radius: 50,
                child: Icon(
                  Icons.person,
                  size: 50,
                ),
              ),

            const SizedBox(height: 14),

            Text(
              profile!.name ?? 'Unnamed User',
              style: Theme.of(context).textTheme.headlineSmall?.copyWith(
                fontWeight: FontWeight.bold,
              ),
              textAlign: TextAlign.center,
            ),

            if (_hasText(profile!.headline)) ...[
              const SizedBox(height: 6),
              Text(
                profile!.headline!,
                textAlign: TextAlign.center,
                style: Theme.of(context).textTheme.titleMedium,
              ),
            ],

            const SizedBox(height: 12),

            if (_hasText(profile!.userEmail))
              _InfoRow(
                icon: Icons.email_outlined,
                text: profile!.userEmail!,
              ),

            if (_hasText(profile!.phone))
              _InfoRow(
                icon: Icons.phone_outlined,
                text: profile!.phone!,
              ),

            if (_hasText(profile!.nationality))
              _InfoRow(
                icon: Icons.public,
                text: profile!.nationality!,
              ),

            if (_hasText(profile!.presentAddressDetails))
              _InfoRow(
                icon: Icons.location_on_outlined,
                text: _buildPresentAddress(profile!),
              ),

            const SizedBox(height: 12),

            _SocialLinks(profile: profile!),
          ],
        ),
      ),
    );
  }
}

class _EducationSection extends StatelessWidget {
  const _EducationSection({
    required this.educations,
  });

  final List<EducationResponseDTO> educations;

  @override
  Widget build(BuildContext context) {
    if (educations.isEmpty) {
      return const SizedBox.shrink();
    }

    return _ResumeSection(
      title: 'Education',
      icon: Icons.school_outlined,
      child: Column(
        children: educations.map((education) {
          return _TimelineCard(
            title: education.educationLevel?.name.toUpperCase() ?? 'Education',
            subtitle: education.institution,
            period: _dateRange(
              education.startDate,
              education.endDate,
              education.currentlyStudying,
            ),
            children: [
              if (_hasText(education.fieldOfStudy))
                _InfoText(
                  label: 'Field of Study',
                  value: education.fieldOfStudy!,
                ),
              if (_hasText(education.board))
                _InfoText(
                  label: 'Board',
                  value: education.board!,
                ),
              if (_hasText(education.gradeOrDivision))
                _InfoText(
                  label: 'Grade / Division',
                  value: education.gradeOrDivision!,
                ),
            ],
          );
        }).toList(),
      ),
    );
  }
}

class _ExperienceSection extends StatelessWidget {
  const _ExperienceSection({
    required this.experiences,
  });

  final List<ExperienceResponseDTO> experiences;

  @override
  Widget build(BuildContext context) {
    if (experiences.isEmpty) {
      return const SizedBox.shrink();
    }

    return _ResumeSection(
      title: 'Experience',
      icon: Icons.work_outline,
      child: Column(
        children: experiences.map((experience) {
          return _TimelineCard(
            title: experience.position ?? 'Experience',
            subtitle: experience.companyName,
            period: _dateRange(
              experience.startDate,
              experience.endDate,
              experience.currentlyWorking,
            ),
            children: [
              if (_hasText(experience.responsibilities))
                Text(
                  experience.responsibilities!,
                  style: const TextStyle(height: 1.5),
                ),
            ],
          );
        }).toList(),
      ),
    );
  }
}

class _SkillsSection extends StatelessWidget {
  const _SkillsSection({
    required this.skills,
  });

  final List<UserSkillResponseDTO> skills;

  @override
  Widget build(BuildContext context) {
    if (skills.isEmpty) {
      return const SizedBox.shrink();
    }

    return _ResumeSection(
      title: 'Skills',
      icon: Icons.psychology_outlined,
      child: Wrap(
        spacing: 8,
        runSpacing: 8,
        children: skills.map((skill) {
          final name = skill.skillName ?? 'Skill';

          final proficiency = skill.proficiencyLevel?.value;

          return Chip(
            avatar: const Icon(
              Icons.check_circle_outline,
              size: 18,
            ),
            label: Text(
              proficiency == null
                  ? name
                  : '$name • $proficiency',
            ),
          );
        }).toList(),
      ),
    );
  }
}

class _LanguagesSection extends StatelessWidget {
  const _LanguagesSection({
    required this.languages,
  });

  final List<UserLanguageResponseDTO> languages;

  @override
  Widget build(BuildContext context) {
    if (languages.isEmpty) {
      return const SizedBox.shrink();
    }

    return _ResumeSection(
      title: 'Languages',
      icon: Icons.language,
      child: Column(
        children: languages.map((language) {
          return ListTile(
            contentPadding: EdgeInsets.zero,
            leading: const CircleAvatar(
              child: Icon(Icons.language),
            ),
            title: Text(
              language.languageName ?? 'Language',
            ),
            subtitle: Text(
              language.proficiency?.value ?? 'Not specified',
            ),
          );
        }).toList(),
      ),
    );
  }
}
class _TrainingSection extends StatelessWidget {
  const _TrainingSection({
    required this.trainings,
  });

  final List<TrainingResponseDTO> trainings;

  @override
  Widget build(BuildContext context) {
    if (trainings.isEmpty) {
      return const SizedBox.shrink();
    }

    return _ResumeSection(
      title: 'Training & Certifications',
      icon: Icons.workspace_premium_outlined,
      child: Column(
        children: trainings.map((training) {
          return _TimelineCard(
            title: training.name ?? 'Training',
            subtitle: training.institution,
            period: _dateRange(
              training.startDate,
              training.endDate,
              training.completed,
            ),
            children: [
              if (_hasText(training.description))
                Text(
                  training.description!,
                  style: const TextStyle(height: 1.5),
                ),
              if (_hasText(training.duration))
                _InfoText(
                  label: 'Duration',
                  value: training.duration!,
                ),
              if (training.trainingType != null)
                _InfoText(
                  label: 'Type',
                  value: training.trainingType!.value,
                ),
            ],
          );
        }).toList(),
      ),
    );
  }
}

class _PortfolioSection extends StatelessWidget {
  const _PortfolioSection({
    required this.portfolios,
  });

  final List<PortfolioResponseDTO> portfolios;

  @override
  Widget build(BuildContext context) {
    if (portfolios.isEmpty) {
      return const SizedBox.shrink();
    }

    return _ResumeSection(
      title: 'Portfolio',
      icon: Icons.folder_outlined,
      child: Column(
        children: portfolios.map((portfolio) {
          return _TimelineCard(
            title: portfolio.title ?? 'Project',
            subtitle: portfolio.technologies,
            children: [
              if (_hasText(portfolio.description))
                Text(
                  portfolio.description!,
                  style: const TextStyle(height: 1.5),
                ),
              if (_hasText(portfolio.projectUrl))
                _InfoText(
                  label: 'Project URL',
                  value: portfolio.projectUrl!,
                ),
            ],
          );
        }).toList(),
      ),
    );
  }
}

class _ReferenceSection extends StatelessWidget {
  const _ReferenceSection({
    required this.references,
  });

  final List<ReferenceResponseDTO> references;

  @override
  Widget build(BuildContext context) {
    if (references.isEmpty) {
      return const SizedBox.shrink();
    }

    return _ResumeSection(
      title: 'References',
      icon: Icons.people_outline,
      child: Column(
        children: references.map((reference) {
          return _TimelineCard(
            title: reference.name ?? 'Reference',
            subtitle: reference.designation,
            children: [
              if (_hasText(reference.organization))
                _InfoText(
                  label: 'Organization',
                  value: reference.organization!,
                ),
              if (_hasText(reference.relation))
                _InfoText(
                  label: 'Relation',
                  value: reference.relation!,
                ),
              if (_hasText(reference.phone))
                _InfoText(
                  label: 'Phone',
                  value: reference.phone!,
                ),
              if (_hasText(reference.email))
                _InfoText(
                  label: 'Email',
                  value: reference.email!,
                ),
            ],
          );
        }).toList(),
      ),
    );
  }
}

class _ExtracurricularSection extends StatelessWidget {
  const _ExtracurricularSection({
    required this.extracurriculars,
  });

  final List<ExtracurricularResponseDTO> extracurriculars;

  @override
  Widget build(BuildContext context) {
    if (extracurriculars.isEmpty) {
      return const SizedBox.shrink();
    }

    return _ResumeSection(
      title: 'Extracurricular Activities',
      icon: Icons.groups_outlined,
      child: Column(
        children: extracurriculars.map((item) {
          return _TimelineCard(
            title: item.title ?? 'Activity',
            subtitle: item.organization,
            children: [
              if (_hasText(item.description))
                Text(
                  item.description!,
                  style: const TextStyle(height: 1.5),
                ),
              if (_hasText(item.role))
                _InfoText(
                  label: 'Role',
                  value: item.role!,
                ),
            ],
          );
        }).toList(),
      ),
    );
  }
}

class _ResumeSection extends StatelessWidget {
  const _ResumeSection({
    required this.title,
    required this.icon,
    required this.child,
  });

  final String title;
  final IconData icon;
  final Widget child;

  @override
  Widget build(BuildContext context) {
    return Card(
      margin: const EdgeInsets.only(bottom: 16),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Icon(icon),
                const SizedBox(width: 8),
                Text(
                  title,
                  style: Theme.of(context)
                      .textTheme
                      .titleLarge
                      ?.copyWith(
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 14),
            child,
          ],
        ),
      ),
    );
  }
}

class _TimelineCard extends StatelessWidget {
  const _TimelineCard({
    required this.title,
    this.subtitle,
    this.period,
    this.children = const [],
  });

  final String title;
  final String? subtitle;
  final String? period;
  final List<Widget> children;

  @override
  Widget build(BuildContext context) {
    return Container(
      width: double.infinity,
      margin: const EdgeInsets.only(bottom: 12),
      padding: const EdgeInsets.all(14),
      decoration: BoxDecoration(
        border: Border.all(
          color: Theme.of(context).dividerColor,
        ),
        borderRadius: BorderRadius.circular(10),
      ),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            title,
            style: const TextStyle(
              fontWeight: FontWeight.bold,
              fontSize: 16,
            ),
          ),

          if (_hasText(subtitle)) ...[
            const SizedBox(height: 4),
            Text(
              subtitle!,
              style: const TextStyle(
                fontWeight: FontWeight.w500,
              ),
            ),
          ],

          if (_hasText(period)) ...[
            const SizedBox(height: 4),
            Text(
              period!,
              style: TextStyle(
                color: Theme.of(context)
                    .colorScheme
                    .primary,
              ),
            ),
          ],

          if (children.isNotEmpty) ...[
            const SizedBox(height: 10),
            ...children.map(
                  (child) => Padding(
                padding: const EdgeInsets.only(bottom: 4),
                child: child,
              ),
            ),
          ],
        ],
      ),
    );
  }
}

class _InfoText extends StatelessWidget {
  const _InfoText({
    required this.label,
    required this.value,
  });

  final String label;
  final String value;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 4),
      child: RichText(
        text: TextSpan(
          style: DefaultTextStyle.of(context).style,
          children: [
            TextSpan(
              text: '$label: ',
              style: const TextStyle(
                fontWeight: FontWeight.bold,
              ),
            ),
            TextSpan(text: value),
          ],
        ),
      ),
    );
  }
}

class _InfoRow extends StatelessWidget {
  const _InfoRow({
    required this.icon,
    required this.text,
  });

  final IconData icon;
  final String text;

  @override
  Widget build(BuildContext context) {
    return Padding(
      padding: const EdgeInsets.only(top: 5),
      child: Row(
        children: [
          Icon(
            icon,
            size: 18,
          ),
          const SizedBox(width: 8),
          Expanded(
            child: Text(text),
          ),
        ],
      ),
    );
  }
}

class _SocialLinks extends StatelessWidget {
  const _SocialLinks({
    required this.profile,
  });

  final UserProfileResponseDTO profile;

  @override
  Widget build(BuildContext context) {
    final links = <String>[
      if (_hasText(profile.githubLink))
        'GitHub: ${profile.githubLink}',
      if (_hasText(profile.linkedinLink))
        'LinkedIn: ${profile.linkedinLink}',
      if (_hasText(profile.portfolioWebsite))
        'Portfolio: ${profile.portfolioWebsite}',
    ];

    if (links.isEmpty) {
      return const SizedBox.shrink();
    }

    return Column(
      children: links
          .map(
            (link) => Padding(
          padding: const EdgeInsets.only(top: 4),
          child: Text(
            link,
            textAlign: TextAlign.center,
          ),
        ),
      )
          .toList(),
    );
  }
}

class _ErrorView extends StatelessWidget {
  const _ErrorView({
    required this.message,
    required this.onRetry,
  });

  final String message;
  final VoidCallback onRetry;

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          mainAxisSize: MainAxisSize.min,
          children: [
            const Icon(
              Icons.error_outline,
              size: 48,
            ),
            const SizedBox(height: 12),
            Text(
              message,
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 16),
            ElevatedButton(
              onPressed: onRetry,
              child: const Text('Retry'),
            ),
          ],
        ),
      ),
    );
  }
}

class _GeneratedCvButton extends ConsumerWidget {
  const _GeneratedCvButton({
    required this.profileId,
  });

  final int? profileId;

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    if (profileId == null) {
      return const SizedBox.shrink();
    }

    return SizedBox(
      width: double.infinity,
      child: ElevatedButton.icon(
        icon: const Icon(Icons.picture_as_pdf),
        label: const Text('View Generated CV'),
        onPressed: () async {
          final url = Uri.parse(
            '${ApiConstants.baseUrl}${ApiConstants.resumePdf(profileId!)}',
          );

          try {
            if (await canLaunchUrl(url)) {
              await launchUrl(
                url,
                mode: LaunchMode.externalApplication,
              );
            } else {
              if (!context.mounted) return;
              ScaffoldMessenger.of(context).showSnackBar(
                const SnackBar(
                  content: Text('Could not open the CV PDF.'),
                ),
              );
            }
          } catch (e) {
            if (!context.mounted) return;

            ScaffoldMessenger.of(context).showSnackBar(
              const SnackBar(
                content: Text(
                  'Error opening CV.',
                ),
              ),
            );
          }
        },
      ),
    );
  }
}

bool _hasText(String? value) {
  return value != null && value.trim().isNotEmpty;
}

String _buildPresentAddress(UserProfileResponseDTO profile) {
  final parts = <String>[
    if (_hasText(profile.presentAddressDetails))
      profile.presentAddressDetails!,
    if (_hasText(profile.presentPoliceStationName))
      profile.presentPoliceStationName!,
    if (_hasText(profile.presentDistrictName))
      profile.presentDistrictName!,
    if (_hasText(profile.presentDivisionName))
      profile.presentDivisionName!,
    if (_hasText(profile.presentCountryName))
      profile.presentCountryName!,
    if (_hasText(profile.presentAddressPostCode))
      profile.presentAddressPostCode!,
  ];

  return parts.join(', ');
}

String? _dateRange(
    DateTime? start,
    DateTime? end,
    bool? ongoing,
    ) {
  if (start == null && end == null) {
    return null;
  }

  String format(DateTime date) {
    return '${date.day.toString().padLeft(2, '0')}/'
        '${date.month.toString().padLeft(2, '0')}/'
        '${date.year}';
  }

  if (start != null && ongoing == true) {
    return '${format(start)} - Present';
  }

  if (start != null && end != null) {
    return '${format(start)} - ${format(end)}';
  }

  if (start != null) {
    return format(start);
  }

  return format(end!);
}