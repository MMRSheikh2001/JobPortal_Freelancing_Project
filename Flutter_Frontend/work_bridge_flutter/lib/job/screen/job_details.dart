import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/enums/application_status.dart';
import 'package:work_bridge_flutter/enums/employment_type.dart';
import 'package:work_bridge_flutter/enums/work_place_type.dart';
import 'package:work_bridge_flutter/job/entity/request/job_application_request.dart';
import 'package:work_bridge_flutter/job/entity/response/job_application_response.dart';
import 'package:work_bridge_flutter/job/entity/response/job_response.dart';
import 'package:work_bridge_flutter/job/entity/response/resume_screening_result.dart';
import 'package:work_bridge_flutter/services/storage_service.dart';
import 'package:work_bridge_flutter/utils/api_constants.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

class JobDetailsScreen extends ConsumerStatefulWidget {
  const JobDetailsScreen({super.key, required this.jobId});

  final int jobId;

  @override
  ConsumerState<JobDetailsScreen> createState() => _JobDetailsScreenState();
}

class _JobDetailsScreenState extends ConsumerState<JobDetailsScreen> {
  final StorageService _storageService = StorageService(
    const FlutterSecureStorage(),
  );

  JobResponseDTO? _job;
  JobApplicationResponseDTO? _application;
  ResumeScreeningResult? _matchResult;

  bool _loading = true;
  bool _checkingApplication = false;
  bool _loadingMatch = false;
  bool _applying = false;

  String? _error;
  int? _userProfileId;

  @override
  void initState() {
    super.initState();
    _loadJobDetails();
  }

  Future<void> _loadJobDetails() async {
    setState(() {
      _loading = true;
      _error = null;
    });

    try {
      final loginResponse = await _storageService.getUser();

      if (loginResponse == null) {
        throw Exception('User session not found.');
      }

      final profileId = loginResponse.profileId;

      if (profileId == null) {
        throw Exception('Your user profile has not been created yet.');
      }

      final job = await ref
          .read(jobRepositoryProvider)
          .getJobById(widget.jobId);

      if (!mounted) return;

      setState(() {
        _userProfileId = profileId;
        _job = job;
        _loading = false;
      });

      await _loadApplicationStatus();
      await _loadAiMatch();
    } catch (e) {
      if (!mounted) return;

      setState(() {
        _loading = false;
        _error = e.toString();
      });
    }
  }

  Future<void> _loadApplicationStatus() async {
    final profileId = _userProfileId;

    if (profileId == null) return;

    setState(() {
      _checkingApplication = true;
    });

    try {
      final repository = ref.read(jobRepositoryProvider);

      final exists = await repository.existsApplication(
        widget.jobId,
        profileId,
      );

      if (!exists) {
        if (!mounted) return;

        setState(() {
          _application = null;
          _checkingApplication = false;
        });

        return;
      }

      final application = await repository.getApplicationByJobAndUser(
        widget.jobId,
        profileId,
      );

      if (!mounted) return;

      setState(() {
        _application = application;
        _checkingApplication = false;
      });
    } catch (_) {
      if (!mounted) return;

      setState(() {
        _checkingApplication = false;
      });
    }
  }

  Future<void> _loadAiMatch() async {
    final profileId = _userProfileId;

    if (profileId == null) return;

    setState(() {
      _loadingMatch = true;
    });

    try {
      final result = await ref
          .read(jobRepositoryProvider)
          .calculateJobMatch(widget.jobId, profileId);

      if (!mounted) return;

      setState(() {
        _matchResult = result;
        _loadingMatch = false;
      });
    } catch (_) {
      if (!mounted) return;

      setState(() {
        _loadingMatch = false;
      });
    }
  }

