package com.wuan.wuan_news.wuan_news_server.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/08/06/ 14:26
 * @description
 */
public class NewsCreationException extends RuntimeException {
    public NewsCreationException(String message) {
        super(message);
    }
}
