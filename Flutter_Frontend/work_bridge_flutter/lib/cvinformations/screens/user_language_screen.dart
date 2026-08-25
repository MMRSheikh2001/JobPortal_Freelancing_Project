import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';

import 'package:work_bridge_flutter/auth/response/login_response.dart';
import 'package:work_bridge_flutter/cvinformations/models/request/user_language_request.dart';
import 'package:work_bridge_flutter/cvinformations/models/response/user_language_response.dart';
import 'package:work_bridge_flutter/enums/language_proficiency.dart';
import 'package:work_bridge_flutter/masterdata/models/response/language_response.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

class UserLanguageScreen extends ConsumerStatefulWidget {
  const UserLanguageScreen({super.key});

  @override
  ConsumerState<UserLanguageScreen> createState() =>
      _UserLanguageScreenState();
}

class _UserLanguageScreenState
    extends ConsumerState<UserLanguageScreen> {
  final _formKey = GlobalKey<FormState>();

  LanguageResponseDTO? _selectedLanguage;
  LanguageProficiency? _selectedProficiency;

  int? _editingLanguageId;

  bool _isSaving = false;

  @override
  Widget build(BuildContext context) {
    final authState = ref.watch(authControllerProvider);

    return Scaffold(
      appBar: AppBar(
        title: const Text('Languages'),
      ),
      body: authState.when(
        loading: () => const Center(
          child: CircularProgressIndicator(),
        ),
        error: (error, stack) => Center(
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

  Widget _buildPage(
      BuildContext context,
      LoginResponse loginResponse,
      ) {
    final profileId = loginResponse.profileId!;

    final languagesAsync = ref.watch(
      allLanguagesProvider,
    );

    return RefreshIndicator(
      onRefresh: () async {
        ref.invalidate(
          userLanguagesProvider(profileId),
        );
        ref.invalidate(
          allLanguagesProvider,
        );

        await ref.read(
          userLanguagesProvider(profileId).future,
        );
      },
      child: ListView(
        padding: const EdgeInsets.all(16),
        children: [
          _buildFormCard(
            context,
            profileId,
            languagesAsync,
          ),

          const SizedBox(height: 24),

          const Text(
            'My Languages',
            style: TextStyle(
              fontSize: 20,
              fontWeight: FontWeight.bold,
            ),
          ),

          const SizedBox(height: 12),

          _buildUserLanguages(
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

  Widget _buildFormCard(
      BuildContext context,
      int profileId,
      AsyncValue<List<LanguageResponseDTO>> languagesAsync,
      ) {
    final isEditing = _editingLanguageId != null;

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
                isEditing ? 'Edit Language' : 'Add Language',
                style: const TextStyle(
                  fontSize: 20,
                  fontWeight: FontWeight.bold,
                ),
              ),

              const SizedBox(height: 16),

              // ------------------------------------------------
              // Language
              // ------------------------------------------------

              languagesAsync.when(
                loading: () => const Center(
                  child: Padding(
                    padding: EdgeInsets.all(8),
                    child: CircularProgressIndicator(),
                  ),
                ),
                error: (error, stack) => Text(
                  'Failed to load languages: $error',
                  style: const TextStyle(
                    color: Colors.red,
                  ),
                ),
                data: (languages) {
                  return DropdownButtonFormField<LanguageResponseDTO>(
                    initialValue: _selectedLanguage,
                    decoration: const InputDecoration(
                      labelText: 'Language',
                      border: OutlineInputBorder(),
                      prefixIcon: Icon(Icons.language),
                    ),
                    items: languages.map((language) {
                      return DropdownMenuItem<LanguageResponseDTO>(
                        value: language,
                        child: Text(
                          language.name ?? 'Unknown',
                        ),
                      );
                    }).toList(),
                    onChanged: _isSaving
                        ? null
                        : (value) {
                      setState(() {
                        _selectedLanguage = value;
                      });
                    },
                    validator: (value) {
                      if (value == null) {
                        return 'Please select a language';
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

              DropdownButtonFormField<LanguageProficiency>(
                initialValue: _selectedProficiency,
                decoration: const InputDecoration(
                  labelText: 'Proficiency',
                  border: OutlineInputBorder(),
                  prefixIcon: Icon(Icons.bar_chart),
                ),
                items: LanguageProficiency.values.map((proficiency) {
                  return DropdownMenuItem<LanguageProficiency>(
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
                          : () => _saveLanguage(profileId),
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
  // USER LANGUAGES
  // ============================================================

  Widget _buildUserLanguages(
      BuildContext context,
      int profileId,
      ) {
    final languagesAsync = ref.watch(
      userLanguagesProvider(profileId),
    );

    return languagesAsync.when(
      loading: () => const Center(
        child: Padding(
          padding: EdgeInsets.all(30),
          child: CircularProgressIndicator(),
        ),
      ),
      error: (error, stack) => Card(
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
                'Failed to load languages.\n$error',
                textAlign: TextAlign.center,
              ),
              const SizedBox(height: 12),
              ElevatedButton(
                onPressed: () {
                  ref.invalidate(
                    userLanguagesProvider(profileId),
                  );
                },
                child: const Text('Retry'),
              ),
            ],
          ),
        ),
      ),
      data: (languages) {
        if (languages.isEmpty) {
          return Card(
            child: Padding(
              padding: const EdgeInsets.all(24),
              child: Column(
                children: const [
                  Icon(
                    Icons.language,
                    size: 48,
                    color: Colors.grey,
                  ),
                  SizedBox(height: 10),
                  Text(
                    'No languages added yet.',
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
          children: languages.map((language) {
            return _buildLanguageCard(
              context,
              profileId,
              language,
            );
          }).toList(),
        );
      },
    );
  }

  // ============================================================
  // LANGUAGE CARD
  // ============================================================

  Widget _buildLanguageCard(
      BuildContext context,
      int profileId,
      UserLanguageResponseDTO language,
      ) {
    return Card(
      margin: const EdgeInsets.only(bottom: 12),
      child: Padding(
        padding: const EdgeInsets.all(16),
        child: Row(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            CircleAvatar(
              child: const Icon(Icons.language),
            ),

            const SizedBox(width: 14),

            Expanded(
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    language.languageName ?? 'Unknown language',
                    style: const TextStyle(
                      fontSize: 17,
                      fontWeight: FontWeight.bold,
                    ),
                  ),

                  const SizedBox(height: 6),

                  Text(
                    'Proficiency: ${_proficiencyLabel(language.proficiency)}',
                    style: const TextStyle(
                      color: Colors.grey,
                    ),
                  ),
                ],
              ),
            ),

            PopupMenuButton<String>(
              onSelected: (value) {
                if (value == 'edit') {
                  _startEditing(language);
                } else if (value == 'delete') {
                  _confirmDelete(
                    context,
                    profileId,
                    language,
                  );
                }
              },
              itemBuilder: (context) => const [
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

  Future<void> _saveLanguage(int profileId) async {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    if (_selectedLanguage?.id == null ||
        _selectedProficiency == null) {
      return;
    }

    setState(() {
      _isSaving = true;
    });

    try {
      final request = UserLanguageRequestDTO(
        proficiency: _selectedProficiency,
        languageId: _selectedLanguage!.id,
        userProfileId: profileId,
      );

      final repository = ref.read(
        cvRepositoryProvider,
      );

      if (_editingLanguageId == null) {
        await repository.saveUserLanguage(request);

        _showMessage(
          'Language added successfully.',
        );
      } else {
        await repository.updateUserLanguage(
          _editingLanguageId!,
          request,
        );

        _showMessage(
          'Language updated successfully.',
        );
      }

      _clearForm();

      ref.invalidate(
        userLanguagesProvider(profileId),
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

  void _startEditing(
      UserLanguageResponseDTO language,
      ) {
    setState(() {
      _editingLanguageId = language.id;

      _selectedProficiency = language.proficiency;

      if (language.languageId != null) {
        _findLanguage(language.languageId!);
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

  Future<void> _findLanguage(int languageId) async {
    try {
      final languages = await ref.read(
        allLanguagesProvider.future,
      );

      final language = languages.firstWhere(
            (item) => item.id == languageId,
      );

      if (mounted) {
        setState(() {
          _selectedLanguage = language;
        });
      }
    } catch (_) {
      // The existing language remains unselected if
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
      _editingLanguageId = null;
      _selectedLanguage = null;
      _selectedProficiency = null;
    });

    _formKey.currentState?.reset();
  }

  // ============================================================
  // DELETE
  // ============================================================

  Future<void> _confirmDelete(
      BuildContext context,
      int profileId,
      UserLanguageResponseDTO language,
      ) async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Delete Language'),
          content: Text(
            'Are you sure you want to delete '
                '${language.languageName ?? 'this language'}?',
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

    if (confirmed != true || language.id == null) {
      return;
    }

    await _deleteLanguage(
      profileId,
      language.id!,
    );
  }

  Future<void> _deleteLanguage(
      int profileId,
      int languageId,
      ) async {
    try {
      await ref.read(
        cvRepositoryProvider,
      ).deleteUserLanguage(languageId);

      if (_editingLanguageId == languageId) {
        _clearForm();
      }

      ref.invalidate(
        userLanguagesProvider(profileId),
      );

      _showMessage(
        'Language deleted successfully.',
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

  String _proficiencyLabel(
      LanguageProficiency? proficiency,
      ) {
    if (proficiency == null) {
      return 'Unknown';
    }

    switch (proficiency) {
      case LanguageProficiency.beginner:
        return 'Beginner';
      case LanguageProficiency.intermediate:
        return 'Intermediate';
      case LanguageProficiency.advanced:
        return 'Advanced';
      case LanguageProficiency.native:
        return 'Native';
    }
  }

  void _showMessage(
      String message, {
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

final allLanguagesProvider =
FutureProvider<List<LanguageResponseDTO>>((ref) {
  return ref
      .watch(masterDataRepositoryProvider)
      .getAllLanguages();
});

final userLanguagesProvider = FutureProvider.family
    .autoDispose<List<UserLanguageResponseDTO>, int>(
      (ref, profileId) {
    return ref
        .watch(cvRepositoryProvider)
        .getUserLanguagesByUserProfileId(profileId);
  },
);