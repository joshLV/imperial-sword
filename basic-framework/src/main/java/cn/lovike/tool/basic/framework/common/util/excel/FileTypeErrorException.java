package cn.lovike.tool.basic.framework.common.util.excel;

/**
 * Created by zhouwenchao on 2016/12/8.
 */
public class FileTypeErrorException extends Exception{

    public FileTypeErrorException(String message) {
        super(message);
    }

    public FileTypeErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileTypeErrorException(Throwable cause) {
        super(cause);
    }

    public FileTypeErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public FileTypeErrorException() {
    }
}
