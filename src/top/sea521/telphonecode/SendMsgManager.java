package top.sea521.telphonecode;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.myx.server.utils.CusAccessObjectUtil;
import com.myx.server.utils.VerificationCodeModel;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by Administrator on 2019/3/12.
 */
@Component
public class SendMsgManager {

    // 1 是否正在清除
    private boolean isClearing = false;


    // 2 建立一个HashMap，储存验证码并且做不要频繁发送验证码的功能
    HashMap<String, VerificationCodeModel> verificationCodeHashMap = new HashMap<String, VerificationCodeModel>(10000);

    /**
     * 2 移除 VerificationCodeModel
     *
     * @return
     */
    public void removeVerificationCodeModel(String ipAddress) {
        if (verificationCodeHashMap.containsKey(ipAddress)) {
            verificationCodeHashMap.remove(ipAddress);
        }
    }

    /**
     * 3 根据ip取出服务端存储的验证码
     */
    public String getCurrentVerificationCode(HttpServletRequest request) {
        String ipAddress = CusAccessObjectUtil.getIpAddress(request);
        if (verificationCodeHashMap.containsKey(ipAddress)) {
            return verificationCodeHashMap.get(ipAddress).getCode();
        }
        return null;
    }

    /**
     * 4 得到当前的 VerificationCodeModel验证信息，
     */
    public VerificationCodeModel getCurrentVerificationCodeModel(HttpServletRequest request) {
        String ipAddress = CusAccessObjectUtil.getIpAddress(request);
        if (verificationCodeHashMap.containsKey(ipAddress)) {
            return verificationCodeHashMap.get(ipAddress);
        }
        return null;
    }

    /**
     * 5 启动一个线程，定期的清除验证码
     *
     * @return
     */
    public void startClearCacheVerificationCode() {
        // 1 间隔时间60s
        final long sleepTime = 60 * 1000;
        // 2 超时时间300s
        final long TIME_OUT_TIME = 1000*60*5;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        if (verificationCodeHashMap == null) {
                            Thread.sleep(sleepTime);
                            continue;
                        }

                        Iterator<String> iterator = verificationCodeHashMap.keySet().iterator();
                        //
                        setClearing(true);
                        while (iterator.hasNext()) {
                            String key = iterator.next();
                            VerificationCodeModel verificationCodeModel = verificationCodeHashMap.get(key);
                            long timeMillis = System.currentTimeMillis();
                            //如果发送时间大于五分钟，则移除
                            if (timeMillis - verificationCodeModel.getFirstSendTime() > TIME_OUT_TIME) {
                                verificationCodeHashMap.remove(key);
                            }
                        }
                        setClearing(false);
                        Thread.sleep(sleepTime);
                    } catch (Exception e) {
                        e.printStackTrace();
                        try {
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e1) {
                            e1.printStackTrace();
                        }
                    }
                }
            }
        }).start();
    }

    public int sendVerificationCode(String phoneNumbers, HttpServletRequest request, String code) {
        String currentIp = CusAccessObjectUtil.getIpAddress(request);
        return sendVerificationCode(phoneNumbers, currentIp, code);
    }

    /**
     * 发送验证码
     * 只需要把发送结果返回即可，不需要做其他操作
     * return :
     * 1 == 成功
     * 2 == 业务限流
     * 3 == ServerException
     * 4 == ClientException
     * 5 == 其他异常
     * 6 == 发送频繁
     */
    public synchronized int sendVerificationCode(String phoneNumbers, String currentIp, String code) {

        // 1 先判断，是否发送频繁
        if (verificationCodeHashMap.containsKey(currentIp)) {
            VerificationCodeModel tempVerificationCodeModel = verificationCodeHashMap.get(currentIp);

            // 2 如果第一次已经发送超时五分钟，则清除
            if (System.currentTimeMillis() - tempVerificationCodeModel.getFirstSendTime() > 1000 * 60 * 5) {
                verificationCodeHashMap.remove(currentIp);
            }

            if (tempVerificationCodeModel.getCurrentSendNumber() >= 3) {
                return 6;
            }
            // 3 小于三次时，需要判断上一次发送的时间是否大于一分钟ms
            if (System.currentTimeMillis() - tempVerificationCodeModel.getSendTime() < 1000 * 60) {
                return 6;
            }
        }
        DefaultProfile profile = DefaultProfile
                .getProfile("cn-shenzhen", "LTAI5uVYC44y3vs6", "upq2Ca2Gv16ZKUDsnui8hr1yvdhUmw");

        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        //request.setProtocol(ProtocolType.HTTPS);
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("PhoneNumbers", phoneNumbers);
        request.putQueryParameter("SignName", "MLabel");
        request.putQueryParameter("TemplateCode", "SMS_156471105");
        request.putQueryParameter("TemplateParam", "{\"code\":" + code + "}");
        try {
            CommonResponse response = client.getCommonResponse(request);
            JSONObject jsonObject = JSONObject.parseObject(response.getData());
            //发送成功
            if (jsonObject.getString("Message").equals("OK")) {
                //判断，当前的ip是否已经发送
                VerificationCodeModel verificationCodeModel = null;
                if (verificationCodeHashMap.containsKey(currentIp)) {
                    verificationCodeModel = verificationCodeHashMap.get(currentIp);
                    verificationCodeModel.setCode(code);
                    verificationCodeModel.setSendTime(System.currentTimeMillis());
                    verificationCodeModel.setCurrentSendNumber(verificationCodeModel.getCurrentSendNumber() + 1);
                    verificationCodeModel.setPhoneNumber(phoneNumbers);
                } else {
                    verificationCodeModel = new VerificationCodeModel();
                    verificationCodeModel.setCode(code);
                    verificationCodeModel.setCurrentSendNumber(1);
                    long currentTimeMillis = System.currentTimeMillis();
                    verificationCodeModel.setFirstSendTime(currentTimeMillis);
                    verificationCodeModel.setSendTime(currentTimeMillis);
                    verificationCodeModel.setPhoneNumber(phoneNumbers);
                    verificationCodeHashMap.put(currentIp, verificationCodeModel);
                }
                return 1;
            }
            String code1 = jsonObject.getString("Code");
            //业务限流
            if (code1.equals("isv.BUSINESS_LIMIT_CONTROL")) {
                return 6;
            }
            System.out.println(response.getData());
        } catch (ServerException e) {
            return 3;
        } catch (ClientException e) {
            return 4;
        } catch (Exception e) {
            return 5;
        }
        return 5;
    }

    public synchronized boolean isClearing() {
        return isClearing;
    }

    public synchronized void setClearing(boolean clearing) {
        isClearing = clearing;
    }
}
