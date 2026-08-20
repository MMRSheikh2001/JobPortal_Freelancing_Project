
import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/auth/auth_provider.dart';
import 'package:work_bridge_flutter/utils/api_client.dart';
import 'package:work_bridge_flutter/widget/common_widget.dart';


class LoginScreen extends ConsumerStatefulWidget {
  const LoginScreen({super.key});

  @override
  ConsumerState<ConsumerStatefulWidget> createState() => _LoginScreenState();

}

class _LoginScreenState extends ConsumerState<LoginScreen> {

  final _formKey = GlobalKey<FormState>();
  final _emailCtrl = TextEditingController();
  final _passwordCtrl = TextEditingController();

  bool _showPassword = false;
  bool _loading = false;
  String? _errorMessage;


  @override
  void dispose() {
    _emailCtrl.dispose();
    _passwordCtrl.dispose();
    super.dispose();
  }

  Future<void> _login() async {
    if (!_formKey.currentState!.validate()) return;

    setState(() {
      _loading = true;
      _errorMessage = null;
    });

    try {
      await ref
          .read(authControllerProvider.notifier)
          .login(_emailCtrl.text.trim(), _passwordCtrl.text);
      if (!mounted) return;
      // Hand off to RoleRedirectScreen ('/'), which reads the now-updated
      // authControllerProvider and sends the user to the right dashboard —
      // mirrors role-redirect.ts being the landing route after login.
      Navigator.of(context).pushNamedAndRemoveUntil('/', (route) => false);
    } catch (e) {
      setState(() => _errorMessage = apiErrorMessage(e));
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
            constraints: const BoxConstraints(maxWidth: 420),
            child: Card(
              clipBehavior: Clip.antiAlias,
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  // ── Header ──────────────────────────────
                  Container(
                    width: double.infinity,
                    padding: const EdgeInsets.symmetric(vertical: 32),
                    color: Colors.blue,
                    child: const Column(
                      children: [
                        Icon(Icons.inventory_2_rounded,
                            color: Colors.white, size: 44),
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
                          'Sign in to your account',
                          style: TextStyle(color: Colors.white70, fontSize: 13),
                        ),
                      ],
                    ),
                  ),

                  // ── Form ────────────────────────────────
                  Padding(
                    padding: const EdgeInsets.all(24),
                    child: Form(
                      key: _formKey,
                      child: Column(
                        children: [
                          if (_errorMessage != null)
                            ErrorBanner(message: _errorMessage!),

                          TextFormField(
                            controller: _emailCtrl,
                            keyboardType: TextInputType.emailAddress,
                            decoration: const InputDecoration(
                              labelText: 'Email',
                              prefixIcon: Icon(Icons.email_outlined),
                              hintText: 'you@example.com',
                            ),
                            validator: (v) {
                              if (v == null || v.trim().isEmpty) {
                                return 'Enter a valid email.';
                              }
                              if (!v.contains('@')) return 'Enter a valid email.';
                              return null;
                            },
                          ),
                          const SizedBox(height: 16),

                          TextFormField(
                            controller: _passwordCtrl,
                            obscureText: !_showPassword,
                            decoration: InputDecoration(
                              labelText: 'Password',
                              prefixIcon: const Icon(Icons.lock_outline),
                              hintText: 'Your password',
                              suffixIcon: IconButton(
                                icon: Icon(_showPassword
                                    ? Icons.visibility_off
                                    : Icons.visibility),
                                onPressed: () => setState(
                                        () => _showPassword = !_showPassword),
                              ),
                            ),
                            validator: (v) => (v == null || v.isEmpty)
                                ? 'Password is required.'
                                : null,
                            onFieldSubmitted: (_) => _login(),
                          ),
                          Align(
                            alignment: Alignment.centerRight,
                            child: TextButton(
                              onPressed: () =>
                                  Navigator.of(context).pushNamed('/forgot-password'),
                              child: const Text('Forgot password?'),
                            ),
                          ),
                          const SizedBox(height: 8),

                          LoadingButton(
                            label: _loading ? 'Signing in...' : 'Sign In',
                            loading: _loading,
                            icon: Icons.login,
                            onPressed: _login,
                          ),
                          const SizedBox(height: 12),
                          TextButton(
                            onPressed: () =>
                                Navigator.of(context).pushNamed('/register'),
                            child: const Text("Don't have an account? Register"),
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
