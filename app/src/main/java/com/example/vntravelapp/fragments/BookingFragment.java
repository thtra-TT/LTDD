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

public class BookingFragment extends Fragment {

    // Số khách lưu bằng biến int, không cần EditText nữa
    private int countAdult = 0;
    private int countChild = 0;

    // Views
    private TextView edtAdult, edtChild;   // giờ là TextView hiển thị số
    private EditText edtName, edtPhone, edtEmail;
    private TextView tvTotal, tvTotalInline, tvPriceDetail;
    private Button btnConfirm;

    private String title, priceStr, location, imageUrl;
    private int imageRes;
    private double priceAdult;

    // ─── newInstance ──────────────────────────────────────────────────────────
    public static BookingFragment newInstance(String title, String price, String location,
                                              String imageUrl, int imageRes) {
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

    // Overload tương thích code cũ
    public static BookingFragment newInstance(String title, String price, String location) {
        return newInstance(title, price, location, null, 0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title    = getArguments().getString("title");
        priceStr = getArguments().getString("price");
        location = getArguments().getString("location");
        imageUrl = getArguments().getString("imageUrl");
        imageRes = getArguments().getInt("imageRes", 0);
        priceAdult = parsePrice(priceStr);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_booking, container, false);

        // ── Ánh xạ views ─────────────────────────────────────────────────────
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

        ImageView ivImage     = view.findViewById(R.id.ivBookingImage);
        TextView  tvBTitle    = view.findViewById(R.id.tvBookingTitle);
        TextView  tvBTitleCard= view.findViewById(R.id.tvBookingTitleCard);
        TextView  tvBLocation = view.findViewById(R.id.tvBookingLocation);
        TextView  tvBPrice    = view.findViewById(R.id.tvBookingPrice);

        // ── Header info ───────────────────────────────────────────────────────
        tvBTitle.setText(title);
        tvBTitleCard.setText(title);
        tvBLocation.setText("📍 " + location);
        tvBPrice.setText(format(priceAdult) + " / người");

        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(this).load(imageUrl)
                    .placeholder(android.R.drawable.ic_menu_gallery)
                    .centerCrop().into(ivImage);
        } else if (imageRes != 0) {
            ivImage.setImageResource(imageRes);
        }

        // ── Stepper người lớn ─────────────────────────────────────────────────
        btnAdultMinus.setOnClickListener(v -> {
            if (countAdult > 0) {
                countAdult--;
                edtAdult.setText(String.valueOf(countAdult));
                calculatePrice();
            }
        });
        btnAdultPlus.setOnClickListener(v -> {
            countAdult++;
            edtAdult.setText(String.valueOf(countAdult));
            calculatePrice();
        });

        // ── Stepper trẻ em ────────────────────────────────────────────────────
        btnChildMinus.setOnClickListener(v -> {
            if (countChild > 0) {
                countChild--;
                edtChild.setText(String.valueOf(countChild));
                calculatePrice();
            }
        });
        btnChildPlus.setOnClickListener(v -> {
            countChild++;
            edtChild.setText(String.valueOf(countChild));
            calculatePrice();
        });

        // ── Hiển thị giá ban đầu ─────────────────────────────────────────────
        calculatePrice();

        // ── Confirm ───────────────────────────────────────────────────────────
        btnConfirm.setOnClickListener(v -> handleBooking());

        return view;
    }

    // ─── Tính giá ─────────────────────────────────────────────────────────────
    private void calculatePrice() {
        double childPrice = priceAdult * 0.7;
        double total = (countAdult * priceAdult) + (countChild * childPrice);

        tvPriceDetail.setText(
                "Người lớn (" + countAdult + " x " + format(priceAdult) + ")\n" +
                        "Trẻ em    (" + countChild + " x " + format(childPrice) + ")"
        );

        String totalStr = format(total);
        tvTotal.setText(totalStr);
        tvTotalInline.setText(totalStr);
    }

    private String format(double price) {
        return String.format("%,.0fđ", price);
    }

    private double parsePrice(String s) {
        try {
            return Double.parseDouble(s.replace(".", "").replace("đ", ""));
        } catch (Exception e) { return 0; }
    }

    // ─── Xử lý đặt tour ──────────────────────────────────────────────────────
    private void handleBooking() {
        String name  = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty() || email.isEmpty() || countAdult == 0) {
            Toast.makeText(getContext(), "Nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper db = new DatabaseHelper(getContext());
        boolean success = db.insertOrder(title, "2026-03-20",
                countAdult + countChild, phone, email);

        if (success) {
            Toast.makeText(getContext(), "Đặt thành công!", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, new TripFragment())
                    .addToBackStack(null)
                    .commit();
        }
    }
}