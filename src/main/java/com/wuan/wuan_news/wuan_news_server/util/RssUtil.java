package com.wuan.wuan_news.wuan_news_server.util;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.wuan.wuan_news.wuan_news_server.dto.NewsDTO;
import com.wuan.wuan_news.wuan_news_server.exception.RssUrlIsInvalidException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
public class RssUtil {
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

    public List<NewsDTO> fetchFromRssUrl(String rssUrl) {
        List<NewsDTO> newsList = new ArrayList<>();
        try {
            HttpURLConnection httpcon = openHttpConnection(rssUrl);

            if (isValidRssContent(httpcon)) {
                SyndFeed feed = fetchSyndFeed(httpcon);
                populateNewsList(newsList, feed);
            } else {
                log.info("当前RSS URL返回的内容的格式不符合要求" + rssUrl);
                // throw new RssUrlIsInvalidException("Unexpected content type for URL: " + rssUrl);
            }
        } catch (IOException | FeedException e) {
            e.printStackTrace();
        }
        return newsList;
    }

    private HttpURLConnection openHttpConnection(String rssUrl) throws IOException {
        URL feedUrl = new URL(rssUrl);
        HttpURLConnection httpcon = (HttpURLConnection) feedUrl.openConnection();
        if (httpcon.getResponseCode() != HttpURLConnection.HTTP_OK) {
            throw new RssUrlIsInvalidException("Failed to fetch RSS feed from URL: " + rssUrl + ". HTTP Error Code: " + httpcon.getResponseCode());
        }
        return httpcon;
    }

    private boolean isValidRssContent(HttpURLConnection httpcon) throws IOException {
        String firstLine;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(httpcon.getInputStream()))) {
            firstLine = reader.readLine();
        }

        boolean startsWithXml = firstLine != null && firstLine.trim().startsWith("<?xml");
        boolean startsWithDocType = firstLine != null && firstLine.trim().startsWith("<!DOCTYPE");
        String contentType = httpcon.getContentType();

        return (contentType.contains("application/rss+xml") || contentType.contains("application/atom+xml"))
                && startsWithXml && !startsWithDocType;
    }

    private SyndFeed fetchSyndFeed(HttpURLConnection httpcon) throws IOException, FeedException {
        try (InputStream is = httpcon.getInputStream()) {
            SyndFeedInput input = new SyndFeedInput();
            return input.build(new XmlReader(is));
        }
    }

    private void populateNewsList(List<NewsDTO> newsList, SyndFeed feed) {
        for (SyndEntry entry : feed.getEntries()) {
            NewsDTO newsDTO = new NewsDTO();
            newsDTO.setTitle(entry.getTitle());
            newsDTO.setDescription(entry.getDescription().getValue());
            newsDTO.setPubDate(entry.getPublishedDate().toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime());
            newsDTO.setLink(entry.getLink());
            newsDTO.setAuthor(entry.getAuthor());
            newsList.add(newsDTO);
        }
    }
}
