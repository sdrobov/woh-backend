package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V25__SourceStatistics extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (Statement statement = context.getConnection().createStatement()) {
            statement.execute("ALTER TABLE source ADD last_success_at DATETIME NULL");
            statement.execute("ALTER TABLE source ADD last_error_at DATETIME NULL");
            statement.execute("ALTER TABLE source ADD last_success_count INT NOT NULL DEFAULT 0");
            statement.execute("ALTER TABLE source ADD last_errors_count INT NOT NULL DEFAULT 0");
        }
    }
}
