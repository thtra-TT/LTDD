package com.example.vntravelapp.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.vntravelapp.R;
import com.example.vntravelapp.database.DatabaseHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class BookingFragment extends Fragment {

    private int countAdult = 0;
    private int countChild = 0;

    private TextView edtAdult, edtChild, tvSelectedDate;
    private EditText edtName, edtPhone, edtEmail;
    private TextView tvTotal, tvPriceDetail;
    private Button btnConfirm;
    private LinearLayout layoutSelectDate;

    private String title, priceStr, location, imageUrl;
    private int imageRes;
    private double priceAdult;
    private String selectedDateStr = "";

    public static BookingFragment newInstance(String title, String price, String location,
                                              String imageUrl, int imageRes, String startDate, String endDate) {
        BookingFragment f = new BookingFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("price", price);
        args.putString("location", location);
        args.putString("imageUrl", imageUrl);
        args.putInt("imageRes", imageRes);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString("title");
            priceStr = getArguments().getString("price");
            location = getArguments().getString("location");
            imageUrl = getArguments().getString("imageUrl");
            imageRes = getArguments().getInt("imageRes", 0);
            priceAdult = parsePrice(priceStr);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        // Ánh xạ View
        tvSelectedDate = view.findViewById(R.id.tvSelectedDate);
        layoutSelectDate = view.findViewById(R.id.layoutSelectDate);
        edtAdult      = view.findViewById(R.id.edtAdult);
        edtChild      = view.findViewById(R.id.edtChild);
        edtName       = view.findViewById(R.id.edtName);
        edtPhone      = view.findViewById(R.id.edtPhone);
        edtEmail      = view.findViewById(R.id.edtEmail);
        tvTotal       = view.findViewById(R.id.tvTotal);
        tvPriceDetail = view.findViewById(R.id.tvPriceDetail);
        btnConfirm    = view.findViewById(R.id.btnConfirm);

        TextView btnAdultMinus = view.findViewById(R.id.btnAdultMinus);
        TextView btnAdultPlus  = view.findViewById(R.id.btnAdultPlus);
        TextView btnChildMinus = view.findViewById(R.id.btnChildMinus);
        TextView btnChildPlus  = view.findViewById(R.id.btnChildPlus);
        ImageView ivBack       = view.findViewById(R.id.ivBack);

        ImageView ivImage     = view.findViewById(R.id.ivBookingImage);
        TextView  tvBTitle    = view.findViewById(R.id.tvBookingTitle);
        TextView  tvBTitleCard= view.findViewById(R.id.tvBookingTitleCard);
        TextView  tvBLocation = view.findViewById(R.id.tvBookingLocation);
        TextView  tvBPrice    = view.findViewById(R.id.tvBookingPrice);

        // Hiển thị dữ liệu cơ bản
        tvBTitle.setText(title);
        tvBTitleCard.setText(title);
        tvBLocation.setText("📍 " + location);
        tvBPrice.setText(format(priceAdult) + " / người");

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).placeholder(android.R.drawable.ic_menu_gallery).centerCrop().into(ivImage);
        } else if (imageRes != 0) {
            ivImage.setImageResource(imageRes);
        }

        // 🔙 Quay lại
        ivBack.setOnClickListener(v -> {
            if (getActivity() != null) getActivity().getSupportFragmentManager().popBackStack();
        });

        // 📅 Xử lý chọn ngày
        layoutSelectDate.setOnClickListener(v -> showDatePicker());

        // 👥 Xử lý tăng giảm số lượng
        btnAdultMinus.setOnClickListener(v -> { if (countAdult > 0) { countAdult--; edtAdult.setText(String.valueOf(countAdult)); calculatePrice(); } });
        btnAdultPlus.setOnClickListener(v -> { countAdult++; edtAdult.setText(String.valueOf(countAdult)); calculatePrice(); });
        btnChildMinus.setOnClickListener(v -> { if (countChild > 0) { countChild--; edtChild.setText(String.valueOf(countChild)); calculatePrice(); } });
        btnChildPlus.setOnClickListener(v -> { countChild++; edtChild.setText(String.valueOf(countChild)); calculatePrice(); });

        calculatePrice();
        btnConfirm.setOnClickListener(v -> handleBooking());

        return view;
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year1, monthOfYear, dayOfMonth) -> {
                    selectedDateStr = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year1;
                    tvSelectedDate.setText(selectedDateStr);
                    tvSelectedDate.setTextColor(getResources().getColor(android.R.color.black));
                }, year, month, day);
        
        // Không cho chọn ngày trong quá khứ
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void calculatePrice() {
        double childPrice = priceAdult * 0.7;
        double total = (countAdult * priceAdult) + (countChild * childPrice);
        tvPriceDetail.setText("Người lớn (" + countAdult + " x " + format(priceAdult) + ")\nTrẻ em (" + countChild + " x " + format(childPrice) + ")");
        tvTotal.setText(format(total));
    }

    private String format(double p) { return String.format("%,.0fđ", p); }
    private double parsePrice(String s) { try { return Double.parseDouble(s.replace(".", "").replace("đ", "")); } catch (Exception e) { return 0; } }

    private void handleBooking() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        // 1. Kiểm tra ngày đi
        if (selectedDateStr.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng chọn ngày khởi hành!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Kiểm tra các trường trống
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || countAdult == 0) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin và ít nhất 1 người lớn!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Bắt lỗi Số điện thoại (10 số, bắt đầu bằng 0)
        if (!phone.matches("^0\\d{9}$")) {
            edtPhone.setError("Số điện thoại không hợp lệ (10 số, bắt đầu bằng 0)");
            edtPhone.requestFocus();
            return;
        }

        // 4. Bắt lỗi Email
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không đúng định dạng");
            edtEmail.requestFocus();
            return;
        }

        DatabaseHelper db = new DatabaseHelper(getContext());
        boolean success = db.insertOrder(title, selectedDateStr, countAdult + countChild, name, phone);

        if (success) {
            Toast.makeText(getContext(), "Đặt tour thành công!", Toast.LENGTH_SHORT).show();
            // Chuyển sang màn hình chuyến đi của tôi
            if (getActivity() != null) {
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, new TripFragment())
                        .commit();
            }
        } else {
            Toast.makeText(getContext(), "Có lỗi xảy ra khi lưu đơn hàng!", Toast.LENGTH_SHORT).show();
        }
    }
}
