package com.wuan.wuan_news.wuan_news_server.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/10/15/ 11:19
 * @description
 */
public class UserTopicCreationFailedException extends RuntimeException {
    public UserTopicCreationFailedException(String message) {
        super(message);
    }
}
