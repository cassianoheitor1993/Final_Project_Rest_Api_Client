package com.example.final_project_client;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private final Context context;
    private List<Question> questions;

    // Constructor to initialize the context and questions list
    public QuestionAdapter(Context context, List<Question> questions) {
        this.context = context;
        this.questions = questions;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.question_item, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(QuestionViewHolder holder, int position) {
        Question currentQuestion = questions.get(position);
        holder.bind(currentQuestion);
    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
        notifyDataSetChanged();
    }

    class QuestionViewHolder extends RecyclerView.ViewHolder {

        private TextView questionTextView;

        QuestionViewHolder(View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.question_text);
        }

        void bind(Question question) {
            int correctAnswerIndex = question.getCorrectAnswer();

            String correctAnswerText = "";

            switch (correctAnswerIndex) {
                case 1:
                    correctAnswerText = "Correct: " + question.getAnswer1();
                    break;
                case 2:
                    correctAnswerText = "Correct: " + question.getAnswer2();
                    break;
                case 3:
                    correctAnswerText = "Correct: " + question.getAnswer3();
                    break;
                case 4:
                    correctAnswerText = "Correct: " + question.getAnswer4();
                    break;
            }

            questionTextView.setText(question.getQuestionText() +
                    "\nA) " + question.getAnswer1() +
                    "\nB) " + question.getAnswer2() +
                    "\nC) " + question.getAnswer3() +
                    "\nD) " + question.getAnswer4() +
                    "\n" + correctAnswerText);

            itemView.setOnClickListener(v -> editQuestion(question));
        }
    }

    public void editQuestion(Question question) {
        Intent intent = new Intent(context, EditQuestionActivity.class);
        intent.putExtra("selected_question", question);
        context.startActivity(intent);
    }
}
