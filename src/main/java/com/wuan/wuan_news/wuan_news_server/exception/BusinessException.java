package com.wuan.wuan_news.wuan_news_server.exception;


import com.wuan.wuan_news.wuan_news_server.common.ErrorCode;
import lombok.Getter;

/**
 * 自定义异常类
 * @author Jingzhen
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

}
