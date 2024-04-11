package com.example.final_project_client;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class EditQuestionActivity extends AppCompatActivity {

    private ApiHelper apiHelper;
    private EditText questionEditText;
    private EditText answer1EditText;
    private EditText answer2EditText;
    private EditText answer3EditText;
    private EditText answer4EditText;
    private EditText correctAnswerEditText;
    private Button saveButton, deleteButton, cancelButton;
    private Spinner spinnerSubjects, spinnerLevels;

    private String[] levels = {"Select complexity", "Easy", "Medium", "High"};
    private String[] subjects;

    public enum ComplexityLevel {
        Easy(1),
        Medium(2),
        High(3);

        private final int value;

        ComplexityLevel(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

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
        deleteButton = findViewById(R.id.delete_button);
        cancelButton = findViewById(R.id.cancel_button);

        // Initialize apiHelper
        apiHelper = new ApiHelper(this);

        // Initialize spinners
        spinnerSubjects = findViewById(R.id.spinnerSubjects);
        spinnerLevels = findViewById(R.id.spinnerComplexityLevel);

        apiHelper.getSubjects(response -> {
            subjects = new String[response.size()];
            for (int i = 0; i < response.size(); i++) {
                subjects[i] = response.get(i).getSubjectName();
            }
            ArrayList<String> subjectsList = new ArrayList<>(Arrays.asList(subjects));
            subjectsList.add(0, "Select subject");
            ArrayAdapter<String> subjectsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, subjectsList);
            spinnerSubjects.setAdapter(subjectsAdapter);
        }, error -> {
        });

        spinnerLevels.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, levels));

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

        deleteButton.setOnClickListener(v -> deleteQuestion());

        cancelButton.setOnClickListener(v -> finish());
    }

    private void deleteQuestion() {
        // Get the selected question again to delete it
        Question selectedQuestion = (Question) getIntent().getSerializableExtra("selected_question");

        if (selectedQuestion != null) {

            // Finish the activity
            finish();
        }
    }

    private void saveQuestion() {
        // Get the user input from EditText fields
        String questionText = questionEditText.getText().toString();
        String answer1 = answer1EditText.getText().toString();
        String answer2 = answer2EditText.getText().toString();
        String answer3 = answer3EditText.getText().toString();
        String answer4 = answer4EditText.getText().toString();
        String subject = spinnerSubjects.getSelectedItem().toString();
        String complexityStr = spinnerLevels.getSelectedItem().toString();
        int correctAnswer = Integer.parseInt(correctAnswerEditText.getText().toString());

        ComplexityLevel complexityLevel;

        // Check if the selected complexity is valid
        if (!"Select complexity".equals(complexityStr)) {
            complexityLevel = ComplexityLevel.valueOf(complexityStr);
        } else {
            Toast.makeText(this, "Please select a valid complexity level", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert ComplexityLevel enum value to its integer representation
        int complexityInt = complexityLevel.getValue();

        // Get the selected question again to update it
        Question selectedQuestion = (Question) getIntent().getSerializableExtra("selected_question");

        if (selectedQuestion != null) {
            // Update the selected question object
            int questionId = selectedQuestion.getId();
            selectedQuestion.setQuestion(questionText);
            selectedQuestion.setAnswer1(answer1);
            selectedQuestion.setAnswer2(answer2);
            selectedQuestion.setAnswer3(answer3);
            selectedQuestion.setAnswer4(answer4);
            selectedQuestion.setCorrectAnswer(correctAnswer);
            selectedQuestion.setSubject(subject);
            selectedQuestion.setComplexity(complexityStr);
            selectedQuestion.setComplexityInt(complexityInt);

            apiHelper.updateQuestion(questionId, selectedQuestion, response -> {
                Toast.makeText(this, "Question updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            }, error -> {
                Toast.makeText(this, "Error updating question", Toast.LENGTH_SHORT).show();
            });
        }
    }

}
