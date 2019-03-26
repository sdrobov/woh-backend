package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V19__AlterPostsSetIsAllowedSmallint extends BaseJavaMigration {
    @Override public void migrate(Context context) throws Exception {
        try (Statement statement = context.getConnection().createStatement()) {
            statement.execute("ALTER TABLE `post` ADD `_is_allowed` SMALLINT NOT NULL DEFAULT 0");
            statement.execute("UPDATE `post` SET `_is_allowed` = COALESCE((`is_allowed`+0), 0)");
            statement.execute("ALTER TABLE `post` DROP `is_allowed`");
            statement.execute("ALTER TABLE `post` CHANGE COLUMN `_is_allowed` `is_allowed` SMALLINT NOT NULL DEFAULT 0");
        }
    }
}
