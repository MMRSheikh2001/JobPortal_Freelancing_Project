import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';

import 'package:work_bridge_flutter/auth/response/login_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/user_skill_request.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/user_skill_response.dart';
import 'package:work_bridge_flutter/enums/proficiency_level.dart';
import 'package:work_bridge_flutter/masterdata/models/response/skill_response.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

class UserSkillScreen extends ConsumerStatefulWidget {
  const UserSkillScreen({super.key});

  @override
  ConsumerState<UserSkillScreen> createState() =>
      _UserSkillScreenState();
}

class _UserSkillScreenState extends ConsumerState<UserSkillScreen> {
  final _formKey = GlobalKey<FormState>();

  SkillResponseDTO? _selectedSkill;
  ProficiencyLevel? _selectedProficiency;
  final _yearsOfExperienceCtrl = TextEditingController();

  int? _editingSkillId;

  bool _isSaving = false;

  @override
  void dispose() {
    _yearsOfExperienceCtrl.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    final authState = ref.watch(authControllerProvider);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Skills'),
      ),
      body: authState.when(
        loading: () =>
        const Center(
          child: CircularProgressIndicator(),
        ),
        error: (error, stack) =>
            Center(
              child: Padding(
                padding: const EdgeInsets.all(20),
                child: Text(
                  'Unable to load user information.\n\n$error',
                  textAlign: TextAlign.center,
                ),
              ),
            ),
        data: (loginResponse) {
          if (loginResponse == null ||
              loginResponse.profileId == null) {
            return const Center(
              child: Text(
                'User profile not found.',
                textAlign: TextAlign.center,
              ),
            );
          }

          return _buildPage(
            context,
            loginResponse,
          );
        },
      ),
    );
  }

  Widget _buildPage(BuildContext context,
      LoginResponse loginResponse,) {
    final profileId = loginResponse.profileId!;

    final skillsAsync = ref.watch(
      allSkillsProvider,
    );

    return RefreshIndicator(
      onRefresh: () async {
        ref.invalidate(
          userSkillsProvider(profileId),
        );
        ref.invalidate(
          allSkillsProvider,
        );

        await ref.read(
          userSkillsProvider(profileId).future,
        );
      },
      child: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          _buildFormCard(
            context,
            profileId,
            skillsAsync,
          ),

          const SizedBox(height: 24),

          const Text(
            'My Skills',
            style: TextStyle(
              fontSize: 20,
              fontWeight: FontWeight.bold,
            ),
          ),

          const SizedBox(height: 12),

          _buildUserSkills(
            context,
            profileId,
          ),
        ],
      ),
    );
  }

  // ============================================================
  // FORM
  // ============================================================

  Widget _buildFormCard(BuildContext context,
      int profileId,
      AsyncValue<List<SkillResponseDTO>> skillsAsync,) {
    final isEditing = _editingSkillId != null;

    return Card(
      elevation: 2,
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Form(
          key: _formKey,
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                isEditing ? 'Edit Skill' : 'Add Skill',
                style: const TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                ),
              ),

              const SizedBox(height: 16),

              // ------------------------------------------------
              // Skill
              // ------------------------------------------------

              skillsAsync.when(
                loading: () =>
                const Center(
                  child: Padding(
                    padding: EdgeInsets.all(8),
                    child: CircularProgressIndicator(),
                  ),
                ),
                error: (error, stack) =>
                    Text(
                      'Failed to load skills: $error',
                      style: const TextStyle(
                        color: Colors.red,
                      ),
                    ),
                data: (skills) {
                  return DropdownButtonFormField<SkillResponseDTO>(
                    initialValue: _selectedSkill,
                    decoration: const InputDecoration(
                      labelText: 'Skill',
                      border: OutlineInputBorder(),
                      prefixIcon: Icon(Icons.build_outlined),
                    ),
                    items: skills.map((skill) {
                      return DropdownMenuItem<SkillResponseDTO>(
                        value: skill,
                        child: Text(
                          skill.skillName ?? 'Unknown',
                        ),
                      );
                    }).toList(),
                    onChanged: _isSaving
                        ? null
                        : (value) {
                      setState(() {
                        _selectedSkill = value;
                      });
                    },
                    validator: (value) {
                      if (value == null) {
                        return 'Please select a skill';
                      }
                      return null;
                    },
                  );
                },
              ),

              const SizedBox(height: 16),

              // ------------------------------------------------
              // Proficiency
              // ------------------------------------------------

              DropdownButtonFormField<ProficiencyLevel>(
                initialValue: _selectedProficiency,
                decoration: const InputDecoration(
                  labelText: 'Proficiency',
                  border: OutlineInputBorder(),
                  prefixIcon: Icon(Icons.bar_chart),
                ),
                items: ProficiencyLevel.values.map((proficiency) {
                  return DropdownMenuItem<ProficiencyLevel>(
                    value: proficiency,
                    child: Text(
                      _proficiencyLabel(proficiency),
                    ),
                  );
                }).toList(),
                onChanged: _isSaving
                    ? null
                    : (value) {
                  setState(() {
                    _selectedProficiency = value;
                  });
                },
                validator: (value) {
                  if (value == null) {
                    return 'Please select proficiency';
                  }
                  return null;
                },
              ),

              const SizedBox(height: 16),

              // ------------------------------------------------
              // Years of Experience
              // ------------------------------------------------

              TextFormField(
                controller: _yearsOfExperienceCtrl,
                enabled: !_isSaving,
                keyboardType: TextInputType.number,
                decoration: const InputDecoration(
                  labelText: 'Years of Experience',
                  border: OutlineInputBorder(),
                  prefixIcon: Icon(Icons.timelapse_outlined),
                ),
              ),

              const SizedBox(height: 20),

              // ------------------------------------------------
              // Buttons
              // ------------------------------------------------

              Row(
                children: [
                  Expanded(
                    child: ElevatedButton.icon(
                      onPressed: _isSaving
                          ? null
                          : () => _saveSkill(profileId),
                      icon: _isSaving
                          ? const SizedBox(
                        width: 18,
                        height: 18,
                        child: CircularProgressIndicator(
                          strokeWidth: 2,
                        ),
                      )
                          : Icon(
                        isEditing
                            ? Icons.update
                            : Icons.add,
                      ),
                      label: Text(
                        _isSaving
                            ? 'Saving...'
                            : isEditing
                            ? 'Update'
                            : 'Save',
                      ),
                    ),
                  ),

                  if (isEditing) ...[
                    const SizedBox(width: 10),
                    Expanded(
                      child: OutlinedButton(
                        onPressed: _isSaving
                            ? null
                            : _cancelEditing,
                        child: const Text('Cancel'),
                      ),
                    ),
                  ],
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }

  // ============================================================
  // USER SKILLS
  // ============================================================

  Widget _buildUserSkills(BuildContext context,
      int profileId,) {
    final skillsAsync = ref.watch(
      userSkillsProvider(profileId),
    );

    return skillsAsync.when(
      loading: () =>
      const Center(
        child: Padding(
          padding: EdgeInsets.all(30),
          child: CircularProgressIndicator(),
        ),
      ),
      error: (error, stack) =>
          Card(
            child: Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                children: [
                  const Icon(
                    Icons.error_outline,
                    size: 40,
                    color: Colors.red,
                  ),
                  const SizedBox(height: 8),
                  Text(
                    'Failed to load skills.\n$error',
                    textAlign: TextAlign.center,
                  ),
                  const SizedBox(height: 12),
                  ElevatedButton(
                    onPressed: () {
                      ref.invalidate(
                        userSkillsProvider(profileId),
                      );
                    },
                    child: const Text('Retry'),
                  ),
                ],
              ),
            ),
          ),
      data: (skills) {
        if (skills.isEmpty) {
          return Card(
            child: Padding(
              padding: const EdgeInsets.all(24),
              child: Column(
                children: const [
                  Icon(
                    Icons.build_outlined,
                    size: 48,
                    color: Colors.grey,
                  ),
                  SizedBox(height: 10),
                  Text(
                    'No skills added yet.',
                    style: TextStyle(
                      color: Colors.grey,
                    ),
                  ),
                ],
              ),
            ),
          );
        }

        return Column(
          children: skills.map((skill) {
            return _buildSkillCard(
              context,
              profileId,
              skill,
            );
          }).toList(),
        );
      },
    );
  }

  // ============================================================
  // SKILL CARD
  // ============================================================

  Widget _buildSkillCard(BuildContext context,
      int profileId,
      UserSkillResponseDTO skill,) {
    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            CircleAvatar(
              child: const Icon(Icons.build_outlined),
            ),

            const SizedBox(width: 14),

            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    skill.skillName ?? 'Unknown skill',
                    style: const TextStyle(
                      fontSize: 17,
                      fontWeight: FontWeight.bold,
                    ),
                  ),

                  const SizedBox(height: 6),

                  Text(
                    'Proficiency: ${_proficiencyLabel(skill.proficiencyLevel)}',
                    style: const TextStyle(
                      color: Colors.grey,
                    ),
                  ),

                  if (skill.yearsOfExperience != null) ...[
                    const SizedBox(height: 2),
                    Text(
                      '${skill.yearsOfExperience} years of experience',
                      style: const TextStyle(
                        color: Colors.grey,
                      ),
                    ),
                  ],
                ],
              ),
            ),

            PopupMenuButton<String>(
              onSelected: (value) {
                if (value == 'edit') {
                  _startEditing(skill);
                } else if (value == 'delete') {
                  _confirmDelete(
                    context,
                    profileId,
                    skill,
                  );
                }
              },
              itemBuilder: (context) =>
              const [
                PopupMenuItem(
                  value: 'edit',
                  child: Row(
                    children: [
                      Icon(Icons.edit),
                      SizedBox(width: 8),
                      Text('Edit'),
                    ],
                  ),
                ),
                PopupMenuItem(
                  value: 'delete',
                  child: Row(
                    children: [
                      Icon(Icons.delete),
                      SizedBox(width: 8),
                      Text('Delete'),
                    ],
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  // ============================================================
  // SAVE / UPDATE
  // ============================================================

  Future<void> _saveSkill(int profileId) async {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    if (_selectedSkill?.skillId == null ||
        _selectedProficiency == null) {
      return;
    }

    setState(() {
      _isSaving = true;
    });

    try {
      final request = UserSkillRequestDTO(
        proficiencyLevel: _selectedProficiency,
        yearsOfExperience: int.tryParse(_yearsOfExperienceCtrl.text.trim()),
        skillId: _selectedSkill!.skillId,
        userProfileId: profileId,
      );

      final repository = ref.read(
        cvRepositoryProvider,
      );

      if (_editingSkillId == null) {
        await repository.saveUserSkill(request);

        _showMessage(
          'Skill added successfully.',
        );
      } else {
        await repository.updateUserSkill(
          _editingSkillId!,
          request,
        );

        _showMessage(
          'Skill updated successfully.',
        );
      }

      _clearForm();

      ref.invalidate(
        userSkillsProvider(profileId),
      );
    } catch (error) {
      _showMessage(
        apiErrorMessage(error),
        isError: true,
      );
    } finally {
      if (mounted) {
        setState(() {
          _isSaving = false;
        });
      }
    }
  }

  // ============================================================
  // EDIT
  // ============================================================

  void _startEditing(UserSkillResponseDTO skill,) {
    setState(() {
      _editingSkillId = skill.id;

      _selectedProficiency = skill.proficiencyLevel;
      _yearsOfExperienceCtrl.text =
          skill.yearsOfExperience?.toString() ?? '';

      if (skill.skillId != null) {
        _findSkill(skill.skillId!);
      }
    });

    // Move the user back toward the form.
    Scrollable.ensureVisible(
      _formKey.currentContext!,
      duration: const Duration(
        milliseconds: 400,
      ),
      curve: Curves.easeInOut,
    );
  }

  Future<void> _findSkill(int skillId) async {
    try {
      final skills = await ref.read(
        allSkillsProvider.future,
      );

      final skill = skills.firstWhere(
            (item) => item.skillId == skillId,
      );

      if (mounted) {
        setState(() {
          _selectedSkill = skill;
        });
      }
    } catch (_) {
      // The existing skill remains unselected if
      // master data cannot be loaded.
    }
  }

  // ============================================================
  // CANCEL EDIT
  // ============================================================

  void _cancelEditing() {
    _clearForm();
  }

  void _clearForm() {
    if (!mounted) return;

    setState(() {
      _editingSkillId = null;
      _selectedSkill = null;
      _selectedProficiency = null;
      _yearsOfExperienceCtrl.clear();
    });

    _formKey.currentState?.reset();
  }

  // ============================================================
  // DELETE
  // ============================================================

  Future<void> _confirmDelete(BuildContext context,
      int profileId,
      UserSkillResponseDTO skill,) async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Delete Skill'),
          content: Text(
            'Are you sure you want to delete '
                '${skill.skillName ?? 'this skill'}?',
          ),
          actions: [
            TextButton(
              onPressed: () {
                Navigator.pop(context, false);
              },
              child: const Text('Cancel'),
            ),
            ElevatedButton(
              onPressed: () {
                Navigator.pop(context, true);
              },
              child: const Text('Delete'),
            ),
          ],
        );
      },
    );

    if (confirmed != true || skill.id == null) {
      return;
    }

    await _deleteSkill(
      profileId,
      skill.id!,
    );
  }

  Future<void> _deleteSkill(int profileId,
      int skillId,) async {
    try {
      await ref.read(
        cvRepositoryProvider,
      ).deleteUserSkill(skillId);

      if (_editingSkillId == skillId) {
        _clearForm();
      }

      ref.invalidate(
        userSkillsProvider(profileId),
      );

      _showMessage(
        'Skill deleted successfully.',
      );
    } catch (error) {
      _showMessage(
        apiErrorMessage(error),
        isError: true,
      );
    } finally {
      if (mounted) {
        setState(() {
          _isSaving = false;
        });
      }
    }
  }

  // ============================================================
  // HELPERS
  // ============================================================

  String _proficiencyLabel(ProficiencyLevel? proficiency,) {
    if (proficiency == null) {
      return 'Unknown';
    }

    switch (proficiency) {
      case ProficiencyLevel.beginner:
        return 'Beginner';
      case ProficiencyLevel.intermediate:
        return 'Intermediate';
      case ProficiencyLevel.advanced:
        return 'Advanced';
      case ProficiencyLevel.expert:
        return 'Expert';
    }
  }

  void _showMessage(String message, {
    bool isError = false,
  }) {
    if (!mounted) return;

    ScaffoldMessenger.of(context).hideCurrentSnackBar();

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Text(message),
        backgroundColor: isError ? Colors.red : null,
      ),
    );
  }
}

// ============================================================
// PROVIDERS
// ============================================================

final allSkillsProvider =
FutureProvider<List<SkillResponseDTO>>((ref) {
  return ref
      .watch(masterDataRepositoryProvider)
      .getAllSkills();
});

final userSkillsProvider = FutureProvider.family
    .autoDispose<List<UserSkillResponseDTO>, int>(
      (ref, profileId) {
    return ref
        .watch(cvRepositoryProvider)
        .getUserSkillsByUserProfileId(profileId);
  },
);