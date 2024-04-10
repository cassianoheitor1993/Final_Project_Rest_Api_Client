package com.example.final_project_client;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditQuestionActivity extends AppCompatActivity {

    private EditText questionEditText;
    private EditText answer1EditText;
    private EditText answer2EditText;
    private EditText answer3EditText;
    private EditText answer4EditText;
    private EditText correctAnswerEditText;
    private Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_question);

        questionEditText = findViewById(R.id.edit_question_text);
        answer1EditText = findViewById(R.id.edit_answer1_text);
        answer2EditText = findViewById(R.id.edit_answer2_text);
        answer3EditText = findViewById(R.id.edit_answer3_text);
        answer4EditText = findViewById(R.id.edit_answer4_text);
        correctAnswerEditText = findViewById(R.id.edit_correct_answer_text);
        saveButton = findViewById(R.id.save_button);

        // Get the selected question from the intent
        Question selectedQuestion = (Question) getIntent().getSerializableExtra("selected_question");
        if (selectedQuestion != null) {
            questionEditText.setText(selectedQuestion.getQuestion());
            answer1EditText.setText(selectedQuestion.getAnswer1());
            answer2EditText.setText(selectedQuestion.getAnswer2());
            answer3EditText.setText(selectedQuestion.getAnswer3());
            answer4EditText.setText(selectedQuestion.getAnswer4());
            correctAnswerEditText.setText(String.valueOf(selectedQuestion.getCorrectAnswer()));
        }

        saveButton.setOnClickListener(v -> saveQuestion());
    }

    private void saveQuestion() {
        // Get the user input from EditText fields
        String question = questionEditText.getText().toString();
        String answer1 = answer1EditText.getText().toString();
        String answer2 = answer2EditText.getText().toString();
        String answer3 = answer3EditText.getText().toString();
        String answer4 = answer4EditText.getText().toString();
        int correctAnswer = Integer.parseInt(correctAnswerEditText.getText().toString());

        // Create a new Question object
        Question newQuestion = new Question();
        newQuestion.setQuestion(question);
        newQuestion.setAnswer1(answer1);
        newQuestion.setAnswer2(answer2);
        newQuestion.setAnswer3(answer3);
        newQuestion.setAnswer4(answer4);
        newQuestion.setCorrectAnswer(correctAnswer);

        // Here you can add logic to save the newQuestion object to your database
        // For example, you can use the APIHelper to save the question
        // apiHelper.saveQuestion(newQuestion);

        // Finish the activity
        finish();
    }

}
