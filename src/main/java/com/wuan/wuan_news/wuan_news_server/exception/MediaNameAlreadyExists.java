package com.wuan.wuan_news.wuan_news_server.exception;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/08/06/ 15:43
 * @description
 */
public class MediaNameAlreadyExists extends RuntimeException {
    public MediaNameAlreadyExists(String message) {
        super(message);
    }
}
