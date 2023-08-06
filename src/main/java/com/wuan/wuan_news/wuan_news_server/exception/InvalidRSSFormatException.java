package com.wuan.wuan_news.wuan_news_server.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/08/06/ 11:03
 * @description
 */
public class InvalidRSSFormatException extends RuntimeException {
    public InvalidRSSFormatException(String message) {
        super(message);
    }
}
