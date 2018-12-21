package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V17__PostTeasers extends BaseJavaMigration {
    @Override public void migrate(Context context) throws Exception {
        try (Statement statement = context.getConnection().createStatement()) {
            statement.execute("ALTER TABLE `post` ADD COLUMN `teaser_image` varchar(255) DEFAULT NULL;");
            statement.execute("ALTER TABLE `post` ADD COLUMN `featured_image` varchar(255) DEFAULT NULL;");
            statement.execute("ALTER TABLE `post` ADD COLUMN `nearest_image` varchar(255) DEFAULT NULL;");
            statement.execute("ALTER TABLE `post` ADD COLUMN `can_be_nearest` SMALLINT NOT NULL DEFAULT 1;");
        }
    }
}
