package com.wuan.wuan_news.wuan_news_server.util;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.wuan.wuan_news.wuan_news_server.dto.NewsDTO;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
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
            SyndFeed feed;

            try (InputStream is = feedUrl.openConnection().getInputStream()) {
                feed = input.build(new XmlReader(is));
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
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed;

            try (InputStream is = feedUrl.openConnection().getInputStream()) {
                feed = input.build(new XmlReader(is));
            }

            for (SyndEntry entry : feed.getEntries()) {
                NewsDTO newsDTO = new NewsDTO();
                newsDTO.setTitle(entry.getTitle());
                newsDTO.setDescription(entry.getDescription().getValue());
                // newsDTO.setPubDate(entry.getPublishedDate().toInstant().atZone(ZoneId.of("UTC+8")).toLocalDateTime());
                newsDTO.setPubDate(entry.getPublishedDate().toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime());
                newsDTO.setLink(entry.getLink());
                newsDTO.setAuthor(entry.getAuthor());
                newsList.add(newsDTO);
            }
        } catch (IOException | IllegalArgumentException | FeedException e) {
            e.printStackTrace();
        }

        return newsList;
    }
}
