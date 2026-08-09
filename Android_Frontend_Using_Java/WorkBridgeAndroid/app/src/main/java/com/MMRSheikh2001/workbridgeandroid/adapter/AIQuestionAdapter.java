package com.MMRSheikh2001.workbridgeandroid.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.MMRSheikh2001.workbridgeandroid.R;
import com.MMRSheikh2001.workbridgeandroid.response.InterviewQuestion;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

import lombok.NonNull;


public class AIQuestionAdapter extends RecyclerView.Adapter<AIQuestionAdapter.ViewHolder> {

    private final Context context;
    private final List<InterviewQuestion> questionList;
    private final boolean readOnly;

    public AIQuestionAdapter(
            Context context,
            List<InterviewQuestion> questionList,
            boolean readOnly) {

        this.context = context;
        this.questionList = questionList;
        this.readOnly = readOnly;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_ai_question, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position) {

        InterviewQuestion question = questionList.get(position);

        holder.tvQuestionNumber.setText(
                "Question " + (position + 1));

        holder.tvQuestion.setText(question.getQuestion());

        holder.etAnswer.setText(question.getAnswer());

        if (readOnly) {

            holder.etAnswer.setEnabled(false);
            holder.layoutAnswer.setEnabled(false);

            holder.tvScore.setVisibility(View.VISIBLE);

            holder.tvScore.setText(
                    "Score : "
                            + (question.getScore() == null
                            ? 0
                            : question.getScore()));

        } else {

            holder.etAnswer.setEnabled(true);
            holder.layoutAnswer.setEnabled(true);

            holder.tvScore.setVisibility(View.GONE);

            holder.etAnswer.addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(
                        CharSequence s,
                        int start,
                        int count,
                        int after) {

                }

                @Override
                public void onTextChanged(
                        CharSequence s,
                        int start,
                        int before,
                        int count) {

                    question.setAnswer(s.toString());

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return questionList.size();
    }

    public List<InterviewQuestion> getQuestionList() {
        return questionList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvQuestionNumber;
        TextView tvQuestion;
        TextView tvScore;

        TextInputLayout layoutAnswer;
        TextInputEditText etAnswer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvQuestionNumber = itemView.findViewById(R.id.tvQuestionNumber);
            tvQuestion = itemView.findViewById(R.id.tvQuestion);
            tvScore = itemView.findViewById(R.id.tvScore);

            layoutAnswer = itemView.findViewById(R.id.layoutAnswer);
            etAnswer = itemView.findViewById(R.id.etAnswer);
        }
    }
}
