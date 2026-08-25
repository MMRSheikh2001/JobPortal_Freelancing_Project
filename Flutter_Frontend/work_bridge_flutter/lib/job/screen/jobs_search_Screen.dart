import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/enums/employment_type.dart';
import 'package:work_bridge_flutter/enums/work_place_type.dart';
import 'package:work_bridge_flutter/job/entity/request/job_search_request.dart';
import 'package:work_bridge_flutter/job/entity/response/job_response.dart';
import 'package:work_bridge_flutter/job/provider/job_provider.dart';
import 'package:work_bridge_flutter/masterdata/widget/category_skill_cascade.dart';
import 'package:work_bridge_flutter/masterdata/widget/location_cascade.dart';
import 'package:work_bridge_flutter/router/app_router.dart';

class JobsSearchScreen extends ConsumerStatefulWidget {
  const JobsSearchScreen({super.key});

  @override
  ConsumerState<JobsSearchScreen> createState() => _JobsSearchScreenState();
}

class _JobsSearchScreenState extends ConsumerState<JobsSearchScreen> {
  final _keywordCtrl = TextEditingController();
  Timer? _debounce;

  @override
  void dispose() {
    _keywordCtrl.dispose();
    _debounce?.cancel();
    super.dispose();
  }

  void _onKeywordChanged(String value) {
    _debounce?.cancel();
    _debounce = Timer(const Duration(milliseconds: 400), () {
      final notifier = ref.read(jobSearchFilterProvider.notifier);
      notifier.state = notifier.state.copyWith(
        keyword: value.trim().isEmpty ? null : value.trim(),
      );
    });
  }

  void _reset() {
    _debounce?.cancel();
    _keywordCtrl.clear();
    ref.read(jobSearchFilterProvider.notifier).state =
    const JobSearchRequestDTO(active: true);
  }

  void _openFilterSheet() {
    showModalBottomSheet(
      context: context,
      isScrollControlled: true,
      builder: (_) => const _JobFilterSheet(),
    );
  }

  @override
  Widget build(BuildContext context) {
    final resultsAsync = ref.watch(jobSearchResultsProvider);
    final activeFilter = ref.watch(jobSearchFilterProvider);
    final activeFilterCount = _countActiveFilters(activeFilter);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Find Jobs'),
        actions: [
          TextButton.icon(
            onPressed: _reset,
            style: TextButton.styleFrom(foregroundColor: Colors.white),
            icon: const Icon(Icons.refresh, size: 18, color: Colors.white),
            label: const Text('Reset', style: TextStyle(color: Colors.white)),
          ),
        ],
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.fromLTRB(16, 12, 16, 8),
            child: Row(
              children: [
                Expanded(
                  child: TextField(
                    controller: _keywordCtrl,
                    onChanged: _onKeywordChanged,
                    decoration: InputDecoration(
                      hintText: 'Search job title, skills...',
                      prefixIcon: const Icon(Icons.search),
                      filled: true,
                      border: OutlineInputBorder(
                        borderRadius: BorderRadius.circular(12),
                        borderSide: BorderSide.none,
                      ),
                    ),
                  ),
                ),
                const SizedBox(width: 8),
                Stack(
                  clipBehavior: Clip.none,
                  children: [
                    IconButton.filledTonal(
                      onPressed: _openFilterSheet,
                      icon: const Icon(Icons.tune),
                    ),
                    if (activeFilterCount > 0)
                      Positioned(
                        right: -2,
                        top: -2,
                        child: Container(
                          padding: const EdgeInsets.all(4),
                          decoration: const BoxDecoration(
                            color: Colors.red,
                            shape: BoxShape.circle,
                          ),
                          child: Text(
                            '$activeFilterCount',
                            style: const TextStyle(
                              color: Colors.white,
                              fontSize: 10,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                        ),
                      ),
                  ],
                ),
              ],
            ),
          ),
          Expanded(
            child: resultsAsync.when(
              loading: () => const Center(child: CircularProgressIndicator()),
              error: (e, _) => Center(
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
                        'Failed to load jobs: $e',
                        textAlign: TextAlign.center,
                      ),
                      const SizedBox(height: 12),
                      OutlinedButton(
                        onPressed: () =>
                            ref.invalidate(jobSearchResultsProvider),
                        child: const Text('Retry'),
                      ),
                    ],
                  ),
                ),
              ),
              data: (jobs) {
                if (jobs.isEmpty) {
                  return const Center(
                    child: Padding(
                      padding: EdgeInsets.all(24),
                      child: Text(
                        'No jobs match your search. Try adjusting your filters.',
                        textAlign: TextAlign.center,
                        style: TextStyle(color: Colors.black54),
                      ),
                    ),
                  );
                }
                return RefreshIndicator(
                  onRefresh: () async =>
                      ref.invalidate(jobSearchResultsProvider),
                  child: ListView.separated(
                    padding: const EdgeInsets.fromLTRB(16, 4, 16, 16),
                    itemCount: jobs.length,
                    separatorBuilder: (_, __) => const SizedBox(height: 10),
                    itemBuilder: (_, i) => _JobCard(job: jobs[i]),
                  ),
                );
              },
            ),
          ),
        ],
      ),
    );
  }

  int _countActiveFilters(JobSearchRequestDTO f) {
    var count = 0;
    if (f.categoryId != null) count++;
    if (f.countryId != null) count++;
    if (f.divisionId != null) count++;
    if (f.districtId != null) count++;
    if (f.policeStationId != null) count++;
    if (f.employmentType != null) count++;
    if (f.workPlaceType != null) count++;
    if (f.minSalary != null) count++;
    if (f.maxSalary != null) count++;
    return count;
  }
}

