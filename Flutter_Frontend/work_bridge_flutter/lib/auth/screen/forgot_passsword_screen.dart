import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/auth/request/forgot_password_request.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/widget/common_widget.dart';

class ForgotPasswordScreen extends ConsumerStatefulWidget {
  const ForgotPasswordScreen({super.key});

  @override
  ConsumerState<ConsumerStatefulWidget> createState() =>
      _ForgotPasswordScreenState();
}

class _ForgotPasswordScreenState extends ConsumerState<ForgotPasswordScreen> {
  final _formKey = GlobalKey<FormState>();
  final _emailCtrl = TextEditingController();
  bool _loading = false;
  String? _error;
  String? _success;

  Future<void> _submit() async {
    if (!_formKey.currentState!.validate()) return;
    setState(() {
      _loading = true;
      _error = null;
      _success = null;
    });
    try {
      final msg = await ref
          .read(authRepositoryProvider)
          .forgotPassword(ForgotPasswordRequest(email: _emailCtrl.text.trim()));
      setState(() => _success = msg);
    } catch (e) {
      setState(() => _error = apiErrorMessage(e));
    } finally {
      if (mounted) setState(() => _loading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(24),
          child: ConstrainedBox(
            constraints: BoxConstraints(maxWidth: 420),
            child: Card(
              child: Padding(
                padding: const EdgeInsets.all(24),
                child: Form(
                  key: _formKey,
                  child: Column(
                    mainAxisSize: MainAxisSize.min,
                    children: [
                      const Text(
                        "Forgot Password",
                        style: TextStyle(
                          color: Colors.blue,
                          fontSize: 20,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      const SizedBox(height: 20),
                      const Text(
                        'Enter your account email and we will send you a reset link.',
                        textAlign: TextAlign.center,
                      ),
                      const SizedBox(height: 20),
                      if (_error != null) ErrorBanner(message: _error!),
                      if (_success != null) SuccessBanner(message: _success!),

                      TextFormField(
                        controller: _emailCtrl,
                        keyboardType: TextInputType.emailAddress,
                        decoration: const InputDecoration(
                          labelText: 'Email',
                          prefixIcon: Icon(Icons.email_outlined),
                        ),
                        validator: (v) => (v == null || !v.contains('@'))
                            ? 'Enter a valid email.'
                            : null,
                      ),
                      const SizedBox(height: 20),

                      LoadingButton(
                        label: 'Send Reset Link',
                        loading: _loading,
                        icon: Icons.send,
                        onPressed: _submit,
                      ),

                      const SizedBox(height: 20),
                      // Login
                      TextButton(
                        onPressed: _loading
                            ? null
                            : () {
                                Navigator.of(
                                  context,
                                ).pushReplacementNamed('/login');
                              },
                        child: const Text('Go to Login'),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ),
        ),
      ),
    );
  }
}
