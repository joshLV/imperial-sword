package cn.lovike.tool.basic.framework.common.enums;

/**
 * 状态码
 *
 * @author lovike
 * @since 2021/2/10
 */
public enum StatusCodeEnum implements IEnum {
    BUSINESS_ERROR(0, "业务异常"),
    UN_KNOW_ERROR(-1, "未知异常"),
    SYSTEM_ERROR(-2, "系统异常"),
    PARAM_ERROR(-3, "参数不正确"),
    DATABASE_ERROR(-4, "数据库异常"),
    ENCRYPT_ERROR(-5, "加解密异常"),
    CACHE_ERROR(-6, "缓存异常"),
    UNAUTHORIZED_ERROR(-7, "未授权"),
    NOT_FOUND_ERROR(-8, "请求不存在"),
    MAXIMUM_UPLOAD_SIZE_ERROR(-9, "上传文件太大"),
    NOT_AVAILABLE_DATA_ERROR(-10, "暂无数据"),
    ;

    private final int    code;
    private final String msg;

    StatusCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getMsg() {
        return this.msg;
    }
}
