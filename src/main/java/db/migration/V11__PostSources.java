package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

public class V11__PostSources implements SpringJdbcMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.execute("CREATE TABLE `source` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `name` varchar(255) NOT NULL,\n" +
            "  `url` varchar(255) NOT NULL,\n" +
            "  `settings` TEXT,\n" +
            "  `created_at` datetime DEFAULT NOW(),\n" +
            "  `last_post_date` datetime DEFAULT NULL,\n" +
            "  `is_locked` bit(1) DEFAULT 0,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  UNIQUE KEY `unq_source_name` (`name`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci");

        jdbcTemplate.execute("ALTER TABLE `post` ADD COLUMN `source_id` bigint(20) DEFAULT NULL");
        jdbcTemplate.execute("ALTER TABLE `post` ADD CONSTRAINT `fk_post_ref_source` FOREIGN KEY (`source_id`) REFERENCES `source` (`id`)");
    }
}
