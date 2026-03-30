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
        tvLogin = findViewById(R.id.tvLogin);
        ivBack = findViewById(R.id.ivBack);
    }

    private void performRegistration() {
        String fullName = edtFullName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        // Loại bỏ khoảng trắng trong số điện thoại nếu có
        String phone = edtPhone.getText().toString().trim().replace(" ", "");
        String pass = edtPassword.getText().toString().trim();
        String confirmPass = edtConfirmPassword.getText().toString().trim();

        // 1. Kiểm tra trống
        if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Kiểm tra định dạng Email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
            return;
        }

        // 3. Kiểm tra Số điện thoại (Định dạng Việt Nam: 10 số, bắt đầu bằng 0)
        if (!phone.matches("^0\\d{9}$")) {
            edtPhone.setError("Số điện thoại phải có 10 số và bắt đầu bằng số 0");
            edtPhone.requestFocus();
            return;
        }

        // 4. Kiểm tra độ dài mật khẩu (Cập nhật về 6 theo yêu cầu)
        if (pass.length() < 6) {
            edtPassword.setError("Mật khẩu phải từ 6 ký tự trở lên");
            edtPassword.requestFocus();
            return;
        }

        // 5. Kiểm tra mật khẩu khớp nhau
        if (!pass.equals(confirmPass)) {
            edtConfirmPassword.setError("Mật khẩu xác nhận không khớp");
            edtConfirmPassword.requestFocus();
            return;
        }

        // 6. Kiểm tra đồng ý điều khoản
        if (!cbPolicy.isChecked()) {
            Toast.makeText(this, "Vui lòng đồng ý với điều khoản sử dụng", Toast.LENGTH_SHORT).show();
            return;
        }

        // 7. Kiểm tra Email đã tồn tại chưa
        if (dbHelper.checkEmailExists(email)) {
            edtEmail.setError("Email này đã được đăng ký!");
            edtEmail.requestFocus();
            return;
        }

        // 8. Thực hiện lưu vào Database
        boolean success = dbHelper.registerUser(email, pass, fullName, phone);

        if (success) {
            Toast.makeText(this, "Chúc mừng! Bạn đã đăng ký thành công", Toast.LENGTH_LONG).show();
            finish(); 
        } else {
            Toast.makeText(this, "Lỗi hệ thống! Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
        }
    }
}
