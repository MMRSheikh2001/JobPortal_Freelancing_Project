import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/auth/request/user_request.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/widget/common_widget.dart';

class RegisterScreen extends ConsumerStatefulWidget {
  const RegisterScreen({super.key});

  @override
  ConsumerState<RegisterScreen> createState() => _RegisterScreenState();
}

class _RegisterScreenState extends ConsumerState<RegisterScreen> {
  final _formKey = GlobalKey<FormState>();

  final _nameCtrl = TextEditingController();
  final _emailCtrl = TextEditingController();
  final _passwordCtrl = TextEditingController();
  final _confirmPasswordCtrl = TextEditingController();

  bool _showPassword = false;
  bool _showConfirmPassword = false;

  // Job Seeker is selected by default.
  UserRole _selectedRole = UserRole.USER;

  bool _loading = false;

  String? _errorMessage;
  String? _successMessage;

  @override
  void dispose() {
    _nameCtrl.dispose();
    _emailCtrl.dispose();
    _passwordCtrl.dispose();
    _confirmPasswordCtrl.dispose();
    super.dispose();
  }

  Future<void> _register() async {
    if (!_formKey.currentState!.validate()) {
      return;
    }

    setState(() {
      _loading = true;
      _errorMessage = null;
      _successMessage = null;
    });

    try {
      final request = UserRequestDTO(
        fullName: _nameCtrl.text.trim(),
        email: _emailCtrl.text.trim(),
        password: _passwordCtrl.text,
        role: _selectedRole,
      );

      final response = await ref.read(authRepositoryProvider).register(request);

      if (!mounted) return;

      _nameCtrl.clear();
      _emailCtrl.clear();
      _passwordCtrl.clear();
      _confirmPasswordCtrl.clear();

      setState(() {
        _successMessage =
            'Account created for ${response.email ?? _emailCtrl.text.trim()}. '
            'Please check your email to verify before signing in.';
      });
    } catch (e) {
      if (!mounted) return;

      setState(() {
        _errorMessage = apiErrorMessage(e);
      });
    } finally {
      if (mounted) {
        setState(() {
          _loading = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(24),
          child: ConstrainedBox(
            constraints: const BoxConstraints(maxWidth: 420),
            child: Card(
              clipBehavior: Clip.antiAlias,
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  // Header
                  Container(
                    width: double.infinity,
                    padding: const EdgeInsets.symmetric(vertical: 32),
                    color: Colors.blue,
                    child: const Column(
                      children: [
                        Icon(
                          Icons.person_add_alt_1_rounded,
                          color: Colors.white,
                          size: 44,
                        ),
                        SizedBox(height: 8),
                        Text(
                          'WorkBridge',
                          style: TextStyle(
                            color: Colors.white,
                            fontSize: 18,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                        SizedBox(height: 2),
                        Text(
                          'Create your account',
                          style: TextStyle(color: Colors.white70, fontSize: 13),
                        ),
                      ],
                    ),
                  ),

                  // Form
                  Padding(
                    padding: const EdgeInsets.all(24),
                    child: Form(
                      key: _formKey,
                      child: Column(
                        children: [
                          if (_errorMessage != null)
                            ErrorBanner(message: _errorMessage!),

                          if (_successMessage != null)
                            SuccessBanner(message: _successMessage!),

                          // Full Name
                          TextFormField(
                            controller: _nameCtrl,
                            textCapitalization: TextCapitalization.words,
                            decoration: const InputDecoration(
                              labelText: 'Full Name',
                              prefixIcon: Icon(Icons.person_outline),
                            ),
                            validator: (value) {
                              if (value == null || value.trim().isEmpty) {
                                return 'Full name is required.';
                              }

                              return null;
                            },
                          ),

                          const SizedBox(height: 16),

                          // Email
                          TextFormField(
                            controller: _emailCtrl,
                            keyboardType: TextInputType.emailAddress,
                            decoration: const InputDecoration(
                              labelText: 'Email',
                              prefixIcon: Icon(Icons.email_outlined),
                              hintText: 'you@example.com',
                            ),
                            validator: (value) {
                              if (value == null || value.trim().isEmpty) {
                                return 'Email is required.';
                              }

                              if (!value.contains('@')) {
                                return 'Enter a valid email.';
                              }

                              return null;
                            },
                          ),

                          const SizedBox(height: 16),

                          // Password
                          TextFormField(
                            controller: _passwordCtrl,
                            obscureText: !_showPassword,
                            decoration: InputDecoration(
                              labelText: 'Password',
                              prefixIcon: const Icon(Icons.lock_outline),
                              suffixIcon: IconButton(
                                icon: Icon(
                                  _showPassword
                                      ? Icons.visibility_off
                                      : Icons.visibility,
                                ),
                                onPressed: () {
                                  setState(() {
                                    _showPassword = !_showPassword;
                                  });
                                },
                              ),
                            ),
                            validator: (value) {
                              if (value == null || value.isEmpty) {
                                return 'Password is required.';
                              }

                              if (value.length < 6) {
                                return 'Password must be at least 6 characters.';
                              }

                              return null;
                            },
                          ),

                          const SizedBox(height: 16),

                          // Confirm Password
                          TextFormField(
                            controller: _confirmPasswordCtrl,
                            obscureText: !_showConfirmPassword,
                            decoration: InputDecoration(
                              labelText: 'Confirm Password',
                              prefixIcon: const Icon(Icons.lock_outline),
                              suffixIcon: IconButton(
                                icon: Icon(
                                  _showConfirmPassword
                                      ? Icons.visibility_off
                                      : Icons.visibility,
                                ),
                                onPressed: () {
                                  setState(() {
                                    _showConfirmPassword =
                                        !_showConfirmPassword;
                                  });
                                },
                              ),
                            ),
                            validator: (value) {
                              if (value == null || value.isEmpty) {
                                return 'Please confirm your password.';
                              }

                              if (value != _passwordCtrl.text) {
                                return 'Passwords do not match.';
                              }

                              return null;
                            },
                            onFieldSubmitted: (_) => _register(),
                          ),

                          const SizedBox(height: 20),

                          // Account Type
                          const Align(
                            alignment: Alignment.centerLeft,
                            child: Text(
                              'Account Type',
                              style: TextStyle(fontWeight: FontWeight.w600),
                            ),
                          ),

                          const SizedBox(height: 8),

                          RadioListTile<UserRole>(
                            contentPadding: EdgeInsets.zero,
                            value: UserRole.USER,
                            groupValue: _selectedRole,
                            title: const Text('Job Seeker'),
                            subtitle: const Text(
                              'Find jobs and build your career',
                            ),
                            onChanged: _loading
                                ? null
                                : (value) {
                                    if (value != null) {
                                      setState(() {
                                        _selectedRole = value;
                                      });
                                    }
                                  },
                          ),

                          RadioListTile<UserRole>(
                            contentPadding: EdgeInsets.zero,
                            value: UserRole.COMPANY,
                            groupValue: _selectedRole,
                            title: const Text('Company'),
                            subtitle: const Text(
                              'Hire talent and manage recruitment',
                            ),
                            onChanged: _loading
                                ? null
                                : (value) {
                                    if (value != null) {
                                      setState(() {
                                        _selectedRole = value;
                                      });
                                    }
                                  },
                          ),

                          const SizedBox(height: 20),

                          // Register Button
                          LoadingButton(
                            label: _loading
                                ? 'Creating Account...'
                                : 'Create Account',
                            loading: _loading,
                            icon: Icons.person_add,
                            onPressed: _register,
                          ),

                          const SizedBox(height: 12),

                          // Login
                          TextButton(
                            onPressed: _loading
                                ? null
                                : () {
                                    Navigator.of(
                                      context,
                                    ).pushReplacementNamed('/login');
                                  },
                            child: const Text(
                              'Already have an account? Sign In',
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ],
              ),
            ),
          ),
        ),
      ),
    );
  }
}
