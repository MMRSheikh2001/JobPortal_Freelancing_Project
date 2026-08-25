import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/education_request.dart';
import 'package:work_bridge_flutter/enums/education_level.dart';
import 'package:work_bridge_flutter/enums/result_type.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

/// Add/Edit Education form — mirrors WorkBridgeAndroid's
/// EducationEditActivity. [educationId] null means "add new" (matches
/// Android's convention of simply not passing the intent extra);
/// non-null means "edit existing" and the form is pre-filled via
/// getEducationById.
class AddEditEducationScreen extends ConsumerStatefulWidget {
  const AddEditEducationScreen({super.key, this.educationId});

  final int? educationId;

  bool get isEditing => educationId != null;

  @override
  ConsumerState<AddEditEducationScreen> createState() =>
      _AddEditEducationScreenState();
}

class _AddEditEducationScreenState
    extends ConsumerState<AddEditEducationScreen> {
  final _formKey = GlobalKey<FormState>();

  final _boardCtrl = TextEditingController();
  final _institutionCtrl = TextEditingController();
  final _fieldOfStudyCtrl = TextEditingController();
  final _resultCtrl = TextEditingController();
  final _outOfCtrl = TextEditingController();
  final _gradeOrDivisionCtrl = TextEditingController();

  EducationLevel? _educationLevel;
  ResultType? _resultType;
  DateTime? _startDate;
  DateTime? _endDate;
  bool _currentlyStudying = false;

  bool _loading = false;
  bool _saving = false;
  String? _errorMessage;

  @override
  void initState() {
    super.initState();
    if (widget.isEditing) {
      _loadExisting();
    }
  }

  Future<void> _loadExisting() async {
    setState(() => _loading = true);
    try {
      final education = await ref
          .read(cvRepositoryProvider)
          .getEducationById(widget.educationId!);

      _boardCtrl.text = education.board ?? '';
      _institutionCtrl.text = education.institution ?? '';
      _fieldOfStudyCtrl.text = education.fieldOfStudy ?? '';
      _resultCtrl.text = education.result?.toString() ?? '';
      _outOfCtrl.text = education.outOf?.toString() ?? '';
      _gradeOrDivisionCtrl.text = education.gradeOrDivision ?? '';
      _educationLevel = education.educationLevel;
      _resultType = education.resultType;
      _startDate = education.startDate;
      _endDate = education.endDate;
      _currentlyStudying = education.currentlyStudying ?? false;
    } catch (e) {
      if (!mounted) return;
      setState(() => _errorMessage = 'Failed to load: $e');
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  @override
  void dispose() {
    _boardCtrl.dispose();
    _institutionCtrl.dispose();
    _fieldOfStudyCtrl.dispose();
    _resultCtrl.dispose();
    _outOfCtrl.dispose();
    _gradeOrDivisionCtrl.dispose();
    super.dispose();
  }

  Future<void> _pickDate({required bool isStart}) async {
    final now = DateTime.now();
    final initial = (isStart ? _startDate : _endDate) ?? now;
    final picked = await showDatePicker(
      context: context,
      initialDate: initial,
      firstDate: DateTime(1970),
      lastDate: DateTime(now.year + 10),
    );
    if (picked != null) {
      setState(() {
        if (isStart) {
          _startDate = picked;
        } else {
          _endDate = picked;
        }
      });
    }
  }

  void _toggleCurrentlyStudying(bool value) {
    setState(() {
      _currentlyStudying = value;
      if (value) _endDate = null;
    });
  }

  Future<void> _save() async {
    if (!_formKey.currentState!.validate()) return;

    final profileId = ref.read(currentUserProvider)?.profileId;
    if (profileId == null) {
      setState(() => _errorMessage = 'Could not determine your profile.');
      return;
    }

    setState(() {
      _saving = true;
      _errorMessage = null;
    });

    try {
      final request = EducationRequestDTO(
        educationLevel: _educationLevel,
        board: _boardCtrl.text.trim().isEmpty ? null : _boardCtrl.text.trim(),
        institution: _institutionCtrl.text.trim().isEmpty
            ? null
            : _institutionCtrl.text.trim(),
        fieldOfStudy: _fieldOfStudyCtrl.text.trim().isEmpty
            ? null
            : _fieldOfStudyCtrl.text.trim(),
        resultType: _resultType,
        result: double.tryParse(_resultCtrl.text.trim()),
        outOf: double.tryParse(_outOfCtrl.text.trim()),
        gradeOrDivision: _gradeOrDivisionCtrl.text.trim().isEmpty
            ? null
            : _gradeOrDivisionCtrl.text.trim(),
        startDate: _startDate,
        endDate: _currentlyStudying ? null : _endDate,
        userProfileId: profileId,
      );

      final repo = ref.read(cvRepositoryProvider);
      if (widget.isEditing) {
        await repo.updateEducation(widget.educationId!, request);
      } else {
        await repo.saveEducation(request);
      }

      if (!mounted) return;
      Navigator.of(context).pop(true); // true = list should refresh
    } catch (e) {
      setState(() => _errorMessage = e.toString());
    } finally {
      if (mounted) setState(() => _saving = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.isEditing ? 'Edit Education' : 'Add Education'),
      ),
      body: _loading
          ? const Center(child: CircularProgressIndicator())
          : SingleChildScrollView(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              if (_errorMessage != null)
                Padding(
                  padding: const EdgeInsets.only(bottom: 16),
                  child: Container(
                    width: double.infinity,
                    padding: const EdgeInsets.all(12),
                    decoration: BoxDecoration(
                      color: Colors.red.shade50,
                      borderRadius: BorderRadius.circular(8),
                    ),
                    child: Text(
                      _errorMessage!,
                      style: const TextStyle(color: Colors.red),
                    ),
                  ),
                ),

              DropdownButtonFormField<EducationLevel>(
                value: _educationLevel,
                decoration: const InputDecoration(
                  labelText: 'Education Level',
                  prefixIcon: Icon(Icons.school_outlined),
                ),
                items: EducationLevel.values
                    .map((e) => DropdownMenuItem(
                  value: e,
                  child: Text(_levelLabel(e)),
                ))
                    .toList(),
                onChanged: (v) => setState(() => _educationLevel = v),
                validator: (v) =>
                v == null ? 'Education level is required.' : null,
              ),
              const SizedBox(height: 14),

              TextFormField(
                controller: _institutionCtrl,
                decoration: const InputDecoration(
                  labelText: 'Institution',
                  prefixIcon: Icon(Icons.account_balance_outlined),
                ),
                validator: (v) => (v == null || v.trim().isEmpty)
                    ? 'Institution is required.'
                    : null,
              ),
              const SizedBox(height: 14),

              TextFormField(
                controller: _boardCtrl,
                decoration: const InputDecoration(labelText: 'Board'),
              ),
              const SizedBox(height: 14),

              TextFormField(
                controller: _fieldOfStudyCtrl,
                decoration:
                const InputDecoration(labelText: 'Field of Study'),
              ),
              const SizedBox(height: 20),

              const Text(
                'Result',
                style: TextStyle(fontSize: 15, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 10),
              DropdownButtonFormField<ResultType>(
                value: _resultType,
                decoration:
                const InputDecoration(labelText: 'Result Type'),
                items: ResultType.values
                    .map((r) => DropdownMenuItem(
                  value: r,
                  child: Text(_resultTypeLabel(r)),
                ))
                    .toList(),
                onChanged: (v) => setState(() => _resultType = v),
              ),
              const SizedBox(height: 14),
              Row(
                children: [
                  Expanded(
                    child: TextFormField(
                      controller: _resultCtrl,
                      keyboardType:
                      const TextInputType.numberWithOptions(
                          decimal: true),
                      decoration:
                      const InputDecoration(labelText: 'Result'),
                    ),
                  ),
                  const SizedBox(width: 12),
                  Expanded(
                    child: TextFormField(
                      controller: _outOfCtrl,
                      keyboardType:
                      const TextInputType.numberWithOptions(
                          decimal: true),
                      decoration:
                      const InputDecoration(labelText: 'Out Of'),
                    ),
                  ),
                ],
              ),
              const SizedBox(height: 14),
              TextFormField(
                controller: _gradeOrDivisionCtrl,
                decoration: const InputDecoration(
                  labelText: 'Grade / Division',
                ),
              ),
              const SizedBox(height: 20),

              const Text(
                'Duration',
                style: TextStyle(fontSize: 15, fontWeight: FontWeight.bold),
              ),
              const SizedBox(height: 10),
              InkWell(
                onTap: () => _pickDate(isStart: true),
                child: InputDecorator(
                  decoration: const InputDecoration(
                    labelText: 'Start Date',
                    prefixIcon: Icon(Icons.calendar_today_outlined),
                  ),
                  child: Text(
                    _startDate != null
                        ? _formatDate(_startDate!)
                        : 'Select date',
                    style: TextStyle(
                      color: _startDate != null ? null : Colors.black45,
                    ),
                  ),
                ),
              ),
              const SizedBox(height: 14),
              InkWell(
                onTap: _currentlyStudying
                    ? null
                    : () => _pickDate(isStart: false),
                child: InputDecorator(
                  decoration: InputDecoration(
                    labelText: 'End Date',
                    prefixIcon: const Icon(Icons.calendar_today_outlined),
                    enabled: !_currentlyStudying,
                  ),
                  child: Text(
                    _endDate != null
                        ? _formatDate(_endDate!)
                        : (_currentlyStudying ? 'N/A' : 'Select date'),
                    style: TextStyle(
                      color: (_endDate != null && !_currentlyStudying)
                          ? null
                          : Colors.black45,
                    ),
                  ),
                ),
              ),
              const SizedBox(height: 8),
              InkWell(
                onTap: () => _toggleCurrentlyStudying(!_currentlyStudying),
                child: Row(
                  children: [
                    Checkbox(
                      value: _currentlyStudying,
                      onChanged: (v) =>
                          _toggleCurrentlyStudying(v ?? false),
                    ),
                    const Text('Currently studying here'),
                  ],
                ),
              ),
              const SizedBox(height: 28),

              FilledButton(
                onPressed: _saving ? null : _save,
                style: FilledButton.styleFrom(
                  minimumSize: const Size.fromHeight(48),
                ),
                child: _saving
                    ? const SizedBox(
                  height: 20,
                  width: 20,
                  child: CircularProgressIndicator(
                    strokeWidth: 2,
                    color: Colors.white,
                  ),
                )
                    : Text(widget.isEditing ? 'Save Changes' : 'Add Education'),
              ),
              const SizedBox(height: 16),
            ],
          ),
        ),
      ),
    );
  }

  String _formatDate(DateTime d) => '${d.day}/${d.month}/${d.year}';

  String _levelLabel(EducationLevel e) => switch (e) {
    EducationLevel.ssc => 'SSC',
    EducationLevel.hsc => 'HSC',
    EducationLevel.diploma => 'Diploma',
    EducationLevel.bachelor => "Bachelor's",
    EducationLevel.pgd => 'PGD',
    EducationLevel.masters => "Master's",
    EducationLevel.mphil => 'MPhil',
    EducationLevel.phd => 'PhD',
  };

  String _resultTypeLabel(ResultType r) => switch (r) {
    ResultType.cgpa => 'CGPA',
    ResultType.gpa => 'GPA',
    ResultType.percentage => 'Percentage',
    ResultType.division => 'Division',
    ResultType.grade => 'Grade',
  };
}