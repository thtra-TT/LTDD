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
    private static final int DATABASE_VERSION = 47; // Bumped version for type separation
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

    // Bus Trip columns
    private static final String COLUMN_DEPARTURE = "departure";
    private static final String COLUMN_DESTINATION = "destination";
    private static final String COLUMN_DEPARTURE_TIME = "departure_time";
    private static final String COLUMN_BUS_COMPANY = "bus_company";
    private static final String COLUMN_AVAILABLE_SEATS = "available_seats";
    private static final String COLUMN_DATE = "date";

    private static final Map<String, double[]> LOCATION_COORDINATES = buildLocationCoordinates();

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EMAIL + " TEXT UNIQUE, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_FULLNAME + " TEXT, " + COLUMN_PHONE + " TEXT, " + COLUMN_USER_IMAGE + " TEXT)");
        db.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ORDERS + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "date TEXT," +
                "people INTEGER," +
                "name TEXT," +
                "phone TEXT" +
                ")");
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
        seedData(db);
    }

    private void seedData(SQLiteDatabase db) {
        String DEFAULT_VIDEO = "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
        
        // Tours
        insertTour(db, "Vịnh Hạ Long: Tuyệt Tác Thiên Nhiên - Du Thuyền 5 Sao", "Quảng Ninh", "2N1Đ", "3.500.000đ", "Hành trình đẳng cấp đưa bạn len lỏi qua hàng ngàn hòn đảo đá vôi kỳ vĩ...", "Ngày 1...", "Du thuyền 5 sao...", "Đồ uống...", 0, "https://images.unsplash.com/photo-1524231757912-21f4fe3a7200", "", DEFAULT_VIDEO, "HOT", 4.9f, 850, 3200, "2026-01-01", "2026-12-31", "Tour");
        insertTour(db, "Hội An: Ký Ức Đèn Lồng & Tinh Hoa Ẩm Thực Miền Trung", "Quảng Nam", "3N2Đ", "2.800.000đ", "Lạc bước vào không gian hoài cổ...", "Ngày 1...", "Khách sạn 4 sao...", "Đồ uống...", 0, "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773591709/4b0d87f2-f7e3-48c5-9496-ad7c04c99379.png", "", DEFAULT_VIDEO, "BEST SELLER", 4.8f, 1200, 5400, "2026-01-01", "2026-12-31", "Tour");
        insertTour(db, "Sapa: Chinh Phục Đỉnh Fansipan & Bản Làng Sương Mờ", "Lào Cai", "3N2Đ", "2.500.000đ", "Khám phá vẻ đẹp hùng vĩ của nóc nhà Đông Dương...", "Ngày 1...", "Xe giường nằm...", "Chi phí cá nhân...", 0, "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1774773297/a1cc804a-5b6f-4238-94a9-21d7c19409a9.png", "", DEFAULT_VIDEO, "POPULAR", 4.7f, 950, 2800, "2026-01-01", "2026-12-31", "Tour");
        insertTour(db, "Nha Trang: Thiên Đường Biển Đảo & Lặn Ngắm San Hô", "Khánh Hòa", "4N3Đ", "3.200.000đ", "Tận hưởng làn nước trong xanh và dịch vụ nghỉ dưỡng cao cấp...", "Ngày 1...", "Khách sạn gần biển...", "Bữa tối riêng...", 0, "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626895/0ab8e4e8-af86-40ce-80fe-5113f04f1693.png", "", DEFAULT_VIDEO, "HOT", 4.8f, 750, 1500, "2026-01-01", "2026-12-31", "Tour");

        // TPHCM Destinations (Type: Destination)
        insertTour(db, "Dinh Độc Lập", "TP. Hồ Chí Minh", "2-3 giờ", "40.000đ", "Di tích lịch sử văn hóa đặc biệt cấp quốc gia, nơi ghi dấu ngày thống nhất đất nước.", "Tham quan các phòng họp, hầm trú ẩn...", "Vé vào cổng", "HDV riêng", 0, "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1774773376/3561f015-9e14-489e-8a6f-909eac6ade9f.png", "", DEFAULT_VIDEO, "CULTURAL", 4.6f, 2500, 15000, "2026-01-01", "2026-12-31", "Destination");
        insertTour(db, "Nhà Thờ Đức Bà", "TP. Hồ Chí Minh", "1 giờ", "Miễn phí", "Biểu tượng kiến trúc cổ kính của thành phố với phong cách Roman & Gothic.", "Tham quan kiến trúc bên ngoài, cầu nguyện...", "Tham quan tự do", "Chi phí gửi xe", 0, "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1774773418/184d0677-05aa-4938-b0b5-6436214cf1aa.png", "", DEFAULT_VIDEO, "ICONIC", 4.7f, 3200, 20000, "2026-01-01", "2026-12-31", "Destination");
        insertTour(db, "Bưu Điện Trung Tâm Thành Phố", "TP. Hồ Chí Minh", "30-45 phút", "Miễn phí", "Công trình kiến trúc kết hợp giữa phong cách châu Âu và nét trang trí phương Đông.", "Tham quan, gửi bưu thiếp...", "Vào cửa tự do", "Dịch vụ bưu chính", 0, "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1774773458/e71855e7-211f-40f4-a76c-636a94f2e314.png", "", DEFAULT_VIDEO, "ICONIC", 4.5f, 1800, 12000, "2026-01-01", "2026-12-31", "Destination");
        insertTour(db, "Landmark 81 SkyView", "TP. Hồ Chí Minh", "1-2 giờ", "300.000đ", "Đài quan sát tại tòa nhà cao nhất Việt Nam, ngắm trọn toàn cảnh thành phố từ trên cao.", "Ngắm cảnh từ tầng 79-81...", "Vé tham quan", "Đồ uống", 0, "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1774773499/006c6407-c690-4411-a912-85326c75c8ea.png", "", DEFAULT_VIDEO, "MODERN", 4.8f, 1500, 8000, "2026-01-01", "2026-12-31", "Destination");
        insertTour(db, "Bảo Tàng Chứng Tích Chiến Tranh", "TP. Hồ Chí Minh", "2-3 giờ", "40.000đ", "Nơi lưu giữ những hình ảnh, hiện vật về các cuộc chiến tranh tại Việt Nam.", "Tham quan khu trưng bày ngoài trời và các phòng chức năng...", "Vé vào cổng", "HDV", 0, "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1774773553/1f47b4f3-ad95-47a7-a231-a0ea12af361b.png", "", DEFAULT_VIDEO, "EDUCATIONAL", 4.7f, 5000, 30000, "2026-01-01", "2026-12-31", "Destination");

        // Tickets (Offers)
        insertTicket(db, "Hà Nội - TP.HCM", "Tháng 4 - Tháng 5", "1.200.000đ", "-20%", "Vé khứ hồi", 0, "https://images.unsplash.com/photo-1436491865332-7a61a109c055");
        insertTicket(db, "TP.HCM - Đà Nẵng", "Cả năm 2026", "850.000đ", "-15%", "Một chiều", 0, "https://images.unsplash.com/photo-1542296332-2e4473faf563");
        insertTicket(db, "Hà Nội - Phú Quốc", "Hè 2026", "1.500.000đ", "-10%", "Vé khứ hồi", 0, "https://images.unsplash.com/photo-1504150559654-a0c1c3f5824c");

        // Bus Trips Seed Data
        String date0204 = "2026-04-02";
        insertBusTrip(db, "Hà Nội", "Hải Phòng", "07:00", "150.000đ", "Hải Âu", 25, date0204);
        insertBusTrip(db, "Hà Nội", "Hải Phòng", "09:00", "150.000đ", "Hải Âu", 20, date0204);
        insertBusTrip(db, "Hà Nội", "Lào Cai", "22:00", "350.000đ", "Sao Việt", 30, date0204);
        insertBusTrip(db, "TP. Hồ Chí Minh", "Đà Lạt", "23:00", "300.000đ", "Thành Bưởi", 20, date0204);
        insertBusTrip(db, "TP. Hồ Chí Minh", "Vũng Tàu", "08:00", "160.000đ", "Hoa Mai", 12, date0204);
        insertBusTrip(db, "Đà Nẵng", "Huế", "08:30", "120.000đ", "The Sinh Tourist", 20, date0204);

        // Combos
        insertCombo(db, "Combo Phú Quốc 3N2Đ", "Phú Quốc", "Vé máy bay + Resort 5 sao", "5.500.000đ", "4.200.000đ", 0, "https://images.unsplash.com/photo-1589779267460-1e5f5f5f5f5f", 4.8f, "SIÊU RẺ");
        insertCombo(db, "Combo Đà Lạt Mộng Mơ", "Đà Lạt", "Xe Limousine + Homestay trung tâm", "2.800.000đ", "1.900.000đ", 0, "https://images.unsplash.com/photo-1542296332-2e4473faf563", 4.7f, "BÁN CHẠY");
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
                            int r, String u, String imageUrls, String video, String badge, float rat, int rev, int bookCount, String startDate, String endDate, String type) {
        double[] coordinates = resolveCoordinates(l);
        insertTour(db, t, l, d, p, desc, itinerary, included, excluded, r, u, imageUrls, video, badge, rat, rev, bookCount,
                coordinates[0], coordinates[1], startDate, endDate, type);
    }

    private void insertTour(SQLiteDatabase db, String t, String l, String d,
                            String p, String desc, String itinerary,
                            String included, String excluded,
                            int r, String u, String imageUrls, String video, String badge, float rat, int rev, int bookCount,
                            double latitude, double longitude, String startDate, String endDate, String type) {

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
                list.add(new Hotel(
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
                ));
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

    public boolean registerUser(String email, String password, String fullname, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_FULLNAME, fullname);
        values.put(COLUMN_PHONE, phone);
        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public Cursor loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{email, password});
    }

    public boolean insertOrder(String title, String date, int people, String name, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("date", date);
        values.put("people", people);
        values.put("name", name);
        values.put("phone", phone);
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

    public List<Tour> getRecommendedTours(int limit) {
        return getToursOrderedBy(COLUMN_TYPE + "=?", new String[]{"Tour"}, "book_count DESC, rating DESC", String.valueOf(limit));
    }

    public boolean updateUserProfile(String currentEmail, String newEmail, String fullname, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (newEmail != null && !newEmail.isEmpty()) {
            values.put(COLUMN_EMAIL, newEmail);
        }
        values.put(COLUMN_FULLNAME, fullname);
        values.put(COLUMN_PHONE, phone);
        int result = db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?", new String[]{currentEmail});
        return result > 0;
    }

    public boolean updateUserPassword(String email, String oldPassword, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);
        int result = db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{email, oldPassword});
        return result > 0;
    }

    public void addFavorite(String title, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, title);
        values.put(COLUMN_ITEM_TYPE, type);
        values.put(COLUMN_CREATED_AT, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        db.insertWithOnConflict(TABLE_FAVORITES, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void removeFavorite(String title, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, COLUMN_TITLE + " = ? AND " + COLUMN_ITEM_TYPE + " = ?", new String[]{title, type});
    }

    public List<String> getFavoriteTitles(String type) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FAVORITES, new String[]{COLUMN_TITLE}, COLUMN_ITEM_TYPE + " = ?", new String[]{type}, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public List<FavoriteItem> getFavoriteItems() {
        List<FavoriteItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        
        // Fetch Tours that are favorites
        String tourQuery = "SELECT f." + COLUMN_TITLE + ", t." + COLUMN_LOCATION + ", t." + COLUMN_IMAGE_URL + ", f." + COLUMN_ITEM_TYPE + 
                           " FROM " + TABLE_FAVORITES + " f JOIN " + TABLE_TOURS + " t ON f." + COLUMN_TITLE + " = t." + COLUMN_TITLE + 
                           " WHERE f." + COLUMN_ITEM_TYPE + " = 'Tour'";
        Cursor c1 = db.rawQuery(tourQuery, null);
        if (c1 != null) {
            if (c1.moveToFirst()) {
                do {
                    list.add(new FavoriteItem(c1.getString(0), c1.getString(1), c1.getString(2), c1.getString(3)));
                } while (c1.moveToNext());
            }
            c1.close();
        }

        // Fetch Hotels that are favorites
        String hotelQuery = "SELECT f." + COLUMN_TITLE + ", h." + COLUMN_LOCATION + ", h." + COLUMN_IMAGE_URL + ", f." + COLUMN_ITEM_TYPE + 
                            " FROM " + TABLE_FAVORITES + " f JOIN " + TABLE_HOTELS + " h ON f." + COLUMN_TITLE + " = h." + COLUMN_TITLE + 
                            " WHERE f." + COLUMN_ITEM_TYPE + " = 'Hotel'";
        Cursor c2 = db.rawQuery(hotelQuery, null);
        if (c2 != null) {
            if (c2.moveToFirst()) {
                do {
                    list.add(new FavoriteItem(c2.getString(0), c2.getString(1), c2.getString(2), c2.getString(3)));
                } while (c2.moveToNext());
            }
            c2.close();
        }
        
        return list;
    }

    public Tour getTourByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_TOURS, null, COLUMN_TITLE + " = ?", new String[]{title}, null, null, null);
        Tour tour = null;
        if (c.moveToFirst()) {
            String imageUrl = c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_URL));
            String rawImageUrls = null;
            int imageUrlsIndex = c.getColumnIndex(COLUMN_IMAGE_URLS);
            if (imageUrlsIndex >= 0) rawImageUrls = c.getString(imageUrlsIndex);

            tour = new Tour(
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
            int typeIndex = c.getColumnIndex(COLUMN_TYPE);
            if (typeIndex >= 0) tour.setType(c.getString(typeIndex));
        }
        c.close();
        return tour;
    }

    public Hotel getHotelByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_HOTELS, null, COLUMN_TITLE + " = ?", new String[]{title}, null, null, null);
        Hotel hotel = null;
        if (c.moveToFirst()) {
            hotel = new Hotel(
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
        }
        c.close();
        return hotel;
    }

    public List<MapItem> getAllMapItems() {
        List<MapItem> items = new ArrayList<>();
        for (Tour tour : getAllTours()) {
            items.add(MapItem.fromTour(tour));
        }
        for (Hotel hotel : getAllHotels()) {
            items.add(MapItem.fromHotel(hotel));
        }
        return items;
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
        if (status.equals("upcoming")) {
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
                        status.toUpperCase(),
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
        @SuppressWarnings("unused")
        ContentValues v = new ContentValues();
        v.put(COLUMN_TITLE, title);
        v.put(COLUMN_DATE_RANGE, date);
        v.put(COLUMN_REASON, reason);
        v.put(COLUMN_CREATED_AT, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        db.insert(TABLE_CANCELLED_TRIPS, null, v);
        
        db.delete(TABLE_ORDERS, "title = ? AND date = ?", new String[]{title, date});
    }
}
