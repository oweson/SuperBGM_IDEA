package top.sea521;

import javax.servlet.http.HttpSession;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class Util {
    /* 1 时间转化成字符串*/
    public static String getTime() {
        return TimeToString(System.currentTimeMillis());
    }

    /* 2 时间转化成字符串*/
    public static String getTime(long currentTimeMillis) {
        return TimeToString(System.currentTimeMillis());
    }

    /*
     * 3 时间格式化
     * */

    public static String TimeToString(long time) {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String now = format.format(calendar.getTime());
        return now;
    }



    /**
     * 6 日期格式化为指定的格式；
     */

    public static String TimeToStringFormat(long time, String format1) {
        if (format1 == null) {
            format1 = "yyyy/MM/dd";
        }
        DateFormat format = new SimpleDateFormat(format1);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        String now = format.format(calendar.getTime());
        return now;
    }


    /**
     * 7 判断版本号的固定格式。格式为：数字和"."
     */
    public static boolean checkVersion(String str) {
        //将String str转化成数组。比如说a45.d转化成[a,4,5,]
        String[] split = str.split("");
        for (int i = 0; i < split.length; i++) {
            String data = split[i];
            if (!isInteger(data)) {
                if (!data.equals(".")) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 8 判断字符串是否是数字
     */
    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 9 正则表达式判断字符串是否为数字。
     */
    public static boolean isInteger(String str) {
        return isNumeric(str);
//        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
//        return pattern.matcher(str).matches();
    }


    /**
     * 10 正则表达式判断注册时用户名是否只含有汉字、数字、字母、下划线，下划线位置不限
     */
    public static boolean isUsernameFormat(String str) {
        Pattern pattern = Pattern.compile("[A-Za-z0-9_\\_\\u4e00-\\u9fa5]{2,9}$");
        return pattern.matcher(str).matches();
    }

    /**
     * 11 正则表达式校验密码——由数字和字母组成，并且要同时含有数字和字母，且长度要在8-16位之间。
     */
    public static boolean isCustomerPassword(String str) {
        Pattern pattern = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$");
        return pattern.matcher(str).matches();
    }

    /**
     * 12 正则表达式校验手机号码——
     * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147
     * 联通号码段:130、131、132、136、185、186、145
     * 电信号码段:133、153、180、189
     */
    public static boolean isCheckCellPhone(String str) {
        String regex = "^((13[0-9])|(14[5,7,9])|(15([0-3]|[5-9]))|(166)|(17[0,1,3,5,6,7,8])|(18[0-9])|(19[8|9]))\\d{8}$";
        Pattern pattern = Pattern.compile(regex);
        return pattern.matcher(str).matches();
    }

    /**
     * 13 正则表达式校验固话
     */
    public static boolean isCheckTelephone(String str) {
        Pattern pattern = Pattern.compile("^(0\\\\d{2}-\\\\d{8}(-\\\\d{1,4})?)|(0\\\\d{3}-\\\\d{7,8}(-\\\\d{1,4})?)$");
        return pattern.matcher(str).matches();
    }

    /**
     * 14 校验地址
     */
    public static boolean checkPlace(String str) {
        int num = 0;
        //将一个字符串分割为子字符串，然后将结果作为字符串数组返回。
        String[] split = str.split("");
        for (int i = 0; i < split.length; i++) {
            String data = split[i];
            if (!isInteger(data)) {
                if (!data.equals(".")) {
                    return false;
                } else {
                    num++;
                    if (num > 1) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 15 操作成功
     */
    public static void setSuccess(JsonEntity jsonEntity, String msg) {
        setSuccess(null, jsonEntity, msg);
    }

    public static void setSuccess(JsonEntity jsonEntity) {
        setSuccess(null, jsonEntity, null);
    }

    public static void setSuccess(Object o, JsonEntity jsonEntity, String msg) {
        jsonEntity.setCode(Cons.OPERATION_SUCCESS);
        if (o != null) {
            jsonEntity.setContent(o);
        }
        if (msg != null) {
            jsonEntity.setMsg(msg);
        }
    }

    /**
     * 16 登录过期
     */
    public static void setLoginOverdue(JsonEntity jsonEntity) {
        setLoginOverdue(jsonEntity, "登陆过期");
    }

    public static void setLoginOverdue(JsonEntity jsonEntity, String msg) {
        jsonEntity.setCode(Cons.LOGIN_OVERDUE);
        jsonEntity.setMsg(msg);
    }

    /**
     * 17 未查到
     */
    public static void setNoFound(JsonEntity jsonEntity, String msg) {
        setNoFound(null, jsonEntity, msg);
    }

    public static void setNoFound(JsonEntity jsonEntity) {
        setNoFound(null, jsonEntity, "null");
    }

    public static void setNoFound(Object o, JsonEntity jsonEntity, String msg) {
        jsonEntity.setCode(Cons.PARAMETER_ERROR);
        if (o != null) {
            jsonEntity.setContent(o);
        }
        if (msg != null) {
            jsonEntity.setMsg(msg);
        }
    }

    public static void setNoDelect(JsonEntity jsonEntity, String msg) {
        setNoDelect(null, jsonEntity, msg);
    }

    public static void setNoDelect(Object o, JsonEntity jsonEntity, String msg) {
        jsonEntity.setCode(Cons.DELECT_ERROR);
        if (o != null) {
            jsonEntity.setContent(o);
        }
        if (msg != null) {
            jsonEntity.setMsg(msg);
        }
    }

    /**
     * 18 随机生成六位数验证码?
     *
     * @return
     */
    public static int getRandomNum() {
        Random r = new Random();
        return r.nextInt(900000) + 100000;
        //(Math.random()*(999999-100000)+100000)
    }


    /**
     * 19 获取UUID
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }


    /**
     * 20 判断某个参数是否为null
     *
     * @return
     */
    public static boolean judgeParameter(Object... parameters) {

        for (Object parameter : parameters) {
            if (parameter == null || parameter.equals("") || parameter.equals("null")) {
                return false;
            }
        }
        return true;
    }

    /**
     * 21 参数错误
     *
     * @param jsonEntity
     */
    public static void parameterError(JsonEntity jsonEntity) {
        jsonEntity.setCode(Cons.PARAMETER_ERROR);
        jsonEntity.setMsg("参数错误");
        jsonEntity.setContent(null);
    }

    /**
     * 22
     * 删除失败
     */
    public static void delectError(JsonEntity jsonEntity) {
        jsonEntity.setCode(Cons.DELECT_ERROR);
        jsonEntity.setMsg("删除失败");
        jsonEntity.setContent("null");
    }

    /**
     * 23
     * 未查到
     */
    public static void parameterNull(JsonEntity jsonEntity) {
        jsonEntity.setCode(Cons.parameterNull);
        jsonEntity.setMsg("未查到");
        jsonEntity.setContent("null");
    }

    /**
     * 24 数据错误
     *
     * @param jsonEntity
     */
    public static void dataError(JsonEntity jsonEntity) {
        jsonEntity.setCode(Cons.DATA_ERROR);
        jsonEntity.setMsg("数据错误");
        jsonEntity.setContent("null");
    }

    /**
     * 25 权限不足
     */
    public static void noPermission(JsonEntity jsonEntity) {
        jsonEntity.setCode(Cons.NO_PERMISSION);
        jsonEntity.setMsg("无相应权限");
        jsonEntity.setTotal(null);
        jsonEntity.setContent(null);
    }

    /**
     * 26 检查权限
     */
    public static boolean checkSuperAdminPermission(JsonEntity jsonEntity, HttpSession session) {

        Integer permission = (Integer) session.getAttribute(Cons.PERMISSION_KEY);
        if (permission == null || permission < 101) {//不是超级管理员权限
            Util.noPermission(jsonEntity);
            return false;
        }
        return true;
    }

    /**
     * 27 获得最近 N 天的时间戳
     */
    public static List<String> getNDayTime(int N) {
        //第一步：获取到当前的时间
        Long timeMillis = System.currentTimeMillis() + 86400000;//得到明天的现在时刻的时间戳
        //解析出最近十天的时间戳
        //时间的集合，应该有11个时间点，总共十个时间段
        //时间段从现在开始到最后一天
        Date currentData = new Date(timeMillis);
        List<String> timeList = new ArrayList<String>();
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String format1 = format.format(currentData);
            Long tempMillis = format.parse(format1).getTime();

            for (int i = 0; i < N - 1; i++) {
                timeList.add(String.valueOf(tempMillis));
                tempMillis = tempMillis - 86400000;
            }
            timeList.add(String.valueOf(tempMillis));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeList;
    }



}
