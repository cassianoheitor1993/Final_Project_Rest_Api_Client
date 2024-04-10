package com.example.final_project_client;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import android.app.ProgressDialog;
import android.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {
    private EditText editTextName, editTextEmail, editTextPassword,
            editTextConfirmPassword, editTextFullName, editTextAddress,
            editTextPhoneNumber, editTextBio;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_CAMERA_PERMISSION = 100;
    private ImageView imageViewProfile;
    private Uri imageUri;
    private LinearLayout buttonSelectPicture, buttonUpdateProfile, buttonLogout;
    private ApiHelper apiHelper;
    private User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);

        Drawable customOverflowIcon = ContextCompat.getDrawable(this, R.drawable.custom_overflow_icon);
        toolbar.setOverflowIcon(customOverflowIcon);

        imageViewProfile = findViewById(R.id.imageViewProfile);
        buttonSelectPicture = findViewById(R.id.buttonSelectPicture);
        buttonUpdateProfile = findViewById(R.id.buttonSaveUserProfile);
        buttonLogout = findViewById(R.id.buttonLogout);

        editTextName = findViewById(R.id.editTextName);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        editTextFullName = findViewById(R.id.editTextFullName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextBio = findViewById(R.id.editTextBio);

        // Initialize apiHelper
        apiHelper = new ApiHelper(this);

        // Get the user object from the intent and update the user object
        Intent intent = getIntent();
        if (intent != null) {
            String userDataString = intent.getStringExtra("response");
            try {
                assert userDataString != null;
                JSONObject userData = new JSONObject(userDataString);
                user.setId(userData.getInt("id"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        fetchUserDataFromServer();

        if (user.getImage() != null && !user.getImage().isEmpty()) {
            Uri imageUri = Uri.parse(user.getImage());
            Glide.with(this)
                    .load(imageUri)
                    .placeholder(R.drawable.default_profile_image)
                    .error(R.drawable.default_profile_image)
                    .into(imageViewProfile);
        } else {
            imageViewProfile.setImageResource(R.drawable.default_profile_image);
        }

        buttonSelectPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkPermission(Manifest.permission.CAMERA)) {
                    openCamera();
                } else {
                    requestPermission(Manifest.permission.CAMERA, REQUEST_CAMERA_PERMISSION);
                }
            }
        });

        buttonUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserProfile(user);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });
    }

    private void fetchUserDataFromServer() {
        // Show a progress dialog
        ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Fetching user data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Get the current time in milliseconds
        long startTime = System.currentTimeMillis();

        apiHelper.getUser(user.getId(),
                response -> {
                    try {
                        long timeTaken = System.currentTimeMillis() - startTime;
                        if (timeTaken < 1000) {
                            new Handler().postDelayed(() -> {
                                progressDialog.dismiss();
                                updateUserAndDisplay(response);
                            }, 1000 - timeTaken);
                        } else {
                            progressDialog.dismiss();
                            updateUserAndDisplay(response);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ProfileActivity.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });
    }

    private void updateUserAndDisplay(JSONObject userData) {
        try {
            user.setId(userData.getInt("id"));
            user.setUsername(userData.getString("username"));
            user.setEmail(userData.getString("email"));
            user.setPassword(userData.getString("password"));
            user.setRole(userData.getString("role"));
            user.setToken(userData.getString("token"));
            user.setRefreshToken(userData.getString("refreshToken"));
            user.setTokenExpires(new Date(System.currentTimeMillis() + 31556952000L));
            user.setRefreshTokenExpires(new Date(System.currentTimeMillis() + 31556952000L));
            user.setFullName(userData.getString("fullName"));
            user.setAddress(userData.getString("address"));
            user.setEmailAddress(userData.getString("emailAddress"));
            user.setPhoneNumber(userData.getString("phoneNumber"));
            user.setImage(userData.getString("image"));
            user.setBio(userData.getString("bio"));

            // Update EditText fields and ImageView with fetched user data
            editTextName.setText(user.getUsername());
            editTextFullName.setText(user.getFullName());
            editTextEmail.setText(user.getEmail());
            editTextFullName.setText(user.getFullName());
            editTextAddress.setText(user.getAddress());
            editTextPhoneNumber.setText(user.getPhoneNumber());
            editTextBio.setText(user.getBio());

            if (user.getImage() != null && !user.getImage().isEmpty()) {
                Uri imageUri = Uri.parse(user.getImage());
                Glide.with(this)
                        .load(imageUri)
                        .placeholder(R.drawable.default_profile_image)
                        .error(R.drawable.default_profile_image)
                        .into(imageViewProfile);
            } else {
                imageViewProfile.setImageResource(R.drawable.default_profile_image);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(ProfileActivity.this, "Error parsing user data", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPermission(String permission) {
        int result = ContextCompat.checkSelfPermission(this, permission);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(String permission, int requestCode) {
        ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
                Log.e("CameraError", "Error creating image file", ex);
            }
            if (photoFile != null) {
                imageUri = FileProvider.getUriForFile(this, "com.example.final_project_client.fileprovider", photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                try {
                    startActivityForResult(cameraIntent, PICK_IMAGE_REQUEST);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("CameraError", "Error starting camera intent", e);
                }
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = null;
        try {
            imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
        } catch (IOException ex) {
            ex.printStackTrace();
            Log.e("CameraError", "Error creating image file", ex);
        }
        if (imageFile == null) {
            throw new IOException("Failed to create image file");
        }
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (imageUri != null) {
                // Load image using Glide
                Glide.with(this)
                        .load(imageUri)
                        .into(imageViewProfile);
                saveImageToDatabase(imageUri);
            } else {
                Toast.makeText(this, "Failed to get image URI", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveImageToDatabase(Uri imageUri) {
        if (imageUri != null) {
            String imagePath = imageUri.toString();
            user.setImage(imagePath);

            // Load image using Glide
            Glide.with(this)
                    .load(imageUri)
                    .into(imageViewProfile);
        } else {
            Toast.makeText(this, "Failed to get image URI", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateUserInput() {
        String name = editTextName.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        String fullName = editTextFullName.getText().toString();
        String address = editTextAddress.getText().toString();
        String phoneNumber = editTextPhoneNumber.getText().toString();
        String bio = editTextBio.getText().toString();

        // Regular expression for email validation
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        // Regular expression for phone number validation
        String phonePattern = "^[0-9]{10}$";

        if (name.isEmpty()) {
            editTextName.setError("Name is required");
            editTextName.requestFocus();
            return false;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return false;
        }

        if (!email.matches(emailPattern)) {
            editTextEmail.setError("Invalid email format");
            editTextEmail.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return false;
        }

        if (confirmPassword.isEmpty()) {
            editTextConfirmPassword.setError("Confirm password is required");
            editTextConfirmPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            editTextConfirmPassword.requestFocus();
            return false;
        }

        if (fullName.isEmpty()) {
            editTextFullName.setError("Full name is required");
            editTextFullName.requestFocus();
            return false;
        }

        if (address.isEmpty()) {
            editTextAddress.setError("Address is required");
            editTextAddress.requestFocus();
            return false;
        }

        if (phoneNumber.isEmpty()) {
            editTextPhoneNumber.setError("Phone number is required");
            editTextPhoneNumber.requestFocus();
            return false;
        }

        if (!phoneNumber.matches(phonePattern)) {
            editTextPhoneNumber.setError("Invalid phone number format");
            editTextPhoneNumber.requestFocus();
            return false;
        }

        if (bio.isEmpty()) {
            editTextBio.setError("Bio is required");
            editTextBio.requestFocus();
            return false;
        }
        return true;
    }

    private void updateUserProfile(User user) {
        if (!validateUserInput()) {
            return;
        }
        // Show a progress dialog
        ProgressDialog progressDialog = new ProgressDialog(ProfileActivity.this);
        progressDialog.setMessage("Updating profile...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        user.setFullName(editTextFullName.getText().toString());
        user.setAddress(editTextAddress.getText().toString());
        user.setPhoneNumber(editTextPhoneNumber.getText().toString());
        user.setBio(editTextBio.getText().toString());
        user.setPassword(editTextPassword.getText().toString());
        user.setEmail(editTextEmail.getText().toString());
        user.setUsername(editTextName.getText().toString());

        // Get the current time in milliseconds
        long startTime = System.currentTimeMillis();

        apiHelper.updateUser(user,
                response -> {
                    try {
                        long timeTaken = System.currentTimeMillis() - startTime;
                        if (timeTaken < 1000) {
                            new Handler().postDelayed(() -> {
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                                builder.setTitle("Success");
                                builder.setMessage("Profile updated successfully");
                                builder.setPositiveButton("OK", (dialog, which) -> {
                                    dialog.dismiss();
                                });
                                builder.show();
                            }, 1000 - timeTaken);
                        } else {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
                            builder.setTitle("Success");
                            builder.setMessage("Profile updated successfully");
                            builder.setPositiveButton("OK", (dialog, which) -> {
                                dialog.dismiss();
                            });
                            builder.show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(ProfileActivity.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(ProfileActivity.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                });
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
        Intent intent = new Intent(ProfileActivity.this, activityClass);
        intent.putExtra("response", Objects.requireNonNull(getIntent().getStringExtra("response")));
        startActivity(intent);
    }

    private void logoutUser() {
        Intent logoutIntent = new Intent(ProfileActivity.this, LoginActivity.class);
        logoutIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(logoutIntent);
        finish();
    }
}
