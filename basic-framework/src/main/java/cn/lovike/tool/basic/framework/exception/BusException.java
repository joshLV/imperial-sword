package cn.lovike.tool.basic.framework.exception;

import cn.lovike.tool.basic.framework.common.enums.IEnum;
import cn.lovike.tool.basic.framework.common.enums.StatusCodeEnum;

/**
 * @author lovike
 * @since 2021/2/10
 */
public class BusException extends RuntimeException implements IEnum {
    private int    code;
    private String msg;

    public BusException(String msg) {
        this.code = StatusCodeEnum.BUSINESS_ERROR.getCode();
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMsg() {
        return msg;
    }
}
