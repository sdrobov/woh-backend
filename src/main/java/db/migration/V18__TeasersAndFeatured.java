package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V18__TeasersAndFeatured extends BaseJavaMigration {
    @Override public void migrate(Context context) throws Exception {
        try (Statement statement = context.getConnection().createStatement()) {
            statement.execute("CREATE TABLE `teaser` (\n" +
                "  `from` datetime NOT NULL,\n" +
                "  `to` datetime NOT NULL,\n" +
                "  `post_id` BIGINT(20) NOT NULL,\n" +
                "  `is_teaser` SMALLINT DEFAULT 0,\n" +
                "  PRIMARY KEY (`from`, `to`, `post_id`, `is_teaser`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci;");

            statement.execute("ALTER TABLE `teaser` ADD CONSTRAINT `fk_teaser_ref_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`);");
        }
    }
}
