package top.sea521;

import lombok.Data;

/**
 * description: json
 * User: Administrator
 */
@Data
public class JsonEntity {

    private String ret = "yes";
    private int code = 200;
    private String msg = "操作成功";
    private Long total = 0L;
    //分页新增 /20180302
    private Object content;


}
