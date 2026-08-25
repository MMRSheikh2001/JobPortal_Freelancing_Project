class InterviewQuestion {
  final String? question;
  final String? answer;
  final int? score;

  const InterviewQuestion({
    this.question,
    this.answer,
    this.score,
  });

  /// Factory constructor to create [InterviewQuestion] from a JSON map.
  factory InterviewQuestion.fromJson(Map<String, dynamic> json) {
    return InterviewQuestion(
      question: json['question'] as String?,
      answer: json['answer'] as String?,
      score: (json['score'] as num?)?.toInt(),
    );
  }

  /// Converts this [InterviewQuestion] instance into a JSON-compatible map.
  Map<String, dynamic> toJson() {
    return {
      if (question != null) 'question': question,
      if (answer != null) 'answer': answer,
      if (score != null) 'score': score,
    };
  }

  /// Helper method to create a modified copy of this object.
  InterviewQuestion copyWith({
    String? question,
    String? answer,
    int? score,
  }) {
    return InterviewQuestion(
      question: question ?? this.question,
      answer: answer ?? this.answer,
      score: score ?? this.score,
    );
  }
}