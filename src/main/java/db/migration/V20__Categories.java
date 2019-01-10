package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V20__Categories extends BaseJavaMigration {
    @Override public void migrate(Context context) throws Exception {
        try (Statement statement = context.getConnection().createStatement()) {
            statement.execute("CREATE TABLE `category` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(255) NOT NULL,\n" +
                "  PRIMARY KEY (`id`),\n" +
                "  UNIQUE KEY `unq_name` (`name`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci");

            statement.execute("CREATE TABLE `categories_ref_posts` (\n" +
                "  `category_id` bigint(20) NOT NULL,\n" +
                "  `post_id` bigint(20) NOT NULL,\n" +
                "  PRIMARY KEY (`category_id`,`post_id`),\n" +
                "  CONSTRAINT `fk_crp_ref_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`),\n" +
                "  CONSTRAINT `fk_crp_ref_post` FOREIGN KEY (`post_id`) REFERENCES `post` (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci");
        }
    }
}
