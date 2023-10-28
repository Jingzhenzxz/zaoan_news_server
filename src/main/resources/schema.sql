USE wuan_news;

CREATE TABLE IF NOT EXISTS `media` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `rss_link` text NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  FULLTEXT KEY `rss_link` (`rss_link`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `news` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `description` text,
  `preview_image` varchar(1000) DEFAULT NULL,
  `pub_date` datetime DEFAULT NULL,
  `link` varchar(1000) NOT NULL,
  `author` varchar(255) DEFAULT NULL,
  `media_name` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `title_media_name` (`title`,`media_name`) USING BTREE,
  KEY `media_name` (`media_name`) USING BTREE,
  CONSTRAINT `news_fk_1` FOREIGN KEY (`media_name`) REFERENCES `media` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=876 DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `topic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `news_topic` (
  `news_id` bigint(20) NOT NULL,
  `topic_id` bigint(20) NOT NULL,
  PRIMARY KEY (`news_id`,`topic_id`),
  KEY `topic_id` (`topic_id`),
  CONSTRAINT `news_topic_ibfk_1` FOREIGN KEY (`news_id`) REFERENCES `news` (`id`) ON DELETE CASCADE,
  CONSTRAINT `news_topic_ibfk_2` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `user_topic` (
  `user_id` bigint(20) NOT NULL,
  `topic_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`topic_id`),
  KEY `user_topic_ibfk_2` (`topic_id`),
  CONSTRAINT `user_topic_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `user_topic_ibfk_2` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
