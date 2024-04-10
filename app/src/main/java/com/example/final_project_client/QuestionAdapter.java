package com.example.final_project_client;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class QuestionAdapter extends ArrayAdapter<Question> {

    private final Context context;
    private final List<Question> questions;

    public QuestionAdapter(Context context, List<Question> questions) {
        super(context, R.layout.question_item, questions);
        this.context = context;
        this.questions = questions;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.question_item, parent, false);

        TextView questionTextView = rowView.findViewById(R.id.question_text);
        Question currentQuestion = questions.get(position);
        int correctAnswerIndex = currentQuestion.getCorrectAnswer();

        String correctAnswerText = "";

        switch (correctAnswerIndex) {
            case 1:
                correctAnswerText = "Correct: " + currentQuestion.getAnswer1();
                break;
            case 2:
                correctAnswerText = "Correct: " + currentQuestion.getAnswer2();
                break;
            case 3:
                correctAnswerText = "Correct: " + currentQuestion.getAnswer3();
                break;
            case 4:
                correctAnswerText = "Correct: " + currentQuestion.getAnswer4();
                break;
        }

        // Set the question text with all answer options and the correct answer after the respective answer option
        questionTextView.setText(currentQuestion.getQuestionText() +
                "\nA) " + currentQuestion.getAnswer1() +
                "\nB) " + currentQuestion.getAnswer2() +
                "\nC) " + currentQuestion.getAnswer3() +
                "\nD) " + currentQuestion.getAnswer4() +
                "\n" + correctAnswerText);

        rowView.setOnClickListener(v -> {
            Question selectedQuestion = questions.get(position);
            editQuestion(selectedQuestion);
        });

        return rowView;
    }

    private void editQuestion(Question question) {
        Intent intent = new Intent(context, EditQuestionActivity.class);
        intent.putExtra("selected_question", (CharSequence) question);
        context.startActivity(intent);
    }
}
