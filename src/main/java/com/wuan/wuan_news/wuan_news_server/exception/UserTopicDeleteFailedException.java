package com.wuan.wuan_news.wuan_news_server.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/10/15/ 11:21
 * @description
 */
public class UserTopicDeleteFailedException extends RuntimeException {
    public UserTopicDeleteFailedException(String message) {
        super(message);
    }
}
