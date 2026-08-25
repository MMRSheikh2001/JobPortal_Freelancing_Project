import 'package:flutter/material.dart';
import 'package:flutter_riverpod/flutter_riverpod.dart';
import 'package:work_bridge_flutter/job/entity/response/ai_interview_session_response.dart';
import 'package:work_bridge_flutter/job/entity/response/interview_question.dart';
import 'package:work_bridge_flutter/utils/providers.dart';

class AiInterviewScreen extends ConsumerStatefulWidget {
  final int applicationId;

  const AiInterviewScreen({super.key, required this.applicationId});

  @override
  ConsumerState<AiInterviewScreen> createState() => _AiInterviewScreenState();
}

class _AiInterviewScreenState extends ConsumerState<AiInterviewScreen> {
  AIInterviewSessionResponseDTO? _session;

  final List<TextEditingController> _answerControllers = [];

  bool _loading = true;
  bool _submitting = false;
  String? _error;

  int _currentQuestion = 0;

  @override
  void initState() {
    super.initState();
    _startInterview();
  }

  @override
  void dispose() {
    for (final controller in _answerControllers) {
      controller.dispose();
    }
    super.dispose();
  }

  // =====================================================
  // Start Interview
  // =====================================================

  Future<void> _startInterview() async {
    try {
      final session = await ref
          .read(jobRepositoryProvider)
          .startInterview(widget.applicationId);

      if (!mounted) return;

      _answerControllers.clear();

      final questions = session.questions ?? [];

      for (final question in questions) {
        _answerControllers.add(
          TextEditingController(text: question.answer ?? ''),
        );
      }

      setState(() {
        _session = session;
        _loading = false;
        _error = null;
      });
    } catch (e) {
      if (!mounted) return;

      setState(() {
        _loading = false;
        _error = e.toString();
      });
    }
  }

  // =====================================================
  // Submit Interview
  // =====================================================

  Future<void> _submitInterview() async {
    if (_session == null) return;

    final questions = _session!.questions ?? [];

    // Make sure every question has an answer.
    for (int i = 0; i < questions.length; i++) {
      if (_answerControllers[i].text.trim().isEmpty) {
        ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(
            content: Text('Please answer question ${i + 1} before submitting.'),
          ),
        );

        setState(() {
          _currentQuestion = i;
        });

        return;
      }
    }

    final answeredQuestions = <InterviewQuestion>[];

    for (int i = 0; i < questions.length; i++) {
      answeredQuestions.add(
        questions[i].copyWith(answer: _answerControllers[i].text.trim()),
      );
    }

    final submitData = _session!.copyWith(
      questions: answeredQuestions,
      completed: false,
    );

    setState(() {
      _submitting = true;
    });

