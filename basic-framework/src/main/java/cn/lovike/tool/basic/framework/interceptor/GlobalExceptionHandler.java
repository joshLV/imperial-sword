package cn.lovike.tool.basic.framework.interceptor;

import cn.lovike.tool.basic.framework.common.enums.IEnum;
import cn.lovike.tool.basic.framework.common.enums.StatusCodeEnum;
import cn.lovike.tool.basic.framework.common.util.Result;
import cn.lovike.tool.basic.framework.exception.BusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lovike
 * @since 2020-02-16
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(value = Exception.class)
    public Result defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e) throws Exception {
        logger.warn("请求{}异常信息", request.getRequestURL(), e);
        response.reset();
        // 设置状态码
        response.setStatus(HttpStatus.OK.value());
        response.setHeader("Cache-Control", "no-cache");
        if (e instanceof MissingServletRequestParameterException) {
            return new Result<>(StatusCodeEnum.PARAM_ERROR);
        } else if (e instanceof ServletRequestBindingException) {
            return new Result<>(StatusCodeEnum.PARAM_ERROR);
        } else if (e instanceof HttpMessageNotReadableException) {
            return new Result<>(StatusCodeEnum.PARAM_ERROR);
        } else if (e instanceof IllegalArgumentException) {
            return new Result<>(StatusCodeEnum.PARAM_ERROR);
        } else if (e instanceof MaxUploadSizeExceededException) {
            return new Result<>(StatusCodeEnum.MAXIMUM_UPLOAD_SIZE_ERROR);
        } else if (e instanceof BusException) {
            return new Result<>((IEnum) e);
        }
        return new Result<>(StatusCodeEnum.UN_KNOW_ERROR);
        // else if (e instanceof RpcException) {
        //     //处理dubbo调用失败异常
        //     RpcExceptionEnum rpcExceptionEnum = getRpcExceptionEnum(e.getMessage());
        //     if (rpcExceptionEnum == null) {
        //         result.setStatus(StatusCode.UNKNOW_ERROR.getStatus());
        //         result.setMsg(StatusCode.UNKNOW_ERROR.getMsg());
        //     } else {
        //         result.setStatus(rpcExceptionEnum.getStatus());
        //         result.setMsg(rpcExceptionEnum.getMsg());
        //     }
        // }
    }

    // /**
    //  * @return
    //  * @RequestParam参数缺失的情况校验
    //  */
    // @ResponseStatus(HttpStatus.BAD_REQUEST)
    // @ExceptionHandler(MissingServletRequestParameterException.class)
    // @ResponseBody
    // public Result handleMissingServletRequestParameterException() {
    //     return Result.fa("参数缺失");
    // }
    //
    // /**
    //  * 校验前端参数错误的情况
    //  *
    //  * @param ex
    //  * @return
    //  */
    // @ResponseStatus(HttpStatus.OK)
    // @ExceptionHandler(ConstraintViolationException.class)
    // @ResponseBody
    // public Result handleConstraintViolationException(final ConstraintViolationException ex) {
    //     return Result.paramError(ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage).collect(Collectors.joining(",")));
    // }
}
