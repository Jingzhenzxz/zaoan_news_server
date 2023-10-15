package com.wuan.wuan_news.wuan_news_server.util;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.wuan.wuan_news.wuan_news_server.dto.NewsDTO;
import com.wuan.wuan_news.wuan_news_server.exception.RssUrlIsInvalidException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
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
            System.out.println("The provided string is not a valid URL");
            return false;
        } catch (IOException | IllegalArgumentException | FeedException e) {
            System.out.println("The URL could not be read or the feed could not be parsed");
            return false;
        }
    }

    public List<NewsDTO> fetchFromRssUrl(String rssUrl) {
        List<NewsDTO> newsList = new ArrayList<>();
        try {
            URL feedUrl = new URL(rssUrl);
            HttpURLConnection httpcon = (HttpURLConnection) feedUrl.openConnection();

            // Check the response code and only process if the request was successful (HTTP 200)
            if (httpcon.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // 下面的代码有待修改，应该检查返回的内容是以“<? xml”开头还是以“<? DOCTYPE”开头
                // Check if the Content-Type is what we expect (RSS/Atom)
                String contentType = httpcon.getContentType();
                if (contentType != null && (contentType.contains("application/rss+xml") || contentType.contains("application/atom+xml"))) {
                    try (InputStream is = httpcon.getInputStream()) {
                        SyndFeedInput input = new SyndFeedInput();
                        SyndFeed feed = input.build(new XmlReader(is));

                        for (SyndEntry entry : feed.getEntries()) {
                            NewsDTO newsDTO = new NewsDTO();
                            newsDTO.setTitle(entry.getTitle());
                            newsDTO.setDescription(entry.getDescription().getValue());
                            newsDTO.setPubDate(entry.getPublishedDate().toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime());
                            newsDTO.setLink(entry.getLink());
                            newsDTO.setAuthor(entry.getAuthor());
                            newsList.add(newsDTO);
                        }
                    } catch (FeedException | IOException e) {
                        // Handle issues related to feed parsing or I/O here
                        e.printStackTrace();
                    }
                } else {
                    // Log or handle the unexpected Content-Type
                    throw new RssUrlIsInvalidException("Unexpected content type for URL: " + rssUrl + ". Content Type: " + contentType);
                }
            } else {
                // Log or handle the invalid response code (404 or other)
                throw new RssUrlIsInvalidException("Failed to fetch RSS feed from URL: " + rssUrl + ". HTTP Error Code: " + httpcon.getResponseCode());
            }
        } catch (IOException e) {
            // Handle issues related to opening the connection (invalid URL, no internet, etc.)
            e.printStackTrace();
        }

        return newsList;
    }
}