  Future<void> _applyForJob() async {
    final profileId = _userProfileId;

    if (profileId == null) {
      _showMessage('Your user profile could not be found.', isError: true);
      return;
    }

    if (_job?.id == null) return;

    final confirmed = await showDialog<bool>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Apply for this job?'),
          content: Text(
            'Are you sure you want to apply for ${_job!.title ?? 'this job'}?',
          ),
          actions: [
            TextButton(
              onPressed: () => Navigator.pop(context, false),
              child: const Text('Cancel'),
            ),
            FilledButton(
              onPressed: () => Navigator.pop(context, true),
              child: const Text('Apply'),
            ),
          ],
        );
      },
    );

    if (confirmed != true) return;

    setState(() {
      _applying = true;
    });

    try {
      final request = JobApplicationRequestDTO(
        jobId: _job!.id,
        userProfileId: profileId,
      );

      final application = await ref
          .read(jobRepositoryProvider)
          .applyJob(request);

      if (!mounted) return;

      setState(() {
        _application = application;
        _applying = false;
      });

      _showMessage('Application submitted successfully.');

      // Refresh AI match/application data after applying.
      await _loadAiMatch();
    } catch (e) {
      if (!mounted) return;

      setState(() {
        _applying = false;
      });

      _showMessage('Failed to apply: ${_cleanError(e)}', isError: true);
    }
  }

  Future<void> _withdrawApplication() async {
    final application = _application;
    final profileId = _userProfileId;

    if (application?.id == null || profileId == null) return;

    final confirmed = await showDialog<bool>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Withdraw application?'),
          content: const Text(
            'Are you sure you want to withdraw your application?',
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
      final updated = await ref
          .read(jobRepositoryProvider)
          .withdrawApplication(application!.id!, profileId);

      if (!mounted) return;

      setState(() {
        _application = updated;
      });

      _showMessage('Application withdrawn.');
    } catch (e) {
      if (!mounted) return;

      _showMessage(
        'Failed to withdraw application: ${_cleanError(e)}',
        isError: true,
      );
    }
  }

  void _showMessage(String message, {bool isError = false}) {
    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(message),
        backgroundColor: isError ? Colors.red : null,
      ),
    );
  }

  String _cleanError(Object error) {
    return error.toString().replaceFirst('Exception: ', '');
  }

  @override
  Widget build(BuildContext context) {
    if (_loading) {
      return const Scaffold(body: Center(child: CircularProgressIndicator()));
    }

    if (_error != null || _job == null) {
      return Scaffold(
        appBar: AppBar(title: const Text('Job Details')),
        body: Center(
          child: Padding(
            padding: const EdgeInsets.all(24),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                const Icon(Icons.error_outline, size: 48, color: Colors.red),
                const SizedBox(height: 12),
                Text(
                  _error ?? 'Unable to load job.',
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 16),
                FilledButton(
                  onPressed: _loadJobDetails,
                  child: const Text('Retry'),
                ),
              ],
            ),
          ),
        ),
      );
    }

    final job = _job!;

    return Scaffold(
      appBar: AppBar(title: const Text('Job Details')),
      bottomNavigationBar: _buildBottomAction(),
      body: RefreshIndicator(
        onRefresh: _loadJobDetails,
        child: ListView(
          padding: const EdgeInsets.fromLTRB(16, 16, 16, 100),
          children: [
            _buildJobHeader(job),
            const SizedBox(height: 16),
            _buildJobSummary(job),
            const SizedBox(height: 16),
            _buildAiMatchCard(),
            const SizedBox(height: 16),
            _buildSection(
              title: 'Job Description',
              icon: Icons.description_outlined,
              child: _buildText(job.jobDescription),
            ),
            _buildSection(
              title: 'Responsibilities',
              icon: Icons.task_alt_outlined,
              child: _buildText(job.jobResponsibilities),
            ),
            _buildSection(
              title: 'Educational Requirements',
              icon: Icons.school_outlined,
              child: _buildText(job.educationalRequirements),
            ),
            _buildSection(
              title: 'Experience Requirements',
              icon: Icons.work_history_outlined,
              child: _buildExperience(job),
            ),
            _buildSection(
              title: 'Additional Requirements',
              icon: Icons.checklist_outlined,
              child: _buildText(job.additionalRequirements),
            ),
            _buildSection(
              title: 'Benefits',
              icon: Icons.card_giftcard_outlined,
              child: _buildText(job.benefits),
            ),
            _buildCompanySection(job),
          ],
        ),
      ),
    );
  }

  Widget _buildJobHeader(JobResponseDTO job) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(18),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            CircleAvatar(
              radius: 32,
              backgroundColor: Colors.blue.shade50,
              backgroundImage: job.companyLogo != null
                  ? NetworkImage(
                '${ApiConstants.companyProfileImageUrl}${job.companyLogo}',
              )
                  : null,
              child: job.companyLogo == null
                  ? const Icon(Icons.business, size: 30, color: Colors.blue)
                  : null,
            ),
            const SizedBox(width: 14),
            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    job.title ?? 'Untitled position',
                    style: const TextStyle(
                      fontSize: 21,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  const SizedBox(height: 5),
                  Text(
                    job.companyName ?? 'Unknown company',
                    style: const TextStyle(fontSize: 15, color: Colors.black54),
                  ),
                  const SizedBox(height: 10),
                  if (job.categoryName != null)
                    Chip(
                      label: Text(job.categoryName!),
                      avatar: const Icon(Icons.category_outlined, size: 17),
                    ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildJobSummary(JobResponseDTO job) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Wrap(
          spacing: 8,
          runSpacing: 8,
          children: [
            _infoChip(Icons.location_on_outlined, _locationLabel(job)),
            if (job.employmentType != null)
              _infoChip(
                Icons.work_outline,
                _employmentLabel(job.employmentType!),
              ),
            if (job.workPlaceType != null)
              _infoChip(
                Icons.home_work_outlined,
                _workplaceLabel(job.workPlaceType!),
              ),
            if (job.vacancy != null)
              _infoChip(
                Icons.people_outline,
                '${job.vacancy} vacancy${job.vacancy == 1 ? '' : 'ies'}',
              ),
            _infoChip(Icons.payments_outlined, _salaryLabel(job)),
            if (job.applicationDeadline != null)
              _infoChip(
                Icons.event_outlined,
                'Apply by ${_formatDate(job.applicationDeadline!)}',
              ),
          ],
        ),
      ),
    );
  }

  Widget _buildAiMatchCard() {
    if (_loadingMatch) {
      return const Card(
        child: Padding(
          padding: EdgeInsets.all(20),
          child: Row(
            children: [
              SizedBox(
                width: 22,
                height: 22,
                child: CircularProgressIndicator(strokeWidth: 2),
              ),
              SizedBox(width: 12),
              Text('Calculating your AI job match...'),
            ],
          ),
        ),
      );
    }

    final result = _matchResult;

    if (result == null) {
      return const SizedBox.shrink();
    }

    final score = result.matchScore;

    return Card(
      child: Padding(
        padding: const EdgeInsets.all(18),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                const Icon(Icons.auto_awesome, color: Colors.deepPurple),
                const SizedBox(width: 8),
                const Expanded(
                  child: Text(
                    'AI Job Match',
                    style: TextStyle(fontSize: 17, fontWeight: FontWeight.bold),
                  ),
                ),
                if (score != null)
                  Text(
                    '$score%',
                    style: TextStyle(
                      fontSize: 22,
                      fontWeight: FontWeight.bold,
                      color: _scoreColor(score),
                    ),
                  ),
              ],
            ),
            if (score != null) ...[
              const SizedBox(height: 12),
              ClipRRect(
                borderRadius: BorderRadius.circular(8),
                child: LinearProgressIndicator(
                  value: (score.clamp(0, 100)) / 100,
                  minHeight: 9,
                ),
              ),
            ],
            if (result.feedback != null &&
                result.feedback!.trim().isNotEmpty) ...[
              const SizedBox(height: 16),
              const Text(
                'AI Feedback',
                style: TextStyle(fontWeight: FontWeight.w600),
              ),
              const SizedBox(height: 6),
              Text(
                result.feedback!,
                style: const TextStyle(height: 1.45, color: Colors.black87),
              ),
            ],
          ],
        ),
      ),
    );
  }

  Widget _buildSection({
    required String title,
    required IconData icon,
    required Widget child,
  }) {
    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Icon(icon, size: 21),
                const SizedBox(width: 8),
                Text(
                  title,
                  style: const TextStyle(
                    fontSize: 16,
                    fontWeight: FontWeight.bold,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 12),
            child,
          ],
        ),
      ),
    );
  }

  Widget _buildCompanySection(JobResponseDTO job) {
    return Card(
      margin: const EdgeInsets.only(top: 4),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const Row(
              children: [
                Icon(Icons.business_outlined),
                SizedBox(width: 8),
                Text(
                  'About the Company',
                  style: TextStyle(fontSize: 16, fontWeight: FontWeight.bold),
                ),
              ],
            ),
            const SizedBox(height: 14),
            if (job.companyName != null)
              _companyInfo(Icons.business, 'Company', job.companyName!),
            if (job.companyEmail != null)
              _companyInfo(Icons.email_outlined, 'Email', job.companyEmail!),
            if (job.companyPhone != null)
              _companyInfo(Icons.phone_outlined, 'Phone', job.companyPhone!),
            if (job.companyWebsite != null)
              _companyInfo(
                Icons.language_outlined,
                'Website',
                job.companyWebsite!,
              ),
            if (job.companyDescription != null &&
                job.companyDescription!.trim().isNotEmpty) ...[
              const SizedBox(height: 12),
              Text(
                job.companyDescription!,
                style: const TextStyle(height: 1.45, color: Colors.black87),
              ),
            ],
          ],
        ),
      ),
    );
  }

  Widget _buildExperience(JobResponseDTO job) {
    if (job.experienceRequirements != null &&
        job.experienceRequirements!.trim().isNotEmpty) {
      return Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Text(
            job.experienceRequirements!,
            style: const TextStyle(height: 1.45),
          ),
          if (job.minExperience != null || job.maxExperience != null) ...[
            const SizedBox(height: 8),
            Text(
              _experienceLabel(job),
              style: const TextStyle(
                color: Colors.black54,
                fontWeight: FontWeight.w500,
              ),
            ),
          ],
        ],
      );
    }

    if (job.minExperience != null || job.maxExperience != null) {
      return Text(_experienceLabel(job));
    }

    return const Text(
      'Not specified.',
      style: TextStyle(color: Colors.black54),
    );
  }

  Widget _buildText(String? value) {
    if (value == null || value.trim().isEmpty) {
      return const Text(
        'Not specified.',
        style: TextStyle(color: Colors.black54),
      );
    }

    return Text(value, style: const TextStyle(height: 1.45));
  }

  Widget _buildBottomAction() {
    if (_checkingApplication) {
      return const SafeArea(
        child: Padding(
          padding: EdgeInsets.all(12),
          child: SizedBox(
            height: 48,
            child: Center(child: CircularProgressIndicator()),
          ),
        ),
      );
    }

    if (_application != null) {
      final status = _application!.status;

      if (status == ApplicationStatus.withdrawn) {
        return SafeArea(
          child: Padding(
            padding: const EdgeInsets.all(12),
            child: FilledButton(
              onPressed: _applying ? null : _applyForJob,
              child: const Text('Apply Again'),
            ),
          ),
        );
      }

      if (status == ApplicationStatus.rejected) {
        return SafeArea(
          child: Padding(
            padding: const EdgeInsets.all(12),
            child: FilledButton(
              onPressed: null,
              child: const Text('Application Rejected'),
            ),
          ),
        );
      }

      return SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(12),
          child: Row(
            children: [
              Expanded(
                child: OutlinedButton(
                  onPressed:
                      status == ApplicationStatus.applied ||
                          status == ApplicationStatus.aiPending ||
                          status == ApplicationStatus.aiCompleted
                      ? _withdrawApplication
                      : null,
                  child: const Text('Withdraw'),
                ),
              ),
              const SizedBox(width: 10),
              Expanded(
                child: FilledButton(
                  onPressed: null,
                  child: Text(_applicationStatusLabel(status)),
                ),
              ),
            ],
          ),
        ),
      );
    }

    final job = _job;

    if (job?.isActive != true) {
      return const SafeArea(
        child: Padding(
          padding: EdgeInsets.all(12),
          child: FilledButton(
            onPressed: null,
            child: Text('Job is no longer active'),
          ),
        ),
      );
    }

    if (job?.applicationDeadline != null &&
        job!.applicationDeadline!.isBefore(DateTime.now())) {
      return const SafeArea(
        child: Padding(
          padding: EdgeInsets.all(12),
          child: FilledButton(
            onPressed: null,
            child: Text('Application Deadline Passed'),
          ),
        ),
      );
    }

    return SafeArea(
      child: Padding(
        padding: const EdgeInsets.all(12),
        child: SizedBox(
          height: 50,
          child: FilledButton.icon(
            onPressed: _applying ? null : _applyForJob,
            icon: _applying
                ? const SizedBox(
                    width: 18,
                    height: 18,
                    child: CircularProgressIndicator(
                      strokeWidth: 2,
                      color: Colors.white,
                    ),
                  )
                : const Icon(Icons.send_outlined),
            label: Text(_applying ? 'Applying...' : 'Apply Now'),
          ),
        ),
      ),
    );
  }

  Widget _infoChip(IconData icon, String label) {
    return Container(
      padding: const EdgeInsets.symmetric(horizontal: 9, vertical: 7),
      decoration: BoxDecoration(
        color: Colors.grey.shade100,
        borderRadius: BorderRadius.circular(9),
      ),
      child: Row(
        mainAxisSize: MainAxisSize.min,
        children: [
          Icon(icon, size: 15, color: Colors.black54),
          const SizedBox(width: 5),
          Text(label, style: const TextStyle(fontSize: 12)),
        ],
      ),
    );
  }

  Widget _companyInfo(IconData icon, String label, String value) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 10),
      child: Row(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Icon(icon, size: 19, color: Colors.black54),
          const SizedBox(width: 10),
          Expanded(
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  label,
                  style: const TextStyle(fontSize: 11, color: Colors.black45),
                ),
                const SizedBox(height: 2),
                Text(value),
              ],
            ),
          ),
        ],
      ),
    );
  }

  String _locationLabel(JobResponseDTO job) {
    final parts = [
      job.locationDistrictName,
      job.locationDivisionName,
      job.locationCountryName,
    ].whereType<String>().where((e) => e.isNotEmpty).toList();

    return parts.isEmpty ? 'Location not specified' : parts.join(', ');
  }

  String _salaryLabel(JobResponseDTO job) {
    if (job.salaryMin == null && job.salaryMax == null) {
      return job.isNegotiable == true ? 'Negotiable' : 'Salary not disclosed';
    }

    if (job.salaryMin != null && job.salaryMax != null) {
      return '৳${job.salaryMin!.toStringAsFixed(0)} - '
          '৳${job.salaryMax!.toStringAsFixed(0)}';
    }

    return '৳${(job.salaryMin ?? job.salaryMax)!.toStringAsFixed(0)}';
  }

  String _experienceLabel(JobResponseDTO job) {
    if (job.minExperience != null && job.maxExperience != null) {
      return '${job.minExperience} - ${job.maxExperience} years experience';
    }

    if (job.minExperience != null) {
      return '${job.minExperience}+ years experience';
    }

    if (job.maxExperience != null) {
      return 'Up to ${job.maxExperience} years experience';
    }

    return 'Experience not specified';
  }

  String _employmentLabel(EmploymentType type) {
    return switch (type) {
      EmploymentType.fullTime => 'Full Time',
      EmploymentType.partTime => 'Part Time',
      EmploymentType.contract => 'Contract',
      EmploymentType.internship => 'Internship',
      EmploymentType.freelance => 'Freelance',
    };
  }

  String _workplaceLabel(WorkPlaceType type) {
    return switch (type) {
      WorkPlaceType.onsite => 'On-site',
      WorkPlaceType.remote => 'Remote',
      WorkPlaceType.hybrid => 'Hybrid',
    };
  }

  String _applicationStatusLabel(ApplicationStatus? status) {
    return switch (status) {
      ApplicationStatus.applied => 'Applied',
      ApplicationStatus.aiPending => 'AI Screening',
      ApplicationStatus.aiCompleted => 'AI Completed',
      ApplicationStatus.automaticQualified => 'Qualified',
      ApplicationStatus.companyShortlisted => 'Shortlisted',
      ApplicationStatus.hired => 'Hired',
      ApplicationStatus.rejected => 'Rejected',
      ApplicationStatus.withdrawn => 'Withdrawn',
      null => 'Applied',
    };
  }

  String _formatDate(DateTime date) {
    return '${date.day}/${date.month}/${date.year}';
  }

  Color _scoreColor(int score) {
    if (score >= 80) return Colors.green;
    if (score >= 60) return Colors.orange;
    return Colors.red;
  }
}
