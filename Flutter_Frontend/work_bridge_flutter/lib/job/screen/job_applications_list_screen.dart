import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/enums/application_status.dart';
import 'package:work_bridge_flutter/job/entity/response/job_application_response.dart';
import 'package:work_bridge_flutter/router/app_router.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

class JobApplicationsListScreen extends ConsumerStatefulWidget {
  const JobApplicationsListScreen({super.key});

  @override
  ConsumerState<JobApplicationsListScreen> createState() =>
      _JobApplicationsListScreenState();
}

class _JobApplicationsListScreenState
    extends ConsumerState<JobApplicationsListScreen> {
  late Future<List<JobApplicationResponseDTO>> _applicationsFuture;

  @override
  void initState() {
    super.initState();
    _loadApplications();
  }

  void _loadApplications() {
    final user = ref.read(currentUserProvider);
    final profileId = user?.profileId;

    if (profileId == null) {
      _applicationsFuture = Future.error(Exception('User profile not found.'));
      return;
    }

    _applicationsFuture = ref
        .read(jobRepositoryProvider)
        .getApplicationsByUserProfileId(profileId);
  }

  Future<void> _refreshApplications() async {
    setState(() {
      _loadApplications();
    });

    await _applicationsFuture;
  }

  Future<void> _withdrawApplication(
    JobApplicationResponseDTO application,
  ) async {
    final applicationId = application.id;
    final profileId =
        application.userProfileId ?? ref.read(currentUserProvider)?.profileId;

    if (applicationId == null || profileId == null) {
      _showMessage('Unable to withdraw this application.');
      return;
    }

    final confirmed = await showDialog<bool>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Withdraw Application'),
          content: Text(
            'Are you sure you want to withdraw your application for '
            '"${application.jobTitle ?? 'this job'}"?',
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context, false),
              child: const Text('Cancel'),
            ),
            FilledButton(
              onPressed: () => Navigator.pop(context, true),
              child: const Text('Withdraw'),
            ),
          ],
        );
      },
    );

    if (confirmed != true) return;

    try {
      await ref
          .read(jobRepositoryProvider)
          .withdrawApplication(applicationId, profileId);

      if (!mounted) return;

      _showMessage('Application withdrawn successfully.');

      setState(() {
        _loadApplications();
      });
    } catch (e) {
      if (!mounted) return;
      _showMessage('Failed to withdraw application.');
    }
  }

  void _openJobDetails(JobApplicationResponseDTO application) {
    final jobId = application.jobId;

    if (jobId == null) {
      _showMessage('Job information is not available.');
      return;
    }

    Navigator.pushNamed(context, AppRouter.jobDetails, arguments: jobId);
  }

  void _startAiInterview(JobApplicationResponseDTO application) {
    final applicationId = application.id;

    if (applicationId == null) {
      _showMessage('Application information is not available.');
      return;
    }

    Navigator.pushNamed(
      context,
      AppRouter.aiInterview,
      arguments: applicationId,
    );
  }

  bool _canWithdraw(JobApplicationResponseDTO application) {
    final status = application.status;

    return status == ApplicationStatus.applied ||
        status == ApplicationStatus.aiPending;
  }

  bool _canStartAiInterview(JobApplicationResponseDTO application) {
    return application.aiInterviewEnabled == true &&
        application.aiInterviewCompleted != true &&
        application.status == ApplicationStatus.aiPending;
  }

  String _getStatusText(ApplicationStatus? status) {
    switch (status) {
      case ApplicationStatus.applied:
        return 'Applied';
      case ApplicationStatus.aiPending:
        return 'AI Interview Pending';
      case ApplicationStatus.aiCompleted:
        return 'AI Interview Completed';
      case ApplicationStatus.automaticQualified:
        return 'Automatically Qualified';
      case ApplicationStatus.companyShortlisted:
        return 'Company Shortlisted';
      case ApplicationStatus.hired:
        return 'Hired';
      case ApplicationStatus.rejected:
        return 'Rejected';
      case ApplicationStatus.withdrawn:
        return 'Withdrawn';
      default:
        return 'Unknown';
    }
  }

  Color _getStatusColor(ApplicationStatus? status) {
    switch (status) {
      case ApplicationStatus.applied:
        return Colors.blue;
      case ApplicationStatus.aiPending:
        return Colors.orange;
      case ApplicationStatus.aiCompleted:
        return Colors.indigo;
      case ApplicationStatus.automaticQualified:
        return Colors.teal;
      case ApplicationStatus.companyShortlisted:
        return Colors.green;
      case ApplicationStatus.hired:
        return Colors.green.shade700;
      case ApplicationStatus.rejected:
        return Colors.red;
      case ApplicationStatus.withdrawn:
        return Colors.grey;
      default:
        return Colors.grey;
    }
  }

  String _formatDate(DateTime? date) {
    if (date == null) return 'Unknown date';

    final localDate = date.toLocal();

    return '${localDate.day.toString().padLeft(2, '0')}/'
        '${localDate.month.toString().padLeft(2, '0')}/'
        '${localDate.year}';
  }

  void _showMessage(String message) {
    ScaffoldMessenger.of(context)
      ..hideCurrentSnackBar()
      ..showSnackBar(SnackBar(content: Text(message)));
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('My Applications')),
      body: FutureBuilder<List<JobApplicationResponseDTO>>(
        future: _applicationsFuture,
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(child: CircularProgressIndicator());
          }

          if (snapshot.hasError) {
            return _buildErrorState();
          }

          final applications = snapshot.data ?? [];

          if (applications.isEmpty) {
            return _buildEmptyState();
          }

          return RefreshIndicator(
            onRefresh: _refreshApplications,
            child: ListView.builder(
              padding: const EdgeInsets.all(16),
              itemCount: applications.length,
              itemBuilder: (context, index) {
                return _buildApplicationCard(applications[index]);
              },
            ),
          );
        },
      ),
    );
  }

  Widget _buildApplicationCard(JobApplicationResponseDTO application) {
    final statusColor = _getStatusColor(application.status);

    return Card(
      margin: const EdgeInsets.only(bottom: 16),
      elevation: 2,
      clipBehavior: Clip.antiAlias,
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                _buildCompanyLogo(application),

                const SizedBox(width: 12),

                Expanded(
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Text(
                        application.jobTitle ?? 'Untitled Job',
                        style: const TextStyle(
                          fontSize: 18,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const SizedBox(height: 4),
                      Text(
                        application.companyName ?? 'Unknown Company',
                        style: TextStyle(
                          fontSize: 14,
                          color: Colors.grey.shade700,
                        ),
                      ),
                    ],
                  ),
                ),
              ],
            ),

            const SizedBox(height: 14),

            Container(
              padding: const EdgeInsets.symmetric(horizontal: 10, vertical: 6),
              decoration: BoxDecoration(
                color: statusColor.withOpacity(0.1),
                borderRadius: BorderRadius.circular(20),
              ),
              child: Text(
                _getStatusText(application.status),
                style: TextStyle(
                  color: statusColor,
                  fontWeight: FontWeight.w600,
                  fontSize: 13,
                ),
              ),
            ),

            const SizedBox(height: 12),

            Row(
              children: [
                Icon(
                  Icons.calendar_today_outlined,
                  size: 15,
                  color: Colors.grey.shade600,
                ),
                const SizedBox(width: 6),
                Text(
                  'Applied: ${_formatDate(application.appliedAt)}',
                  style: TextStyle(color: Colors.grey.shade700, fontSize: 13),
                ),
              ],
            ),

            if (application.aiInterviewEnabled == true) ...[
              const SizedBox(height: 8),
              Row(
                children: [
                  Icon(
                    Icons.smart_toy_outlined,
                    size: 16,
                    color: Colors.grey.shade600,
                  ),
                  const SizedBox(width: 6),
                  Text(
                    'AI Interview Enabled',
                    style: TextStyle(color: Colors.grey.shade700, fontSize: 13),
                  ),
                ],
              ),
            ],

            const SizedBox(height: 16),
            const Divider(),
            const SizedBox(height: 4),

            Wrap(
              spacing: 8,
              runSpacing: 8,
              children: [
                OutlinedButton.icon(
                  onPressed: () => _openJobDetails(application),
                  icon: const Icon(Icons.work_outline, size: 18),
                  label: const Text('Job Details'),
                ),

                if (_canStartAiInterview(application))
                  FilledButton.icon(
                    onPressed: () => _startAiInterview(application),
                    icon: const Icon(Icons.smart_toy_outlined, size: 18),
                    label: const Text('Start AI Interview'),
                  ),

                if (_canWithdraw(application))
                  TextButton.icon(
                    onPressed: () => _withdrawApplication(application),
                    icon: const Icon(Icons.cancel_outlined, size: 18),
                    label: const Text('Withdraw'),
                    style: TextButton.styleFrom(foregroundColor: Colors.red),
                  ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildCompanyLogo(JobApplicationResponseDTO application) {
    final logo = application.companyLogo;

    if (logo == null || logo.trim().isEmpty) {
      return _buildLogoPlaceholder();
    }

    final imageUrl = '${ApiConstants.companyProfileImageUrl}$logo';

    return ClipRRect(
      borderRadius: BorderRadius.circular(8),
      child: Image.network(
        imageUrl,
        width: 60,
        height: 60,
        fit: BoxFit.cover,
        errorBuilder: (context, error, stackTrace) {
          return _buildLogoPlaceholder();
        },
        loadingBuilder: (context, child, loadingProgress) {
          if (loadingProgress == null) {
            return child;
          }

          return Container(
            width: 60,
            height: 60,
            alignment: Alignment.center,
            decoration: BoxDecoration(
              color: Colors.grey.shade100,
              borderRadius: BorderRadius.circular(8),
            ),
            child: const SizedBox(
              width: 22,
              height: 22,
              child: CircularProgressIndicator(strokeWidth: 2),
            ),
          );
        },
      ),
    );
  }

  Widget _buildLogoPlaceholder() {
    return Container(
      width: 60,
      height: 60,
      decoration: BoxDecoration(
        color: Colors.grey.shade100,
        borderRadius: BorderRadius.circular(8),
      ),
      child: Icon(
        Icons.business_outlined,
        size: 30,
        color: Colors.grey.shade500,
      ),
    );
  }

  Widget _buildEmptyState() {
    return RefreshIndicator(
      onRefresh: _refreshApplications,
      child: ListView(
        physics: const AlwaysScrollableScrollPhysics(),
        children: [
          SizedBox(height: MediaQuery.of(context).size.height * 0.25),
          Icon(
            Icons.description_outlined,
            size: 70,
            color: Colors.grey.shade400,
          ),
          const SizedBox(height: 16),
          const Center(
            child: Text(
              'No Applications Yet',
              style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
            ),
          ),
          const SizedBox(height: 8),
          Center(
            child: Text(
              'Jobs you apply for will appear here.',
              style: TextStyle(color: Colors.grey.shade600),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildErrorState() {
    return Center(
      child: Padding(
        padding: const EdgeInsets.all(24),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(Icons.error_outline, size: 60, color: Colors.red.shade300),
            const SizedBox(height: 16),
            const Text(
              'Unable to load applications',
              style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
            ),
            const SizedBox(height: 8),
            Text(
              'Something went wrong while loading your applications.',
              textAlign: TextAlign.center,
              style: TextStyle(color: Colors.grey.shade600),
            ),
            const SizedBox(height: 20),
            FilledButton.icon(
              onPressed: () {
                setState(() {
                  _loadApplications();
                });
              },
              icon: const Icon(Icons.refresh),
              label: const Text('Try Again'),
            ),
          ],
        ),
      ),
    );
  }
}
