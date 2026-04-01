package com.example.vntravelapp.database;

import android.net.Uri;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.vntravelapp.models.Combo;
import com.example.vntravelapp.models.Hotel;
import com.example.vntravelapp.models.MapItem;
import com.example.vntravelapp.models.Tour;
import com.example.vntravelapp.models.TicketOffer;
import com.example.vntravelapp.models.BusTrip;
import com.example.vntravelapp.models.FavoriteItem;
import com.example.vntravelapp.models.TripItem;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "vntravel.db";
    private static final int DATABASE_VERSION = 56; // Add comments + travel diary tables
    private static final String FALLBACK_TOUR_IMAGE = "https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&fit=crop&w=1400&q=80";

    private static final String TABLE_TOURS = "tours";
    private static final String TABLE_HOTELS = "hotels";
    private static final String TABLE_COMBOS = "combos";
    private static final String TABLE_TICKETS = "tickets";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ORDERS = "orders";
    private static final String TABLE_FAVORITES = "favorites";
    private static final String TABLE_JOURNAL = "journal_entries";
    private static final String TABLE_REVIEWS = "user_reviews";
    private static final String TABLE_NOTIFICATIONS = "notifications";
    private static final String TABLE_CANCELLED_TRIPS = "trip_cancellations";
    private static final String TABLE_BUS_TRIPS = "bus_trips";
    private static final String TABLE_COMMENTS = "comments";
    private static final String TABLE_TRAVEL_DIARY = "travel_diary";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE_RES = "image_res";
    private static final String COLUMN_IMAGE_URL = "image_url";
    private static final String COLUMN_IMAGE_URLS = "image_urls";
    private static final String COLUMN_VIDEO_URL = "video_url";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_REVIEWS = "reviews";
    private static final String COLUMN_BOOK_COUNT = "book_count";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_ORIGINAL_PRICE = "original_price";
    private static final String COLUMN_BADGE = "badge";
    private static final String COLUMN_DATE_RANGE = "date_range";
    private static final String COLUMN_DISCOUNT = "discount";
    private static final String COLUMN_TYPE = "type";

    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_FULLNAME = "fullname";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_USER_IMAGE = "user_image";
    private static final String COLUMN_CREATED_AT = "created_at";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_ITEM_TYPE = "item_type";
    private static final String COLUMN_ITEM_TITLE = "item_title";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_ITINERARY = "itinerary";
    private static final String COLUMN_INCLUDED = "included";
    private static final String COLUMN_EXCLUDED = "excluded";
    private static final String COLUMN_REASON = "reason";

    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_START_DATE = "start_date";
    private static final String COLUMN_END_DATE = "end_date";
    private static final String COLUMN_USER_EMAIL = "user_email";
    private static final String COLUMN_USER_ID = "user_id";
    private static final String COLUMN_TOUR_ID = "tour_id";
    private static final String COLUMN_UPDATED_AT = "updated_at";

    private static final String ROLE_BUYER = "BUYER";
    private static final String ROLE_SELLER = "SELLER";

    private static final String COLUMN_SELLER_EMAIL = "seller_email";
    private static final String COLUMN_DEPARTURE = "departure";
    private static final String COLUMN_DESTINATION = "destination";
    private static final String COLUMN_DEPARTURE_TIME = "departure_time";
    private static final String COLUMN_BUS_COMPANY = "bus_company";
    private static final String COLUMN_AVAILABLE_SEATS = "available_seats";
    private static final String COLUMN_DATE = "date";

    private static final String COLUMN_ROLE = "role";

    private static final String COLUMN_ORDER_STATUS = "order_status";
    private static final Map<String, double[]> LOCATION_COORDINATES = buildLocationCoordinates();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON");
        ensureSeedUsers(db);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TOURS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_DURATION + " TEXT, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_ITINERARY + " TEXT, " +
                COLUMN_INCLUDED + " TEXT, " +
                COLUMN_EXCLUDED + " TEXT, " +
                COLUMN_IMAGE_RES + " INTEGER, " +
                COLUMN_IMAGE_URL + " TEXT, " +
                COLUMN_IMAGE_URLS + " TEXT, " +
                COLUMN_VIDEO_URL + " TEXT, " +
                COLUMN_BADGE + " TEXT, " +
                COLUMN_RATING + " REAL, " +
                COLUMN_REVIEWS + " INTEGER, " +
                COLUMN_BOOK_COUNT + " INTEGER DEFAULT 0, " +
                COLUMN_LATITUDE + " REAL DEFAULT 0, " +
                COLUMN_LONGITUDE + " REAL DEFAULT 0, " +
                COLUMN_START_DATE + " TEXT, " +
                COLUMN_END_DATE + " TEXT, " +
                COLUMN_SELLER_EMAIL + " TEXT, " +
                COLUMN_TYPE + " TEXT DEFAULT 'Tour')");
        db.execSQL("CREATE TABLE " + TABLE_HOTELS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_LOCATION + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_IMAGE_RES + " INTEGER, " +
                COLUMN_IMAGE_URL + " TEXT, " +
                COLUMN_RATING + " REAL, " +
                COLUMN_REVIEWS + " INTEGER, " +
                COLUMN_LATITUDE + " REAL DEFAULT 0, " +
                COLUMN_LONGITUDE + " REAL DEFAULT 0)");
        db.execSQL("CREATE TABLE " + TABLE_COMBOS + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE + " TEXT, " + COLUMN_LOCATION + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_ORIGINAL_PRICE + " TEXT, " + COLUMN_PRICE + " TEXT, " + COLUMN_IMAGE_RES + " INTEGER, " + COLUMN_IMAGE_URL + " TEXT, " + COLUMN_RATING + " REAL, " + COLUMN_BADGE + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_TICKETS + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE + " TEXT, " + COLUMN_DATE_RANGE + " TEXT, " + COLUMN_PRICE + " TEXT, " + COLUMN_DISCOUNT + " TEXT, " + COLUMN_TYPE + " TEXT, " + COLUMN_IMAGE_RES + " INTEGER, " + COLUMN_IMAGE_URL + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_FULLNAME + " TEXT, " +
                COLUMN_PHONE + " TEXT, " +
                COLUMN_USER_IMAGE + " TEXT, " +
                COLUMN_ROLE + " TEXT DEFAULT 'BUYER')");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_FAVORITES + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_ITEM_TYPE + " TEXT, " +
                COLUMN_CREATED_AT + " TEXT, " +
                "UNIQUE(" + COLUMN_TITLE + ", " + COLUMN_ITEM_TYPE + ")) ");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_JOURNAL + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_CONTENT + " TEXT, " +
                COLUMN_IMAGE_URL + " TEXT, " +
                COLUMN_RATING + " REAL, " +
                COLUMN_CREATED_AT + " TEXT) ");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_REVIEWS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEM_TYPE + " TEXT, " +
                COLUMN_ITEM_TITLE + " TEXT, " +
                COLUMN_RATING + " REAL, " +
                COLUMN_CONTENT + " TEXT, " +
                COLUMN_CREATED_AT + " TEXT) ");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_NOTIFICATIONS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_CONTENT + " TEXT, " +
                COLUMN_STATUS + " TEXT, " +
                COLUMN_CREATED_AT + " TEXT) ");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_CANCELLED_TRIPS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DATE_RANGE + " TEXT, " +
                COLUMN_REASON + " TEXT, " +
                COLUMN_USER_EMAIL + " TEXT, " +
                COLUMN_CREATED_AT + " TEXT) ");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_BUS_TRIPS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DEPARTURE + " TEXT, " +
                COLUMN_DESTINATION + " TEXT, " +
                COLUMN_DEPARTURE_TIME + " TEXT, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_BUS_COMPANY + " TEXT, " +
                COLUMN_AVAILABLE_SEATS + " INTEGER, " +
                COLUMN_DATE + " TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ORDERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_DATE + " TEXT, " +
                "people INTEGER, " +
                "name TEXT, " +
                "phone TEXT, " +
                COLUMN_USER_EMAIL + " TEXT, " +
                COLUMN_ORDER_STATUS + " TEXT DEFAULT 'PENDING'" +
                ")");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_COMMENTS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TOUR_ID + " INTEGER, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_RATING + " REAL, " +
                COLUMN_CONTENT + " TEXT, " +
                COLUMN_CREATED_AT + " TEXT, " +
                COLUMN_UPDATED_AT + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_TOUR_ID + ") REFERENCES " + TABLE_TOURS + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_TRAVEL_DIARY + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USER_ID + " INTEGER, " +
                COLUMN_TOUR_ID + " INTEGER, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_CONTENT + " TEXT, " +
                COLUMN_IMAGE_URL + " TEXT, " +
                COLUMN_RATING + " REAL, " +
                COLUMN_CREATED_AT + " TEXT, " +
                COLUMN_UPDATED_AT + " TEXT, " +
                "FOREIGN KEY(" + COLUMN_TOUR_ID + ") REFERENCES " + TABLE_TOURS + "(" + COLUMN_ID + "), " +
                "FOREIGN KEY(" + COLUMN_USER_ID + ") REFERENCES " + TABLE_USERS + "(" + COLUMN_ID + "))");
        seedData(db);
    }

    private void seedData(SQLiteDatabase db) {
        String VIDEO_HA_LONG = "https://res.cloudinary.com/dgiu7ewoy/video/upload/v1774972489/ng%E1%BA%AFn_v%E1%BB%81_c%E1%BA%A3nh_%C4%91%E1%BA%B9p_h%E1%BA%A1_long_-_T%C3%ACm_tr%C3%AAn_Google_kfbyq5.mp4";
        String VIDEO_HUE = "https://res.cloudinary.com/dgiu7ewoy/video/upload/v1774972511/N%E1%BA%BFu_c%E1%BA%A3_%C4%91%E1%BB%9Di_n%C3%A0y_kh%C3%B4ng_r%E1%BB%B1c_r%E1%BB%9B_th%C3%AC_sao---_Th%C3%AC_%C4%91i_Hu%E1%BA%BF_Th%C3%A1ng_3_-_Hu%E1%BA%BF_%C4%91ang_v%C3%A0o_m%C3%B9a_%C4%91%E1%BA%B9p_r%E1%BB%B1c_r%E1%BB%9B_nh%E1%BA%A5t_trong_n%C4%83m_u6h7zx.mp4";
        String VIDEO_HON_SON = "https://res.cloudinary.com/dgiu7ewoy/video/upload/v1774972502/T%E1%BB%95ng_h%E1%BB%A3p_c%C3%A1c_%C4%91i%E1%BB%83m_check_in_H%C3%B2n_S%C6%A1n_-_An_Giang_Ki%C3%AAn_Giang_c%C5%A9_cho_h%E1%BB%87_th%C3%ADch_chill_v%E1%BB%9Bi_bi%E1%BB%83n_cqwkq1.mp4";

        // =========================
        // TOURS
        // =========================
        insertTour(db,
                "Vịnh Hạ Long",
                "Quảng Ninh",
                "2N1Đ",
                "2.990.000đ",
                "Vịnh Hạ Long là kỳ quan thiên nhiên thế giới, nơi hàng nghìn đảo đá vươn lên giữa làn nước xanh ngọc. Tour này dành cho những ai muốn thật sự “chạm” vào vẻ đẹp của vịnh: ngủ đêm trên du thuyền, ngắm hoàng hôn rót vàng trên mặt biển, hít hà mùi mặn của gió và nghe tiếng sóng lách tách dưới mạn tàu. Điểm khác biệt của tour là trải nghiệm đêm trên vịnh kết hợp hoạt động kayak và tiệc sunset, giúp bạn cảm nhận trọn vẹn nhịp sống vịnh Hạ Long theo cách chậm rãi và sang trọng.",
                "Ngày 1: Hà Nội - Hạ Long - Du thuyền\n" +
                        "07:00 Đón tại Hà Nội, khởi hành đi Hạ Long theo cao tốc.\n" +
                        "10:30 Đến cảng, làm thủ tục lên du thuyền, nhận phòng.\n" +
                        "12:30 Ăn trưa hải sản trên tàu, ngắm đảo đá kỳ vĩ.\n" +
                        "14:30 Tham quan Hang Sửng Sốt, chụp ảnh toàn cảnh vịnh.\n" +
                        "16:00 Chèo kayak tại Hang Luồn hoặc tắm biển Titop.\n" +
                        "17:30 Tiệc sunset, ngắm hoàng hôn trên boong tàu.\n" +
                        "19:00 Ăn tối, thưởng thức hải sản đặc sản.\n" +
                        "21:00 Câu mực đêm, thư giãn trên boong, nghỉ đêm.\n" +
                        "Ngày 2: Bình minh - Trở về\n" +
                        "06:00 Đón bình minh, trà/cafe sáng trên boong.\n" +
                        "07:00 Tập Tai Chi nhẹ nhàng, hít thở không khí biển.\n" +
                        "08:00 Tham quan hang nhỏ, chụp ảnh check-in.\n" +
                        "09:30 Brunch nhẹ, làm thủ tục trả phòng.\n" +
                        "11:30 Cập bến, lên xe về Hà Nội.\n" +
                        "15:30 Kết thúc tour tại điểm đón.",
                "Xe đưa đón\nDu thuyền ngủ đêm\nĂn 3 bữa\nVé tham quan\nKayak",
                "Chi phí cá nhân\nĐồ uống\nTip HDV",
                0,
                "https://images.unsplash.com/photo-1643029891412-92f9a81a8c16?q=80&w=1486&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                "https://images.unsplash.com/photo-1643029891412-92f9a81a8c16?q=80&w=1486&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                VIDEO_HA_LONG + "|" + VIDEO_HON_SON,
                "HOT",
                4.8f,
                150,
                1800,
                "2026-01-01",
                "2026-12-31",
                "Tour"
        );

        insertTour(db,
                "Phố Cổ Hội An",
                "Quảng Nam",
                "3N2Đ",
                "3.500.000đ",
                "Hội An là nơi thời gian như chậm lại giữa những mái ngói rêu phong và ánh đèn lồng lung linh. Bạn sẽ đi bộ qua những con hẻm nhỏ, nghe mùi cao lầu thơm nức, ngồi bên sông Hoài thả hoa đăng và cảm nhận nhịp sống bình yên hiếm có. Điểm nổi bật của tour là kết hợp văn hóa di sản với trải nghiệm làng nghề và biển An Bàng, giúp chuyến đi vừa sâu sắc vừa thư giãn.",
                "Ngày 1: Đà Nẵng - Hội An\n" +
                        "08:00 Đón tại Đà Nẵng, di chuyển về Hội An.\n" +
                        "10:30 Nhận phòng khách sạn, nghỉ ngơi nhẹ.\n" +
                        "12:00 Ăn trưa với cao lầu, mì Quảng.\n" +
                        "15:00 Tham quan Chùa Cầu, nhà cổ Tấn Ký, hội quán Phúc Kiến.\n" +
                        "18:30 Dạo phố đèn lồng, thả hoa đăng trên sông Hoài.\n" +
                        "20:00 Tự do thưởng thức ẩm thực đường phố.\n" +
                        "Ngày 2: Làng nghề - Biển An Bàng\n" +
                        "07:30 Ăn sáng, khởi hành đi làng gốm Thanh Hà.\n" +
                        "09:30 Trải nghiệm làm gốm, chụp ảnh check-in.\n" +
                        "11:00 Thăm làng rau Trà Quế, thử làm nông dân.\n" +
                        "12:30 Ăn trưa với món đặc sản địa phương.\n" +
                        "15:00 Tắm biển An Bàng, nghỉ ngơi tự do.\n" +
                        "19:00 Xem show Ký Ức Hội An hoặc dạo chợ đêm.\n" +
                        "Ngày 3: Hội An - Mua sắm - Trở về\n" +
                        "08:00 Café phố cổ, mua quà lưu niệm.\n" +
                        "10:30 Trả phòng, lên xe về Đà Nẵng.\n" +
                        "12:00 Kết thúc tour.",
                "Xe đưa đón\nKhách sạn 3*\nĂn sáng\nVé tham quan",
                "Chi phí cá nhân\nĂn trưa & tối\nTip HDV",
                0,
                "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773591709/4b0d87f2-f7e3-48c5-9496-ad7c04c99379.png",
                "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773591709/4b0d87f2-f7e3-48c5-9496-ad7c04c99379.png",
                VIDEO_HUE,
                "BEST SELLER",
                4.9f,
                200,
                2400,
                "2026-01-01",
                "2026-12-31",
                "Tour"
        );

        insertTour(db,
                "Chinh phục Fansipan",
                "Lào Cai",
                "3N2Đ",
                "4.200.000đ",
                "Fansipan là nóc nhà Đông Dương, nơi mây trời ôm lấy những thửa ruộng bậc thang và mái nhà nhỏ của Sa Pa. Tour mang đến cảm giác hồi hộp khi đi cáp treo xuyên mây, chạm tay vào mốc 3.143m và ngắm toàn cảnh núi non hùng vĩ. Điểm khác biệt là lịch trình cân bằng giữa khám phá bản làng và hành trình chinh phục đỉnh cao, phù hợp cả gia đình lẫn nhóm bạn.",
                "Ngày 1: Hà Nội - Sa Pa\n" +
                        "05:30 Đến Sa Pa, ăn sáng và gửi hành lý.\n" +
                        "08:00 Tham quan bản Cát Cát, thác Tiên Sa.\n" +
                        "11:30 Ăn trưa, nhận phòng khách sạn.\n" +
                        "14:30 Check-in Moana, thưởng thức cafe view núi.\n" +
                        "18:30 Dạo chợ đêm Sa Pa, thưởng thức lẩu cá tầm.\n" +
                        "Ngày 2: Cáp treo Fansipan\n" +
                        "07:00 Ăn sáng, di chuyển đến ga cáp treo.\n" +
                        "08:30 Trải nghiệm cáp treo, leo bậc đá lên đỉnh.\n" +
                        "11:30 Ăn trưa tại khu ẩm thực trên núi.\n" +
                        "15:00 Tự do chụp ảnh, mua quà lưu niệm.\n" +
                        "19:00 Nghỉ tối, tự do khám phá Sa Pa.\n" +
                        "Ngày 3: Sa Pa - Hà Nội\n" +
                        "07:30 Trả phòng, tham quan nhà thờ đá.\n" +
                        "09:00 Mua đặc sản, lên xe về Hà Nội.\n" +
                        "15:30 Về đến Hà Nội, kết thúc tour.",
                "Xe giường nằm\nKhách sạn\nVé cáp treo\nĂn sáng",
                "Chi phí cá nhân\nĂn trưa/tối",
                0,
                "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626520/1a3903f1-7c6a-4458-9fc6-519589789751.png",
                "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626520/1a3903f1-7c6a-4458-9fc6-519589789751.png",
                VIDEO_HA_LONG,
                "ADVENTURE",
                4.7f,
                85,
                1200,
                "2026-01-01",
                "2026-12-31",
                "Tour"
        );

        insertTour(db,
                "Cố Đô Huế",
                "Thừa Thiên Huế",
                "2N1Đ",
                "1.500.000đ",
                "Huế mang vẻ đẹp trầm mặc của cố đô, nơi sông Hương lững lờ và tiếng chuông chùa vang nhẹ. Tour này đưa bạn đi qua những công trình cung đình uy nghi, thưởng thức ẩm thực tinh tế và cảm nhận nhịp sống chậm rãi của người Huế. Điểm khác biệt là trải nghiệm nghe ca Huế trên sông Hương vào buổi tối, một khoảnh khắc vừa lãng mạn vừa sâu lắng.",
                "Ngày 1: Đà Nẵng - Huế\n" +
                        "07:00 Đón tại Đà Nẵng, di chuyển qua đèo Hải Vân.\n" +
                        "10:30 Đến Huế, ăn trưa với bún bò Huế.\n" +
                        "13:30 Tham quan Đại Nội, Ngọ Môn, Tử Cấm Thành.\n" +
                        "16:30 Ghé chùa Thiên Mụ, ngắm sông Hương.\n" +
                        "19:00 Du thuyền nghe ca Huế, thả hoa đăng.\n" +
                        "Ngày 2: Lăng tẩm - Trở về\n" +
                        "07:30 Tham quan lăng Khải Định, chụp ảnh kiến trúc cổ.\n" +
                        "09:30 Ghé lăng Tự Đức, vườn thơ cổ.\n" +
                        "11:30 Ăn trưa với cơm hến, bánh bèo.\n" +
                        "13:00 Lên xe về Đà Nẵng, kết thúc tour.",
                "Xe đưa đón\nVé tham quan\nĂn sáng\nDu thuyền sông Hương",
                "Chi phí cá nhân\nĂn trưa/tối",
                0,
                "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626555/2f7a9346-9a04-4efc-b96a-73652ed4c945.png",
                "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626555/2f7a9346-9a04-4efc-b96a-73652ed4c945.png",
                VIDEO_HUE + "|" + VIDEO_HA_LONG,
                "CULTURAL",
                4.6f,
                120,
                950,
                "2026-01-01",
                "2026-12-31",
                "Tour"
        );

        insertTour(db,
                "Thành phố Ngàn Hoa",
                "Đà Lạt",
                "3N2Đ",
                "2.800.000đ",
                "Đà Lạt là thành phố của hoa và sương, nơi sáng se lạnh, chiều nắng vàng và tối lành lạnh đầy lãng mạn. Bạn sẽ thức dậy giữa mùi thông, nhâm nhi cà phê view đồi và ghé thăm những vườn hoa rực rỡ. Điểm khác biệt của tour là lịch trình cân bằng giữa check-in nổi tiếng và trải nghiệm đời sống địa phương, giúp chuyến đi vừa “chill” vừa đáng nhớ.",
                "Ngày 1: TP.HCM - Đà Lạt\n" +
                        "06:00 Khởi hành từ TP.HCM, dừng chân ăn sáng.\n" +
                        "12:00 Đến Đà Lạt, nhận phòng khách sạn.\n" +
                        "15:00 Dạo hồ Xuân Hương, quảng trường Lâm Viên.\n" +
                        "18:30 Ăn tối, khám phá chợ đêm Đà Lạt.\n" +
                        "Ngày 2: Đồi chè - Langbiang\n" +
                        "07:00 Tham quan đồi chè Cầu Đất, săn mây.\n" +
                        "10:00 Check-in vườn hoa cẩm tú cầu.\n" +
                        "12:00 Ăn trưa, nghỉ ngơi.\n" +
                        "14:30 Tham quan Thung lũng Tình Yêu.\n" +
                        "16:30 Lên đỉnh Langbiang, ngắm hoàng hôn.\n" +
                        "19:00 Thưởng thức cà phê view núi.\n" +
                        "Ngày 3: Dinh Bảo Đại - Trở về\n" +
                        "07:30 Thăm Dinh Bảo Đại, nhà thờ Con Gà.\n" +
                        "10:00 Mua đặc sản mứt, trà.\n" +
                        "13:00 Khởi hành về TP.HCM, kết thúc tour.",
                "Xe đưa đón\nKhách sạn\nĂn sáng",
                "Chi phí cá nhân\nVé trò chơi",
                0,
                "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626599/9e94999d-423a-4d23-a187-41317434956c.png",
                "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626599/9e94999d-423a-4d23-a187-41317434956c.png",
                VIDEO_HON_SON,
                "ROMANTIC",
                4.8f,
                310,
                3200,
                "2026-01-01",
                "2026-12-31",
                "Tour"
        );

        insertTour(db,
                "Bà Nà Hills - Cầu Vàng",
                "Đà Nẵng",
                "3N2Đ",
                "3.150.000đ",
                "Bà Nà Hills nổi tiếng với Cầu Vàng và làng Pháp như bước ra từ truyện cổ. Tour kết hợp thiên nhiên mát lạnh, vui chơi giải trí và ẩm thực phong phú, phù hợp cho nhóm bạn trẻ lẫn gia đình. Điểm khác biệt là dành trọn một ngày cho Bà Nà để bạn khám phá đủ mọi góc sống ảo và khu vui chơi Fantasy Park mà không vội vàng.",
                "Ngày 1: Đà Nẵng - Biển Mỹ Khê\n" +
                        "10:00 Đón khách, nhận phòng khách sạn.\n" +
                        "15:00 Tắm biển Mỹ Khê, thư giãn.\n" +
                        "18:30 Ăn tối hải sản, dạo cầu Rồng.\n" +
                        "Ngày 2: Bà Nà Hills trọn ngày\n" +
                        "07:30 Ăn sáng, khởi hành đi Bà Nà.\n" +
                        "08:45 Đi cáp treo, ngắm rừng nguyên sinh.\n" +
                        "09:30 Check-in Cầu Vàng, vườn hoa Le Jardin.\n" +
                        "11:30 Tham quan làng Pháp, chụp ảnh phố cổ.\n" +
                        "12:30 Buffet trưa tại Bà Nà.\n" +
                        "14:00 Vui chơi Fantasy Park, hầm rượu.\n" +
                        "16:30 Xuống núi, về khách sạn nghỉ.\n" +
                        "19:00 Tự do khám phá Đà Nẵng về đêm.\n" +
                        "Ngày 3: Ngũ Hành Sơn - Trở về\n" +
                        "08:00 Tham quan Ngũ Hành Sơn, làng đá Non Nước.\n" +
                        "11:00 Mua sắm chợ Hàn, trả phòng.\n" +
                        "13:00 Kết thúc tour.",
                "Vé cáp treo\nXe đưa đón\nĂn sáng",
                "Chi phí cá nhân",
                0,
                "https://images.unsplash.com/photo-1507525428034-b723cf961d3e",
                "https://images.unsplash.com/photo-1507525428034-b723cf961d3e",
                VIDEO_HA_LONG,
                "TOP RATED",
                4.7f,
                180,
                2100,
                "2026-01-01",
                "2026-12-31",
                "Tour"
        );

        insertTour(db,
                "Phố cổ Hà Nội",
                "Hà Nội",
                "2N1Đ",
                "1.400.000đ",
                "Hà Nội là sự giao thoa giữa nét cổ kính và nhịp sống hiện đại. Tour này đưa bạn lang thang qua 36 phố phường, thưởng thức cafe trứng, bún chả và lắng nghe những câu chuyện lịch sử ngay giữa lòng phố cổ. Điểm khác biệt là lịch trình nhẹ nhàng nhưng giàu trải nghiệm văn hóa, phù hợp cả với người đi lần đầu lẫn muốn “sống” lại không khí Hà Nội xưa.",
                "Ngày 1: Phố cổ - Hồ Gươm\n" +
                        "09:00 Gặp hướng dẫn viên, bắt đầu dạo phố cổ.\n" +
                        "10:30 Tham quan đền Ngọc Sơn, cầu Thê Húc.\n" +
                        "12:00 Ăn trưa bún chả phố cổ.\n" +
                        "14:00 Check-in nhà cổ Mã Mây, chợ Đồng Xuân.\n" +
                        "18:00 Dạo phố đi bộ, thưởng thức cafe trứng.\n" +
                        "20:00 Tự do khám phá ẩm thực đường phố.\n" +
                        "Ngày 2: Văn hóa - Trở về\n" +
                        "07:30 Tham quan Lăng Bác, quảng trường Ba Đình.\n" +
                        "09:30 Ghé Văn Miếu - Quốc Tử Giám.\n" +
                        "12:00 Ăn trưa phở bò truyền thống.\n" +
                        "14:00 Kết thúc tour.",
                "Khách sạn\nĂn sáng\nXe đưa đón",
                "Chi phí cá nhân",
                0,
                "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee",
                "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee",
                VIDEO_HUE,
                "ICONIC",
                4.6f,
                210,
                1700,
                "2026-01-01",
                "2026-12-31",
                "Tour"
        );

        insertTour(db,
                "Vũng Tàu biển xanh",
                "Bà Rịa - Vũng Tàu",
                "2N1Đ",
                "1.300.000đ",
                "Vũng Tàu là lựa chọn hoàn hảo cho chuyến đi ngắn gần Sài Gòn: biển xanh, hải sản tươi và không khí thoáng đãng. Bạn sẽ tắm biển, leo tượng Chúa Kitô để ngắm toàn cảnh và thưởng thức bánh khọt nóng giòn. Điểm đặc biệt của tour là lịch trình tối ưu cho cuối tuần, vừa nghỉ ngơi vừa có đủ hoạt động check-in.",
                "Ngày 1: TP.HCM - Vũng Tàu\n" +
                        "06:00 Khởi hành từ TP.HCM, dừng ăn sáng.\n" +
                        "08:30 Đến Vũng Tàu, nhận phòng và nghỉ ngơi.\n" +
                        "10:00 Tắm biển Bãi Sau, chụp ảnh bờ biển.\n" +
                        "15:00 Leo tượng Chúa Kitô, ngắm toàn cảnh thành phố.\n" +
                        "17:30 Ngắm hoàng hôn tại Mũi Nghinh Phong.\n" +
                        "19:00 Ăn tối hải sản tươi sống.\n" +
                        "Ngày 2: Hải đăng - Trở về\n" +
                        "06:30 Tham quan hải đăng Vũng Tàu, chụp ảnh bình minh.\n" +
                        "08:30 Thưởng thức bánh khọt Gốc Vú Sữa.\n" +
                        "10:00 Mua quà lưu niệm, trả phòng.\n" +
                        "12:00 Khởi hành về TP.HCM, kết thúc tour.",
                "Xe đưa đón\nKhách sạn",
                "Ăn uống\nChi phí cá nhân",
                0,
                "https://images.unsplash.com/photo-1500375592092-40eb2168fd21",
                "https://images.unsplash.com/photo-1500375592092-40eb2168fd21",
                VIDEO_HON_SON,
                "BEACH",
                4.5f,
                95,
                1400,
                "2026-01-01",
                "2026-12-31",
                "Tour"
        );

        insertTour(db,
                "Nha Trang 4 đảo",
                "Khánh Hòa",
                "3N2Đ",
                "3.600.000đ",
                "Nha Trang sở hữu những bãi biển trong xanh và hệ sinh thái biển đa dạng. Tour 4 đảo đưa bạn lướt sóng, lặn ngắm san hô và tắm bùn thư giãn, cảm giác như đang tận hưởng một kỳ nghỉ resort đúng nghĩa. Điểm khác biệt là lịch trình trọn ngày trên biển, kết hợp ăn trưa trên bè và trải nghiệm bùn khoáng, giúp chuyến đi vừa vui vừa phục hồi năng lượng.",
                "Ngày 1: TP.HCM - Nha Trang\n" +
                        "08:00 Đến Nha Trang, nhận phòng khách sạn.\n" +
                        "10:00 Tham quan Tháp Bà Ponagar.\n" +
                        "12:00 Ăn trưa, nghỉ ngơi.\n" +
                        "18:30 Dạo biển Trần Phú, chợ đêm Nha Trang.\n" +
                        "Ngày 2: Tour 4 đảo\n" +
                        "08:00 Lên tàu tham quan vịnh Nha Trang.\n" +
                        "09:00 Hòn Mun lặn ngắm san hô.\n" +
                        "11:00 Tắm bùn khoáng Hòn Tằm.\n" +
                        "12:30 Ăn trưa hải sản trên bè nổi.\n" +
                        "14:00 Ghé Hòn Một, chèo kayak, chụp ảnh.\n" +
                        "16:00 Về đất liền, tự do nghỉ ngơi.\n" +
                        "Ngày 3: Biển - Mua sắm - Trở về\n" +
                        "07:30 Tắm biển, ăn sáng.\n" +
                        "10:00 Mua yến sào, đặc sản Nha Trang.\n" +
                        "13:00 Kết thúc tour.",
                "Tàu tham quan\nĂn trưa\nKhách sạn",
                "Chi phí cá nhân",
                0,
                "https://images.unsplash.com/photo-1470770841072-f978cf4d019e",
                "https://images.unsplash.com/photo-1470770841072-f978cf4d019e",
                VIDEO_HON_SON + "|" + VIDEO_HA_LONG,
                "HOT",
                4.7f,
                160,
                2300,
                "2026-01-01",
                "2026-12-31",
                "Tour"
        );

        insertTour(db,
                "Cao nguyên đá Đồng Văn",
                "Hà Giang",
                "3N2Đ",
                "3.900.000đ",
                "Đồng Văn là bản hùng ca của núi đá, nơi mỗi cung đường đều như một bức tranh. Bạn sẽ băng qua đèo Mã Pì Lèng, nhìn dòng Nho Quế xanh ngọc dưới chân và ngủ lại trong phố cổ giữa vùng cao. Điểm khác biệt của tour là trải nghiệm cung đường đèo đẹp nhất Việt Nam kết hợp văn hóa bản địa, phù hợp cho người mê khám phá.",
                "Ngày 1: Hà Nội - Hà Giang - Yên Minh\n" +
                        "06:00 Khởi hành từ Hà Nội, dừng ăn trưa tại Tuyên Quang.\n" +
                        "15:00 Check-in cổng trời Quản Bạ, núi Đôi Cô Tiên.\n" +
                        "18:00 Đến Yên Minh, nhận phòng nghỉ đêm.\n" +
                        "Ngày 2: Lũng Cú - Mã Pì Lèng - Đồng Văn\n" +
                        "07:00 Tham quan dinh Vua Mèo (Vương Chí Sình).\n" +
                        "10:00 Chinh phục cột cờ Lũng Cú.\n" +
                        "12:00 Ăn trưa tại Đồng Văn.\n" +
                        "14:00 Chinh phục đèo Mã Pì Lèng, du thuyền sông Nho Quế.\n" +
                        "18:30 Dạo phố cổ Đồng Văn, thưởng thức thắng dền.\n" +
                        "Ngày 3: Đồng Văn - Hà Nội\n" +
                        "07:00 Ghé chợ phiên (nếu đúng ngày).\n" +
                        "09:00 Lên xe về Hà Nội.\n" +
                        "19:00 Về đến Hà Nội, kết thúc tour.",
                "Xe đưa đón\nKhách sạn\nĂn sáng",
                "Chi phí cá nhân",
                0,
                "https://images.unsplash.com/photo-1501785888041-af3ef285b470",
                "https://images.unsplash.com/photo-1501785888041-af3ef285b470",
                VIDEO_HA_LONG,
                "ADVENTURE",
                4.8f,
                110,
                1500,
                "2026-01-01",
                "2026-12-31",
                "Tour"
        );

        // =========================
        // TICKETS
        // =========================
        insertTicket(db, "Hà Nội - TP.HCM", "Tháng 4 - Tháng 5", "1.200.000đ", "-20%", "Vé khứ hồi", 0,
                "https://images.unsplash.com/photo-1436491865332-7a61a109c055");
        insertTicket(db, "TP.HCM - Đà Nẵng", "Cả năm 2026", "850.000đ", "-15%", "Một chiều", 0,
                "https://images.unsplash.com/photo-1542296332-2e4473faf563");
        insertTicket(db, "Hà Nội - Phú Quốc", "Hè 2026", "1.500.000đ", "-10%", "Vé khứ hồi", 0,
                "https://images.unsplash.com/photo-1504150559654-a0c1c3f5824c");

        // =========================
        // BUS TRIPS
        // =========================
        String date0204 = "2026-04-02";
        insertBusTrip(db, "Hà Nội", "Hải Phòng", "07:00", "150.000đ", "Hải Âu", 25, date0204);
        insertBusTrip(db, "Hà Nội", "Hải Phòng", "09:00", "150.000đ", "Hải Âu", 20, date0204);
        insertBusTrip(db, "Hà Nội", "Lào Cai", "22:00", "350.000đ", "Sao Việt", 30, date0204);
        insertBusTrip(db, "TP. Hồ Chí Minh", "Đà Lạt", "23:00", "300.000đ", "Thành Bưởi", 20, date0204);
        insertBusTrip(db, "TP. Hồ Chí Minh", "Vũng Tàu", "08:00", "160.000đ", "Hoa Mai", 12, date0204);
        insertBusTrip(db, "Đà Nẵng", "Huế", "08:30", "120.000đ", "The Sinh Tourist", 20, date0204);

        // =========================
        // COMBOS
        // =========================
        insertCombo(db, "Combo Phú Quốc 3N2Đ", "Phú Quốc", "Vé máy bay + Resort 5 sao", "5.500.000đ", "4.200.000đ", 0,
                "https://images.unsplash.com/photo-1589779267460-1e5f5f5f5f5f", 4.8f, "SIÊU RẺ");
        insertCombo(db, "Combo Đà Lạt Mộng Mơ", "Đà Lạt", "Xe Limousine + Homestay trung tâm", "2.800.000đ", "1.900.000đ", 0,
                "https://images.unsplash.com/photo-1542296332-2e4473faf563", 4.7f, "BÁN CHẠY");

        // =========================
        // USERS + MOCK DATA
        // =========================
        insertUser(db, "anh@gmail.com", "12345678", "Vân Ánh", "0987654321", ROLE_BUYER, "https://res.cloudinary.com/dgiu7ewoy/image/upload/v1774379896/samples/animals/three-dogs.jpg");
        insertUser(db, "admin@gmail.com", "12345678", "Admin", "091234560", ROLE_SELLER, "https://res.cloudinary.com/dgiu7ewoy/image/upload/v1774379898/samples/landscapes/nature-mountains.jpg");

        ensureSeedUsers(db);

        int userId = getUserIdByEmail(db, "anh@gmail.com");
        int tourHaLong = getTourIdByTitle(db, "Vịnh Hạ Long");
        int tourHoiAn = getTourIdByTitle(db, "Phố Cổ Hội An");
        int tourDaLat = getTourIdByTitle(db, "Thành phố Ngàn Hoa");
        int tourNhaTrang = getTourIdByTitle(db, "Nha Trang 4 đảo");

        insertCommentSeed(db, userId, tourHaLong, 5f,
                "Trải nghiệm ngủ đêm trên du thuyền quá chill, sáng dậy nhìn sương trên vịnh thấy đúng kiểu nghỉ dưỡng.",
                "2026-03-22 20:45:00");
        insertCommentSeed(db, userId, tourHoiAn, 4.8f,
                "Phố cổ buổi tối đẹp hơn tưởng tượng, thả hoa đăng xong mình ngồi bên sông nghe nhạc thật yên.",
                "2026-03-21 21:20:00");
        insertCommentSeed(db, userId, tourDaLat, 4.7f,
                "Săn mây Cầu Đất đúng nghĩa “đã đời”, thời tiết se lạnh làm chuyến đi cực dễ chịu.",
                "2026-02-20 18:45:00");
        insertCommentSeed(db, userId, tourNhaTrang, 4.6f,
                "Ngày đi đảo nắng đẹp, nước trong. Lặn ngắm san hô và ăn trưa trên bè rất vui.",
                "2026-03-12 16:10:00");

        insertTravelDiarySeed(db, userId, tourHaLong,
                "Một tối trên vịnh Hạ Long",
                "Khoảnh khắc hoàng hôn buông xuống, mình đứng trên boong tàu và cảm giác mọi thứ chậm lại. Sóng vỗ nhè nhẹ, mùi biển mằn mặn rất dễ chịu.",
                "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee",
                4.9f,
                "2026-03-22 22:10:00",
                "2026-03-22 22:10:00");
        insertTravelDiarySeed(db, userId, tourHoiAn,
                "Đêm đèn lồng Hội An",
                "Mình thả hoa đăng rồi lang thang qua những con hẻm nhỏ, nghe tiếng guốc gõ và tiếng rao nhẹ. Cảm giác như đang ở một Hội An rất riêng.",
                "https://images.unsplash.com/photo-1507525428034-b723cf961d3e",
                4.8f,
                "2026-03-20 21:40:00",
                "2026-03-20 21:40:00");

        insertFavorite(db, "Vịnh Hạ Long", "Tour", "2026-03-25");
        insertFavorite(db, "Phố Cổ Hội An", "Tour", "2026-03-20");
        insertFavorite(db, "Nha Trang 4 đảo", "Tour", "2026-03-10");

        insertOrderSeed(db, "Vịnh Hạ Long", "2026-04-10", 2, "Vân Ánh", "0987654321", "anh@gmail.com", "CONFIRMED");
        insertOrderSeed(db, "Bà Nà Hills - Cầu Vàng", "2026-05-05", 3, "Vân Ánh", "0987654321", "anh@gmail.com", "PENDING");
        insertOrderSeed(db, "Phố Cổ Hội An", "2026-03-20", 2, "Vân Ánh", "0987654321", "anh@gmail.com", "COMPLETED");
        insertOrderSeed(db, "Thành phố Ngàn Hoa", "2026-02-18", 1, "Vân Ánh", "0987654321", "anh@gmail.com", "COMPLETED");

        insertCancelledTripSeed(db, "Vũng Tàu biển xanh", "2026-03-05", "Lịch công tác thay đổi nên hủy sớm để sắp xếp lại.", "2026-03-01 09:15:00", "anh@gmail.com");

        insertReviewSeed(db, "Tour", "Vịnh Hạ Long", 4.9f,
                "Đêm ngủ trên du thuyền thật yên bình, ngắm bình minh trên vịnh là khoảnh khắc đáng nhớ nhất chuyến đi. HDV nhiệt tình, lịch trình hợp lý.",
                "2026-03-22 20:10:00");
        insertReviewSeed(db, "Tour", "Phố Cổ Hội An", 4.8f,
                "Mình rất thích phần làng nghề và thả hoa đăng. Phố cổ về đêm đẹp hơn ảnh rất nhiều, đồ ăn ngon và không quá đông.",
                "2026-03-21 21:05:00");
        insertReviewSeed(db, "Tour", "Thành phố Ngàn Hoa", 4.7f,
                "Đà Lạt đúng nghĩa để nghỉ dưỡng, lịch trình vừa đủ, không quá mệt. Săn mây Cầu Đất cực đã!",
                "2026-02-20 18:30:00");

        insertJournalSeed(db, "Một đêm trên vịnh",
                "Tối nay mình ngồi trên boong tàu, gió biển mát rượi và mặt nước lấp lánh như sao. Lần đầu câu mực, hồi hộp mà vui không tả được.",
                "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee", 4.9f, "2026-03-22 22:10:00");
        insertJournalSeed(db, "Hội An mùa đèn lồng",
                "Khoảnh khắc thả hoa đăng xuống sông Hoài làm mình thấy nhẹ lòng. Phố cổ về đêm vừa rực rỡ vừa yên bình.",
                "https://images.unsplash.com/photo-1507525428034-b723cf961d3e", 4.8f, "2026-03-20 21:40:00");

        insertNotificationSeed(db, "Đặt tour thành công",
                "Bạn đã đặt tour Vịnh Hạ Long (2N1Đ) ngày 10/04/2026. Hãy chuẩn bị hành lý và giấy tờ cần thiết nhé!",
                "UNREAD", "2026-03-25 08:30:00");
        insertNotificationSeed(db, "Khuyến mãi mùa hè",
                "Ưu đãi giảm 15% cho tour biển tháng 5. Áp dụng cho Nha Trang và Vũng Tàu.",
                "READ", "2026-03-28 09:00:00");
        insertNotificationSeed(db, "Nhắc lịch khởi hành",
                "Tour Vịnh Hạ Long sẽ khởi hành sau 10 ngày. Vui lòng xác nhận giờ đón.",
                "UNREAD", "2026-03-31 10:00:00");
    }

    private void ensureSeedUsers(SQLiteDatabase db) {
        if (db == null) return;
        insertUserIfMissing(db, "anh@gmail.com", "12345678", "Vân Ánh", "0987654321", ROLE_BUYER,
                "https://res.cloudinary.com/dgiu7ewoy/image/upload/v1774379896/samples/animals/three-dogs.jpg");
        insertUserIfMissing(db, "admin@gmail.com", "12345678", "Admin", "091234560", ROLE_SELLER,
                "https://res.cloudinary.com/dgiu7ewoy/image/upload/v1774379898/samples/landscapes/nature-mountains.jpg");
    }

    private void insertUserIfMissing(SQLiteDatabase db, String email, String password, String fullname, String phone, String role, String userImage) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_EMAIL, email);
        v.put(COLUMN_PASSWORD, password);
        v.put(COLUMN_FULLNAME, fullname);
        v.put(COLUMN_PHONE, phone);
        v.put(COLUMN_ROLE, role);
        if (userImage != null) v.put(COLUMN_USER_IMAGE, userImage);
        db.insertWithOnConflict(TABLE_USERS, null, v, SQLiteDatabase.CONFLICT_IGNORE);
    }

    private int getUserIdByEmail(SQLiteDatabase db, String email) {
        if (db == null || email == null || email.trim().isEmpty()) return -1;
        Cursor c = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
        try {
            if (!c.moveToFirst()) return -1;
            return c.getInt(0);
        } finally {
            c.close();
        }
    }

    private int getTourIdByTitle(SQLiteDatabase db, String title) {
        if (db == null || title == null || title.trim().isEmpty()) return -1;
        Cursor c = db.rawQuery("SELECT " + COLUMN_ID + " FROM " + TABLE_TOURS + " WHERE " + COLUMN_TITLE + " = ?", new String[]{title});
        try {
            if (!c.moveToFirst()) return -1;
            return c.getInt(0);
        } finally {
            c.close();
        }
    }

    private void insertCommentSeed(SQLiteDatabase db, int userId, int tourId, float rating, String content, String createdAt) {
        if (db == null || userId <= 0 || tourId <= 0) return;
        ContentValues v = new ContentValues();
        v.put(COLUMN_USER_ID, userId);
        v.put(COLUMN_TOUR_ID, tourId);
        v.put(COLUMN_RATING, rating);
        v.put(COLUMN_CONTENT, content);
        v.put(COLUMN_CREATED_AT, createdAt);
        v.put(COLUMN_UPDATED_AT, createdAt);
        db.insert(TABLE_COMMENTS, null, v);
    }

    private void insertTravelDiarySeed(SQLiteDatabase db, int userId, int tourId, String title, String content, String imageUrl, float rating, String createdAt, String updatedAt) {
        if (db == null || userId <= 0) return;
        ContentValues v = new ContentValues();
        v.put(COLUMN_USER_ID, userId);
        if (tourId > 0) v.put(COLUMN_TOUR_ID, tourId);
        v.put(COLUMN_TITLE, title);
        v.put(COLUMN_CONTENT, content);
        v.put(COLUMN_IMAGE_URL, imageUrl);
        v.put(COLUMN_RATING, rating);
        v.put(COLUMN_CREATED_AT, createdAt);
        v.put(COLUMN_UPDATED_AT, updatedAt);
        db.insert(TABLE_TRAVEL_DIARY, null, v);
    }

    private void insertUser(SQLiteDatabase db, String email, String password, String fullname, String phone, String role, String userImage) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_EMAIL, email);
        v.put(COLUMN_PASSWORD, password);
        v.put(COLUMN_FULLNAME, fullname);
        v.put(COLUMN_PHONE, phone);
        v.put(COLUMN_ROLE, role);
        if (userImage != null) v.put(COLUMN_USER_IMAGE, userImage);
        db.insert(TABLE_USERS, null, v);
    }

    private void insertFavorite(SQLiteDatabase db, String title, String itemType, String createdAt) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_TITLE, title);
        v.put(COLUMN_ITEM_TYPE, itemType);
        v.put(COLUMN_CREATED_AT, createdAt);
        db.insert(TABLE_FAVORITES, null, v);
    }

    private void insertOrderSeed(SQLiteDatabase db, String title, String date, int people, String name, String phone, String userEmail, String status) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_TITLE, title);
        v.put(COLUMN_DATE, date);
        v.put("people", people);
        v.put("name", name);
        v.put("phone", phone);
        v.put(COLUMN_USER_EMAIL, userEmail);
        v.put(COLUMN_ORDER_STATUS, status);
        db.insert(TABLE_ORDERS, null, v);
    }

    private void insertCancelledTripSeed(SQLiteDatabase db, String title, String dateRange, String reason, String createdAt, String userEmail) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_TITLE, title);
        v.put(COLUMN_DATE_RANGE, dateRange);
        v.put(COLUMN_REASON, reason);
        v.put(COLUMN_USER_EMAIL, userEmail);
        v.put(COLUMN_CREATED_AT, createdAt);
        db.insert(TABLE_CANCELLED_TRIPS, null, v);
    }

    private void insertReviewSeed(SQLiteDatabase db, String itemType, String itemTitle, float rating, String content, String createdAt) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_ITEM_TYPE, itemType);
        v.put(COLUMN_ITEM_TITLE, itemTitle);
        v.put(COLUMN_RATING, rating);
        v.put(COLUMN_CONTENT, content);
        v.put(COLUMN_CREATED_AT, createdAt);
        db.insert(TABLE_REVIEWS, null, v);
    }

    private void insertJournalSeed(SQLiteDatabase db, String title, String content, String imageUrl, float rating, String createdAt) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_TITLE, title);
        v.put(COLUMN_CONTENT, content);
        v.put(COLUMN_IMAGE_URL, imageUrl);
        v.put(COLUMN_RATING, rating);
        v.put(COLUMN_CREATED_AT, createdAt);
        db.insert(TABLE_JOURNAL, null, v);
    }

    private void insertNotificationSeed(SQLiteDatabase db, String title, String content, String status, String createdAt) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_TITLE, title);
        v.put(COLUMN_CONTENT, content);
        v.put(COLUMN_STATUS, status);
        v.put(COLUMN_CREATED_AT, createdAt);
        db.insert(TABLE_NOTIFICATIONS, null, v);
    }


    private void insertTicket(SQLiteDatabase db, String route, String dateRange, String price, String discount, String type, int res, String url) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_TITLE, route);
        v.put(COLUMN_DATE_RANGE, dateRange);
        v.put(COLUMN_PRICE, price);
        v.put(COLUMN_DISCOUNT, discount);
        v.put(COLUMN_TYPE, type);
        v.put(COLUMN_IMAGE_RES, res);
        v.put(COLUMN_IMAGE_URL, url);
        db.insert(TABLE_TICKETS, null, v);
    }

    private void insertBusTrip(SQLiteDatabase db, String dep, String dest, String time, String price, String company, int seats, String date) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_DEPARTURE, dep);
        v.put(COLUMN_DESTINATION, dest);
        v.put(COLUMN_DEPARTURE_TIME, time);
        v.put(COLUMN_PRICE, price);
        v.put(COLUMN_BUS_COMPANY, company);
        v.put(COLUMN_AVAILABLE_SEATS, seats);
        v.put(COLUMN_DATE, date);
        db.insert(TABLE_BUS_TRIPS, null, v);
    }

    private void insertCombo(SQLiteDatabase db, String title, String location, String description, String originalPrice, String price, int imageRes, String imageUrl, float rating, String badge) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_TITLE, title);
        v.put(COLUMN_LOCATION, location);
        v.put(COLUMN_DESCRIPTION, description);
        v.put(COLUMN_ORIGINAL_PRICE, originalPrice);
        v.put(COLUMN_PRICE, price);
        v.put(COLUMN_IMAGE_RES, imageRes);
        v.put(COLUMN_IMAGE_URL, imageUrl);
        v.put(COLUMN_RATING, rating);
        v.put(COLUMN_BADGE, badge);
        db.insert(TABLE_COMBOS, null, v);
    }

    public List<BusTrip> searchBusTrips(String dep, String dest, String date, int minSeats) {
        List<BusTrip> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_BUS_TRIPS, null,
                COLUMN_DEPARTURE + "=? AND " + COLUMN_DESTINATION + "=? AND " + COLUMN_DATE + "=? AND " + COLUMN_AVAILABLE_SEATS + ">=?",
                new String[]{dep, dest, date, String.valueOf(minSeats)}, null, null, COLUMN_DEPARTURE_TIME + " ASC");

        if (c.moveToFirst()) {
            do {
                list.add(new BusTrip(
                        c.getInt(c.getColumnIndexOrThrow(COLUMN_ID)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_DEPARTURE)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_DESTINATION)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_DEPARTURE_TIME)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_PRICE)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_BUS_COMPANY)),
                        c.getInt(c.getColumnIndexOrThrow(COLUMN_AVAILABLE_SEATS)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_DATE))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    private void insertTour(SQLiteDatabase db, String t, String l, String d,
                            String p, String desc, String itinerary,
                            String included, String excluded,
                            int r, String u, String imageUrls, String video,
                            String badge, float rat, int rev, int bookCount,
                            String startDate, String endDate, String type) {
        double[] coordinates = resolveCoordinates(l);
        insertTour(db, t, l, d, p, desc, itinerary, included, excluded, r, u, imageUrls, video,
                badge, rat, rev, bookCount, coordinates[0], coordinates[1], startDate, endDate, type);
    }

    private void insertTour(SQLiteDatabase db, String t, String l, String d,
                            String p, String desc, String itinerary,
                            String included, String excluded,
                            int r, String u, String imageUrls, String video,
                            String badge, float rat, int rev, int bookCount,
                            double latitude, double longitude,
                            String startDate, String endDate, String type) {

        String safeImageUrl = sanitizePrimaryImageUrl(u);
        String safeImageUrls = sanitizeImageUrls(imageUrls, safeImageUrl);

        ContentValues v = new ContentValues();
        v.put(COLUMN_TITLE, t);
        v.put(COLUMN_LOCATION, l);
        v.put(COLUMN_DURATION, d);
        v.put(COLUMN_PRICE, p);
        v.put(COLUMN_DESCRIPTION, desc);
        v.put(COLUMN_ITINERARY, itinerary);
        v.put(COLUMN_INCLUDED, included);
        v.put(COLUMN_EXCLUDED, excluded);
        v.put(COLUMN_IMAGE_RES, r);
        v.put(COLUMN_IMAGE_URL, safeImageUrl);
        v.put(COLUMN_IMAGE_URLS, safeImageUrls);
        v.put(COLUMN_VIDEO_URL, video);
        v.put(COLUMN_BADGE, badge);
        v.put(COLUMN_RATING, rat);
        v.put(COLUMN_REVIEWS, rev);
        v.put(COLUMN_BOOK_COUNT, bookCount);
        v.put(COLUMN_LATITUDE, latitude);
        v.put(COLUMN_LONGITUDE, longitude);
        v.put(COLUMN_START_DATE, startDate);
        v.put(COLUMN_END_DATE, endDate);
        v.put(COLUMN_TYPE, type);

        db.insert(TABLE_TOURS, null, v);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int n) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOURS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOTELS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMBOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNAL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_REVIEWS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CANCELLED_TRIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUS_TRIPS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRAVEL_DIARY);
        onCreate(db);
    }

    public List<Tour> getAllTours() {
        return getToursOrderedBy(COLUMN_TYPE + "=?", new String[]{"Tour"}, "book_count DESC, rating DESC", null);
    }

    public List<Tour> getPopularTours() {
        return getToursOrderedBy(COLUMN_TYPE + "=?", new String[]{"Tour"}, "book_count DESC, rating DESC", "10");
    }

    public List<Tour> getAllDestinations() {
        return getToursOrderedBy(COLUMN_TYPE + "=?", new String[]{"Destination"}, "title ASC", null);
    }

    private List<Tour> getToursOrderedBy(String selection, String[] selectionArgs, String orderBy, String limit) {
        List<Tour> list = new ArrayList<>();
        Cursor c = getReadableDatabase().query(TABLE_TOURS, null, selection, selectionArgs, null, null, orderBy, limit);
        if (c.moveToFirst()) {
            do {
                String imageUrl = c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_URL));
                String rawImageUrls = null;
                int imageUrlsIndex = c.getColumnIndex(COLUMN_IMAGE_URLS);
                if (imageUrlsIndex >= 0) rawImageUrls = c.getString(imageUrlsIndex);

                Tour tour = new Tour(
                        c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_LOCATION)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_DURATION)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_PRICE)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_ITINERARY)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_INCLUDED)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_EXCLUDED)),
                        c.getInt(c.getColumnIndexOrThrow(COLUMN_IMAGE_RES)),
                        imageUrl,
                        parseImageUrls(rawImageUrls, imageUrl),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_VIDEO_URL)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_BADGE)),
                        c.getFloat(c.getColumnIndexOrThrow(COLUMN_RATING)),
                        c.getInt(c.getColumnIndexOrThrow(COLUMN_REVIEWS)),
                        c.getInt(c.getColumnIndexOrThrow(COLUMN_BOOK_COUNT)),
                        c.getDouble(c.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                        c.getDouble(c.getColumnIndexOrThrow(COLUMN_LONGITUDE)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_START_DATE)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_END_DATE))
                );

                tour.setId(c.getInt(c.getColumnIndexOrThrow(COLUMN_ID)));
                int typeIndex = c.getColumnIndex(COLUMN_TYPE);
                if (typeIndex >= 0) {
                    tour.setType(c.getString(typeIndex));
                }

                list.add(tour);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<Combo> getAllCombos() {
        List<Combo> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_COMBOS, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                list.add(new Combo(
                        c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_LOCATION)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_ORIGINAL_PRICE)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_PRICE)),
                        c.getInt(c.getColumnIndexOrThrow(COLUMN_IMAGE_RES)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                        c.getFloat(c.getColumnIndexOrThrow(COLUMN_RATING)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_BADGE))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<Hotel> getAllHotels() {
        List<Hotel> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_HOTELS, null);
        if (c.moveToFirst()) {
            do {
                Hotel hotel = new Hotel(
                        c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_LOCATION)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_PRICE)),
                        c.getInt(c.getColumnIndexOrThrow(COLUMN_IMAGE_RES)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                        c.getFloat(c.getColumnIndexOrThrow(COLUMN_RATING)),
                        c.getInt(c.getColumnIndexOrThrow(COLUMN_REVIEWS)),
                        c.getDouble(c.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                        c.getDouble(c.getColumnIndexOrThrow(COLUMN_LONGITUDE))
                );
                hotel.setId(c.getInt(c.getColumnIndexOrThrow(COLUMN_ID)));
                list.add(hotel);
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<TicketOffer> getAllTickets() {
        List<TicketOffer> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_TICKETS, null, null, null, null, null, null);
        if (c.moveToFirst()) {
            do {
                list.add(new TicketOffer(
                        c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_DATE_RANGE)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_PRICE)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_DISCOUNT)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_TYPE)),
                        c.getInt(c.getColumnIndexOrThrow(COLUMN_IMAGE_RES)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_URL))
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    private static String buildImageList(String... urls) {
        StringBuilder builder = new StringBuilder();
        if (urls == null) return "";
        for (String url : urls) {
            if (url == null) continue;
            String trimmed = url.trim();
            if (trimmed.isEmpty()) continue;
            if (builder.length() > 0) builder.append("|");
            builder.append(trimmed);
        }
        return builder.toString();
    }

    private static boolean isTrustedImageUrl(String url) {
        if (url == null) return false;
        String trimmed = url.trim();
        // Allow any valid http or https URL
        return (trimmed.startsWith("http://") || trimmed.startsWith("https://")) && trimmed.length() > 10;
    }

    private static String sanitizePrimaryImageUrl(String imageUrl) {
        if (isTrustedImageUrl(imageUrl)) return imageUrl.trim();
        return FALLBACK_TOUR_IMAGE;
    }

    private static String sanitizeImageUrls(String imageUrls, String fallbackPrimary) {
        List<String> valid = new ArrayList<>();
        if (imageUrls != null && !imageUrls.trim().isEmpty()) {
            String[] parts = imageUrls.split("\\|");
            for (String part : parts) {
                if (isTrustedImageUrl(part)) valid.add(part.trim());
            }
        }
        if (valid.isEmpty()) valid.add(sanitizePrimaryImageUrl(fallbackPrimary));
        return buildImageList(valid.toArray(new String[0]));
    }

    private static List<String> parseImageUrls(String raw, String fallback) {
        List<String> list = new ArrayList<>();
        if (raw != null && !raw.trim().isEmpty()) {
            String[] parts = raw.split("\\|");
            for (String part : parts) {
                if (part == null) continue;
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) list.add(trimmed);
            }
        }
        if (list.isEmpty() && fallback != null && !fallback.trim().isEmpty()) {
            list.add(fallback.trim());
        }
        return list;
    }

    private static double[] resolveCoordinates(String location) {
        if (location == null) return new double[]{0.0, 0.0};
        double[] values = LOCATION_COORDINATES.get(location.trim().toLowerCase(Locale.ROOT));
        if (values == null) return new double[]{0.0, 0.0};
        return values;
    }

    private static Map<String, double[]> buildLocationCoordinates() {
        Map<String, double[]> coordinates = new HashMap<>();
        coordinates.put("quảng ninh", new double[]{20.9518, 107.0739});
        coordinates.put("quảng nam", new double[]{15.8801, 108.3380});
        coordinates.put("lào cai", new double[]{22.4837, 103.9755});
        coordinates.put("thừa thiên huế", new double[]{16.4637, 107.5909});
        coordinates.put("đà lạt", new double[]{11.9404, 108.4583});
        coordinates.put("đà nẵng", new double[]{16.0544, 108.2022});
        coordinates.put("hà nội", new double[]{21.0278, 105.8342});
        coordinates.put("bà rịa - vũng tàu", new double[]{10.5417, 107.2429});
        coordinates.put("khánh hòa", new double[]{12.2388, 109.1967});
        coordinates.put("hà giang", new double[]{22.8233, 104.9836});
        coordinates.put("hải phòng", new double[]{20.8449, 106.6881});
        coordinates.put("lâm đồng", new double[]{11.5753, 108.1429});
        coordinates.put("kon tum", new double[]{14.3497, 108.0005});
        coordinates.put("kiên giang", new double[]{10.0125, 105.0809});
        coordinates.put("phú quốc", new double[]{10.2899, 103.9840});
        coordinates.put("sa pa", new double[]{22.3364, 103.8438});
        coordinates.put("tp. hồ chí minh", new double[]{10.7769, 106.7009});
        coordinates.put("ninh thuận", new double[]{11.6739, 108.8629});
        coordinates.put("nha trang", new double[]{12.2388, 109.1967});
        coordinates.put("bình thuận", new double[]{10.9804, 108.2615});
        coordinates.put("bình phước", new double[]{11.7512, 106.7234});
        coordinates.put("tây ninh", new double[]{11.3352, 106.1099});
        coordinates.put("cần thơ", new double[]{10.0452, 105.7469});
        coordinates.put("bình định", new double[]{13.7767, 109.2243});
        coordinates.put("phú yên", new double[]{13.0882, 109.3025});
        coordinates.put("đắk lắk", new double[]{12.6667, 108.0333});
        coordinates.put("cao bằng", new double[]{22.6667, 106.2500});
        return coordinates;
    }

    public boolean checkEmailExists(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?", new String[]{email});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean registerUser(String email, String password, String fullname, String phone, String role) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_FULLNAME, fullname);
        values.put(COLUMN_PHONE, phone);
        values.put(COLUMN_ROLE, role);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public Cursor loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{email, password});
    }

    public boolean insertOrder(String title, String date, int people, String name, String phone, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("date", date);
        values.put("people", people);
        values.put("name", name);
        values.put("phone", phone);
        values.put("user_email", userEmail);
        values.put(COLUMN_ORDER_STATUS, "PENDING");
        return db.insert(TABLE_ORDERS, null, values) != -1;
    }

    public Cursor getAllOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_ORDERS, null);
    }

    public int getTripCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_ORDERS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getVisitedLocationCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(DISTINCT title) FROM " + TABLE_ORDERS, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public int getFavoriteCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_FAVORITES, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }

    public List<String> getFavoriteTitles(String itemType) {
        List<String> titles = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT " + COLUMN_TITLE + " FROM " + TABLE_FAVORITES + " WHERE " + COLUMN_ITEM_TYPE + " = ?",
                new String[]{itemType}
        );
        if (c.moveToFirst()) {
            do {
                titles.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        return titles;
    }

    public boolean addFavorite(String title, String itemType) {
        if (title == null || title.trim().isEmpty()) return false;
        ContentValues v = new ContentValues();
        v.put(COLUMN_TITLE, title);
        v.put(COLUMN_ITEM_TYPE, itemType);
        v.put(COLUMN_CREATED_AT, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        long inserted = getWritableDatabase().insertWithOnConflict(TABLE_FAVORITES, null, v, SQLiteDatabase.CONFLICT_IGNORE);
        return inserted != -1;
    }

    public boolean removeFavorite(String title, String itemType) {
        if (title == null || title.trim().isEmpty()) return false;
        int deleted = getWritableDatabase().delete(TABLE_FAVORITES,
                COLUMN_TITLE + " = ? AND " + COLUMN_ITEM_TYPE + " = ?",
                new String[]{title, itemType}
        );
        return deleted > 0;
    }

    public List<FavoriteItem> getFavoriteItems() {
        List<FavoriteItem> items = new ArrayList<>();
        String sql = "SELECT f.item_type, " +
                "t.id, t.title, t.location, t.image_url, " +
                "h.id, h.title, h.location, h.image_url " +
                "FROM " + TABLE_FAVORITES + " f " +
                "LEFT JOIN " + TABLE_TOURS + " t ON f.title = t.title AND f.item_type = 'Tour' " +
                "LEFT JOIN " + TABLE_HOTELS + " h ON f.title = h.title AND f.item_type = 'Hotel' " +
                "ORDER BY f.created_at DESC";
        Cursor c = getReadableDatabase().rawQuery(sql, null);
        if (c.moveToFirst()) {
            do {
                String type = c.getString(0);
                int tourId = c.getInt(1);
                String tourTitle = c.getString(2);
                String tourLocation = c.getString(3);
                String tourImage = c.getString(4);
                int hotelId = c.getInt(5);
                String hotelTitle = c.getString(6);
                String hotelLocation = c.getString(7);
                String hotelImage = c.getString(8);

                if ("Hotel".equals(type)) {
                    if (hotelTitle == null) continue;
                    items.add(new FavoriteItem(hotelId, hotelTitle,
                            hotelLocation == null ? "" : hotelLocation,
                            hotelImage == null ? "" : hotelImage,
                            type));
                } else {
                    if (tourTitle == null) continue;
                    items.add(new FavoriteItem(tourId, tourTitle,
                            tourLocation == null ? "" : tourLocation,
                            tourImage == null ? "" : tourImage,
                            type));
                }
            } while (c.moveToNext());
        }
        c.close();
        return items;
    }

    public List<MapItem> getAllMapItems() {
        List<MapItem> mapItems = new ArrayList<>();
        for (Tour tour : getAllTours()) {
            mapItems.add(MapItem.fromTour(tour));
        }
        for (Hotel hotel : getAllHotels()) {
            mapItems.add(MapItem.fromHotel(hotel));
        }
        return mapItems;
    }

    public Tour getTourById(int id) {
        Cursor c = getReadableDatabase().query(TABLE_TOURS, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null, "1");
        if (!c.moveToFirst()) {
            c.close();
            return null;
        }
        String imageUrl = c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_URL));
        String rawImageUrls = null;
        int imageUrlsIndex = c.getColumnIndex(COLUMN_IMAGE_URLS);
        if (imageUrlsIndex >= 0) rawImageUrls = c.getString(imageUrlsIndex);
        Tour tour = new Tour(
                c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_LOCATION)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_DURATION)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_PRICE)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_ITINERARY)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_INCLUDED)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_EXCLUDED)),
                c.getInt(c.getColumnIndexOrThrow(COLUMN_IMAGE_RES)),
                imageUrl,
                parseImageUrls(rawImageUrls, imageUrl),
                c.getString(c.getColumnIndexOrThrow(COLUMN_VIDEO_URL)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_BADGE)),
                c.getFloat(c.getColumnIndexOrThrow(COLUMN_RATING)),
                c.getInt(c.getColumnIndexOrThrow(COLUMN_REVIEWS)),
                c.getInt(c.getColumnIndexOrThrow(COLUMN_BOOK_COUNT)),
                c.getDouble(c.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                c.getDouble(c.getColumnIndexOrThrow(COLUMN_LONGITUDE)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_START_DATE)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_END_DATE))
        );
        tour.setId(c.getInt(c.getColumnIndexOrThrow(COLUMN_ID)));
        int typeIndex = c.getColumnIndex(COLUMN_TYPE);
        if (typeIndex >= 0) {
            tour.setType(c.getString(typeIndex));
        }
        c.close();
        return tour;
    }

    public Hotel getHotelById(int id) {
        Cursor c = getReadableDatabase().query(TABLE_HOTELS, null, COLUMN_ID + " = ?",
                new String[]{String.valueOf(id)}, null, null, null, "1");
        if (!c.moveToFirst()) {
            c.close();
            return null;
        }
        Hotel hotel = new Hotel(
                c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_LOCATION)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_PRICE)),
                c.getInt(c.getColumnIndexOrThrow(COLUMN_IMAGE_RES)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                c.getFloat(c.getColumnIndexOrThrow(COLUMN_RATING)),
                c.getInt(c.getColumnIndexOrThrow(COLUMN_REVIEWS)),
                c.getDouble(c.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                c.getDouble(c.getColumnIndexOrThrow(COLUMN_LONGITUDE))
        );
        hotel.setId(c.getInt(c.getColumnIndexOrThrow(COLUMN_ID)));
        c.close();
        return hotel;
    }

    public Tour getTourByTitle(String title) {
        if (title == null || title.trim().isEmpty()) return null;
        Cursor c = getReadableDatabase().query(TABLE_TOURS, null, COLUMN_TITLE + " = ?",
                new String[]{title}, null, null, null, "1");
        if (!c.moveToFirst()) {
            c.close();
            return null;
        }
        String imageUrl = c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_URL));
        String rawImageUrls = null;
        int imageUrlsIndex = c.getColumnIndex(COLUMN_IMAGE_URLS);
        if (imageUrlsIndex >= 0) rawImageUrls = c.getString(imageUrlsIndex);
        Tour tour = new Tour(
                c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_LOCATION)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_DURATION)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_PRICE)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_ITINERARY)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_INCLUDED)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_EXCLUDED)),
                c.getInt(c.getColumnIndexOrThrow(COLUMN_IMAGE_RES)),
                imageUrl,
                parseImageUrls(rawImageUrls, imageUrl),
                c.getString(c.getColumnIndexOrThrow(COLUMN_VIDEO_URL)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_BADGE)),
                c.getFloat(c.getColumnIndexOrThrow(COLUMN_RATING)),
                c.getInt(c.getColumnIndexOrThrow(COLUMN_REVIEWS)),
                c.getInt(c.getColumnIndexOrThrow(COLUMN_BOOK_COUNT)),
                c.getDouble(c.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                c.getDouble(c.getColumnIndexOrThrow(COLUMN_LONGITUDE)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_START_DATE)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_END_DATE))
        );
        tour.setId(c.getInt(c.getColumnIndexOrThrow(COLUMN_ID)));
        int typeIndex = c.getColumnIndex(COLUMN_TYPE);
        if (typeIndex >= 0) {
            tour.setType(c.getString(typeIndex));
        }
        c.close();
        return tour;
    }

    public Hotel getHotelByTitle(String title) {
        if (title == null || title.trim().isEmpty()) return null;
        Cursor c = getReadableDatabase().query(TABLE_HOTELS, null, COLUMN_TITLE + " = ?",
                new String[]{title}, null, null, null, "1");
        if (!c.moveToFirst()) {
            c.close();
            return null;
        }
        Hotel hotel = new Hotel(
                c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_LOCATION)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_PRICE)),
                c.getInt(c.getColumnIndexOrThrow(COLUMN_IMAGE_RES)),
                c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                c.getFloat(c.getColumnIndexOrThrow(COLUMN_RATING)),
                c.getInt(c.getColumnIndexOrThrow(COLUMN_REVIEWS)),
                c.getDouble(c.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                c.getDouble(c.getColumnIndexOrThrow(COLUMN_LONGITUDE))
        );
        hotel.setId(c.getInt(c.getColumnIndexOrThrow(COLUMN_ID)));
        c.close();
        return hotel;
    }

    public boolean updateUserProfile(String oldEmail, String newEmail, String fullname, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (newEmail != null && !newEmail.trim().isEmpty()) values.put(COLUMN_EMAIL, newEmail.trim());
        if (fullname != null) values.put(COLUMN_FULLNAME, fullname.trim());
        if (phone != null) values.put(COLUMN_PHONE, phone.trim());
        int updated = db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?", new String[]{oldEmail});
        return updated > 0;
    }

    public boolean updateUserPassword(String email, String oldPassword, String newPassword) {
        if (email == null || oldPassword == null || newPassword == null) return false;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PASSWORD + " FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ?",
                new String[]{email});
        try {
            if (!cursor.moveToFirst()) return false;
            String current = cursor.getString(0);
            if (!oldPassword.equals(current)) return false;
        } finally {
            cursor.close();
        }
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);
        return db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?", new String[]{email}) > 0;
    }

    public List<Tour> getRecommendedTours(int limit) {
        int safeLimit = Math.max(4, Math.min(12, limit));
        return getToursOrderedBy(COLUMN_TYPE + "=?", new String[]{"Tour"}, "book_count DESC, rating DESC", String.valueOf(safeLimit));
    }

    public List<com.example.vntravelapp.models.ReviewEntry> getReviewEntries() {
        List<com.example.vntravelapp.models.ReviewEntry> list = new ArrayList<>();
        Cursor c = getReadableDatabase().query(TABLE_REVIEWS, null, null, null, null, null, COLUMN_CREATED_AT + " DESC");
        if (c.moveToFirst()) {
            do {
                String itemType = c.getString(c.getColumnIndexOrThrow(COLUMN_ITEM_TYPE));
                String itemTitle = c.getString(c.getColumnIndexOrThrow(COLUMN_ITEM_TITLE));
                float rating = c.getFloat(c.getColumnIndexOrThrow(COLUMN_RATING));
                String content = c.getString(c.getColumnIndexOrThrow(COLUMN_CONTENT));
                String createdAt = c.getString(c.getColumnIndexOrThrow(COLUMN_CREATED_AT));
                list.add(new com.example.vntravelapp.models.ReviewEntry(itemType, itemTitle, rating, content, createdAt));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<com.example.vntravelapp.models.ReviewEntry> getReviewEntriesByUser(String userEmail) {
        List<com.example.vntravelapp.models.ReviewEntry> list = new ArrayList<>();
        int userId = getUserIdByEmail(getReadableDatabase(), userEmail);
        if (userId <= 0) return list;
        String sql = "SELECT c." + COLUMN_ID + ", c." + COLUMN_TOUR_ID + ", t." + COLUMN_TITLE + ", c." + COLUMN_RATING + ", c." + COLUMN_CONTENT + ", c." + COLUMN_CREATED_AT + " " +
                "FROM " + TABLE_COMMENTS + " c " +
                "LEFT JOIN " + TABLE_TOURS + " t ON c." + COLUMN_TOUR_ID + " = t." + COLUMN_ID + " " +
                "WHERE c." + COLUMN_USER_ID + " = ? " +
                "ORDER BY c." + COLUMN_CREATED_AT + " DESC";
        Cursor c = getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(userId)});
        if (c.moveToFirst()) {
            do {
                int id = c.getInt(0);
                int tourId = c.getInt(1);
                String tourTitle = c.getString(2);
                float rating = c.getFloat(3);
                String content = c.getString(4);
                String createdAt = c.getString(5);
                list.add(new com.example.vntravelapp.models.ReviewEntry(id, tourId, "Tour", tourTitle, rating, content, createdAt));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<com.example.vntravelapp.models.CommentItem> getCommentsForTour(int tourId) {
        List<com.example.vntravelapp.models.CommentItem> list = new ArrayList<>();
        if (tourId <= 0) return list;
        String sql = "SELECT c." + COLUMN_ID + ", u." + COLUMN_FULLNAME + ", c." + COLUMN_RATING + ", c." + COLUMN_CONTENT + ", c." + COLUMN_CREATED_AT + " " +
                "FROM " + TABLE_COMMENTS + " c " +
                "LEFT JOIN " + TABLE_USERS + " u ON c." + COLUMN_USER_ID + " = u." + COLUMN_ID + " " +
                "WHERE c." + COLUMN_TOUR_ID + " = ? " +
                "ORDER BY c." + COLUMN_CREATED_AT + " DESC";
        Cursor c = getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(tourId)});
        if (c.moveToFirst()) {
            do {
                list.add(new com.example.vntravelapp.models.CommentItem(
                        c.getInt(0),
                        c.getString(1),
                        c.getFloat(2),
                        c.getString(3),
                        c.getString(4)
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public boolean addComment(int userId, int tourId, float rating, String content) {
        if (userId <= 0 || tourId <= 0 || content == null || content.trim().isEmpty()) return false;
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues v = new ContentValues();
        v.put(COLUMN_USER_ID, userId);
        v.put(COLUMN_TOUR_ID, tourId);
        v.put(COLUMN_RATING, rating);
        v.put(COLUMN_CONTENT, content.trim());
        v.put(COLUMN_CREATED_AT, now);
        v.put(COLUMN_UPDATED_AT, now);
        return getWritableDatabase().insert(TABLE_COMMENTS, null, v) != -1;
    }

    public boolean updateComment(int commentId, String content, float rating) {
        if (commentId <= 0 || content == null || content.trim().isEmpty()) return false;
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues v = new ContentValues();
        v.put(COLUMN_CONTENT, content.trim());
        v.put(COLUMN_RATING, rating);
        v.put(COLUMN_UPDATED_AT, now);
        return getWritableDatabase().update(TABLE_COMMENTS, v, COLUMN_ID + " = ?", new String[]{String.valueOf(commentId)}) > 0;
    }

    public boolean deleteComment(int commentId) {
        if (commentId <= 0) return false;
        return getWritableDatabase().delete(TABLE_COMMENTS, COLUMN_ID + " = ?", new String[]{String.valueOf(commentId)}) > 0;
    }

    public List<com.example.vntravelapp.models.JournalEntry> getTravelDiaryEntriesByUser(String userEmail) {
        List<com.example.vntravelapp.models.JournalEntry> list = new ArrayList<>();
        int userId = getUserIdByEmail(getReadableDatabase(), userEmail);
        if (userId <= 0) return list;
        String sql = "SELECT " + COLUMN_ID + ", " + COLUMN_TOUR_ID + ", " + COLUMN_TITLE + ", " + COLUMN_CONTENT + ", " + COLUMN_IMAGE_URL + ", " + COLUMN_RATING + ", " + COLUMN_CREATED_AT + ", " + COLUMN_UPDATED_AT + " " +
                "FROM " + TABLE_TRAVEL_DIARY + " WHERE " + COLUMN_USER_ID + " = ? ORDER BY " + COLUMN_UPDATED_AT + " DESC";
        Cursor c = getReadableDatabase().rawQuery(sql, new String[]{String.valueOf(userId)});
        if (c.moveToFirst()) {
            do {
                list.add(new com.example.vntravelapp.models.JournalEntry(
                        c.getInt(0),
                        c.getInt(1),
                        c.getString(2),
                        c.getString(3),
                        c.getString(4),
                        c.getFloat(5),
                        c.getString(6),
                        c.getString(7)
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public long addTravelDiaryEntry(int userId, Integer tourId, String title, String content, String imageUrl, float rating) {
        if (userId <= 0 || title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) return -1;
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues v = new ContentValues();
        v.put(COLUMN_USER_ID, userId);
        if (tourId != null && tourId > 0) v.put(COLUMN_TOUR_ID, tourId);
        v.put(COLUMN_TITLE, title.trim());
        v.put(COLUMN_CONTENT, content.trim());
        v.put(COLUMN_IMAGE_URL, imageUrl != null ? imageUrl.trim() : null);
        v.put(COLUMN_RATING, rating);
        v.put(COLUMN_CREATED_AT, now);
        v.put(COLUMN_UPDATED_AT, now);
        return getWritableDatabase().insert(TABLE_TRAVEL_DIARY, null, v);
    }

    public boolean updateTravelDiaryEntry(int entryId, String title, String content, String imageUrl, float rating, Integer tourId) {
        if (entryId <= 0 || title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) return false;
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        ContentValues v = new ContentValues();
        v.put(COLUMN_TITLE, title.trim());
        v.put(COLUMN_CONTENT, content.trim());
        v.put(COLUMN_IMAGE_URL, imageUrl != null ? imageUrl.trim() : null);
        v.put(COLUMN_RATING, rating);
        v.put(COLUMN_UPDATED_AT, now);
        if (tourId != null) v.put(COLUMN_TOUR_ID, tourId);
        return getWritableDatabase().update(TABLE_TRAVEL_DIARY, v, COLUMN_ID + " = ?", new String[]{String.valueOf(entryId)}) > 0;
    }

    public boolean appendTravelDiaryEntry(int entryId, String extraContent) {
        if (entryId <= 0 || extraContent == null || extraContent.trim().isEmpty()) return false;
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery("SELECT " + COLUMN_CONTENT + " FROM " + TABLE_TRAVEL_DIARY + " WHERE " + COLUMN_ID + " = ?", new String[]{String.valueOf(entryId)});
        String current = "";
        if (c.moveToFirst()) current = c.getString(0);
        c.close();
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        String combined = (current == null ? "" : current.trim()) + "\n\n[" + now + "] " + extraContent.trim();
        ContentValues v = new ContentValues();
        v.put(COLUMN_CONTENT, combined);
        v.put(COLUMN_UPDATED_AT, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        return db.update(TABLE_TRAVEL_DIARY, v, COLUMN_ID + " = ?", new String[]{String.valueOf(entryId)}) > 0;
    }

    public boolean deleteTravelDiaryEntry(int entryId) {
        if (entryId <= 0) return false;
        return getWritableDatabase().delete(TABLE_TRAVEL_DIARY, COLUMN_ID + " = ?", new String[]{String.valueOf(entryId)}) > 0;
    }

    public List<com.example.vntravelapp.models.NotificationItem> getNotifications() {
        List<com.example.vntravelapp.models.NotificationItem> list = new ArrayList<>();
        Cursor c = getReadableDatabase().query(TABLE_NOTIFICATIONS, null, null, null, null, null, COLUMN_CREATED_AT + " DESC");
        if (c.moveToFirst()) {
            do {
                String title = c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE));
                String content = c.getString(c.getColumnIndexOrThrow(COLUMN_CONTENT));
                String status = c.getString(c.getColumnIndexOrThrow(COLUMN_STATUS));
                String createdAt = c.getString(c.getColumnIndexOrThrow(COLUMN_CREATED_AT));
                list.add(new com.example.vntravelapp.models.NotificationItem(title, content, status, createdAt));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<TripItem> getUpcomingTrips() {
        return getTripsByStatus("upcoming");
    }

    public List<TripItem> getCompletedTrips() {
        return getTripsByStatus("completed");
    }

    private List<TripItem> getTripsByStatus(String status) {
        List<TripItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String query;
        if ("upcoming".equals(status)) {
            query = "SELECT o.title, o.date, t.location, t.price, t.image_url FROM " + TABLE_ORDERS + " o " +
                    "LEFT JOIN " + TABLE_TOURS + " t ON o.title = t.title " +
                    "WHERE o.date >= ?";
        } else {
            query = "SELECT o.title, o.date, t.location, t.price, t.image_url FROM " + TABLE_ORDERS + " o " +
                    "LEFT JOIN " + TABLE_TOURS + " t ON o.title = t.title " +
                    "WHERE o.date < ?";
        }

        Cursor c = db.rawQuery(query, new String[]{currentDate});
        if (c.moveToFirst()) {
            do {
                list.add(new TripItem(
                        c.getString(0),
                        c.getString(2) != null ? c.getString(2) : "Unknown",
                        c.getString(1),
                        status.toUpperCase(Locale.ROOT),
                        c.getString(3) != null ? c.getString(3) : "0đ",
                        c.getString(4),
                        ""
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<TripItem> getCancelledTrips() {
        List<TripItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c.title, c.date_range, t.location, t.price, t.image_url, c.reason FROM " + TABLE_CANCELLED_TRIPS + " c " +
                "LEFT JOIN " + TABLE_TOURS + " t ON c.title = t.title";

        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst()) {
            do {
                list.add(new TripItem(
                        c.getString(0),
                        c.getString(2) != null ? c.getString(2) : "Unknown",
                        c.getString(1),
                        "CANCELLED",
                        c.getString(3) != null ? c.getString(3) : "0đ",
                        c.getString(4),
                        c.getString(5)
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<String> getVisitedLocations() {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String query = "SELECT DISTINCT t.location FROM " + TABLE_ORDERS + " o " +
                "JOIN " + TABLE_TOURS + " t ON o.title = t.title " +
                "WHERE o.date < ?";
        Cursor c = db.rawQuery(query, new String[]{currentDate});
        if (c.moveToFirst()) {
            do {
                if (c.getString(0) != null) list.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public void cancelTrip(String title, String date, String reason) {
        SQLiteDatabase db = this.getWritableDatabase();
        String userEmail = null;
        Cursor cursor = db.rawQuery(
                "SELECT " + COLUMN_USER_EMAIL + " FROM " + TABLE_ORDERS + " WHERE " + COLUMN_TITLE + " = ? AND " + COLUMN_DATE + " = ? LIMIT 1",
                new String[]{title, date}
        );
        if (cursor.moveToFirst()) {
            userEmail = cursor.getString(0);
        }
        cursor.close();

        ContentValues v = new ContentValues();
        v.put(COLUMN_TITLE, title);
        v.put(COLUMN_DATE_RANGE, date);
        v.put(COLUMN_REASON, reason);
        v.put(COLUMN_USER_EMAIL, userEmail);
        v.put(COLUMN_CREATED_AT, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        db.insert(TABLE_CANCELLED_TRIPS, null, v);

        db.delete(TABLE_ORDERS, "title = ? AND date = ?", new String[]{title, date});
    }

    public Cursor getOrdersByUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT * FROM orders WHERE user_email = ?",
                new String[]{email}
        );
    }

    public List<TripItem> getUpcomingTripsByUser(String email) {
        List<TripItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor c = db.rawQuery(
                "SELECT o.title, o.date, t.location, t.price, t.image_url " +
                        "FROM orders o LEFT JOIN tours t ON o.title = t.title " +
                        "WHERE o.user_email = ?",
                new String[]{email}
        );

        if (c.moveToFirst()) {
            do {
                list.add(new TripItem(
                        c.getString(0),
                        c.getString(2) != null ? c.getString(2) : "Unknown",
                        c.getString(1),
                        "Sắp đi",
                        c.getString(3) != null ? c.getString(3) : "0đ",
                        c.getString(4),
                        ""
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<TripItem> getCompletedTripsByUser(String email) {
        List<TripItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        String query = "SELECT o.title, o.date, t.location, t.price, t.image_url " +
                "FROM orders o LEFT JOIN tours t ON o.title = t.title " +
                "WHERE o.user_email = ? AND o.date < ?";

        Cursor c = db.rawQuery(query, new String[]{email, currentDate});
        if (c.moveToFirst()) {
            do {
                list.add(new TripItem(
                        c.getString(0),
                        c.getString(2) != null ? c.getString(2) : "Unknown",
                        c.getString(1),
                        "Đã hoàn thành",
                        c.getString(3) != null ? c.getString(3) : "0đ",
                        c.getString(4),
                        ""
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<TripItem> getCancelledTripsByUser(String email) {
        List<TripItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c.title, c.date_range, t.location, t.price, t.image_url, c.reason FROM " + TABLE_CANCELLED_TRIPS + " c " +
                "LEFT JOIN " + TABLE_TOURS + " t ON c.title = t.title " +
                "WHERE c.user_email = ? ORDER BY c.created_at DESC";

        Cursor c = db.rawQuery(query, new String[]{email});
        if (c.moveToFirst()) {
            do {
                list.add(new TripItem(
                        c.getString(0),
                        c.getString(2) != null ? c.getString(2) : "Unknown",
                        c.getString(1),
                        "Đã hủy",
                        c.getString(3) != null ? c.getString(3) : "0đ",
                        c.getString(4),
                        c.getString(5)
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public int getUserIdByEmail(String email) {
        return getUserIdByEmail(getReadableDatabase(), email);
    }

    public int getTourIdByTitle(String title) {
        return getTourIdByTitle(getReadableDatabase(), title);
    }

    public boolean insertSellerTour(String title,
                                    String location,
                                    String duration,
                                    String price,
                                    String description,
                                    String itinerary,
                                    String included,
                                    String excluded,
                                    String imageUrl,
                                    String startDate,
                                    String endDate,
                                    String sellerEmail) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_DURATION, duration);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_ITINERARY, itinerary);
        values.put(COLUMN_INCLUDED, included);
        values.put(COLUMN_EXCLUDED, excluded);
        values.put(COLUMN_IMAGE_RES, 0);
        values.put(COLUMN_IMAGE_URL, imageUrl);
        values.put(COLUMN_IMAGE_URLS, imageUrl);
        values.put(COLUMN_VIDEO_URL, "");
        values.put(COLUMN_BADGE, "SELLER");
        values.put(COLUMN_RATING, 0f);
        values.put(COLUMN_REVIEWS, 0);
        values.put(COLUMN_BOOK_COUNT, 0);
        values.put(COLUMN_START_DATE, startDate);
        values.put(COLUMN_END_DATE, endDate);
        values.put(COLUMN_SELLER_EMAIL, sellerEmail);
        values.put(COLUMN_TYPE, "Tour");

        long result = db.insert(TABLE_TOURS, null, values);
        return result != -1;
    }

    public boolean updateSellerTour(int tourId,
                                    String title,
                                    String location,
                                    String duration,
                                    String price,
                                    String description,
                                    String itinerary,
                                    String included,
                                    String excluded,
                                    String imageUrl,
                                    String startDate,
                                    String endDate) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_LOCATION, location);
        values.put(COLUMN_DURATION, duration);
        values.put(COLUMN_PRICE, price);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_ITINERARY, itinerary);
        values.put(COLUMN_INCLUDED, included);
        values.put(COLUMN_EXCLUDED, excluded);
        values.put(COLUMN_IMAGE_URL, imageUrl);
        values.put(COLUMN_IMAGE_URLS, imageUrl);
        values.put(COLUMN_START_DATE, startDate);
        values.put(COLUMN_END_DATE, endDate);

        int result = db.update(
                TABLE_TOURS,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(tourId)}
        );

        return result > 0;
    }

    public boolean deleteSellerTour(int tourId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_TOURS, COLUMN_ID + " = ?", new String[]{
                String.valueOf(tourId)
        });
        return result > 0;
    }

    public Cursor getAllOrdersForSeller() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT id, title, date, people, name, phone, user_email, order_status " +
                        "FROM orders ORDER BY id DESC",
                null
        );
    }

    public boolean cancelOrder(int orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_STATUS, "CANCELLED");
        int result = db.update("orders", values, "id = ?", new String[]{String.valueOf(orderId)});
        return result > 0;
    }

    public Cursor getSellerTourStats() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery(
                "SELECT title, COUNT(*) as total_orders " +
                        "FROM orders " +
                        "GROUP BY title " +
                        "ORDER BY total_orders DESC",
                null
        );
    }

    public boolean confirmOrder(int orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_ORDER_STATUS, "CONFIRMED");
        int result = db.update(TABLE_ORDERS, values, COLUMN_ID + " = ?", new String[]{String.valueOf(orderId)});
        return result > 0;
    }
}