    try {
      final result = await ref
          .read(jobRepositoryProvider)
          .submitInterview(submitData);

      if (!mounted) return;

      setState(() {
        _session = result;
        _submitting = false;
      });

      ScaffoldMessenger.of(context).showSnackBar(
        const SnackBar(content: Text('Interview submitted successfully.')),
      );
    } catch (e) {
      if (!mounted) return;

      setState(() {
        _submitting = false;
      });

      ScaffoldMessenger.of(
        context,
      ).showSnackBar(SnackBar(content: Text('Failed to submit interview: $e')));
    }
  }

  // =====================================================
  // Build
  // =====================================================

  @override
  Widget build(BuildContext context) {
    if (_loading) {
      return Scaffold(
        appBar: AppBar(title: const Text('AI Interview')),
        body: const Center(child: CircularProgressIndicator()),
      );
    }

    if (_error != null) {
      return Scaffold(
        appBar: AppBar(title: const Text('AI Interview')),
        body: Center(
          child: Padding(
            padding: const EdgeInsets.all(24),
            child: Column(
              mainAxisSize: MainAxisSize.min,
              children: [
                const Icon(Icons.error_outline, size: 60, color: Colors.red),
                const SizedBox(height: 16),
                const Text(
                  'Unable to start the interview.',
                  style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
                  textAlign: TextAlign.center,
                ),
                const SizedBox(height: 8),
                Text(_error!, textAlign: TextAlign.center),
                const SizedBox(height: 24),
                ElevatedButton(
                  onPressed: () {
                    setState(() {
                      _loading = true;
                      _error = null;
                    });

                    _startInterview();
                  },
                  child: const Text('Try Again'),
                ),
              ],
            ),
          ),
        ),
      );
    }

    if (_session == null) {
      return Scaffold(
        appBar: AppBar(title: const Text('AI Interview')),
        body: const Center(child: Text('No interview session found.')),
      );
    }

    if (_session!.completed == true) {
      return _buildCompletedScreen();
    }

    return _buildInterviewScreen();
  }

  // =====================================================
  // Interview Screen
  // =====================================================

  Widget _buildInterviewScreen() {
    final questions = _session!.questions ?? [];

    if (questions.isEmpty) {
      return Scaffold(
        appBar: AppBar(title: const Text('AI Interview')),
        body: const Center(
          child: Text('No interview questions were generated.'),
        ),
      );
    }

    final question = questions[_currentQuestion];

    return Scaffold(
      appBar: AppBar(title: const Text('AI Interview')),
      body: SafeArea(
        child: Padding(
          padding: const EdgeInsets.all(20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              // Progress
              Text(
                'Question ${_currentQuestion + 1} of ${questions.length}',
                style: const TextStyle(
                  fontSize: 16,
                  fontWeight: FontWeight.w600,
                ),
              ),

              const SizedBox(height: 10),

              LinearProgressIndicator(
                value: (_currentQuestion + 1) / questions.length,
              ),

              const SizedBox(height: 30),

              // Question
              Text(
                question.question ?? 'Question unavailable',
                style: const TextStyle(
                  fontSize: 21,
                  fontWeight: FontWeight.bold,
                ),
              ),

              const SizedBox(height: 24),

              // Answer
              Expanded(
                child: TextField(
                  controller: _answerControllers[_currentQuestion],
                  maxLines: null,
                  expands: true,
                  textAlignVertical: TextAlignVertical.top,
                  decoration: const InputDecoration(
                    hintText: 'Write your answer here...',
                    border: OutlineInputBorder(),
                    alignLabelWithHint: true,
                  ),
                ),
              ),

              const SizedBox(height: 20),

              // Navigation buttons
              Row(
                children: [
                  if (_currentQuestion > 0)
                    Expanded(
                      child: OutlinedButton(
                        onPressed: _submitting
                            ? null
                            : () {
                                setState(() {
                                  _currentQuestion--;
                                });
                              },
                        child: const Text('Previous'),
                      ),
                    ),

                  if (_currentQuestion > 0) const SizedBox(width: 12),

                  Expanded(
                    child: ElevatedButton(
                      onPressed: _submitting
                          ? null
                          : () {
                              if (_currentQuestion < questions.length - 1) {
                                setState(() {
                                  _currentQuestion++;
                                });
                              } else {
                                _showSubmitConfirmation();
                              }
                            },
                      child: Text(
                        _currentQuestion == questions.length - 1
                            ? 'Submit Interview'
                            : 'Next',
                      ),
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

  // =====================================================
  // Submit Confirmation
  // =====================================================

  Future<void> _showSubmitConfirmation() async {
    final confirmed = await showDialog<bool>(
      context: context,
      builder: (context) {
        return AlertDialog(
          title: const Text('Submit Interview?'),
          content: const Text(
            'Are you sure you want to submit your interview? '
            'Your answers will be evaluated by AI and the interview '
            'cannot be retaken.',
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
              child: const Text('Submit'),
            ),
          ],
        );
      },
    );

    if (confirmed == true) {
      await _submitInterview();
    }
  }

  // =====================================================
  // Completed Screen
  // =====================================================

  Widget _buildCompletedScreen() {
    final questions = _session!.questions ?? [];

    return Scaffold(
      appBar: AppBar(title: const Text('Interview Result')),
      body: SafeArea(
        child: SingleChildScrollView(
          padding: const EdgeInsets.all(20),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Center(
                child: Column(
                  children: [
                    const Icon(
                      Icons.check_circle,
                      size: 80,
                      color: Colors.green,
                    ),
                    const SizedBox(height: 16),
                    const Text(
                      'Interview Completed',
                      style: TextStyle(
                        fontSize: 24,
                        fontWeight: FontWeight.bold,
                      ),
                    ),
                    const SizedBox(height: 12),
                    Text(
                      'Total Score: ${_session!.totalScore ?? 0}',
                      style: const TextStyle(
                        fontSize: 20,
                        fontWeight: FontWeight.w600,
                      ),
                    ),
                  ],
                ),
              ),

              const SizedBox(height: 30),

              const Text(
                'Questions & Results',
                style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
              ),

              const SizedBox(height: 15),

              ...List.generate(questions.length, (index) {
                final question = questions[index];

                return Card(
                  margin: const EdgeInsets.only(bottom: 16),
                  child: Padding(
                    padding: const EdgeInsets.all(16),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Text(
                          'Question ${index + 1}',
                          style: const TextStyle(fontWeight: FontWeight.bold),
                        ),

                        const SizedBox(height: 8),

                        Text(
                          question.question ?? '',
                          style: const TextStyle(
                            fontSize: 17,
                            fontWeight: FontWeight.w600,
                          ),
                        ),

                        const SizedBox(height: 16),

                        const Text(
                          'Your Answer',
                          style: TextStyle(fontWeight: FontWeight.bold),
                        ),

                        const SizedBox(height: 6),

                        Text(question.answer ?? 'No answer'),

                        const SizedBox(height: 16),

                        Text(
                          'Score: ${question.score ?? 0}',
                          style: const TextStyle(fontWeight: FontWeight.bold),
                        ),
                      ],
                    ),
                  ),
                );
              }),

              const SizedBox(height: 10),

              SizedBox(
                width: double.infinity,
                child: ElevatedButton(
                  onPressed: () {
                    Navigator.pop(context);
                  },
                  child: const Text('Back to Applications'),
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }
}
