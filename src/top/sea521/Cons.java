package top.sea521;

import java.util.HashMap;
import java.util.Map;

public class Cons {


    public static boolean isDebug = true;


    /**
     * 3 用户权限key
     */
    public final static String PERMISSION_KEY = "permission";

    //登录过期
    public final static int LOGIN_OVERDUE = 100;

    //登录失败
    public final static int LOGIN_FAIL = 101;
    //参数错误
    public final static int PARAMETER_ERROR = 102;
    //数据错误
    public final static int DATA_ERROR = 103;
    //未知错误
    public final static int UNKNOWN_ERROR = 104;
    //未查到
    public final static int parameterNull = 105;
    //删除失败
    public final static int DELECT_ERROR = 107;
    //无权限
    public final static int NO_PERMISSION = 106;
    //文件存储错误
    public final static int FILE_ERROR = 108;

    //操作成功
    public final static int OPERATION_SUCCESS = 200;


    /*************测试 begin *************/
//    public final static String ali_baseUrl_my = "http://192.168.0.6:8083/MHT/";
//    public final static String tencent_baseUrl_my = "http://192.168.1.121:8083/MHT/";
//
//    public final static String baseUrl_my = ali_baseUrl_my;
//
//    public final static String path ="C:\\MHTFile\\APP\\";
//    public final static String picture = "C:\\MHTFile\\picture\\";
//    //    Android路径
//    public final static String path_android = path + "Android\\";
//    public final static String judgeFile = "C:\\MHTFile\\txt\\judge.txt";
//    public final static String pushInfoFile = "C:\\MHTFile\\txt\\pushInfo.txt";
//    public final static String pushInfoFileEn = "C:\\MHTFile\\txt\\pushInfoEn.txt";
//    public final static String temp= "C:\\MHTFile\\temp\\";
//    public final static String  hotTemp = "C:\\MHTFile\\hotTemp\\";
//    public final static String logoimage_native = "C:\\MHTFile\\logoimage\\";
//    public final static String temp_font_native = "C:\\MHTFile\\tempFont\\";
//    public final static String print_content = "C:\\MHTFile\\printContent\\";
//    public final static String apkFile = "C:\\MHTFile\\apkFileDealer\\";
//    public final static String print_content_lable = "C:\\MHTFile\\printContentLable\\";
//    public final static String print_content_receipt = "C:\\MHTFile\\printContentReceipt\\";
//    public final static String label_storage = "C:\\MHTFile\\labelStorage\\";
//    public final static String log = "C:\\MHTFile\\Log.txt\\";
//    public final static String temp_image = "C:\\MHTFile\\tempImage\\";
//
//    public final static String languageTypeTemp = "C:\\MHTFile\\languageTypeTemp\\";
//    public final static String logoTypeTemp = "C:\\MHTFile\\logoTypeTemp\\";
//    public final static String MARKET_IMAGE_PATH = "C:\\MHTFile\\market\\image\\";
    /*************测试  end *************

     /*************服务器 begin ***********/
    public final static String ali_baseUrl_my = "https://www.mhtclouding.com/MHT/";
    public static String baseUrl_my = ali_baseUrl_my;


    //    注意，以后需要存储资源，要再三考虑，是否要把BaseUrl也放入数据库中
    public final static String path = "/root/MHTFile/APP/";
    public final static String picture = "/root/MHTFile/picture/";
    //Android路径
    public final static String path_android = path + "Android/";
    public final static String judgeFile = "/root/MHTFile/txt/judge.txt";
    public final static String pushInfoFile = "/root/MHTFile/txt/pushInfo.txt";
    public final static String pushInfoFileEn = "/root/MHTFile/txt/pushInfoEn.txt";
    public final static String hotTemp = "/root/MHTFile/hotTemp/";

    public final static String print_content = "/root/MHTFile/printContent/";

    public final static String print_content_lable = "/mnt/MHTFile/printContentLable/";
    public final static String print_content_receipt = "/mnt/MHTFile/printContentReceipt/";
    public final static String temp = "/mnt/MHTFile/temp/";
    public final static String label_storage = "/mnt/MHTFile/labelStorage/";
    public final static String apkFile = "/root/MHTFile/apkFileDealer/";
    public final static String logoimage_native = "/root/MHTFile/logoimage/";
    public final static String temp_font_native = "/root/MHTFile/tempFont/";
    public final static String temp_image = "/mnt/MHTFile/tempImage/";
    public final static String languageTypeTemp = "/mnt/MHTFile/languageTypeTemp/";
    public final static String logoTypeTemp = "/mnt/MHTFile/logoTypeTemp/";
    public final static String MARKET_IMAGE_PATH = "/mnt/MHTFile/market/image/";
    /************服务器 end *************/

    /************************ 路径 start ***********************/
    //模板保存路径
//    在版本之后，关于Android与iOS端，就只会改变到了temp文件加，上次模板的时候。
//    logoimage_native是管理后台，后面一切都不要在腾讯云的管理后台中做！
    public final static String temp_font = "/MHTFile/tempFont/";
    public final static String apk_file_sql = "/MHTFile/apkFileDealer/";
    public final static String logoimage = "/MHTFile/logoimage/";
    public final static String logoimage_url = baseUrl_my + logoimage;
    public final static String print_content_sql = "/MHTFile/printContent/";
    public final static String print_content_lable_sql = "/MHTFile1/printContentLable/";
    public final static String print_content_receipt_sql = "/MHTFile1/printContentReceipt/";
    public final static String lable_storage_sql = "/MHTFile1/labelStorage/";
    public final static String temp_image_sql = "/MHTFile1/tempImage/";
    /*存储图片*/
    public final static String picUrl = baseUrl_my + "MHTFile/picture/";
    public final static String androidUrl = baseUrl_my + "MHTFile/APP/Android/";
    public final static String iosUrl = baseUrl_my + "MHTFile/APP/ios/";
    /*默认的用户头像位置*/
    public final static String PICPATH = picUrl + "default_head.png";
    /************************ 路径 end  ***********************/

    /**
     * 分页基数
     */
    public final static int LIMIT_BASE = 10;

    public final static int NORMAL_CODE = 1;


    public static Map<String, Integer> modelMap = new HashMap<String, Integer>();
    public static Map<String, String> languageTypeTempMap = new HashMap<String, String>();

    static {
        modelMap.put("100001", 1);
        modelMap.put("100002", 1);
        modelMap.put("100101", 1);
        modelMap.put("100102", 1);
        modelMap.put("100201", 1);
        modelMap.put("100202", 1);
        modelMap.put("100301", 1);
        modelMap.put("100302", 1);
        modelMap.put("100401", 1);
        modelMap.put("100402", 1);
        modelMap.put("10000", 1);
        modelMap.put("wifi_P16", 1);
        languageTypeTempMap.put("EN", "EN");
    }
}
