package com.wuan.wuan_news.wuan_news_server.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/10/15/ 13:44
 * @description
 */
public class RssUrlIsInvalidException extends RuntimeException {
    public RssUrlIsInvalidException(String message) {
        super(message);
    }
}
