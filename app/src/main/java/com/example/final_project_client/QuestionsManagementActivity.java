package com.example.final_project_client;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class QuestionsManagementActivity extends AppCompatActivity {

    private ApiHelper apiHelper;
    private Spinner spinnerSubjects, spinnerLevels;
    private String[] levels = {"Select complexity", "Easy", "Medium", "High"};
    private RecyclerView questionsRecyclerView;
    private QuestionAdapter questionAdapter;
    private TextView questionsListLabel;
    private String[] subjects;

    private LinearLayout buttonContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_questions_management);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Question");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);
        Drawable customOverflowIcon = ContextCompat.getDrawable(this, R.drawable.custom_overflow_icon);
        toolbar.setOverflowIcon(customOverflowIcon);

        // Initialize apiHelper
        apiHelper = new ApiHelper(this);

        // Initialize spinners
        spinnerSubjects = findViewById(R.id.spinnerSubjects);
        spinnerLevels = findViewById(R.id.spinnerComplexityLevel);

        // label for the questions list
        questionsListLabel = findViewById(R.id.questionsListLabel);

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

        // Initialize RecyclerView
        questionsRecyclerView = findViewById(R.id.questionsList);
        questionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        questionAdapter = new QuestionAdapter(this, new ArrayList<>());
        questionsRecyclerView.setAdapter(questionAdapter);

        onSpinnerChange();
    }

    private void applyFilters() {
        if (spinnerSubjects.getSelectedItem() != null && spinnerLevels.getSelectedItem() != null) {
            String subject = spinnerSubjects.getSelectedItem().toString();
            String complexity = spinnerLevels.getSelectedItem().toString();
            if ("Select subject".equals(subject)) {
                subject = "none";
            }
            if ("Select complexity".equals(complexity)) {
                complexity = "none";
            }
            apiHelper.getQuestions(subject, complexity, response -> {
                questionAdapter.setQuestions(response);
                questionsListLabel.setText("Questions (Total: " + response.size() + "):");
            }, error -> {
            });
        }
    }

    private void onSpinnerChange() {
        spinnerSubjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                applyFilters();
            }
        });

        spinnerLevels.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                applyFilters();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                applyFilters();
            }
        });
    }

    private void editQuestion(Question question) {
        Intent intent = new Intent(this, EditQuestionActivity.class);
        intent.putExtra("selected_question", question); // Question is Serializable now
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fragment_menu, menu);
        boolean isLoggedIn = checkUserLoggedIn();
        menu.findItem(R.id.home_activity).setVisible(true);
        menu.findItem(R.id.profile).setVisible(isLoggedIn);
        menu.findItem(R.id.previous_quizzes).setVisible(isLoggedIn);
        menu.findItem(R.id.questions_mngmt).setVisible(isAdmin(isLoggedIn));
        menu.findItem(R.id.users_mngmt).setVisible(isAdmin(isLoggedIn));
        menu.findItem(R.id.quizzes_mngmt).setVisible(isAdmin(isLoggedIn));
        menu.findItem(R.id.action_login).setVisible(!isLoggedIn);
        menu.findItem(R.id.action_logout).setVisible(isLoggedIn);
        return true;
    }

    private boolean checkUserLoggedIn() {
        Intent intent = getIntent();
        if (intent != null) {
            String userDataString = intent.getStringExtra("response");
            try {
                JSONObject userData = new JSONObject(userDataString);
                return userData.has("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean isAdmin(boolean isLoggedIn) {
        if (isLoggedIn) {
            Intent intent = getIntent();
            if (intent != null) {
                String userDataString = intent.getStringExtra("response");
                try {
                    JSONObject userData = new JSONObject(userDataString);
                    String role = userData.getString("role");
                    return "admin".equals(role) || "teacher".equals(role);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.home_activity) {
            navigateTo(MainActivity.class);
            return true;
        } else if (id == R.id.action_login) {
            navigateTo(LoginActivity.class);
            return true;
        } else if (id == R.id.action_logout) {
            logoutUser();
            return true;
        } else if (id == R.id.profile) {
            navigateTo(ProfileActivity.class);
            return true;
        } else if (id == R.id.previous_quizzes) {
            navigateTo(PreviousQuizzesActivity.class);
            return true;
        } else if (id == R.id.questions_mngmt) {
            navigateTo(QuestionsManagementActivity.class);
            return true;
        } else if (id == R.id.users_mngmt) {
            navigateTo(UsersManagementActivity.class);
            return true;
        } else if (id == R.id.quizzes_mngmt) {
            navigateTo(QuizzesManagementActivity.class);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void navigateTo(Class<?> activityClass) {
        Intent intent = new Intent(QuestionsManagementActivity.this, activityClass);
        intent.putExtra("response", Objects.requireNonNull(getIntent().getStringExtra("response")));
        startActivity(intent);
    }

    private void logoutUser() {
        Intent logoutIntent = new Intent(QuestionsManagementActivity.this, LoginActivity.class);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logoutIntent);
        finish();
    }
}