package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V22__PostIndexes2 extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (Statement statement = context.getConnection().createStatement()) {
            statement.execute("create index post_deleted_at_is_allowed_moderated_at_index on post (deleted_at, is_allowed, moderated_at);");
        }
    }
}