class _JobCard extends StatelessWidget {
  const _JobCard({required this.job});

  final JobResponseDTO job;

  String get _salaryLabel {
    if (job.salaryMin == null && job.salaryMax == null) {
      return job.isNegotiable == true ? 'Negotiable' : 'Not disclosed';
    }
    if (job.salaryMin != null && job.salaryMax != null) {
      return '৳${job.salaryMin!.toStringAsFixed(0)} - ৳${job.salaryMax!.toStringAsFixed(0)}';
    }
    return '৳${(job.salaryMin ?? job.salaryMax)!.toStringAsFixed(0)}';
  }

  String get _locationLabel {
    final parts = [
      job.locationDistrictName,
      job.locationDivisionName,
    ].whereType<String>().toList();
    return parts.isEmpty ? 'Location not specified' : parts.join(', ');
  }

  @override
  Widget build(BuildContext context) {
    return Card(
      clipBehavior: Clip.antiAlias,
      child: InkWell(
        onTap: () {
          if (job.id == null) return;
          Navigator.of(
            context,
          ).pushNamed(AppRouter.jobDetails, arguments: job.id);
        },
        child: Padding(
          padding: const EdgeInsets.all(14),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  CircleAvatar(
                    radius: 22,
                    backgroundColor: Colors.blue.shade50,
                    backgroundImage: job.companyLogo != null
                        ? NetworkImage(job.companyLogo!)
                        : null,
                    child: job.companyLogo == null
                        ? const Icon(Icons.apartment, color: Colors.blue)
                        : null,
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          job.title ?? 'Untitled position',
                          style: const TextStyle(
                            fontWeight: FontWeight.bold,
                            fontSize: 15,
                          ),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                        const SizedBox(height: 2),
                        Text(
                          job.companyName ?? 'Unknown company',
                          style: const TextStyle(color: Colors.black54),
                          maxLines: 1,
                          overflow: TextOverflow.ellipsis,
                        ),
                      ],
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 10),
              Wrap(
                spacing: 6,
                runSpacing: 6,
                children: [
                  _Tag(icon: Icons.location_on_outlined, label: _locationLabel),
                  if (job.employmentType != null)
                    _Tag(
                      icon: Icons.work_outline,
                      label: _employmentLabel(job.employmentType!),
                    ),
                  if (job.workPlaceType != null)
                    _Tag(
                      icon: Icons.home_work_outlined,
                      label: _workplaceLabel(job.workPlaceType!),
                    ),
                ],
              ),
              const SizedBox(height: 8),
              Row(
                children: [
                  Icon(
                    Icons.payments_outlined,
                    size: 16,
                    color: Colors.green.shade700,
                  ),
                  const SizedBox(width: 4),
                  Text(
                    _salaryLabel,
                    style: TextStyle(
                      color: Colors.green.shade700,
                      fontWeight: FontWeight.w600,
                    ),
                  ),
                  const Spacer(),
                  if (job.applicationDeadline != null)
                    Text(
                      'Apply by ${_formatDate(job.applicationDeadline!)}',
                      style: const TextStyle(
                        fontSize: 12,
                        color: Colors.black45,
                      ),
                    ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }

  String _employmentLabel(EmploymentType type) => switch (type) {
    EmploymentType.fullTime => 'Full Time',
    EmploymentType.partTime => 'Part Time',
    EmploymentType.contract => 'Contract',
    EmploymentType.internship => 'Internship',
    EmploymentType.freelance => 'Freelance',
  };

  String _workplaceLabel(WorkPlaceType type) => switch (type) {
    WorkPlaceType.onsite => 'On-site',
    WorkPlaceType.remote => 'Remote',
    WorkPlaceType.hybrid => 'Hybrid',
  };

  String _formatDate(DateTime d) => '${d.day}/${d.month}/${d.year}';
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

/// Filter bottom sheet: location cascade + category (skills hidden — job
/// search only filters by categoryId) + employment/workplace type chips +
/// salary range. Applies straight into jobSearchFilterProvider, which
/// jobSearchResultsProvider is already watching.
class _JobFilterSheet extends ConsumerStatefulWidget {
  const _JobFilterSheet();

  @override
  ConsumerState<_JobFilterSheet> createState() => _JobFilterSheetState();
}

class _JobFilterSheetState extends ConsumerState<_JobFilterSheet> {
  late JobSearchRequestDTO _draft;
  final _minSalaryCtrl = TextEditingController();
  final _maxSalaryCtrl = TextEditingController();

  @override
  void initState() {
    super.initState();
    _draft = ref.read(jobSearchFilterProvider);
    _minSalaryCtrl.text = _draft.minSalary?.toStringAsFixed(0) ?? '';
    _maxSalaryCtrl.text = _draft.maxSalary?.toStringAsFixed(0) ?? '';
  }

  @override
  void dispose() {
    _minSalaryCtrl.dispose();
    _maxSalaryCtrl.dispose();
    super.dispose();
  }

  void _apply() {
    ref.read(jobSearchFilterProvider.notifier).state = _draft.copyWith(
      minSalary: double.tryParse(_minSalaryCtrl.text),
      maxSalary: double.tryParse(_maxSalaryCtrl.text),
    );
    Navigator.of(context).pop();
  }

  void _clear() {
    setState(() {
      _draft = JobSearchRequestDTO(
        active: true,
        keyword: _draft.keyword, // keep the search bar's keyword
      );
      _minSalaryCtrl.clear();
      _maxSalaryCtrl.clear();
    });
  }

  @override
  Widget build(BuildContext context) {
    return DraggableScrollableSheet(
      initialChildSize: 0.85,
      minChildSize: 0.5,
      maxChildSize: 0.95,
      expand: false,
      builder: (context, scrollController) {
        return Padding(
          padding: EdgeInsets.only(
            left: 16,
            right: 16,
            top: 16,
            bottom: MediaQuery.of(context).viewInsets.bottom + 16,
          ),
          child: ListView(
            controller: scrollController,
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  const Text(
                    'Filters',
                    style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
                  ),
                  TextButton(onPressed: _clear, child: const Text('Clear all')),
                ],
              ),
              const SizedBox(height: 12),

              CategorySkillCascade(
                showSkills: false,
                categoryLabel: 'Category',
                onChanged: (sel) => setState(() {
                  _draft = _draft.copyWith(categoryId: sel.categoryId);
                }),
              ),
              const SizedBox(height: 16),

              LocationCascade(
                onChanged: (loc) => setState(() {
                  _draft = _draft.copyWith(
                    countryId: loc.countryId,
                    divisionId: loc.divisionId,
                    districtId: loc.districtId,
                    policeStationId: loc.policeStationId,
                  );
                }),
              ),
              const SizedBox(height: 16),

              const Text(
                'Employment Type',
                style: TextStyle(fontWeight: FontWeight.w600),
              ),
              const SizedBox(height: 8),
              Wrap(
                spacing: 8,
                runSpacing: 8,
                children: EmploymentType.values.map((type) {
                  final selected = _draft.employmentType == type;
                  return ChoiceChip(
                    label: Text(_employmentLabel(type)),
                    selected: selected,
                    onSelected: (_) => setState(() {
                      _draft = _draft.copyWith(
                        employmentType: selected ? null : type,
                      );
                    }),
                  );
                }).toList(),
              ),
              const SizedBox(height: 16),

              const Text(
                'Workplace Type',
                style: TextStyle(fontWeight: FontWeight.w600),
              ),
              const SizedBox(height: 8),
              Wrap(
                spacing: 8,
                runSpacing: 8,
                children: WorkPlaceType.values.map((type) {
                  final selected = _draft.workPlaceType == type;
                  return ChoiceChip(
                    label: Text(_workplaceLabel(type)),
                    selected: selected,
                    onSelected: (_) => setState(() {
                      _draft = _draft.copyWith(
                        workPlaceType: selected ? null : type,
                      );
                    }),
                  );
                }).toList(),
              ),
              const SizedBox(height: 16),

              const Text(
                'Salary Range',
                style: TextStyle(fontWeight: FontWeight.w600),
              ),
              const SizedBox(height: 8),
              Row(
                children: [
                  Expanded(
                    child: TextField(
                      controller: _minSalaryCtrl,
                      keyboardType: TextInputType.number,
                      decoration: const InputDecoration(
                        labelText: 'Min',
                        prefixText: '৳ ',
                      ),
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: TextField(
                      controller: _maxSalaryCtrl,
                      keyboardType: TextInputType.number,
                      decoration: const InputDecoration(
                        labelText: 'Max',
                        prefixText: '৳ ',
                      ),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 24),

              FilledButton(
                onPressed: _apply,
                style: FilledButton.styleFrom(
                  minimumSize: const Size.fromHeight(48),
                ),
                child: const Text('Apply Filters'),
              ),
            ],
          ),
        );
      },
    );
  }

  String _employmentLabel(EmploymentType type) => switch (type) {
    EmploymentType.fullTime => 'Full Time',
    EmploymentType.partTime => 'Part Time',
    EmploymentType.contract => 'Contract',
    EmploymentType.internship => 'Internship',
    EmploymentType.freelance => 'Freelance',
  };

  String _workplaceLabel(WorkPlaceType type) => switch (type) {
    WorkPlaceType.onsite => 'On-site',
    WorkPlaceType.remote => 'Remote',
    WorkPlaceType.hybrid => 'Hybrid',
  };
}
