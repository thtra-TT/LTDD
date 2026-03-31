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
    private RadioGroup rgRole;
    private RadioButton rbBuyer, rbSeller;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        dbHelper = new DatabaseHelper(this);
        initViews();

        ivBack.setOnClickListener(v -> finish());
        tvLogin.setOnClickListener(v -> finish());
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
        tvLogin = findViewById(R.id.tvLogin);
        ivBack = findViewById(R.id.ivBack);

        rgRole = findViewById(R.id.rgRole);
        rbBuyer = findViewById(R.id.rbBuyer);
        rbSeller = findViewById(R.id.rbSeller);
    }

    private void performRegistration() {
        String fullName = edtFullName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim().replace(" ", "");
        String pass = edtPassword.getText().toString().trim();
        String confirmPass = edtConfirmPassword.getText().toString().trim();

        String role = rbSeller.isChecked() ? "SELLER" : "BUYER";

        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
            return;
        }

        if (!phone.matches("^0\\d{9}$")) {
            edtPhone.setError("Số điện thoại phải có 10 số và bắt đầu bằng số 0");
            edtPhone.requestFocus();
            return;
        }

        if (pass.length() < 6) {
            edtPassword.setError("Mật khẩu phải từ 6 ký tự trở lên");
            edtPassword.requestFocus();
            return;
        }

        if (!pass.equals(confirmPass)) {
            edtConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            edtConfirmPassword.requestFocus();
            return;
        }

        if (!cbPolicy.isChecked()) {
            Toast.makeText(this, "Vui lòng đồng ý với điều khoản sử dụng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (dbHelper.checkEmailExists(email)) {
            edtEmail.setError("Email này đã được đăng ký!");
            edtEmail.requestFocus();
            return;
        }

        boolean success = dbHelper.registerUser(email, pass, fullName, phone, role);

        if (success) {
            Toast.makeText(this, "Đăng ký thành công với vai trò: " + (role.equals("SELLER") ? "Người bán" : "Người mua"), Toast.LENGTH_LONG).show();
            finish();
        } else {
            Toast.makeText(this, "Lỗi hệ thống! Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
        }
    }
}