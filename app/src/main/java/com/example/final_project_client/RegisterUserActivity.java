package com.example.final_project_client;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText, confirmPasswordEditText;
    private Button registerButton, returnButton;
    private ApiHelper apiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register_user);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailEditText = findViewById(R.id.editTextEmailRegister);
        passwordEditText = findViewById(R.id.editTextPasswordRegister);
        confirmPasswordEditText = findViewById(R.id.editTextConfirmPasswordRegister);
        registerButton = findViewById(R.id.buttonRegisterUser);
        returnButton = findViewById(R.id.buttonReturnToLogin);
        apiHelper = new ApiHelper(this);

        registerButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();

            if (!email.isEmpty() && !password.isEmpty() && !confirmPassword.isEmpty()) {
                if (password.equals(confirmPassword)) {
                    apiHelper.registerUser(
                            email,
                            password,
                            response -> {
                                Toast.makeText(RegisterUserActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                finish();
                            },
                            error -> Toast.makeText(RegisterUserActivity.this, "Error registering user", Toast.LENGTH_SHORT).show()
                    );
                } else {
                    Toast.makeText(RegisterUserActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RegisterUserActivity.this, "Email, password, and confirm password cannot be empty", Toast.LENGTH_SHORT).show();
            }
        });

        returnButton.setOnClickListener(v -> {
            finish();
        });


    }
}