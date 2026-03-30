package com.example.vntravelapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vntravelapp.database.DatabaseHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "GoogleSignIn";

    private EditText edtEmail, edtPassword;
    private Button btnLogin, btnGoogle;
    private TextView tvRegister, tvForgotPassword;
    private DatabaseHelper dbHelper;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbHelper = new DatabaseHelper(this);
        initViews();
        setupGoogleSignIn();

        // Kiểm tra nếu đã đăng nhập rồi thì vào thẳng Home
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        if (pref.getBoolean("is_logged_in", false)) {
            navigateToHome(pref.getString("saved_email", ""), 
                           pref.getString("saved_username", ""), 
                           pref.getString("saved_user_image", ""));
            return;
        }

        tvRegister.setOnClickListener(v -> startActivity(new Intent(this, RegisterActivity.class)));
        tvForgotPassword.setOnClickListener(v -> Toast.makeText(this, "Chức năng đang phát triển", Toast.LENGTH_SHORT).show());
        btnLogin.setOnClickListener(v -> handleLogin());
        btnGoogle.setOnClickListener(v -> signInWithGoogle());
    }

    private void initViews() {
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnGoogle = findViewById(R.id.btnGoogle);
        tvRegister = findViewById(R.id.tvRegister);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);
    }

    private void setupGoogleSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            if (account != null) {
                String email = account.getEmail();
                String name = account.getDisplayName();
                String photoUrl = account.getPhotoUrl() != null ? account.getPhotoUrl().toString() : "";

                // Tự động tạo tài khoản trong DB nếu chưa tồn tại (để đồng nhất dữ liệu)
                if (!dbHelper.checkEmailExists(email)) {
                    dbHelper.registerUser(email, "google_auth_no_password", name, "");
                }

                saveUserToPrefs(email, name, photoUrl);
                Toast.makeText(this, "Chào mừng " + name, Toast.LENGTH_SHORT).show();
                navigateToHome(email, name, photoUrl);
            }
        } catch (ApiException e) {
            // Log mã lỗi để bạn dễ debug (Lỗi 10 là thiếu SHA-1, 12500 là lỗi cấu hình)
            Log.e(TAG, "Lỗi đăng nhập Google. Mã lỗi: " + e.getStatusCode());
            Toast.makeText(this, "Đăng nhập Google thất bại (Mã: " + e.getStatusCode() + ")", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleLogin() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
            return;
        }

        Cursor cursor = dbHelper.loginUser(email, password);
        if (cursor != null && cursor.moveToFirst()) {
            String fullname = cursor.getString(cursor.getColumnIndexOrThrow("fullname"));
            String userImage = cursor.getString(cursor.getColumnIndexOrThrow("user_image"));
            cursor.close();

            saveUserToPrefs(email, fullname, userImage);
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            navigateToHome(email, fullname, userImage);
        } else {
            if (cursor != null) cursor.close();
            Toast.makeText(this, "Sai email hoặc mật khẩu", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveUserToPrefs(String email, String name, String image) {
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("is_logged_in", true);
        editor.putString("saved_email", email);
        editor.putString("saved_username", name);
        editor.putString("saved_user_image", image);
        editor.apply();
    }

    private void navigateToHome(String email, String name, String image) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("USER_EMAIL", email);
        intent.putExtra("USER_NAME", name);
        intent.putExtra("USER_IMAGE", image);
        startActivity(intent);
        finish();
    }
}

