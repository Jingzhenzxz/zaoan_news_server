package com.wuan.wuan_news.wuan_news_server.util;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.wuan.wuan_news.wuan_news_server.common.ErrorCode;
import com.wuan.wuan_news.wuan_news_server.exception.BusinessException;
import com.wuan.wuan_news.wuan_news_server.model.entity.News;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author Jingzhen
 * @date 2023/08/05/ 9:41
 * @description
 */
@Service
@Slf4j
public class RssUtils {
    public boolean isValidRssUrl(String rssUrl) {
        try {
            URL feedUrl = new URL(rssUrl);
            SyndFeedInput input = new SyndFeedInput();

            try (InputStream is = feedUrl.openConnection().getInputStream()) {
                input.build(new XmlReader(is));
            }
            // If no exception is thrown, then the URL is a valid RSS feed
            return true;
        } catch (MalformedURLException e) {
            log.error("The provided string is not a valid URL" + e);
            return false;
        } catch (IOException | IllegalArgumentException | FeedException e) {
            log.error("" + e);
            return false;
        }
    }

    private HttpURLConnection openHttpConnection(String rssUrl) throws IOException {
        URL feedUrl = new URL(rssUrl);
        HttpURLConnection httpcon = (HttpURLConnection) feedUrl.openConnection();
        if (httpcon.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取RSS订阅失败: " + rssUrl + ". HTTP Error Code: " + httpcon.getResponseCode());
        }
        return httpcon;
    }

    public List<News> fetchFromRssUrl(String rssUrl) {
        List<News> newsList = new ArrayList<>();
        try {
            HttpURLConnection httpcon = openHttpConnection(rssUrl);

            // 创建一次InputStream并用于后续操作
            try (InputStream is = httpcon.getInputStream();
                 BufferedInputStream bis = new BufferedInputStream(is);
                 ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = bis.read(buffer)) != -1) {
                    baos.write(buffer, 0, bytesRead);
                }

                byte[] data = baos.toByteArray();
                ByteArrayInputStream bais1 = new ByteArrayInputStream(data);
                ByteArrayInputStream bais2 = new ByteArrayInputStream(data);

                if (isValidRssContent(bais1)) {
                    // bais1 流会被 isValidRssContent 关闭，所以我们把 bais2 流传给 fetchSyndFeed
                    SyndFeed feed = fetchSyndFeed(bais2);
                    populateNewsList(newsList, feed);
                } else {
                    log.info("当前RSS URL返回的内容的格式不符合要求：{}", rssUrl);
                }
            }
        } catch (IOException | FeedException e) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, e.getMessage());
        }
        return newsList;
    }


    private boolean isValidRssContent(InputStream is) throws IOException {
        String firstLine;
        // 读取输入流的第一行并关闭流（由于BufferedReader的自动关闭特性）
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            firstLine = reader.readLine();
        }
        boolean startsWithXml = firstLine != null && firstLine.trim().startsWith("<?xml");
        boolean startsWithDocType = firstLine != null && firstLine.trim().startsWith("<!DOCTYPE");

        return startsWithXml && !startsWithDocType;
    }

    private SyndFeed fetchSyndFeed(InputStream is) throws IOException, FeedException {
        SyndFeedInput input = new SyndFeedInput();
        return input.build(new XmlReader(is));
    }

    private void populateNewsList(List<News> newsList, SyndFeed feed) {
        for (SyndEntry entry : feed.getEntries()) {
            News news = new News();
            news.setTitle(entry.getTitle());
            news.setDescription(entry.getDescription().getValue());
            news.setPubDate(entry.getPublishedDate().toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime());
            news.setLink(entry.getLink());
            news.setAuthor(entry.getAuthor());
            newsList.add(news);
        }
    }
}
