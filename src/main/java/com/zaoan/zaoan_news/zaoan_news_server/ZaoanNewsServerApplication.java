package com.zaoan.zaoan_news.zaoan_news_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/8/5 12:56
 * @description
 */
@SpringBootApplication
@EnableScheduling
public class ZaoanNewsServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZaoanNewsServerApplication.class, args);
    }

}