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
import com.example.vntravelapp.models.MapItem;
import com.example.vntravelapp.models.Tour;
import com.example.vntravelapp.models.TicketOffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String DATABASE_NAME = "vntravel.db";
    private static final int DATABASE_VERSION = 24;

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
    private static final String COLUMN_LATITUDE = "latitude";
    private static final String COLUMN_LONGITUDE = "longitude";

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
                COLUMN_LATITUDE + " REAL DEFAULT 0, " +
                COLUMN_LONGITUDE + " REAL DEFAULT 0)");
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
                buildImageList("https://images.unsplash.com/photo-1643029891412-92f9a81a8c16?q=80&w=1486&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"),
                DEFAULT_VIDEO,
                "HOT",
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
                buildImageList("https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773591709/4b0d87f2-f7e3-48c5-9496-ad7c04c99379.png"),
                DEFAULT_VIDEO,
                "BEST SELLER",
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
                buildImageList("https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626520/1a3903f1-7c6a-4458-9fc6-519589789751.png"),
                DEFAULT_VIDEO,
                "TREKKING",
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
                buildImageList("https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626555/2f7a9346-9a04-4efc-b96a-73652ed4c945.png"),
                DEFAULT_VIDEO,
                "HISTORIC",
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
                buildImageList("https://res.cloudinary.com/dzjlcbwwh/image/upload/v1773626599/9e94999d-423a-4d23-a187-41317434956c.png"),
                DEFAULT_VIDEO,
                "FAMILY",
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
                buildImageList("https://images.unsplash.com/photo-1507525428034-b723cf961d3e"),
                DEFAULT_VIDEO,
                "NEW",
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
                buildImageList("https://images.unsplash.com/photo-1500530855697-b586d89ba3ee"),
                DEFAULT_VIDEO,
                "CITY",
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
                buildImageList("https://images.unsplash.com/photo-1500375592092-40eb2168fd21"),
                DEFAULT_VIDEO,
                "SEA",
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
                buildImageList("https://images.unsplash.com/photo-1470770841072-f978cf4d019e"),
                DEFAULT_VIDEO,
                "ISLAND",
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
                buildImageList("https://images.unsplash.com/photo-1501785888041-af3ef285b470"),
                DEFAULT_VIDEO,
                "PHUOT",
                4.8f,
                110
        );

        // --- NEW 10 TOURS (MARKETING) ---
        insertTour(db, "Côn Đảo - Huyền Thoại Biển Xanh", "Bà Rịa - Vũng Tàu", "3N2Đ", "5.900.000đ", 
                "Bạn đã bao giờ thức giấc giữa tiếng sóng vỗ rì rào của một trong những bãi biển hoang sơ nhất hành tinh? Côn Đảo không chỉ là trang sử hào hùng mà còn là thiên đường nhiệt đới đẹp nao lòng.\n\n✨ Điểm nổi bật:\n• Thả rùa con về biển tại Hòn Bảy Cạnh.\n• Lặn ngắm san hô đa sắc màu và khám phá hệ sinh thái biển nguyên vẹn.\n• Check-in bãi Đầm Trầu, ngắm máy bay hạ cánh sát sàn sạt trên đỉnh đầu.\n\nPhù hợp cho: Tâm hồn tìm kiếm sự bình yên, cặp đôi và người yêu thiên nhiên.",
                "Ngày 1: Bay tới Côn Đảo → Tắm bãi Đầm Trầu\nNgày 2: Hòn Bảy Cạnh thả rùa\nNgày 3: Khám phá chợ Côn Đảo → Về",
                "Vé máy bay, Resort cao cấp 4*, Ăn đặc sản hải sản", "Chi phí cá nhân", 0, "https://images.unsplash.com/photo-1596422846543-7f2cb24ba0c7", buildImageList("https://images.unsplash.com/photo-1596422846543-7f2cb24ba0c7"), DEFAULT_VIDEO, "LUXURY", 4.9f, 250);

        insertTour(db, "Y Tý Sapa - Chạm Tay Vào Mây Ngàn", "Lào Cai", "3N2Đ", "4.100.000đ", 
                "Rời xa khói bụi thành phố, bạn có muốn một lần đứng trên đỉnh núi cao, ngắm nhìn biển mây bồng bềnh ngay dưới chân mình? Hành trình săn mây Y Tý chính là chốn tiên cảnh thực sự giữa non cao Tây Bắc.\n\n✨ Điểm nổi bật:\n• Săn mây cực đỉnh tại núi Lảo Thẩn - 'nóc nhà thứ 2' của Y Tý.\n• Check-in bản làng người Hà Nhì với những ngôi nhà trình tường nấm độc đáo.\n• Thưởng thức lợn bản cắp nách, gà đồi nướng mắc khén thơm lừng.\n\nPhù hợp cho: Tín đồ xê dịch, đam mê nhiếp ảnh và trekking.",
                "Ngày 1: Hà Nội → Sapa → Y Tý\nNgày 2: Trekking Lảo Thẩn săn mây\nNgày 3: Khám phá kiến trúc nhà trình tường → Trở về",
                "Xe giường nằm VIP, Homestay bản địa view mây", "Đồ uống cá nhân, Tip porter", 0, "https://images.unsplash.com/photo-1520108343750-f8670ab03b6d", buildImageList("https://images.unsplash.com/photo-1520108343750-f8670ab03b6d"), DEFAULT_VIDEO, "TREKKING", 4.8f, 180);

        insertTour(db, "Vịnh Lan Hạ - Trái Tim Xanh Của Cát Bà", "Hải Phòng", "2N1Đ", "3.200.000đ", 
                "Cần gì đi đâu xa khi ngay Vịnh Bắc Bộ có một 'tiểu Hạ Long' thanh bình và quyến rũ đến thế! Hãy lên du thuyền và thả mình vào không gian bình lặng của Vịnh Lan Hạ mộng mơ.\n\n✨ Điểm nổi bật:\n• Nghỉ đêm trên du thuyền Boutique 5 sao xịn xò.\n• Chèo thuyền Kayak xuyên qua hang Sáng Tối kỳ ảo.\n• Thưởng tiệc Sunset Party với hoàng hôn màu ráng đỏ cực chill.\n\nPhù hợp cho: Cặp đôi lãng mạn, gia đình cần nghỉ dưỡng.",
                "Ngày 1: Bến Bèo → Check-in du thuyền\nNgày 2: Đạp xe làng chài Việt Hải → Trở Về",
                "Du thuyền 5*, Kayak, Xe đạp, Các bữa ăn sang trọng", "Chi phí massage trên tàu, Đồ uống", 0, "https://images.unsplash.com/photo-1559494396-7ef2f20de9c8", buildImageList("https://images.unsplash.com/photo-1559494396-7ef2f20de9c8"), DEFAULT_VIDEO, "CRUISE", 4.8f, 310);

        insertTour(db, "Đà Lạt Glamping - Bản Tình Ca Kẻ Du Mục", "Lâm Đồng", "2N1Đ", "2.500.000đ", 
                "Đổi gió với trải nghiệm ngủ lều 'sang chảnh' giữa rừng thông reo ngút ngàn của Đà Lạt! Đón bình minh mờ sương và nhâm nhi ly cafe nóng rực bên bếp lửa bập bùng, bạn đã sẵn sàng?\n\n✨ Điểm nổi bật:\n• Ngủ lều Mông Cổ siêu decor giữa rừng thông - tha hồ sống ảo.\n• Tiệc BBQ nướng thịt ngoài trời, nhâm nhi rượu vang ấm áp.\n• Đón bình minh săn mây và thức dậy với bữa sáng Floating mây bồng bềnh.\n\nPhù hợp cho: Nhóm bạn 'chill phết', couple hẹn hò.",
                "Ngày 1: Check-in khu Glamping → Tiệc BBQ & Acoustic\nNgày 2: Thức giấc săn mây → Thưởng thức cafe sáng",
                "Lều Glamping cao cấp, Tiệc BBQ tối", "Chi phí di chuyển đến Đà Lạt", 0, "https://images.unsplash.com/photo-1523987355523-c7b5b0dd90a7", buildImageList("https://images.unsplash.com/photo-1523987355523-c7b5b0dd90a7"), DEFAULT_VIDEO, "GLAMPING", 4.7f, 400);

        insertTour(db, "Măng Đen - 'Đà Lạt Thứ Hai' Yên Bình", "Kon Tum", "3N2Đ", "3.800.000đ", 
                "Giữa đại ngàn Tây Nguyên sừng sững, có một 'nàng thơ' Măng Đen đang ẩn mình. Không ồn ào, không khói bụi, chỉ có tiếng thông reo, thác đổ và không khí trong trẻo lạnh buốt.\n\n✨ Điểm nổi bật:\n• Tham quan thác Pa Sỹ cuồn cuộn đổ trắng xóa.\n• Dạo bước dưới hàng thông trăm tuổi, rảo bước bên hồ Đăk Ke.\n• Thưởng thức gà nướng bản Đôn, cơm lam dẻo thơm.\n\nPhù hợp cho: Những ai muốn bỏ trốn khỏi phố thị xô bồ.",
                "Ngày 1: Pleiku → Kon Tum → Măng Đen\nNgày 2: Thác Pa Sỹ → Hồ Đăk Ke\nNgày 3: Săn mây Măng Đen → Trở về",
                "Xe di chuyển toàn tuyến, Khách sạn/Villa", "Chi phí cá nhân", 0, "https://images.unsplash.com/photo-1447752875215-b2761acb3c5d", buildImageList("https://images.unsplash.com/photo-1447752875215-b2761acb3c5d"), DEFAULT_VIDEO, "NATURE", 4.8f, 215);

        insertTour(db, "Nha Trang - Đi Bộ Dưới Đáy Biển", "Khánh Hòa", "3N2Đ", "5.200.000đ", 
                "Biến giấc mơ dạo bước dưới thủy cung thành hiện thực! Không cần biết bơi, bạn vẫn dễ dàng tận mắt ngắm nhìn thế giới san hô rực rỡ và những đàn cá sặc sỡ bơi lội quanh mình.\n\n✨ Điểm nổi bật:\n• Trải nghiệm Seawalker (đi bộ dưới đáy biển) cực kỳ an toàn.\n• Quẩy hết mình trên du thuyền tổ chức tiệc bọt tuyết bồng bềnh.\n• Ăn sập hải sản tươi rói tại làng chài truyền thống.\n\nPhù hợp cho: Giới trẻ năng động, tín đồ thể thao biển.",
                "Ngày 1: Đón vịnh → Tự do dạo biển đêm\nNgày 2: Tour VIP 3 đảo → Đi bộ dưới đáy biển\nNgày 3: Mua sắm đặc sản → Kết thúc",
                "Khách sạn 4* view biển, Vé Seawalker", "Flyboard và các trò cảm giác mạnh khác", 0, "https://images.unsplash.com/photo-1544551763-46a013bb70d5", buildImageList("https://images.unsplash.com/photo-1544551763-46a013bb70d5"), DEFAULT_VIDEO, "SEA", 4.9f, 350);

        insertTour(db, "Cù Lao Chàm - Hóa Thân Thành Ngư Dân", "Quảng Nam", "1N", "1.100.000đ", 
                "Chỉ mất 20 phút cano lướt sóng, bạn đã đặt chân lên một khu dự trữ sinh quyển thế giới! Hãy lột bỏ lớp da thành thị để hóa thân thành ngư dân biển đảo 1 ngày.\n\n✨ Điểm nổi bật:\n• Lặn ngắm san hô tại hòn Tai với thảm sinh vật vô tiền khoáng hậu.\n• Check-in Bãi Ông cát trắng muốt, nước trong vắt màu ngọc bích.\n• Thưởng thức ngay nhum nướng mỡ hành vừa bắt.\n\nPhù hợp cho: Người thích hoạt động thể chất, khám phá thiên nhiên gần gũi.",
                "Sáng: Cano Cửa Đại → Đảo Cù Lao Chàm\nChiều: Tắm Bãi Ông → Thưởng thức chiến lợi phẩm",
                "Cano cao tốc 2 chiều, Vé sinh quyển, Bữa trưa hải sản", "Nước uống, Tiền tip", 0, "https://images.unsplash.com/photo-1538332576228-eb5b4c4de6f5", buildImageList("https://images.unsplash.com/photo-1538332576228-eb5b4c4de6f5"), DEFAULT_VIDEO, "ISLAND", 4.7f, 298);

        insertTour(db, "Nam Du - Vẻ Đẹp Hoang Sơ Gây Nghiện", "Kiên Giang", "2N1Đ", "2.900.000đ", 
                "Chưa bị thương mại hóa, quần đảo Nam Du giữ trọn vẹn vẻ hoang sơ, mộc mạc làm xao xuyến bao trái tim xê dịch. Chuẩn bị những bộ bikini rực rỡ nhất để 'sống ảo' cháy máy thôi!\n\n✨ Điểm nổi bật:\n• View trọn đại dương từ ngọn hải đăng Nam Du cao nhất đảo.\n• Thả dáng tại Bãi Mến - bãi biển có hàng dừa xanh nghiêng bóng tuyệt đẹp.\n• Nhậu hải sản bắt tại bờ, nhum nướng, mực chớp sống ngọt lịm.\n\nPhù hợp cho: Dân phượt, giới trẻ thích du lịch tự do.",
                "Ngày 1: Rạch Giá → Nam Du → Bãi Mến\nNgày 2: Ngọn Hải đăng → Săn ảnh bình minh → Trở về",
                "Vé tàu cao tốc khứ hồi, Nhà nghỉ sát biển", "Chi phí cá nhân", 0, "https://images.unsplash.com/photo-1506452305024-9d3f02d1c9b5", buildImageList("https://images.unsplash.com/photo-1506452305024-9d3f02d1c9b5"), DEFAULT_VIDEO, "HOANG SO", 4.6f, 150);

        insertTour(db, "Bù Gia Mập - Trekking Gọi Rừng Thiêng", "Bình Phước", "2N1Đ", "2.200.000đ", 
                "Bỏ lại ánh đèn neon chói lòa, ta tìm về rừng nguyên sinh Bù Gia Mập để lắng nghe hơi thở của tự nhiên. Hành trình đi bộ vượt rừng đầy thách thức và cũng vô cùng đền đáp!\n\n✨ Điểm nổi bật:\n• Trekking băng qua những tán cây cổ thụ khổng lồ.\n• Tự tay học cách sinh tồn: nướng thịt bằng ống tre, nấu cơm lam.\n• Ngủ võng giữa rừng già, nghe tiếng dế mèn và vượn hú đêm khuya.\n\nPhù hợp cho: Đam mê thể thao, người thích phiêu lưu sinh tồn.",
                "Ngày 1: Bắt đầu Trekking → Dựng trại rừng lõi\nNgày 2: Tắm thác Đắk Mai → Rút khỏi rừng → Trở về",
                "Xe đưa đón, HDV và khuân vác, Đồ cắm trại", "Đồ dùng cá nhân", 0, "https://images.unsplash.com/photo-1511497584788-876760111969", buildImageList("https://images.unsplash.com/photo-1511497584788-876760111969"), DEFAULT_VIDEO, "ADVENTURE", 4.9f, 95);

        insertTour(db, "Tây Ninh - Dấu Ấn Kiêu Hãnh Núi Bà", "Tây Ninh", "1N", "1.200.000đ", 
                "Xách balo lên đi ngay trong ngày để giải tỏa căng thẳng! Núi Bà Đen giờ đây quy tụ hệ thống cáp treo hiện đại và đỉnh núi tráng lệ như một bức tranh thần thoại.\n\n✨ Điểm nổi bật:\n• Đi cáp treo Sun World hiện đại băng qua mảng rừng xanh rì.\n• Chiêm bái tượng Phật Bà Tây Bổ Đà Sơn bằng đồng cao nhất Châu Á.\n• Thưởng thức đặc sản bò tơ Tây Ninh mềm ngọt xèo xèo.\n\nPhù hợp cho: Chuyến đi ngắn ngày cùng gia đình, nhóm bạn.",
                "Sáng: TP.HCM → Tây Ninh → Đi cáp treo\nChiều: Tự do check-in → Viếng chùa Bà",
                "Xe đưa đón khứ hồi, Vé cáp treo", "Mua sắm đặc sản mắm, trái cây", 0, "https://images.unsplash.com/photo-1518098268026-4e89f1a2cd8e", buildImageList("https://images.unsplash.com/photo-1518098268026-4e89f1a2cd8e"), DEFAULT_VIDEO, "DAY TRIP", 4.8f, 410);

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
                            int r, String u, String imageUrls, String video, String badge, float rat, int rev) {
        double[] coordinates = resolveCoordinates(l);
        insertTour(db, t, l, d, p, desc, itinerary, included, excluded, r, u, imageUrls, video, badge, rat, rev,
                coordinates[0], coordinates[1]);
    }

    private void insertTour(SQLiteDatabase db, String t, String l, String d,
                            String p, String desc, String itinerary,
                            String included, String excluded,
                            int r, String u, String imageUrls, String video, String badge, float rat, int rev,
                            double latitude, double longitude) {

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
        v.put(COLUMN_IMAGE_URLS, imageUrls);
        v.put(COLUMN_VIDEO_URL, video);
        v.put(COLUMN_BADGE, badge);
        v.put(COLUMN_RATING, rat);
        v.put(COLUMN_REVIEWS, rev);
        v.put(COLUMN_LATITUDE, latitude);
        v.put(COLUMN_LONGITUDE, longitude);

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
                        c.getDouble(c.getColumnIndexOrThrow(COLUMN_LATITUDE)),
                        c.getDouble(c.getColumnIndexOrThrow(COLUMN_LONGITUDE))
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
        if (urls == null) {
            return "";
        }
        for (String url : urls) {
            if (url == null) {
                continue;
            }
            String trimmed = url.trim();
            if (trimmed.isEmpty()) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append("|");
            }
            builder.append(trimmed);
        }
        return builder.toString();
    }

    private static List<String> parseImageUrls(String raw, String fallback) {
        List<String> list = new ArrayList<>();
        if (raw != null && !raw.trim().isEmpty()) {
            String[] parts = raw.split("\\|");
            for (String part : parts) {
                if (part == null) {
                    continue;
                }
                String trimmed = part.trim();
                if (!trimmed.isEmpty()) {
                    list.add(trimmed);
                }
            }
        }
        if (list.isEmpty() && fallback != null && !fallback.trim().isEmpty()) {
            list.add(fallback.trim());
        }
        return list;
    }

    private static double[] resolveCoordinates(String location) {
        if (location == null) {
            return new double[]{0.0, 0.0};
        }
        double[] values = LOCATION_COORDINATES.get(location.trim().toLowerCase(Locale.ROOT));
        if (values == null) {
            return new double[]{0.0, 0.0};
        }
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
        return coordinates;
    }
}
