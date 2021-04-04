package cn.lovike.tool.basic.framework.common.util;

import cn.lovike.tool.basic.framework.common.enums.IEnum;
import lombok.Data;
import org.springframework.http.HttpStatus;

/**
 * 通用返回结果
 *
 * @author lovike
 * @since 2020-02-16
 */
@Data
public class Result<T> {
    /**
     * 状态码
     */
    private int code;

    /**
     * 状态信息
     */
    private String msg;

    /**
     * 返回的数据
     */
    private T data;

    public Result() {
        this.code = HttpStatus.OK.value();
        this.msg = HttpStatus.OK.getReasonPhrase();
    }

    public Result(T data) {
        this.code = HttpStatus.OK.value();
        this.msg = HttpStatus.OK.getReasonPhrase();
        this.data = data;
    }

    public Result(IEnum iEnum) {
        this.code = iEnum.getCode();
        this.msg = iEnum.getMsg();
    }

    public Result(IEnum iEnum, T data) {
        this.code = iEnum.getCode();
        this.msg = iEnum.getMsg();
        this.data = data;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(Throwable t) {
        // this.code = SystemEnum.FAILED.getCode();
        this.msg = t.getMessage();
    }

    public static Result<Void> success() {
        return new Result<>();
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    // public static Result<Void> fail() {
    //     return new Result(SystemEnum.FAILED);
    // }
}
