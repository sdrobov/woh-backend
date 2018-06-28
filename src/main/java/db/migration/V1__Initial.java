package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

public class V1__Initial implements SpringJdbcMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("CREATE TABLE `role` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `name` varchar(255) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci");

        jdbcTemplate.execute("CREATE TABLE `tag` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `name` varchar(255) NOT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `unq_name` (`name`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci");

        jdbcTemplate.execute("CREATE TABLE `user` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `avatar` varchar(255) DEFAULT NULL,\n" +
            "  `created_at` datetime DEFAULT NULL,\n" +
            "  `deleted_at` datetime DEFAULT NULL,\n" +
            "  `email` varchar(255) DEFAULT NULL,\n" +
            "  `fb` varchar(255) DEFAULT NULL,\n" +
            "  `google` varchar(255) DEFAULT NULL,\n" +
            "  `name` varchar(255) DEFAULT NULL,\n" +
            "  `password` varchar(255) DEFAULT NULL,\n" +
            "  `token` varchar(255) DEFAULT NULL,\n" +
            "  `updated_at` datetime DEFAULT NULL,\n" +
            "  `vk` varchar(255) DEFAULT NULL,\n" +
            "  `role_id` bigint(20) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `unq_email` (`email`),\n" +
            "  UNIQUE KEY `unq_fb` (`fb`),\n" +
            "  UNIQUE KEY `unq_google` (`google`),\n" +
            "  UNIQUE KEY `unq_token` (`token`),\n" +
            "  UNIQUE KEY `unq_vk` (`vk`),\n" +
            "  CONSTRAINT `fk_user_ref_role` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci");

        jdbcTemplate.execute("CREATE TABLE `tags_ref_users` (\n" +
            "  `tag_id` bigint(20) NOT NULL,\n" +
            "  `user_id` bigint(20) NOT NULL,\n" +
            "  PRIMARY KEY (`user_id`,`tag_id`),\n" +
            "  CONSTRAINT `fk_trf_ref_tag` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`),\n" +
            "  CONSTRAINT `fk_trf_ref_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci");

        jdbcTemplate.execute("CREATE TABLE `post` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `created_at` datetime DEFAULT NULL,\n" +
            "  `deleted_at` datetime DEFAULT NULL,\n" +
            "  `is_allowed` bit(1) DEFAULT NULL,\n" +
            "  `moderated_at` datetime DEFAULT NULL,\n" +
            "  `rating` bigint(20) DEFAULT NULL,\n" +
            "  `source` varchar(255) DEFAULT NULL,\n" +
            "  `text` varchar(255) DEFAULT NULL,\n" +
            "  `title` varchar(255) DEFAULT NULL,\n" +
            "  `updated_at` datetime DEFAULT NULL,\n" +
            "  `moderator_id` bigint(20) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  CONSTRAINT `fk_post_moderator` FOREIGN KEY (`moderator_id`) REFERENCES `user` (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci");

        jdbcTemplate.execute("CREATE TABLE `tags_ref_posts` (\n" +
            "  `tag_id` bigint(20) NOT NULL,\n" +
            "  `post_id` bigint(20) NOT NULL,\n" +
            "  PRIMARY KEY (`tag_id`,`post_id`),\n" +
            "  CONSTRAINT `fk_trp_ref_tag` FOREIGN KEY (`tag_id`) REFERENCES `tag` (`id`),\n" +
            "  CONSTRAINT `fk_trp_ref_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci");

        jdbcTemplate.execute("CREATE TABLE `comment` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `created_at` datetime DEFAULT NULL,\n" +
            "  `deleted_at` datetime DEFAULT NULL,\n" +
            "  `rating` bigint(20) DEFAULT NULL,\n" +
            "  `text` varchar(255) DEFAULT NULL,\n" +
            "  `updated_at` datetime DEFAULT NULL,\n" +
            "  `post_id` bigint(20) DEFAULT NULL,\n" +
            "  `user_id` bigint(20) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  CONSTRAINT `fk_comment_ref_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),\n" +
            "  CONSTRAINT `fk_comment_ref_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci");
    }
}
