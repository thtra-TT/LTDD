package com.example.vntravelapp;

import android.os.Bundle;
import android.util.Patterns;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

import com.example.vntravelapp.database.DatabaseHelper;

public class RegisterActivity extends AppCompatActivity {

    private EditText edtFullName, edtEmail, edtPhone, edtPassword, edtConfirmPassword;
    private CheckBox cbPolicy;
    private Button btnRegister;
    private TextView tvLogin;
    private ImageView ivBack;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);
        initViews();

        // 🔙 Quay lại
        ivBack.setOnClickListener(v -> finish());
        tvLogin.setOnClickListener(v -> finish());

        // 📝 Đăng ký
        btnRegister.setOnClickListener(v -> performRegistration());
    }

    private void initViews() {
        edtFullName = findViewById(R.id.edtFullName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        edtConfirmPassword = findViewById(R.id.edtConfirmPassword);
        cbPolicy = findViewById(R.id.cbPolicy);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin = findViewById(R.id.tvLogin);   // FIX đúng ID
        ivBack = findViewById(R.id.ivBack);     // FIX đúng ID
    }

    private void performRegistration() {
        String fullName = edtFullName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String pass = edtPassword.getText().toString();
        String confirmPass = edtConfirmPassword.getText().toString();

        // 🔴 Validate
        if (fullName.isEmpty() || email.isEmpty() || pass.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ các trường", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
            return;
        }

        if (pass.length() < 6) {
            edtPassword.setError("Mật khẩu tối thiểu 6 ký tự");
            return;
        }

        if (!pass.equals(confirmPass)) {
            edtConfirmPassword.setError("Mật khẩu không khớp");
            return;
        }

        if (!cbPolicy.isChecked()) {
            Toast.makeText(this, "Bạn phải đồng ý với điều khoản", Toast.LENGTH_SHORT).show();
            return;
        }

        // 🔍 Check email tồn tại
        if (dbHelper.checkEmailExists(email)) {
            edtEmail.setError("Email đã tồn tại");
            edtEmail.requestFocus();
            return;
        }

        // 💾 Insert DB
        boolean success = dbHelper.registerUser(email, pass, fullName, phone);

        if (success) {
            Toast.makeText(this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
            finish(); // quay về login
        } else {
            Toast.makeText(this, "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
        }
    }
}