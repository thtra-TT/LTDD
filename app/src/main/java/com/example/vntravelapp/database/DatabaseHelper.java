package com.example.vntravelapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.vntravelapp.R;
import com.example.vntravelapp.models.Combo;
import com.example.vntravelapp.models.Hotel;
import com.example.vntravelapp.models.Tour;
import com.example.vntravelapp.models.TicketOffer;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "vntravel.db";
    private static final int DATABASE_VERSION = 19;

    private static final String TABLE_TOURS = "tours";
    private static final String TABLE_HOTELS = "hotels";
    private static final String TABLE_COMBOS = "combos";
    private static final String TABLE_TICKETS = "tickets";
    private static final String TABLE_USERS = "users";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_PRICE = "price";
    private static final String COLUMN_IMAGE_RES = "image_res";
    private static final String COLUMN_IMAGE_URL = "image_url";
    private static final String COLUMN_RATING = "rating";
    private static final String COLUMN_DURATION = "duration";
    private static final String COLUMN_REVIEWS = "reviews";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_ORIGINAL_PRICE = "original_price";
    private static final String COLUMN_BADGE = "badge";
    private static final String COLUMN_DATE_RANGE = "date_range";
    private static final String COLUMN_DISCOUNT = "discount";
    private static final String COLUMN_TYPE = "type";
    
    // User columns
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_FULLNAME = "fullname";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_USER_IMAGE = "user_image";

    private static final String COLUMN_ITINERARY = "itinerary";
    private static final String COLUMN_INCLUDED = "included";
    private static final String COLUMN_EXCLUDED = "excluded";

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
                COLUMN_RATING + " REAL, " +
                COLUMN_REVIEWS + " INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_HOTELS + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE + " TEXT, " + COLUMN_LOCATION + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_PRICE + " TEXT, " + COLUMN_IMAGE_RES + " INTEGER, " + COLUMN_IMAGE_URL + " TEXT, " + COLUMN_RATING + " REAL, " + COLUMN_REVIEWS + " INTEGER)");
        db.execSQL("CREATE TABLE " + TABLE_COMBOS + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE + " TEXT, " + COLUMN_LOCATION + " TEXT, " + COLUMN_DESCRIPTION + " TEXT, " + COLUMN_ORIGINAL_PRICE + " TEXT, " + COLUMN_PRICE + " TEXT, " + COLUMN_IMAGE_RES + " INTEGER, " + COLUMN_IMAGE_URL + " TEXT, " + COLUMN_RATING + " REAL, " + COLUMN_BADGE + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_TICKETS + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_TITLE + " TEXT, " + COLUMN_DATE_RANGE + " TEXT, " + COLUMN_PRICE + " TEXT, " + COLUMN_DISCOUNT + " TEXT, " + COLUMN_TYPE + " TEXT, " + COLUMN_IMAGE_RES + " INTEGER, " + COLUMN_IMAGE_URL + " TEXT)");
        db.execSQL("CREATE TABLE " + TABLE_USERS + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_EMAIL + " TEXT UNIQUE, " + COLUMN_PASSWORD + " TEXT, " + COLUMN_FULLNAME + " TEXT, " + COLUMN_PHONE + " TEXT, " + COLUMN_USER_IMAGE + " TEXT)");
        db.execSQL("CREATE TABLE orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT," +
                "date TEXT," +
                "people INTEGER," +
                "name TEXT," +
                "phone TEXT" +
                ")");
        seedData(db);
    }

    private void seedData(SQLiteDatabase db) {
        insertTour(db,
                "Vịnh Hạ Long",
                "Quảng Ninh",
                "2N1Đ",
                "2.990.000đ",
                "Khám phá kỳ quan thiên nhiên thế giới.",

                "Ngày 1: Hà Nội → Hạ Long → Check-in du thuyền\n" +
                        "Ngày 2: Hang Sửng Sốt → Kayak → Hà Nội",

                "Xe đưa đón\nKhách sạn 3*\nĂn 3 bữa\nVé tham quan",

                "Chi phí cá nhân\nĐồ uống\nTip HDV",

                0,
                "https://images.unsplash.com/photo-1643029891412-92f9a81a8c16?q=80&w=1486&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D",
                4.8f,
                150
        );
        insertTour(db,
                "Phố Cổ Hội An",
                "Quảng Nam",
                "3N2Đ",
                "3.500.000đ",
                "Khám phá phố cổ lung linh đèn lồng và văn hóa đặc trưng miền Trung.",

                "Ngày 1: Đà Nẵng → Hội An → Check-in phố cổ\n" +
                        "Ngày 2: Tham quan Chùa Cầu, làng gốm Thanh Hà\n" +
                        "Ngày 3: Tự do mua sắm → về",

                "Xe đưa đón\nKhách sạn 3*\nĂn sáng\nVé tham quan",

                "Chi phí cá nhân\nĂn trưa & tối\nTip HDV",

                0,
                "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773591709/4b0d87f2-f7e3-48c5-9496-ad7c04c99379.png",
                4.9f,
                200
        );

        insertTour(db,
                "Chinh phục Fansipan",
                "Lào Cai",
                "3N2Đ",
                "4.200.000đ",
                "Trải nghiệm nóc nhà Đông Dương tại Sa Pa.",

                "Ngày 1: Hà Nội → Sa Pa\n" +
                        "Ngày 2: Cáp treo Fansipan\n" +
                        "Ngày 3: Bản Cát Cát → về",

                "Xe giường nằm\nKhách sạn\nVé cáp treo\nĂn sáng",

                "Chi phí cá nhân\nĂn trưa/tối",

                0,
                "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626520/1a3903f1-7c6a-4458-9fc6-519589789751.png",
                4.7f,
                85
        );

        insertTour(db,
                "Cố Đô Huế",
                "Thừa Thiên Huế",
                "2N1Đ",
                "1.500.000đ",
                "Tham quan di sản văn hóa triều Nguyễn.",

                "Ngày 1: Đại Nội, chùa Thiên Mụ\n" +
                        "Ngày 2: Lăng Khải Định → về",

                "Xe đưa đón\nVé tham quan\nĂn sáng",

                "Chi phí cá nhân\nĂn trưa/tối",

                0,
                "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626555/2f7a9346-9a04-4efc-b96a-73652ed4c945.png",
                4.6f,
                120
        );

        insertTour(db,
                "Thành phố Ngàn Hoa",
                "Đà Lạt",
                "3N2Đ",
                "2.800.000đ",
                "Check-in thiên đường hoa và khí hậu mát mẻ.",

                "Ngày 1: TP.HCM → Đà Lạt\n" +
                        "Ngày 2: Thung lũng tình yêu, Langbiang\n" +
                        "Ngày 3: Chợ Đà Lạt → về",

                "Xe đưa đón\nKhách sạn\nĂn sáng",

                "Chi phí cá nhân\nVé trò chơi",

                0,
                "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626599/9e94999d-423a-4d23-a187-41317434956c.png",
                4.8f,
                310
        );

        insertTour(db,
                "Bà Nà Hills - Cầu Vàng",
                "Đà Nẵng",
                "3N2Đ",
                "3.150.000đ",
                "Check-in Cầu Vàng và làng Pháp.",

                "Ngày 1: Đà Nẵng\n" +
                        "Ngày 2: Bà Nà Hills\n" +
                        "Ngày 3: Biển Mỹ Khê → về",

                "Vé cáp treo\nXe đưa đón\nĂn sáng",

                "Chi phí cá nhân",

                0,
                "https://images.unsplash.com/photo-1507525428034-b723cf961d3e",
                4.7f,
                180
        );

        insertTour(db,
                "Phố cổ Hà Nội",
                "Hà Nội",
                "2N1Đ",
                "1.400.000đ",
                "Trải nghiệm văn hóa thủ đô.",

                "Ngày 1: Hồ Gươm, phố cổ\n" +
                        "Ngày 2: Lăng Bác → về",

                "Khách sạn\nĂn sáng\nXe đưa đón",

                "Chi phí cá nhân",

                0,
                "https://images.unsplash.com/photo-1500530855697-b586d89ba3ee",
                4.6f,
                210
        );

        insertTour(db,
                "Vũng Tàu biển xanh",
                "Bà Rịa - Vũng Tàu",
                "2N1Đ",
                "1.300.000đ",
                "Du lịch biển gần Sài Gòn.",

                "Ngày 1: TP.HCM → Vũng Tàu\n" +
                        "Ngày 2: Tắm biển → về",

                "Xe đưa đón\nKhách sạn",

                "Ăn uống\nChi phí cá nhân",

                0,
                "https://images.unsplash.com/photo-1500375592092-40eb2168fd21",
                4.5f,
                95
        );

        insertTour(db,
                "Nha Trang 4 đảo",
                "Khánh Hòa",
                "3N2Đ",
                "3.600.000đ",
                "Tour biển đảo hấp dẫn.",

                "Ngày 1: TP.HCM → Nha Trang\n" +
                        "Ngày 2: Tour 4 đảo\n" +
                        "Ngày 3: Tắm bùn → về",

                "Tàu tham quan\nĂn trưa\nKhách sạn",

                "Chi phí cá nhân",

                0,
                "https://images.unsplash.com/photo-1470770841072-f978cf4d019e",
                4.7f,
                160
        );

        insertTour(db,
                "Cao nguyên đá Đồng Văn",
                "Hà Giang",
                "3N2Đ",
                "3.900.000đ",
                "Cung đường phượt đẹp nhất Việt Nam.",

                "Ngày 1: Hà Nội → Hà Giang\n" +
                        "Ngày 2: Mã Pì Lèng\n" +
                        "Ngày 3: Đồng Văn → về",

                "Xe đưa đón\nKhách sạn\nĂn sáng",

                "Chi phí cá nhân",

                0,
                "https://images.unsplash.com/photo-1501785888041-af3ef285b470",
                4.8f,
                110
        );
        insertHotel(db, "Vinpearl Phú Quốc", "Phú Quốc", "Mô tả Vinpearl", "2.500.000đ", 0, "https://res.cloudinary.com/demo/image/upload/v1312461204/sample.jpg", 4.7f, 300);
        insertHotel(db, "InterContinental Đà Nẵng", "Đà Nẵng", "Tọa lạc tại Bán đảo Sơn Trà, thiết kế bởi kiến trúc sư lừng danh Bill Bensley.", "8.500.000đ", 0, "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb", 4.9f, 120);
        insertHotel(db, "Hotel de la Coupole", "Sa Pa", "Sự kết hợp hoàn hảo giữa thời trang Pháp và văn hóa dân tộc thiểu số Sa Pa.", "3.200.000đ", 0, "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773591886/8c59f89a-64c9-45e0-8b54-7e513f8b25b1.png", 4.8f, 450);
        insertHotel(db, "Caravelle Saigon", "TP. Hồ Chí Minh", "Khách sạn biểu tượng lịch sử ngay trung tâm thành phố với view nhìn ra Nhà hát lớn.", "4.100.000đ", 0, "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626268/4ae52bfa-a1b2-42b6-9b70-df0f458f479b.png", 4.6f, 890);
        insertHotel(db, "Amanoi Resort", "Ninh Thuận", "Khu nghỉ dưỡng 6 sao ẩn mình trong Vườn quốc gia Núi Chúa, yên bình và riêng tư.", "25.000.000đ", 0, "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626323/a2a6d3b5-01de-4fba-924d-c3f152302981.png", 5.0f, 50);
        insertHotel(db, "Six Senses Ninh Van Bay", "Nha Trang", "Tận hưởng không gian thiên nhiên hoang sơ với các villa nằm sát mép biển.", "12.000.000đ", 0, "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626387/fc826700-1d72-4f78-b7bc-f54f9293a31d.png", 4.9f, 210);
        insertHotel(db, "Furama Resort Đà Nẵng", "Đà Nẵng", "Resort bên bãi biển Mỹ Khê, không gian nghỉ dưỡng sang trọng.", "5.200.000đ", 0, "https://images.unsplash.com/photo-1445019980597-93fa8acb246c", 4.7f, 260);
        insertHotel(db, "JW Marriott Phu Quoc Emerald Bay", "Phú Quốc", "Thiết kế độc đáo, bãi biển riêng và dịch vụ 5 sao.", "9.800.000đ", 0, "https://images.unsplash.com/photo-1501117716987-c8e1ecb210c0", 4.9f, 190);
        insertHotel(db, "Vinpearl Hạ Long Bay Resort", "Quảng Ninh", "Resort trên đảo riêng giữa vịnh Hạ Long.", "4.900.000đ", 0, "https://images.unsplash.com/photo-1505693416388-ac5ce068fe85", 4.8f, 230);
        insertHotel(db, "Lotte Hotel Hà Nội", "Hà Nội", "Khách sạn cao cấp tại trung tâm Ba Đình, view toàn cảnh thành phố.", "3.600.000đ", 0, "https://images.unsplash.com/photo-1500051638674-ff996a0ec29e", 4.6f, 410);
        insertHotel(db, "Meliá Ba Vì Mountain Retreat", "Hà Nội", "Nghỉ dưỡng giữa rừng thông, gần Vườn quốc gia Ba Vì.", "2.900.000đ", 0, "https://images.unsplash.com/photo-1469474968028-56623f02e42e", 4.5f, 120);
        insertHotel(db, "Anantara Mũi Né Resort", "Bình Thuận", "Resort ven biển với không gian yên tĩnh và hồ bơi rộng.", "4.200.000đ", 0, "https://images.unsplash.com/photo-1501117716987-c8e1ecb210c0", 4.7f, 180);

        insertCombo(db, "Siêu Combo Đà Nẵng", "Đà Nẵng", "Vé máy bay khứ hồi + Khách sạn 4 sao + Ăn sáng buffet.", "5.000.000đ", "3.990.000đ", 0, "https://res.cloudinary.com/demo/image/upload/v1312461204/sample.jpg", 4.8f, "HOT");
        insertCombo(db, "Combo Kỳ Nghỉ Phú Quốc", "Kiên Giang", "Vé máy bay + VinOasis 3N2Đ + Vé VinWonders & Safari.", "7.500.000đ", "5.850.000đ", 0, "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626789/834c6022-0daa-4f57-82d4-c1907850ea7e.png", 4.9f, "BEST SELLER");
        insertCombo(db, "Combo Sapa Mờ Sương", "Lào Cai", "Xe giường nằm InterBus + Khách sạn view núi + Ăn sáng.", "2.200.000đ", "1.650.000đ", 0, "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626828/9dd0a628-4ddf-44bb-a760-87b8f78f8b67.png", 4.7f, "GIÁ TỐT");
        insertCombo(db, "Combo Quy Nhơn Biển Nhớ", "Bình Định", "Vé máy bay + FLC Quy Nhơn 3N2Đ + Đưa đón sân bay.", "6.200.000đ", "4.990.000đ", 0, "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626858/e8527db1-1289-49bb-b720-89660d3a6bd8.png", 4.6f, "NEW");
        insertCombo(db, "Combo Nha Trang Hè Rực Rỡ", "Khánh Hòa", "Khách sạn mặt biển + Tour 4 đảo + Tiệc hải sản.", "4.500.000đ", "3.200.000đ", 0, "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626895/0ab8e4e8-af86-40ce-80fe-5113f04f1693.png", 4.8f, "GIẢM 30%");

        insertTicket(db, "Hà Nội - Đà Nẵng", "15/04 - 20/04", "1.590.000đ", "Giảm 20%", "Vé khứ hồi", 0, "https://images.unsplash.com/photo-1436491865332-7a61a109cc05");
        insertTicket(db, "TP. Hồ Chí Minh - Phú Quốc", "05/05 - 10/05", "1.990.000đ", "Giảm 15%", "Vé khứ hồi", 0, "https://images.unsplash.com/photo-1500534314209-a26db0f5c15c");
        insertTicket(db, "Hà Nội - Nha Trang", "10/06 - 15/06", "2.250.000đ", "Giảm 10%", "Vé khứ hồi", 0, "https://images.unsplash.com/photo-1529070538774-1843cb3265df");
        insertTicket(db, "Đà Nẵng - Cần Thơ", "08/07 - 12/07", "1.780.000đ", "Giảm 12%", "Vé khứ hồi", 0, "https://images.unsplash.com/photo-1469474968028-56623f02e42e");
        insertTicket(db, "TP. Hồ Chí Minh - Huế", "20/07 - 25/07", "1.450.000đ", "Giảm 18%", "Vé khứ hồi", 0, "https://images.unsplash.com/photo-1507525428034-b723cf961d3e");
        insertTicket(db, "Hà Nội - Đà Lạt", "02/08 - 06/08", "2.050.000đ", "Giảm 8%", "Vé khứ hồi", 0, "https://images.unsplash.com/photo-1470770841072-f978cf4d019e");
    }

    private void insertTour(SQLiteDatabase db, String t, String l, String d,
                            String p, String desc, String itinerary,
                            String included, String excluded,
                            int r, String u, float rat, int rev) {

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
        v.put(COLUMN_IMAGE_URL, u);
        v.put(COLUMN_RATING, rat);
        v.put(COLUMN_REVIEWS, rev);

        db.insert(TABLE_TOURS, null, v);
    }
    private void insertHotel(SQLiteDatabase db, String t, String l, String d, String p, int r, String u, float rat, int rev) {
        ContentValues v = new ContentValues();
        v.put(COLUMN_TITLE, t); v.put(COLUMN_LOCATION, l); v.put(COLUMN_DESCRIPTION, d); v.put(COLUMN_PRICE, p); v.put(COLUMN_IMAGE_RES, r); v.put(COLUMN_IMAGE_URL, u); v.put(COLUMN_RATING, rat); v.put(COLUMN_REVIEWS, rev);
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

    public boolean insertOrder(String title, String date, int people, String name, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("date", date);
        values.put("people", people);
        values.put("name", name);
        values.put("phone", phone);

        long result = db.insert("orders", null, values);
        return result != -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int n) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOURS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOTELS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMBOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Auth methods
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

    public List<Tour> getAllTours() {
        List<Tour> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery("SELECT * FROM " + TABLE_TOURS, null);
        if (c.moveToFirst()) {
            do {
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
                        c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_URL)),
                        c.getFloat(c.getColumnIndexOrThrow(COLUMN_RATING)),
                        c.getInt(c.getColumnIndexOrThrow(COLUMN_REVIEWS))
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
                    c.getInt(c.getColumnIndexOrThrow(COLUMN_REVIEWS))
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

    public Cursor getAllOrders() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM orders", null);
    }
}
