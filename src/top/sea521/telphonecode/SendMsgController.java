package top.sea521.telphonecode;

import com.myx.server.common.ResponseCode;
import com.myx.server.common.ServerResponse;
import com.myx.server.dao.SendMsgLogMapper;
import com.myx.server.dao.UserMapper;
import com.myx.server.domain.SendMsgLog;
import com.myx.server.service.UserService;
import com.myx.server.utils.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Administrator on 2019/1/18.
 */
@RestController
public class SendMsgController {

    @Autowired
    UserService userService;
    @Autowired
    UserMapper userMapper;
    @Autowired
    SendMsgLogMapper sendMsgLogMapper;


    @Autowired
    SendMsgManager sendMsgManager;


    /**
     * 1 普通常规的发送验证码
     * isJudgeRegister = "1" 手机号有被注册的时候，才可以发送???
     * isJudgeRegister = "2" 手机号没有被注册的时候，才可以发送!!!
     * isJudgeRegister 默认等于"1"
     * client          客户端
     * clientVersion   客户端的版本
     * sendCodePurpose 发送的用途
     */
    @RequestMapping(value = "/sendMsg", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST, RequestMethod.GET})
    /**表示将功能处理方法将生产json格式的数据，此时根据请求头中的Accept进行匹配，如请求头“Accept:application/json”时即可匹配;*/
    public
    ServerResponse<String> sendMsg(String phoneNumber, HttpServletRequest request, String isJudgeRegister
            , String client, String clientVersion, String sendCodePurpose) {
        /** 1 如果为空，设置默认值,已经是注册用户*/
        if (isJudgeRegister == null) {
            isJudgeRegister = "1";
        }
        /** 2 验证手机号是否为空*/
        if (!Util.judgeParameter(phoneNumber)) {
            /**验证不通过，设置错误码和提示信息*/
            return ServerResponse.createByErrorCodeMessage(Cons.PARAMETER_ERROR, "参数错误");
        }

        /** 3 判断是否是手机号*/
        if (!Util.isCheckCellPhone(phoneNumber)) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ERROR.getCode(), "手机号错误");
        }
        /** 4 判断当前手机号，是否注册*/
        int aLong = userMapper.checkTelphone(phoneNumber);
        if (isJudgeRegister.equals("1")) {
            //走进这里，需要判断是否注册
            if (aLong <= 0) {
                //如果是小于等于 0 说明当前没有注册
                return ServerResponse.createByErrorCodeMessage(ResponseCode.ERROR.getCode() , "当前账号，没有被注册");
            }
        } else {
            if (aLong > 0) {
                return ServerResponse.createByErrorCodeMessage(ResponseCode.ERROR.getCode(),  "当前账号，已经被注册");
            }
        }

        int clientInt = 3;
        if (client == null) {
            client = "";
        }
        if (client.equals("1")) {
            clientInt = 1;
        } else if (client.equals("2")) {
            clientInt = 2;
        }

        int sendCodePurposeInt = 3;
        if (sendCodePurpose == null) {
            sendCodePurpose = "";
        }
        if (sendCodePurpose.equals("1")) {
            sendCodePurposeInt = 1;
        } else if (sendCodePurpose.equals("2")) {
            sendCodePurposeInt = 2;
        }

        //生成验证码，返回结果标识；
        String code = String.valueOf(Util.getRandomNum());
        int i = sendMsgManager.sendVerificationCode(phoneNumber, request, code);
        if (i == 1) {
            /**验证码信息记录发送入库*/

            // jsonEntity.setCode(200);
            String currentIp = CusAccessObjectUtil.getIpAddress(request);
            SendMsgLog sendMsgLog = new SendMsgLog();
            sendMsgLog.setIdAddress(currentIp);
            sendMsgLog.setPhoneNumber(phoneNumber);
            sendMsgLog.setVerificationCode(code);
            sendMsgLog.setSendClient(clientInt);
            sendMsgLog.setSendClientVersion(clientVersion);
            sendMsgLog.setSendCodePurpose(sendCodePurposeInt);

            String data = HttpClientHelper.sendPost("http://pv.sohu.com/cityjson?ie=utf-8", null, "UTF8");
            String address = "";
            try {
                if (data.startsWith("var returnCitySN = ")) {
                    String json = data.substring(19, data.length() - 1);
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.has("cip") && jsonObject.has("cname")) {
                        address = jsonObject.getString("cname");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // 地址信息设置并入库
            sendMsgLog.setPlace(address);
            sendMsgLogMapper.insertSelective(sendMsgLog);
            return ServerResponse.createBySuccessMessage("发送成功！");
        } else if (i == 6) {
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ERROR.getCode(), "发送频繁");

        }
        return ServerResponse.createByErrorCodeMessage(ResponseCode.ERROR.getCode() , "发送失败");
    }

    /**
     * 2 忘记密码时发送验证码
     */
    @RequestMapping(value = "/forgetPasswordSendCodeFirstRest", produces = "application/json;charset=UTF-8")
    public ServerResponse<String> forgetPasswordSendCodeFirstRest(String phoneNumber, HttpServletRequest request,
                                                                  String client, String clientVersion, String sendCodePurpose) {
        return sendMsg(phoneNumber, request, "1", client, clientVersion, sendCodePurpose);
    }


    /**
     * 3
     *
     * @param phoneNumbers
     * @param request
     * @return code ：
     * 200 成功
     * 201 发送频繁
     * 202 发送失败
     */
    @RequestMapping(value = "registerSendMsg", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ServerResponse<String> registerSendMsg(String phoneNumbers, HttpServletRequest request,
                                                  String client, String clientVersion, String sendCodePurpose) {
        return sendMsg(phoneNumbers, request, "2", client, clientVersion, sendCodePurpose);

    }

}
