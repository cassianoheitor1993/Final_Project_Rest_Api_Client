package com.example.final_project_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (isNetworkAvailable()) {
            ApiHelper apiHelper = new ApiHelper(this);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Home");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);

        Drawable customOverflowIcon = ContextCompat.getDrawable(this, R.drawable.custom_overflow_icon);
        toolbar.setOverflowIcon(customOverflowIcon);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fragment_menu, menu);
        boolean isLoggedIn = checkUserLoggedIn();
        if (isLoggedIn) {
            if (userIsAdmin()) {
                menu.findItem(R.id.home_activity).setVisible(true);
                menu.findItem(R.id.profile).setVisible(true);
                menu.findItem(R.id.previous_quizzes).setVisible(true);
                menu.findItem(R.id.questions_mngmt).setVisible(true);
                menu.findItem(R.id.users_mngmt).setVisible(true);
                menu.findItem(R.id.quizzes_mngmt).setVisible(true);
                menu.findItem(R.id.action_login).setVisible(false);
                menu.findItem(R.id.action_logout).setVisible(true);
            } else {
                menu.findItem(R.id.home_activity).setVisible(true);
                menu.findItem(R.id.profile).setVisible(true);
                menu.findItem(R.id.previous_quizzes).setVisible(true);
                menu.findItem(R.id.questions_mngmt).setVisible(false);
                menu.findItem(R.id.users_mngmt).setVisible(false);
                menu.findItem(R.id.quizzes_mngmt).setVisible(false);
                menu.findItem(R.id.action_login).setVisible(false);
                menu.findItem(R.id.action_logout).setVisible(true);
            }
        } else {
            menu.findItem(R.id.home_activity).setVisible(true);
            menu.findItem(R.id.profile).setVisible(false);
            menu.findItem(R.id.previous_quizzes).setVisible(false);
            menu.findItem(R.id.questions_mngmt).setVisible(false);
            menu.findItem(R.id.users_mngmt).setVisible(false);
            menu.findItem(R.id.quizzes_mngmt).setVisible(false);
            menu.findItem(R.id.action_login).setVisible(true);
            menu.findItem(R.id.action_logout).setVisible(false);
        }

        return true;
    }

    private boolean checkUserLoggedIn() {
        Intent intent = getIntent();
        if (intent != null) {
            String userDataString = intent.getStringExtra("response");
            try {
                assert userDataString != null;
                JSONObject userData = new JSONObject(userDataString);
                return userData.has("token");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean userIsAdmin() {
        Intent intent = getIntent();
        if (intent != null) {
            String userDataString = intent.getStringExtra("response");
            try {
                assert userDataString != null;
                JSONObject userData = new JSONObject(userDataString);
                String role = userData.getString("role");
                if ("admin".equals(role) || "teacher".equals(role)) {
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home_activity) {
            Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
            homeIntent.putExtra("response", Objects.requireNonNull(getIntent().getStringExtra("response")));
            startActivity(homeIntent);
            return true;
        }else if (id == R.id.action_login) {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            return true;
        }else if (id == R.id.action_logout) {
            logoutUser();
            return true;
        }else if (id == R.id.profile) {
            Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
            profileIntent.putExtra("response", Objects.requireNonNull(getIntent().getStringExtra("response")));
            startActivity(profileIntent);
            return true;
        }else if (id == R.id.previous_quizzes) {
            Intent previousQuizzesIntent = new Intent(MainActivity.this, PreviousQuizzesActivity.class);
            previousQuizzesIntent.putExtra("response", Objects.requireNonNull(getIntent().getStringExtra("response")));
            startActivity(previousQuizzesIntent);
            return true;
        }else if (id == R.id.questions_mngmt) {
            Intent questionsMngmtIntent = new Intent(MainActivity.this, QuestionsManagementActivity.class);
            questionsMngmtIntent.putExtra("response", Objects.requireNonNull(getIntent().getStringExtra("response")));
            startActivity(questionsMngmtIntent);
            return true;
        }else if (id == R.id.users_mngmt) {
            Intent usersMngmtIntent = new Intent(MainActivity.this, UsersManagementActivity.class);
            usersMngmtIntent.putExtra("response", Objects.requireNonNull(getIntent().getStringExtra("response")));
            startActivity(usersMngmtIntent);
            return true;
        }else if (id == R.id.quizzes_mngmt) {
            Intent quizzesMngmtIntent = new Intent(MainActivity.this, QuizzesManagementActivity.class);
            quizzesMngmtIntent.putExtra("response", Objects.requireNonNull(getIntent().getStringExtra("response")));
            startActivity(quizzesMngmtIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logoutUser() {
        Intent logoutIntent = new Intent(MainActivity.this, LoginActivity.class);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logoutIntent);
        finish();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
