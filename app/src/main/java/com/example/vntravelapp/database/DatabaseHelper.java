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
    private static final int DATABASE_VERSION = 41; // Bumped version to force seed data update
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
                COLUMN_END_DATE + " TEXT)");
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
        
        insertTour(db, "Vịnh Hạ Long: Tuyệt Tác Thiên Nhiên - Du Thuyền 5 Sao", "Quảng Ninh", "2N1Đ", "3.500.000đ", "Hành trình đẳng cấp đưa bạn len lỏi qua hàng ngàn hòn đảo đá vôi kỳ vĩ...", "Ngày 1...", "Du thuyền 5 sao...", "Đồ uống...", 0, "https://images.unsplash.com/photo-1524231757912-21f4fe3a7200", "", DEFAULT_VIDEO, "HOT", 4.9f, 850, 3200, "2026-01-01", "2026-12-31");
        insertTour(db, "Hội An: Ký Ức Đèn Lồng & Tinh Hoa Ẩm Thực Miền Trung", "Quảng Nam", "3N2Đ", "2.800.000đ", "Lạc bước vào không gian hoài cổ...", "Ngày 1...", "Khách sạn 4 sao...", "Đồ uống...", 0, "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773591709/4b0d87f2-f7e3-48c5-9496-ad7c04c99379.png", "", DEFAULT_VIDEO, "BEST SELLER", 4.8f, 1200, 5400, "2026-01-01", "2026-12-31");

        // Bus Trips Seed Data - Updated Date to 2026-04-02
        String date0204 = "2026-04-02";
        insertBusTrip(db, "Hà Nội", "Hải Phòng", "07:00", "150.000đ", "Hải Âu", 25, date0204);
        insertBusTrip(db, "Hà Nội", "Hải Phòng", "09:00", "150.000đ", "Hải Âu", 20, date0204);
        insertBusTrip(db, "Hà Nội", "Hải Phòng", "11:00", "180.000đ", "Hoàng Long", 15, date0204);
        insertBusTrip(db, "Hà Nội", "Lào Cai", "22:00", "350.000đ", "Sao Việt", 30, date0204);
        insertBusTrip(db, "TP. Hồ Chí Minh", "Đà Lạt", "23:00", "300.000đ", "Thành Bưởi", 20, date0204);
        insertBusTrip(db, "TP. Hồ Chí Minh", "Vũng Tàu", "08:00", "160.000đ", "Hoa Mai", 12, date0204);
        insertBusTrip(db, "Đà Nẵng", "Huế", "08:30", "120.000đ", "The Sinh Tourist", 20, date0204);
        insertBusTrip(db, "Hà Nội", "Hải Phòng", "14:00", "150.000đ", "Hải Âu", 10, date0204);
        insertBusTrip(db, "Hà Nội", "Hải Phòng", "16:00", "170.000đ", "Dcar Limousine", 7, date0204);
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
                            int r, String u, String imageUrls, String video, String badge, float rat, int rev, int bookCount, String startDate, String endDate) {
        double[] coordinates = resolveCoordinates(l);
        insertTour(db, t, l, d, p, desc, itinerary, included, excluded, r, u, imageUrls, video, badge, rat, rev, bookCount,
                coordinates[0], coordinates[1], startDate, endDate);
    }

    private void insertTour(SQLiteDatabase db, String t, String l, String d,
                            String p, String desc, String itinerary,
                            String included, String excluded,
                            int r, String u, String imageUrls, String video, String badge, float rat, int rev, int bookCount,
                            double latitude, double longitude, String startDate, String endDate) {

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

        db.insert(TABLE_TOURS, null, v);
    }
    private void insertHotel(SQLiteDatabase db, String t, String l, String d, String p, int r, String u, float rat, int rev) {
        double[] coordinates = resolveCoordinates(l);
        ContentValues v = new ContentValues();
        v.put(COLUMN_TITLE, t);
        v.put(COLUMN_LOCATION, l);
        v.put(COLUMN_DESCRIPTION, d);
        v.put(COLUMN_PRICE, p);
        v.put(COLUMN_IMAGE_RES, r);
        v.put(COLUMN_IMAGE_URL, u);
        v.put(COLUMN_RATING, rat);
        v.put(COLUMN_REVIEWS, rev);
        v.put(COLUMN_LATITUDE, coordinates[0]);
        v.put(COLUMN_LONGITUDE, coordinates[1]);
        db.insert(TABLE_HOTELS, null, v);
    }
    private void insertCombo(SQLiteDatabase db, String t, String l, String d, String o, String p, int r, String u, float rat, String b) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_TITLE, t); v.put(COLUMN_LOCATION, l); v.put(COLUMN_DESCRIPTION, d); v.put(COLUMN_ORIGINAL_PRICE, o); v.put(COLUMN_PRICE, p); v.put(COLUMN_IMAGE_RES, r); v.put(COLUMN_IMAGE_URL, u); v.put(COLUMN_RATING, rat); v.put(COLUMN_BADGE, b);
        db.insert(TABLE_COMBOS, null, v);
    }
    private void insertTicket(SQLiteDatabase db, String r, String d, String p, String dis, String t, int img, String url) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_TITLE, r); v.put(COLUMN_DATE_RANGE, d); v.put(COLUMN_PRICE, p); v.put(COLUMN_DISCOUNT, dis); v.put(COLUMN_TYPE, t); v.put(COLUMN_IMAGE_RES, img); v.put(COLUMN_IMAGE_URL, url);
        db.insert(TABLE_TICKETS, null, v);
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

    public List<Tour> getAllTours() {
        return getToursOrderedBy("book_count DESC, rating DESC", null);
    }

    public List<Hotel> getAllHotels() {
        List<Hotel> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_HOTELS, null);
        int latitudeIndex = c.getColumnIndex(COLUMN_LATITUDE);
        int longitudeIndex = c.getColumnIndex(COLUMN_LONGITUDE);
        if (c.moveToFirst()) {
            do {
                double latitude = latitudeIndex >= 0 ? c.getDouble(latitudeIndex) : 0.0;
                double longitude = longitudeIndex >= 0 ? c.getDouble(longitudeIndex) : 0.0;
                list.add(new Hotel(
                        c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_LOCATION)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_PRICE)),
                        c.getInt(c.getColumnIndexOrThrow(COLUMN_IMAGE_RES)),
                        c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                        c.getFloat(c.getColumnIndexOrThrow(COLUMN_RATING)),
                        c.getInt(c.getColumnIndexOrThrow(COLUMN_REVIEWS)),
                        latitude,
                        longitude
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    private List<Tour> getToursOrderedBy(String orderBy, String limit) {
        List<Tour> list = new ArrayList<>();
        Cursor c = getReadableDatabase().query(TABLE_TOURS, null, null, null, null, null, orderBy, limit);
        if (c.moveToFirst()) {
            do {
                String imageUrl = c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_URL));
                String rawImageUrls = null;
                int imageUrlsIndex = c.getColumnIndex(COLUMN_IMAGE_URLS);
                if (imageUrlsIndex >= 0) rawImageUrls = c.getString(imageUrlsIndex);
                list.add(new Tour(
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
                ));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<TicketOffer> getAllTickets() {
        List<TicketOffer> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_TICKETS, null);
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

    public List<Combo> getAllCombos() {
        List<Combo> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_COMBOS, null);
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

    public List<MapItem> getAllMapItems() {
        List<MapItem> list = new ArrayList<>();
        for (Tour t : getAllTours()) {
            list.add(MapItem.fromTour(t));
        }
        for (Hotel h : getAllHotels()) {
            list.add(MapItem.fromHotel(h));
        }
        return list;
    }

    public int getTripCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_ORDERS, null);
        int count = 0;
        if (cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getVisitedLocationCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(DISTINCT title) FROM " + TABLE_ORDERS, null);
        int count = 0;
        if (cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public int getFavoriteCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_FAVORITES, null);
        int count = 0;
        if (cursor.moveToFirst()) count = cursor.getInt(0);
        cursor.close();
        return count;
    }

    public List<Tour> getRecommendedTours(int limit) {
        return getToursOrderedBy("rating DESC, book_count DESC", String.valueOf(limit));
    }

    public List<Tour> getPopularTours() {
        return getToursOrderedBy("book_count DESC, rating DESC", "10");
    }

    public List<TripItem> getUpcomingTrips() {
        return getOrdersWithStatus("Sắp tới");
    }

    public List<TripItem> getCompletedTrips() {
        return getOrdersWithStatus("Đã hoàn thành");
    }

    private List<TripItem> getOrdersWithStatus(String status) {
        List<TripItem> list = new ArrayList<>();
        Cursor c = getAllOrders();
        if (c.moveToFirst()) {
            do {
                String title = c.getString(c.getColumnIndexOrThrow("title"));
                String date = c.getString(c.getColumnIndexOrThrow("date"));
                
                boolean isUpcoming = true;
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date tripDate = sdf.parse(date);
                    if (tripDate != null && tripDate.before(new Date())) {
                        isUpcoming = false;
                    }
                } catch (Exception ignored) {}
                
                if ((status.equals("Sắp tới") && isUpcoming) || (status.equals("Đã hoàn thành") && !isUpcoming)) {
                    String location = "";
                    String price = "";
                    String imageUrl = "";
                    
                    Tour t = getTourByTitle(title);
                    if (t != null) {
                        location = t.getLocation();
                        price = t.getPrice();
                        imageUrl = t.getPrimaryImageUrl();
                    } else {
                        Hotel h = getHotelByTitle(title);
                        if (h != null) {
                            location = h.getLocation();
                            price = h.getPrice();
                            imageUrl = h.getImageUrl();
                        }
                    }
                    
                    list.add(new TripItem(title, location, date, status, price, imageUrl, ""));
                }
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<TripItem> getCancelledTrips() {
        List<TripItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_CANCELLED_TRIPS, null, null, null, null, null, COLUMN_CREATED_AT + " DESC");
        if (c.moveToFirst()) {
            do {
                String title = c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE));
                String date = c.getString(c.getColumnIndexOrThrow(COLUMN_DATE_RANGE));
                String reason = c.getString(c.getColumnIndexOrThrow(COLUMN_REASON));
                
                String location = "";
                String price = "";
                String imageUrl = "";
                
                Tour t = getTourByTitle(title);
                if (t != null) {
                    location = t.getLocation();
                    price = t.getPrice();
                    imageUrl = t.getPrimaryImageUrl();
                } else {
                    Hotel h = getHotelByTitle(title);
                    if (h != null) {
                        location = h.getLocation();
                        price = h.getPrice();
                        imageUrl = h.getImageUrl();
                    }
                }
                list.add(new TripItem(title, location, date, "Đã hủy", price, imageUrl, reason));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public void cancelTrip(String title, String date, String reason) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ORDERS, "title = ? AND date = ?", new String[]{title, date});
        
        ContentValues v = new ContentValues();
        v.put(COLUMN_TITLE, title);
        v.put(COLUMN_DATE_RANGE, date);
        v.put(COLUMN_REASON, reason);
        v.put(COLUMN_CREATED_AT, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        db.insert(TABLE_CANCELLED_TRIPS, null, v);
    }

    public List<String> getVisitedLocations() {
        List<String> list = new ArrayList<>();
        List<TripItem> completed = getCompletedTrips();
        for (TripItem item : completed) {
            if (!list.contains(item.getLocation())) {
                list.add(item.getLocation());
            }
        }
        return list;
    }

    public void updateUserProfile(String oldEmail, String newEmail, String name, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        if (newEmail != null) values.put(COLUMN_EMAIL, newEmail);
        values.put(COLUMN_FULLNAME, name);
        values.put(COLUMN_PHONE, phone);
        db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ?", new String[]{oldEmail});
    }

    public boolean updateUserPassword(String email, String oldPass, String newPass) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPass);
        int rows = db.update(TABLE_USERS, values, COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{email, oldPass});
        return rows > 0;
    }

    public void addFavorite(String title, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put(COLUMN_TITLE, title);
        v.put(COLUMN_ITEM_TYPE, type);
        v.put(COLUMN_CREATED_AT, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date()));
        db.insertWithOnConflict(TABLE_FAVORITES, null, v, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void removeFavorite(String title, String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FAVORITES, COLUMN_TITLE + " = ? AND " + COLUMN_ITEM_TYPE + " = ?", new String[]{title, type});
    }

    public List<String> getFavoriteTitles(String type) {
        List<String> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_FAVORITES, new String[]{COLUMN_TITLE}, COLUMN_ITEM_TYPE + " = ?", new String[]{type}, null, null, null);
        if (c.moveToFirst()) {
            do {
                list.add(c.getString(0));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public List<FavoriteItem> getFavoriteItems() {
        List<FavoriteItem> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_FAVORITES, null, null, null, null, null, COLUMN_CREATED_AT + " DESC");
        if (c.moveToFirst()) {
            do {
                String title = c.getString(c.getColumnIndexOrThrow(COLUMN_TITLE));
                String type = c.getString(c.getColumnIndexOrThrow(COLUMN_ITEM_TYPE));
                String location = "";
                String imageUrl = "";
                if ("Hotel".equals(type)) {
                    Hotel h = getHotelByTitle(title);
                    if (h != null) {
                        location = h.getLocation();
                        imageUrl = h.getImageUrl();
                    }
                } else if ("Tour".equals(type)) {
                    Tour t = getTourByTitle(title);
                    if (t != null) {
                        location = t.getLocation();
                        imageUrl = t.getPrimaryImageUrl();
                    }
                }
                list.add(new FavoriteItem(title, location, imageUrl, type));
            } while (c.moveToNext());
        }
        c.close();
        return list;
    }

    public Hotel getHotelByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_HOTELS, null, COLUMN_TITLE + " = ?", new String[]{title}, null, null, null);
        Hotel h = null;
        if (c.moveToFirst()) {
            h = new Hotel(
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
        return h;
    }

    public Tour getTourByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_TOURS, null, COLUMN_TITLE + " = ?", new String[]{title}, null, null, null);
        Tour t = null;
        if (c.moveToFirst()) {
            String imageUrl = c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_URL));
            String rawImageUrls = null;
            int imageUrlsIndex = c.getColumnIndex(COLUMN_IMAGE_URLS);
            if (imageUrlsIndex >= 0) rawImageUrls = c.getString(imageUrlsIndex);
            t = new Tour(
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
        }
        c.close();
        return t;
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
        if (trimmed.isEmpty() || !trimmed.startsWith("https://")) return false;
        try {
            Uri uri = Uri.parse(trimmed);
            String host = uri.getHost();
            if (host == null) return false;
            String lowerHost = host.toLowerCase(Locale.ROOT);
            return "images.unsplash.com".equals(lowerHost) || "images.pexels.com".equals(lowerHost);
        } catch (Exception ignored) {
            return false;
        }
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
}
