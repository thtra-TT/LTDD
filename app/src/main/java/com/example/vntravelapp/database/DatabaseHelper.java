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
    private static final int DATABASE_VERSION = 35; // Bumped version for full 20-tour refresh
    private static final String FALLBACK_TOUR_IMAGE = "https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&fit=crop&w=1400&q=80";

    private static final String TABLE_TOURS = "tours";
    private static final String TABLE_HOTELS = "hotels";
    private static final String TABLE_COMBOS = "combos";
    private static final String TABLE_TICKETS = "tickets";
    private static final String TABLE_USERS = "users";
    private static final String TABLE_ORDERS = "orders";

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

    private static final String COLUMN_ITINERARY = "itinerary";
    private static final String COLUMN_INCLUDED = "included";
    private static final String COLUMN_EXCLUDED = "excluded";
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";
    private static final String COLUMN_START_DATE = "start_date";
    private static final String COLUMN_END_DATE = "end_date";

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
        seedData(db);
    }

    private void seedData(SQLiteDatabase db) {
        String DEFAULT_VIDEO = "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
        
        // 1. Vịnh Hạ Long - Quảng Ninh
        insertTour(db, "Vịnh Hạ Long: Tuyệt Tác Thiên Nhiên - Du Thuyền 5 Sao", "Quảng Ninh", "2N1Đ", "3.500.000đ", 
            "Hành trình đẳng cấp đưa bạn len lỏi qua hàng ngàn hòn đảo đá vôi kỳ vĩ. Thưởng thức hải sản tươi ngon, đón bình minh trên boong tàu và khám phá những hang động thạch nhũ lung linh huyền ảo. Trải nghiệm không gian nghỉ dưỡng sang trọng giữa kỳ quan thiên nhiên thế giới.", 
            "Ngày 1:\n08:00 – Khởi hành từ Hà Nội\n12:30 – Nhận phòng du thuyền, thưởng thức bữa trưa\n15:00 – Tham quan Hang Sửng Sốt, chèo Kayak tại Hang Luồn\n19:00 – Bữa tối sang trọng & câu mực đêm\n\nNgày 2:\n06:30 – Tập Tai Chi đón bình minh\n08:00 – Chinh phục đỉnh Ti Tốp\n11:00 – Buffet trưa & làm thủ tục trả phòng\n15:30 – Trở về Hà Nội", 
            "Du thuyền 5 sao hiện đại\nCác bữa ăn hải sản cao cấp\nHướng dẫn viên suốt tuyến\nVé tham quan & phí bến bãi\nBảo hiểm du lịch", 
            "Đồ uống gọi thêm\nChi phí cá nhân\nTiền tip HDV", 
            0, "https://images.unsplash.com/photo-1524231757912-21f4fe3a7200", "", DEFAULT_VIDEO, "HOT", 4.9f, 850, 3200, "2026-01-01", "2026-12-31");

        // 2. Hội An - Quảng Nam
        insertTour(db, "Hội An: Ký Ức Đèn Lồng & Tinh Hoa Ẩm Thực Miền Trung", "Quảng Nam", "3N2Đ", "2.800.000đ", 
            "Lạc bước vào không gian hoài cổ với những dãy nhà vàng rêu phong, lung linh ánh đèn lồng buổi tối. Thưởng thức Cao Lầu, Cơm Gà trứ danh và tự tay làm đồ gốm truyền thống. Hội An là điểm đến của sự bình yên và những giá trị văn hóa vượt thời gian.", 
            "Ngày 1:\n14:00 – Đón khách tại Đà Nẵng, về khách sạn Hội An\n16:30 – Tham quan Chùa Cầu, nhà cổ Tân Ký\n18:30 – Ăn tối đặc sản, thả đèn hoa đăng sông Hoài\n\nNgày 2:\n08:30 – Tham quan Rừng dừa Bảy Mẫu bằng thúng\n14:00 – Ghé thăm Làng gốm Thanh Hà\n19:00 – Tự do khám phá phố cổ về đêm\n\nNgày 3:\n09:00 – Mua sắm đặc sản, thưởng thức cafe phố cổ\n12:00 – Trả phòng, kết thúc hành trình", 
            "Khách sạn 4 sao trung tâm\nXe đưa đón máy lạnh\nVé tham quan các điểm\nĂn uống theo chương trình", 
            "Đồ uống ngoài menu\nChi phí mua sắm\nTip HDV", 
            0, "https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773591709/4b0d87f2-f7e3-48c5-9496-ad7c04c99379.png", "", DEFAULT_VIDEO, "BEST SELLER", 4.8f, 1200, 5400, "2026-01-01", "2026-12-31");

        // 3. Đà Lạt - Lâm Đồng
        insertTour(db, "Đà Lạt: Thành Phố Ngàn Hoa - Săn Mây & Thông Reo", "Đà Lạt", "3N2Đ", "2.450.000đ", 
            "Trốn khỏi nắng nóng để tìm về cái se lạnh của cao nguyên Lâm Viên. Trải nghiệm cảm giác săn mây bồng bềnh tại Cầu Đất, dạo bước giữa rừng thông xanh ngắt và check-in những quán cafe cực chill với view thung lũng thơ mộng.", 
            "Ngày 1:\n09:00 – Đón sân bay Liên Khương\n11:30 – Ăn trưa lẩu rau Đà Lạt\n15:00 – Check-in Quảng trường Lâm Viên, Hồ Xuân Hương\n\nNgày 2:\n04:30 – Khởi hành săn mây Cầu Đất\n09:00 – Tham quan Vườn hoa Thành phố, Thung lũng Tình yêu\n14:30 – Ghé thăm Chùa Linh Phước\n\nNgày 3:\n08:00 – Đi chợ Đà Lạt mua quà\n11:30 – Trả phòng, kết thúc tour", 
            "Xe du lịch đời mới\nKhách sạn tiêu chuẩn 3 sao\nVé vào cổng các khu du lịch\nBảo hiểm hành khách", 
            "Bữa tối tự túc khám phá chợ đêm\nĐồ uống cá nhân", 
            0, "https://images.unsplash.com/photo-1583417319070-4a69db38a482", "", DEFAULT_VIDEO, "MUST GO", 4.7f, 980, 4800, "2026-01-01", "2026-12-31");

        // 4. Sapa - Lào Cai
        insertTour(db, "Sapa: Chinh Phục Đỉnh Fansipan - Mây Ngàn Tây Bắc", "Lào Cai", "3N2Đ", "3.950.000đ", 
            "Vượt lên chính mình để đứng trên 'Nóc nhà Đông Dương'. Chiêm ngưỡng vẻ đẹp ruộng bậc thang như những nấc thang lên thiên đường và tìm hiểu đời sống đậm đà bản sắc của đồng bào dân tộc Mông, Dao giữa sương mờ đại ngàn.", 
            "Ngày 1:\n06:30 – Khởi hành từ Hà Nội bằng Limousine\n13:00 – Nhận phòng, tham quan Bản Cát Cát\n18:30 – Ăn tối thắng cố, cơm lam\n\nNgày 2:\n08:30 – Cáp treo Sun World Fansipan Legend\n14:00 – Check-in Đèo Ô Quy Hồ, Thác Bạc\n\nNgày 3:\n09:00 – Khám phá Núi Hàm Rồng\n15:30 – Lên xe trở về Hà Nội", 
            "Xe Limousine cao cấp\nVé cáp treo Fansipan khứ hồi\nKhách sạn view thung lũng Mường Hoa\nHướng dẫn viên bản địa", 
            "Dịch vụ giặt là\nĐồ uống trong bữa ăn", 
            0, "https://images.unsplash.com/photo-1591340156063-e31b67f13f1e", "", DEFAULT_VIDEO, "TOP RATED", 4.9f, 620, 2900, "2026-01-01", "2026-12-31");

        // 5. Hà Giang
        insertTour(db, "Hà Giang: Hùng Vĩ Mã Pí Lèng - Sông Nho Quế Xanh Ngọc", "Hà Giang", "4N3Đ", "4.200.000đ", 
            "Hành trình xuyên qua những cung đường đèo hiểm trở nhưng đẹp mê hồn. Chinh phục Mã Pí Lèng - một trong 'tứ đại đỉnh đèo', ngồi thuyền xuôi dòng sông Nho Quế xanh ngắt và chạm tay vào cột cờ Lũng Cú linh thiêng.", 
            "Ngày 1:\n08:00 – Hà Nội đi Hà Giang\n16:00 – Check-in Cổng trời Quản Bạ, núi đôi Cô Tiên\n\nNgày 2:\n08:00 – Tham quan Nhà của Pao, Dinh thự họ Vương\n15:00 – Khám phá Phố cổ Đồng Văn\n\nNgày 3:\n07:30 – Chinh phục đèo Mã Pí Lèng\n10:00 – Đi thuyền hẻm Tu Sản sông Nho Quế\n14:00 – Cột cờ Lũng Cú\n\nNgày 4:\n08:30 – Mua sắm chợ phiên, trở về Hà Nội", 
            "Xe ô tô chuyên dụng leo núi\nHomestay/Khách sạn sạch sẽ\nVé thuyền sông Nho Quế\nCác bữa ăn đặc sản núi rừng", 
            "Tiền tip lái xe\nChi tiêu ngoài chương trình", 
            0, "https://images.unsplash.com/photo-1501785888041-af3ef285b470", "", DEFAULT_VIDEO, "ADVENTURE", 4.8f, 410, 1850, "2026-09-01", "2026-11-30");

        // 6. Đà Nẵng
        insertTour(db, "Đà Nẵng: Bà Nà Hills - Đường Lên Tiên Cảnh & Cầu Vàng", "Đà Nẵng", "1 ngày", "1.250.000đ", 
            "Lạc lối giữa làng Pháp cổ kính ngay trên đỉnh núi Chúa. Trải nghiệm hệ thống cáp treo đạt nhiều kỷ lục thế giới và check-in Cầu Vàng nổi tiếng với đôi bàn tay khổng lồ nâng đỡ, nơi mây và núi hòa làm một.", 
            "08:00 – Đón khách tại Đà Nẵng\n09:30 – Lên cáp treo Bà Nà\n10:00 – Tham quan Cầu Vàng, Hầm rượu, Vườn hoa\n12:00 – Buffet trưa quốc tế 100 món\n14:00 – Vui chơi Fantasy Park, Làng Pháp\n16:30 – Xuống núi, trở về trung tâm", 
            "Xe đưa đón đời mới\nVé cáp treo Bà Nà Hills\nĂn trưa buffet 5 sao\nHướng dẫn viên nhiệt tình", 
            "Vé tham quan Bảo tàng sáp\nĐồ uống cá nhân", 
            0, "https://res.cloudinary.com/dgiu7ewoy/image/upload/v1774414004/anh-dep-da-nang-thumb_x4umpt.jpg", "", DEFAULT_VIDEO, "HOT", 4.9f, 1500, 6800, "2026-01-01", "2026-12-31");

        // 7. Ninh Bình
        insertTour(db, "Ninh Bình: Tuyệt Tác Tràng An - Hang Múa - Bái Đính", "Ninh Bình", "1 ngày", "980.000đ", 
            "Khám phá 'Hạ Long trên cạn' với những dãy núi đá vôi nhấp nhô giữa đồng lúa. Ngồi thuyền xuôi dòng Tràng An qua các hang động kỳ bí và leo 500 bậc đá tại Hang Múa để ngắm trọn vẹn vẻ đẹp hùng vĩ của vùng đất cố đô.", 
            "07:30 – Khởi hành từ Hà Nội\n10:30 – Chiêm bái Chùa Bái Đính - ngôi chùa lớn nhất VN\n12:30 – Ăn trưa đặc sản cơm cháy, thịt dê\n14:00 – Đi thuyền tham quan Tràng An\n16:00 – Chinh phục đỉnh Hang Múa\n17:30 – Lên xe về lại Hà Nội", 
            "Xe du lịch chất lượng cao\nVé thuyền Tràng An\nBữa trưa đặc sản Ninh Bình\nVé tham quan các điểm", 
            "Tiền tip lái đò\nĐồ uống phát sinh", 
            0, "https://images.unsplash.com/photo-1624467104710-d096536006e8", "", DEFAULT_VIDEO, "HISTORIC", 4.7f, 540, 2100, "2026-01-01", "2026-12-31");

        // 8. Phú Quốc
        insertTour(db, "Phú Quốc: Tour 4 Đảo Thiên Đường - Cano Cao Tốc", "Phú Quốc", "1 ngày", "1.150.000đ", 
            "Tận hưởng mùa hè rực rỡ tại Đảo Ngọc. Lặn ngắm san hô tự nhiên tại Hòn Gầm Ghì, check-in 'sống ảo' tại Hòn Mây Rút và Hòn Móng Tay. Trải nghiệm cáp treo vượt biển Hòn Thơm dài nhất thế giới ngắm nhìn đại dương từ trên cao.", 
            "08:30 – Đón khách tại resort/khách sạn\n09:30 – Cano khởi hành đi các đảo\n10:30 – Lặn ngắm san hô, tắm biển bãi cát trắng\n12:30 – Ăn trưa hải sản trên đảo\n15:00 – Đi cáp treo Hòn Thơm vượt biển\n17:00 – Trở về, ngắm hoàng hôn Sunset Sanato", 
            "Cano cao tốc an toàn\nĂn trưa hải sản 8 món\nDụng cụ lặn (kính, ống thở)\nVé cáp treo Sun World", 
            "Dịch vụ đi bộ dưới đáy biển\nChi tiêu ngoài menu", 
            0, "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb", "", DEFAULT_VIDEO, "BEACH", 4.8f, 1300, 4500, "2026-01-01", "2026-12-31");

        // 9. Huế
        insertTour(db, "Huế: Cố Đô Cổ Kính - Ký Ức Hoàng Triều Nguyễn", "Thừa Thiên Huế", "1 ngày", "790.000đ", 
            "Tìm về không gian cung đình xưa cũ với những công trình lăng tẩm mang đậm dấu ấn thời gian. Thưởng thức Nhã nhạc cung đình Huế trên dòng sông Hương thơ mộng và cảm nhận sự thong dong, yên bình của mảnh đất thần kinh.", 
            "08:00 – Khởi hành từ Đà Nẵng đi Huế\n10:30 – Tham quan Đại Nội (Hoàng Thành Huế)\n12:00 – Ăn trưa món ăn cung đình\n14:00 – Viếng Chùa Thiên Mụ, Lăng Khải Định\n16:00 – Mua sắm đặc sản chợ Đông Ba\n17:30 – Quay về Đà Nẵng", 
            "Xe đưa đón suốt tuyến\nVé tham quan các lăng tẩm\nBữa trưa đặc sản Huế\nHướng dẫn viên chuyên nghiệp", 
            "Đồ uống gọi thêm\nQuà lưu niệm", 
            0, "https://images.unsplash.com/photo-1583070408542-a841d1d86d26", "", DEFAULT_VIDEO, "CULTURE", 4.6f, 320, 1400, "2026-01-01", "2026-12-31");

        // 10. Mũi Né
        insertTour(db, "Mũi Né: Đồi Cát Bay - Trải Nghiệm Xe Jeep Địa Hình", "Bình Thuận", "1 ngày", "650.000đ", 
            "Khám phá 'tiểu sa mạc' tại Việt Nam với những đồi cát trắng xóa và cát đỏ rực rỡ. Trải nghiệm cảm giác mạnh trên xe Jeep vượt địa hình, check-in Suối Tiên với nhũ đá kỳ lạ và ngắm hoàng hôn lãng mạn bên làng chài.", 
            "04:30 – Xe Jeep đón tại khách sạn đi săn bình minh\n05:30 – Tham quan Đồi Cát Trắng (Bàu Trắng)\n07:30 – Check-in Đồi Cát Đỏ\n09:00 – Đi bộ dọc Suối Tiên\n10:30 – Tham quan Làng chài Mũi Né\n11:30 – Kết thúc tour, về khách sạn", 
            "Xe Jeep tham quan theo lịch trình\nVé vào cổng các điểm\nHướng dẫn viên nhiệt tình\nNước uống đóng chai", 
            "Mô tô nước tại Bàu Trắng\nĂn sáng & trưa", 
            0, "https://images.unsplash.com/photo-1614051061807-7b6e41b2e597", "", DEFAULT_VIDEO, "ADVENTURE", 4.7f, 480, 2200, "2026-01-01", "2026-12-31");

        // 11. Cần Thơ
        insertTour(db, "Cần Thơ: Chợ Nổi Cái Răng - Miền Tây Sông Nước", "Cần Thơ", "2N1Đ", "1.850.000đ", 
            "Về miền Tây gạo trắng nước trong để trải nghiệm văn hóa sông nước độc đáo. Đi ghe máy giữa chợ nổi Cái Răng tấp nập lúc tờ mờ sáng, thưởng thức hủ tiếu ngay trên thuyền và len lỏi vào các vườn trái cây trĩu quả.", 
            "Ngày 1:\n08:00 – Khởi hành từ TP.HCM đi Cần Thơ\n13:00 – Tham quan nhà cổ Bình Thủy\n15:00 – Ghé vườn trái cây, thưởng thức hoa quả\n\nNgày 2:\n05:30 – Đi thuyền tham quan Chợ nổi Cái Răng\n08:30 – Tham quan lò hủ tiếu truyền thống\n14:00 – Lên xe trở về TP.HCM", 
            "Xe du lịch máy lạnh\nKhách sạn 3 sao trung tâm\nGhe thuyền tham quan\nCác bữa ăn theo thực đơn địa phương", 
            "Mua sắm cá nhân\nĐồ uống ngoài menu", 
            0, "https://images.unsplash.com/photo-1589118949245-7d38baf380d6", "", DEFAULT_VIDEO, "RIVER", 4.6f, 290, 1150, "2026-01-01", "2026-12-31");

        // 12. Nha Trang
        insertTour(db, "Nha Trang: Biển Xanh Gọi Mời - Tour 3 Đảo Cao Cấp", "Nha Trang", "1 ngày", "750.000đ", 
            "Tận hưởng làn nước trong xanh tại một trong những vịnh biển đẹp nhất thế giới. Lặn ngắm san hô tại Hòn Mun, tham gia các trò chơi thể thao biển tại Bãi Tranh và thưởng thức tiệc hải sản tươi sống ngay trên bè nổi.", 
            "08:30 – Đón khách tại cảng Vĩnh Trường\n09:30 – Tham quan Hòn Mun (Lặn ngắm san hô)\n11:30 – Đến Làng Chài dùng bữa trưa hải sản\n13:30 – Nghỉ ngơi & tắm biển tại Bãi Tranh\n16:00 – Cano đưa khách về lại đất liền", 
            "Cano cao tốc hiện đại\nĂn trưa hải sản thịnh soạn\nKính lặn, ống thở, áo phao\nVé vào cổng các đảo", 
            "Các trò chơi dù lượn, mô tô nước\nLặn bình khí chuyên dụng", 
            0, "https://images.unsplash.com/photo-1627440614110-381016898492", "", DEFAULT_VIDEO, "HOT", 4.9f, 860, 3100, "2026-01-01", "2026-12-31");

        // 13. Tây Ninh
        insertTour(db, "Tây Ninh: Chinh Phục Núi Bà Đen - Nóc Nhà Nam Bộ", "Tây Ninh", "1 ngày", "1.050.000đ", 
            "Hành trình tâm linh về với 'Đệ nhất thiên sơn'. Chinh phục đỉnh núi Bà Đen cao 986m bằng hệ thống cáp treo hiện đại, chiêm bái tượng Phật Bà bằng đồng cao nhất Châu Á và tham quan Tòa Thánh Cao Đài với kiến trúc độc nhất vô nhị.", 
            "07:00 – Khởi hành từ TP.HCM\n09:30 – Lên cáp treo đỉnh núi Bà Đen\n11:00 – Tham quan quần thể tâm linh, tượng Phật Bà\n12:30 – Ăn trưa buffet trên đỉnh núi\n14:30 – Tham quan Tòa Thánh Tây Ninh\n17:30 – Trở về TP.HCM", 
            "Xe du lịch đời mới\nVé cáp treo khứ hồi (Đỉnh & Chùa)\nBuffet trưa cao cấp trên núi\nHướng dẫn viên nhiệt tình", 
            "Đồ uống phát sinh\nChi phí lễ cúng", 
            0, "https://res.cloudinary.com/dgiu7ewoy/image/upload/v1774414423/120246-vna_potal_khu_du_lich_quoc_gia_nui_ba_den_thu_hut_khach_du_lich_ngay_dau_nam_moi_phong_vien_phat_tin_ve_ban_da_thong_qua_truong_cqtt_a_7786251_yv5ccp.jpg", "", DEFAULT_VIDEO, "SPIRITUAL", 4.7f, 510, 2450, "2026-01-01", "2026-12-31");

        // 14. Phan Thiết
        insertTour(db, "Phan Thiết: Nghỉ Dưỡng Resort & Biển Xanh Nắng Vàng", "Bình Thuận", "2N1Đ", "1.950.000đ", 
            "Kỳ nghỉ lý tưởng tại những resort cao cấp ven biển Mũi Né - Phan Thiết. Tận hưởng bãi biển xanh ngắt, cát trắng mịn màng và tham quan tháp Chàm Poshanư cổ kính mang đậm dấu ấn văn hóa Chăm.", 
            "Ngày 1:\n08:00 – TP.HCM đi Phan Thiết\n12:30 – Nhận phòng resort, ăn trưa hải sản\n15:00 – Tự do tắm biển, hồ bơi\n\nNgày 2:\n08:30 – Tham quan tháp Poshanư, Lầu Ông Hoàng\n11:00 – Trả phòng, mua sắm nước mắm Phan Thiết\n14:00 – Trở về TP.HCM", 
            "Xe limousine đưa đón\nResort 4 sao sát biển\nĂn sáng buffet & 02 bữa chính\nBảo hiểm du lịch", 
            "Chi phí cá nhân\nĐồ uống ngoài chương trình", 
            0, "https://images.unsplash.com/photo-1506748686214-e9df14d4d9d0", "", DEFAULT_VIDEO, "RELAX", 4.5f, 210, 950, "2026-01-01", "2026-12-31");

        // 15. Côn Đảo
        insertTour(db, "Côn Đảo: Hành Trình Tâm Linh - Về Nguồn Đất Thiêng", "Bà Rịa - Vũng Tàu", "3N2Đ", "5.500.000đ", 
            "Tìm về vùng đất lịch sử linh thiêng nơi ghi dấu sự hy sinh anh dũng của các chiến sĩ cách mạng. Viếng mộ cô Sáu tại nghĩa trang Hàng Dương vào ban đêm và tận hưởng vẻ đẹp hoang sơ của bãi Nhát, bãi Đầm Trầu.", 
            "Ngày 1:\n10:00 – Đón sân bay Côn Đảo\n14:00 – Tham quan hệ thống Nhà tù Côn Đảo\n23:30 – Viếng nghĩa trang Hàng Dương đêm\n\nNgày 2:\n08:30 – Check-in Bãi Nhát, đỉnh Tình Yêu\n14:00 – Tắm biển Bãi Đầm Trầu thơ mộng\n\nNgày 3:\n09:00 – Mua sắm đặc sản hạt bàng\n11:30 – Kết thúc tour, tiễn sân bay", 
            "Vé máy bay khứ hồi (HCM/HN)\nKhách sạn tiêu chuẩn 3 sao\nHướng dẫn viên am hiểu lịch sử\nXe tham quan trên đảo", 
            "Đồ cúng lễ\nChi tiêu ngoài lịch trình", 
            0, "https://images.unsplash.com/photo-1589118949245-7d38baf380d6", "", DEFAULT_VIDEO, "SPECIAL", 4.9f, 380, 1200, "2026-01-01", "2026-12-31");

        // 16. Quy Nhơn - Bình Định
        insertTour(db, "Quy Nhơn: Kỳ Co - Eo Gió - Thiên Đường Maldives Việt Nam", "Bình Định", "1 ngày", "850.000đ", 
            "Khám phá bãi tắm Kỳ Co với làn nước xanh trong vắt thấy đáy. Check-in Eo Gió - nơi ngắm hoàng hôn đẹp nhất Việt Nam và chiêm bái tượng Phật đôi cao nhất Việt Nam tại chùa Ngọc Hòa.", 
            "08:00 – Đón khách tại Quy Nhơn\n09:00 – Cano đi bãi tắm Kỳ Co\n11:30 – Lặn ngắm san hô bãi Dứa\n12:30 – Ăn trưa hải sản tươi sống\n14:30 – Tham quan Eo Gió, Chùa Ngọc Hòa\n16:30 – Trở về thành phố", 
            "Cano cao tốc khứ hồi\nĂn trưa hải sản địa phương\nVé tham quan Kỳ Co & Eo Gió\nHướng dẫn viên", 
            "Dịch vụ đi bộ dưới biển\nNước uống cá nhân", 
            0, "https://images.unsplash.com/photo-1507525428034-b723cf961d3e", "", DEFAULT_VIDEO, "MUST GO", 4.8f, 620, 2800, "2026-01-01", "2026-12-31");

        // 17. Phú Yên
        insertTour(db, "Phú Yên: Gành Đá Đĩa - Hoa Vàng Trên Cỏ Xanh", "Phú Yên", "1 ngày", "720.000đ", 
            "Chạm tay vào kiệt tác thiên nhiên Gành Đá Đĩa với những khối đá lăng trụ xếp chồng lên nhau kỳ ảo. Tham quan Bãi Xép - nơi xuất hiện trong bộ phim nổi tiếng, ngắm biển xanh ngắt từ vách đá hùng vĩ.", 
            "08:00 – Đón khách tại Tuy Hòa\n09:30 – Tham quan Gành Đá Đĩa\n11:00 – Ghé thăm Nhà thờ Mằng Lăng cổ kính\n12:30 – Ăn trưa đầm Ô Loan (Sò huyết nổi tiếng)\n14:30 – Check-in Bãi Xép - Gành Ông\n16:30 – Trở về Tuy Hòa", 
            "Xe du lịch đưa đón\nVé tham quan tất cả các điểm\nBữa trưa tại đầm Ô Loan\nNước uống & khăn lạnh", 
            "Mua sắm đặc sản\nTiền tip HDV", 
            0, "https://images.unsplash.com/photo-1506744038136-46273834b3fb", "", DEFAULT_VIDEO, "TOP RATED", 4.7f, 410, 1950, "2026-01-01", "2026-12-31");

        // 18. Vũng Tàu
        insertTour(db, "Vũng Tàu: Biển Xanh Nắng Vàng - Check-in Tượng Chúa Kitô", "Bà Rịa - Vũng Tàu", "1 ngày", "550.000đ", 
            "Chuyến đi ngắn ngày hoàn hảo để nạp lại năng lượng. Chinh phục tượng Chúa Kitô Vua ngắm toàn cảnh thành phố biển, tham quan Bạch Dinh lãng mạn và thưởng thức bánh khọt Bà Hai nổi tiếng.", 
            "07:00 – Khởi hành từ TP.HCM\n09:30 – Leo núi Tao Phùng, viếng tượng Chúa Kitô\n11:30 – Ăn trưa đặc sản bánh khọt/Hải sản\n13:30 – Tham quan Bạch Dinh, Niết Bàn Tịnh Xá\n15:00 – Tắm biển Bãi Sau\n17:30 – Trở về TP.HCM", 
            "Xe limousine đưa đón\nĂn trưa món ngon Vũng Tàu\nVé vào cổng các điểm\nHướng dẫn viên vui vẻ", 
            "Thuê ghế dù bãi biển\nĐồ uống ngoài thực đơn", 
            0, "https://images.unsplash.com/photo-1537996194471-e657df975ab4", "", DEFAULT_VIDEO, "HOT", 4.5f, 920, 4200, "2026-01-01", "2026-12-31");

        // 19. Đắk Lắk
        insertTour(db, "Đắk Lắk: Khám Phá Thủ Phủ Cà Phê - Buôn Ma Thuột", "Đắk Lắk", "3N2Đ", "2.900.000đ", 
            "Đến với cao nguyên đại ngàn để thưởng thức hương vị cà phê nguyên bản nhất. Tham quan bảo tàng Thế giới Cà phê kiến trúc độc đáo, cưỡi voi tại Buôn Đôn và chiêm ngưỡng sự hùng vĩ của cụm thác Dray Nur - Dray Sap.", 
            "Ngày 1:\n10:00 – Đón sân bay Buôn Ma Thuột\n14:00 – Tham quan Bảo tàng Thế giới Cà Phê\n18:30 – Ăn tối cơm lam, gà nướng bản địa\n\nNgày 2:\n08:30 – Khám phá Buôn Đôn, cầu treo sông Serepok\n14:00 – Tham quan thác Dray Nur hùng vĩ\n\nNgày 3:\n08:00 – Ghé thăm Hồ Lắk, đi thuyền độc mộc\n11:30 – Kết thúc tour, tiễn sân bay", 
            "Xe du lịch máy lạnh\nKhách sạn tiêu chuẩn 3-4 sao\nVé vào cổng tham quan\nCác bữa ăn đậm chất Tây Nguyên", 
            "Cưỡi voi/Chèo thuyền Hồ Lắk\nChi phí cá nhân", 
            0, "https://images.unsplash.com/photo-1591147139263-95aa2f19be00", "", DEFAULT_VIDEO, "MUST TRY", 4.6f, 310, 1300, "2026-01-01", "2026-12-31");

        // 20. Cao Bằng
        insertTour(db, "Cao Bằng: Thác Bản Giốc - Động Ngườm Ngao", "Cao Bằng", "3N2Đ", "3.600.000đ", 
            "Chiêm ngưỡng một trong những thác nước xuyên biên giới lớn nhất thế giới. Thác Bản Giốc mang vẻ đẹp tráng lệ giữa núi rừng biên cương, cùng với động Ngườm Ngao - hang động thạch nhũ lộng lẫy và suối Lê-nin xanh trong kỳ ảo.", 
            "Ngày 1:\n06:00 – Khởi hành từ Hà Nội đi Cao Bằng\n15:00 – Tham quan suối Lê-nin, hang Pác Bó\n\nNgày 2:\n08:30 – Tham quan Thác Bản Giốc hùng vĩ\n11:30 – Ăn trưa vịt quay 7 vị Cao Bằng\n14:00 – Khám phá Động Ngườm Ngao\n\nNgày 3:\n08:00 – Ghé thăm làng rèn Phúc Sen\n12:00 – Ăn trưa, lên xe về lại Hà Nội", 
            "Xe limousine chuyên tuyến vùng cao\nKhách sạn tiêu chuẩn tốt nhất\nVé tham quan & đi bè tại thác\nHướng dẫn viên nhiệt tình", 
            "Tiền tip lái xe & HDV\nBữa tối tự do khám phá", 
            0, "https://res.cloudinary.com/dgiu7ewoy/image/upload/v1774414752/Ban_Gioc_-_Detian_Falls2_uic1wb.jpg", "", DEFAULT_VIDEO, "SPECIAL", 4.9f, 250, 1100, "2026-05-01", "2026-09-30");

        // Seed other tables
        insertHotel(db, "Vinpearl Phú Quốc Resort", "Phú Quốc", "Khu nghỉ dưỡng 5 sao sang trọng bậc nhất với bãi biển riêng và tiện nghi cao cấp.", "2.500.000đ", 0, "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb", 4.7f, 300);
        insertHotel(db, "Hotel de la Coupole - MGallery", "Sa Pa", "Kiến trúc Pháp hòa quyện nét văn hóa dân tộc H'Mông độc bản tại trung tâm Sapa.", "3.200.000đ", 0, "https://images.unsplash.com/photo-1591340156063-e31b67f13f1e", 4.8f, 450);
        
        insertCombo(db, "Siêu Combo Phú Quốc 3N2Đ", "Kiên Giang", "Vé máy bay + VinOasis 5 sao + Buffet sáng & Vé VinWonders.", "7.500.000đ", "5.850.000đ", 0, "https://images.unsplash.com/photo-1542314831-068cd1dbfeeb", 4.9f, "BEST SELLER");

        insertTicket(db, "Vé Máy Bay Hà Nội - Đà Nẵng", "Năm 2026", "1.590.000đ", "Giảm 20%", "Khứ hồi", 0, "https://images.unsplash.com/photo-1436491865332-7a61a109cc05");
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

    public boolean insertOrder(String title, String date, int people, String name, String phone) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("title", title);
        values.put("date", date);
        values.put("people", people);
        values.put("name", name);
        values.put("phone", phone);

        long result = db.insert(TABLE_ORDERS, null, values);
        return result != -1;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int n) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TOURS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HOTELS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMBOS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TICKETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ORDERS);
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

    public List<Tour> getAllTours() {
        return getToursOrderedBy("book_count DESC, rating DESC");
    }

    // Backend-style source for /tours (already sorted by popularity first)
    public List<Tour> getTours() {
        return getAllTours();
    }

    public List<Tour> getPopularTours() {
        return getPopularTours(5);
    }

    // Backend-style source for /tours/popular where limit is kept in [5..10]
    public List<Tour> getPopularTours(int limit) {
        int safeLimit = Math.max(5, Math.min(10, limit));
        return getToursOrderedBy("book_count DESC, rating DESC LIMIT " + safeLimit);
    }

    public boolean canBookTour(String tourTitle, String selectedDateYmd) {
        if (tourTitle == null || tourTitle.trim().isEmpty()) return false;

        Cursor c = getReadableDatabase().query(
                TABLE_TOURS,
                new String[]{COLUMN_START_DATE, COLUMN_END_DATE},
                COLUMN_TITLE + " = ?",
                new String[]{tourTitle.trim()},
                null,
                null,
                null,
                "1"
        );

        try {
            if (!c.moveToFirst()) return false;
            String start = c.getString(c.getColumnIndexOrThrow(COLUMN_START_DATE));
            String end = c.getString(c.getColumnIndexOrThrow(COLUMN_END_DATE));

            Date targetDate = null;
            if (selectedDateYmd != null && !selectedDateYmd.trim().isEmpty()) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                try {
                    targetDate = sdf.parse(selectedDateYmd.trim());
                } catch (ParseException ignored) {
                    targetDate = null;
                }
            }

            return Tour.isActiveOn(start, end, targetDate);
        } finally {
            c.close();
        }
    }

    private List<Tour> getToursOrderedBy(String orderBy) {
        List<Tour> list = new ArrayList<>();
        Cursor c = getReadableDatabase().query(TABLE_TOURS, null, null, null, null, null, orderBy);
        if (c.moveToFirst()) {
            do {
                String imageUrl = c.getString(c.getColumnIndexOrThrow(COLUMN_IMAGE_URL));
                String rawImageUrls = null;
                int imageUrlsIndex = c.getColumnIndex(COLUMN_IMAGE_URLS);
                if (imageUrlsIndex >= 0) {
                    rawImageUrls = c.getString(imageUrlsIndex);
                }

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
        return db.rawQuery("SELECT * FROM " + TABLE_ORDERS, null);
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
