package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V21__PostIndexes extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (Statement statement = context.getConnection().createStatement()) {
            statement.execute("create index post_created_at_index on post (created_at)");
            statement.execute("create index post_moderated_at_index on post (moderated_at)");
            statement.execute("create index post_published_at_index on post (published_at)");
        }
    }
}
