package com.example.vntravelapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.vntravelapp.R;
import com.example.vntravelapp.database.DatabaseHelper;
import com.example.vntravelapp.models.Tour;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BookingFragment extends Fragment {

    private int countAdult = 0;
    private int countChild = 0;

    private TextView edtAdult, edtChild;
    private EditText edtName, edtPhone, edtEmail;
    private TextView tvTotal, tvTotalInline, tvPriceDetail;
    private Button btnConfirm;

    private String title, priceStr, location, imageUrl;
    private int imageRes;
    private double priceAdult;
    private String startDate, endDate;
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
        args.putString("startDate", startDate);
        args.putString("endDate", endDate);
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
            startDate = getArguments().getString("startDate");
            endDate = getArguments().getString("endDate");
            priceAdult = parsePrice(priceStr);
        }
        // Gán ngày mặc định là ngày hiện tại vì phần chọn ngày đã bị xoá
        selectedDateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        // Ánh xạ View
        edtAdult      = view.findViewById(R.id.edtAdult);
        edtChild      = view.findViewById(R.id.edtChild);
        edtName       = view.findViewById(R.id.edtName);
        edtPhone      = view.findViewById(R.id.edtPhone);
        edtEmail      = view.findViewById(R.id.edtEmail);
        tvTotal       = view.findViewById(R.id.tvTotal);
        tvTotalInline = view.findViewById(R.id.tvTotalInline);
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

        // Hiển thị dữ liệu
        tvBTitle.setText(title);
        tvBTitleCard.setText(title);
        tvBLocation.setText("📍 " + location);
        tvBPrice.setText(format(priceAdult) + " / người");

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl).placeholder(android.R.drawable.ic_menu_gallery).centerCrop().into(ivImage);
        } else if (imageRes != 0) {
            ivImage.setImageResource(imageRes);
        }

        // Sự kiện Click
        ivBack.setOnClickListener(v -> {
            if (getActivity() != null) getActivity().getSupportFragmentManager().popBackStack();
        });

        btnAdultMinus.setOnClickListener(v -> { if (countAdult > 0) { countAdult--; edtAdult.setText(String.valueOf(countAdult)); calculatePrice(); } });
        btnAdultPlus.setOnClickListener(v -> { countAdult++; edtAdult.setText(String.valueOf(countAdult)); calculatePrice(); });
        btnChildMinus.setOnClickListener(v -> { if (countChild > 0) { countChild--; edtChild.setText(String.valueOf(countChild)); calculatePrice(); } });
        btnChildPlus.setOnClickListener(v -> { countChild++; edtChild.setText(String.valueOf(countChild)); calculatePrice(); });

        calculatePrice();
        btnConfirm.setOnClickListener(v -> handleBooking());

        return view;
    }

    private void calculatePrice() {
        double childPrice = priceAdult * 0.7;
        double total = (countAdult * priceAdult) + (countChild * childPrice);
        tvPriceDetail.setText("Người lớn (" + countAdult + " x " + format(priceAdult) + ")\nTrẻ em (" + countChild + " x " + format(childPrice) + ")");
        tvTotal.setText(format(total));
        tvTotalInline.setText(format(total));
    }

    private String format(double p) { return String.format("%,.0fđ", p); }
    private double parsePrice(String s) { try { return Double.parseDouble(s.replace(".", "").replace("đ", "")); } catch (Exception e) { return 0; } }

    private void handleBooking() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || countAdult == 0) {
            Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin và ít nhất 1 người lớn!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper db = new DatabaseHelper(getContext());
        boolean success = db.insertOrder(title, selectedDateStr, countAdult + countChild, name, phone);

        if (success) {
            Toast.makeText(getContext(), "Đặt tour thành công!", Toast.LENGTH_SHORT).show();
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
