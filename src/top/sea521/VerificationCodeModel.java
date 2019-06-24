package top.sea521;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2019/3/12.
 */
@Getter
@Setter
public class VerificationCodeModel {

    /**
     * 1 当前验证码
     */
    private String code;

    /**
     * 2 当前验证码发送的时间戳
     */
    private long sendTime;

    /**
     * 3 第一次发送的时间
     */
    private long firstSendTime;

    /**
     * 4 当前发送的次数
     */
    private int currentSendNumber;

    /**
     * 5 当前的手机号
     * @return
     */
    private String phoneNumber;


}
