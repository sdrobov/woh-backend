package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

public class V10__Likes implements SpringJdbcMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.execute("CREATE TABLE `post_likes` (\n" +
            "  `post_id` bigint(20) NOT NULL,\n" +
            "  `user_id` bigint(20) NOT NULL,\n" +
            "  `like` tinyint(1) NOT NULL,\n" +
            "  PRIMARY KEY (`user_id`,`post_id`),\n" +
            "  CONSTRAINT `fk_pl_ref_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`),\n" +
            "  CONSTRAINT `fk_pl_ref_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci");

        jdbcTemplate.execute("CREATE TABLE `comment_likes` (\n" +
            "  `comment_id` bigint(20) NOT NULL,\n" +
            "  `user_id` bigint(20) NOT NULL,\n" +
            "  `like` tinyint(1) NOT NULL,\n" +
            "  PRIMARY KEY (`user_id`,`comment_id`),\n" +
            "  CONSTRAINT `fk_cl_ref_comment` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`),\n" +
            "  CONSTRAINT `fk_cl_ref_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci");
    }
}
