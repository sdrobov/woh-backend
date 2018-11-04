package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

public class V13__SourcePreview implements SpringJdbcMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) {
        jdbcTemplate.execute("CREATE TABLE `source_post_preview` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `created_at` datetime DEFAULT NULL,\n" +
            "  `deleted_at` datetime DEFAULT NULL,\n" +
            "  `source_id` bigint(20) NOT NULL,\n" +
            "  `text` MEDIUMTEXT,\n" +
            "  `announce` TEXT,\n" +
            "  `title` varchar(255) DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  CONSTRAINT `fk_source_post_preview_source` FOREIGN KEY (`source_id`) REFERENCES `source` (`id`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci");
    }
}
