package com.example.vntravelapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.Tour;

public class AddEditTourActivity extends AppCompatActivity {

    private EditText edtTitle, edtLocation, edtDuration, edtPrice, edtDescription,
            edtItinerary, edtIncluded, edtExcluded, edtImageUrl, edtStartDate, edtEndDate;
    private Button btnSaveTour;
    private ImageView ivBack;
    private TextView tvFormTitle;

    private DatabaseHelper dbHelper;
    private String mode = "add";
    private int tourId = -1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_tour);

        dbHelper = new DatabaseHelper(this);

        initViews();
        readIntentData();
        setupHeader();
        setupActions();

        if ("edit".equals(mode) && tourId != -1) {
            loadTourData();
        }
    }

    private void initViews() {
        edtTitle = findViewById(R.id.edtTourTitle);
        edtLocation = findViewById(R.id.edtTourLocation);
        edtDuration = findViewById(R.id.edtTourDuration);
        edtPrice = findViewById(R.id.edtTourPrice);
        edtDescription = findViewById(R.id.edtTourDescription);
        edtItinerary = findViewById(R.id.edtTourItinerary);
        edtIncluded = findViewById(R.id.edtTourIncluded);
        edtExcluded = findViewById(R.id.edtTourExcluded);
        edtImageUrl = findViewById(R.id.edtTourImageUrl);
        edtStartDate = findViewById(R.id.edtTourStartDate);
        edtEndDate = findViewById(R.id.edtTourEndDate);
        btnSaveTour = findViewById(R.id.btnSaveTour);

        ivBack = findViewById(R.id.ivBack);
        tvFormTitle = findViewById(R.id.tvFormTitle);
    }

    private void readIntentData() {
        String incomingMode = getIntent().getStringExtra("mode");
        if (incomingMode != null && !incomingMode.trim().isEmpty()) {
            mode = incomingMode;
        }
        tourId = getIntent().getIntExtra("tour_id", -1);
    }

    private void setupHeader() {
        if ("edit".equals(mode)) {
            tvFormTitle.setText("Chỉnh sửa tour");
            btnSaveTour.setText("Cập nhật tour");
        } else {
            tvFormTitle.setText("Thêm tour mới");
            btnSaveTour.setText("Lưu tour");
        }
    }

    private void setupActions() {
        ivBack.setOnClickListener(v -> finish());
        btnSaveTour.setOnClickListener(v -> saveTour());
    }

    private void loadTourData() {
        Tour tour = dbHelper.getTourById(tourId);
        if (tour == null) {
            Toast.makeText(this, "Không tìm thấy dữ liệu tour", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        edtTitle.setText(tour.getTitle());
        edtLocation.setText(tour.getLocation());
        edtDuration.setText(tour.getDuration());
        edtPrice.setText(tour.getPrice());
        edtDescription.setText(tour.getDescription());
        edtItinerary.setText(tour.getItinerary());
        edtIncluded.setText(tour.getIncluded());
        edtExcluded.setText(tour.getExcluded());
        edtImageUrl.setText(tour.getPrimaryImageUrl());
        edtStartDate.setText(tour.getStartDate());
        edtEndDate.setText(tour.getEndDate());
    }

    private void saveTour() {
        String title = edtTitle.getText().toString().trim();
        String location = edtLocation.getText().toString().trim();
        String duration = edtDuration.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String itinerary = edtItinerary.getText().toString().trim();
        String included = edtIncluded.getText().toString().trim();
        String excluded = edtExcluded.getText().toString().trim();
        String imageUrl = edtImageUrl.getText().toString().trim();
        String startDate = edtStartDate.getText().toString().trim();
        String endDate = edtEndDate.getText().toString().trim();

        if (TextUtils.isEmpty(title)) {
            edtTitle.setError("Nhập tên tour");
            edtTitle.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(location)) {
            edtLocation.setError("Nhập địa điểm");
            edtLocation.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(price)) {
            edtPrice.setError("Nhập giá tour");
            edtPrice.requestFocus();
            return;
        }

        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String sellerEmail = pref.getString("saved_email", "");

        boolean success;
        if ("edit".equals(mode)) {
            success = dbHelper.updateSellerTour(
                    tourId,
                    title,
                    location,
                    duration,
                    price,
                    description,
                    itinerary,
                    included,
                    excluded,
                    imageUrl,
                    startDate,
                    endDate
            );
        } else {
            success = dbHelper.insertSellerTour(
                    title,
                    location,
                    duration,
                    price,
                    description,
                    itinerary,
                    included,
                    excluded,
                    imageUrl,
                    startDate,
                    endDate,
                    sellerEmail
            );
        }

        Toast.makeText(
                this,
                success
                        ? ("edit".equals(mode) ? "Cập nhật tour thành công" : "Thêm tour thành công")
                        : ("edit".equals(mode) ? "Cập nhật tour thất bại" : "Thêm tour thất bại"),
                Toast.LENGTH_SHORT
        ).show();

        if (success) {
            finish();
        }
    }
}