create database if not exists wuan_news;

USE wuan_news;

CREATE TABLE IF NOT EXISTS `media` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '媒体编码',
  `name` varchar(255) NOT NULL COMMENT '媒体名称',
  `rss_link` text NOT NULL COMMENT 'rss地址',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户编码',
  `username` varchar(255) NOT NULL COMMENT '用户名称',
  `email` varchar(255) NOT NULL COMMENT '电子邮箱',
  `password` varchar(255) NOT NULL COMMENT '用户密码',
  `user_role` varchar(10) NOT NULL DEFAULT 'user' COMMENT '用户角色：admin/user/ban',
  `user_avatar` varchar(512) DEFAULT NULL COMMENT '用户头像',
  `user_profile` varchar(1024) DEFAULT NULL COMMENT '用户简介',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `is_delete` tinyint(4) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `user_media` (
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户编码',
  `media_id` bigint(20) DEFAULT NULL COMMENT '媒体编码',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  KEY `user_media_media_id_fk` (`media_id`),
  KEY `user_media_user_id_fk` (`user_id`),
  CONSTRAINT `user_media_media_id_fk` FOREIGN KEY (`media_id`) REFERENCES `media` (`id`),
  CONSTRAINT `user_media_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户与媒体之间的关系';

CREATE TABLE IF NOT EXISTS `news` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '新闻编码',
  `title` varchar(255) NOT NULL COMMENT '新闻标题',
  `description` longtext COMMENT '新闻简介',
  `preview_image` varchar(1000) DEFAULT NULL COMMENT '预览图',
  `pub_date` datetime DEFAULT NULL COMMENT '发表日期',
  `link` varchar(1000) NOT NULL COMMENT '新闻地址',
  `author` varchar(255) DEFAULT NULL COMMENT '作者',
  `media_name` varchar(255) NOT NULL COMMENT '媒体名称',
  `media_id` bigint(20) NOT NULL COMMENT '所属媒体编码',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1294 DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `topic` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '话题编码',
  `name` varchar(255) NOT NULL COMMENT '话题名称',
  `user_id` bigint(20) NOT NULL COMMENT '创建者编码',
  `latest_news_1_id` bigint(20) DEFAULT NULL COMMENT '最新的一条新闻的编码',
  `latest_news_2_id` bigint(20) DEFAULT NULL COMMENT '最新的第二条新闻的编码',
  `latest_news_3_id` bigint(20) DEFAULT NULL COMMENT '最新的第三条新闻的编码',
  `new_content_today_count` int(11) NOT NULL DEFAULT '0' COMMENT '今日新增的新闻数量',
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=56 DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `news_topic` (
  `news_id` bigint(20) NOT NULL COMMENT '新闻编码',
  `topic_id` bigint(20) NOT NULL COMMENT '话题编码',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`news_id`,`topic_id`),
  KEY `topic_id` (`topic_id`),
  CONSTRAINT `news_topic_news_id_fk` FOREIGN KEY (`news_id`) REFERENCES `news` (`id`),
  CONSTRAINT `news_topic_topic_id_fk` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE IF NOT EXISTS `user_topic_following` (
  `user_id` bigint(20) NOT NULL,
  `topic_id` bigint(20) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`user_id`,`topic_id`),
  KEY `user_topic_ibfk_2` (`topic_id`),
  CONSTRAINT `user_topic_topic_id_fk` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`id`),
  CONSTRAINT `user_topic_user_id_fk` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
