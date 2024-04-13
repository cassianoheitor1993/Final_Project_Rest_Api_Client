package com.example.final_project_client;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class QuizzesManagementActivity extends AppCompatActivity {

    // Declare UI elements
    private ImageView imageViewQuizImage;
    private Button buttonAddImage;
    private EditText editTextQuestion;
    private Button buttonAddQuestion;
    private RecyclerView recyclerViewQuestions;

    // Request code for image selection
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    // Adapter for RecyclerView
    private QuestionAdapter questionsAdapter;
    private List<Question> questionList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quizzes_management);

        // Initialize UI elements
        imageViewQuizImage = findViewById(R.id.imageViewQuizImage);
        buttonAddImage = findViewById(R.id.buttonAddImage);
        editTextQuestion = findViewById(R.id.editTextQuestion);
        buttonAddQuestion = findViewById(R.id.buttonAddQuestions);
        recyclerViewQuestions = findViewById(R.id.recyclerViewQuestions);

        // Set up RecyclerView
        questionList = new ArrayList<>();
        questionsAdapter = new QuestionAdapter(this, questionList);
        recyclerViewQuestions.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewQuestions.setAdapter(questionsAdapter);

        // Add question button click listener
        buttonAddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String questionText = editTextQuestion.getText().toString().trim();
                if (!questionText.isEmpty()) {
                    Question question = new Question();
                    question.setQuestion(questionText);
                    questionList.add(question);
                    questionsAdapter.notifyDataSetChanged();
                    editTextQuestion.getText().clear();
                } else {
                    Toast.makeText(QuizzesManagementActivity.this, "Please enter a question", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Add image button click listener
        buttonAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
    }

    // Start image selection intent
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // Handle the result of image capture intent
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageViewQuizImage.setImageBitmap(imageBitmap);
        }
    }
}
