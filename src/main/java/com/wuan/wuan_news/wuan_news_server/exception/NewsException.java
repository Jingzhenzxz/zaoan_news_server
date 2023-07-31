package com.wuan.wuan_news.wuan_news_server.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/07/30/ 16:04
 * @description
 */
public class NewsException extends RuntimeException {
    public NewsException(String message) {
        super(message);
    }
}
