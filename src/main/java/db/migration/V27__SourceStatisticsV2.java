package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V27__SourceStatisticsV2 extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (Statement statement = context.getConnection().createStatement()) {
            statement.execute("ALTER TABLE source DROP last_success_at");
            statement.execute("ALTER TABLE source DROP last_error_at");
            statement.execute("ALTER TABLE source DROP last_success_count");
            statement.execute("ALTER TABLE source DROP last_errors_count");

            statement.execute("CREATE TABLE source_stat\n" +
                "(\n" +
                "    source_id    BIGINT NOT NULL,\n" +
                "    started_at   DATETIME NOT NULL,\n" +
                "    finished_at  DATETIME NOT NULL,\n" +
                "    parsed_count INT NULL,\n" +
                "    is_success   INT NOT NULL,\n" +
                "    error_text   TEXT NULL,\n" +
                "    CONSTRAINT source_stat_pk\n" +
                "        PRIMARY KEY (source_id, started_at),\n" +
                "    CONSTRAINT source_stat_source_id_fk\n" +
                "        FOREIGN KEY (source_id) REFERENCES source (id)\n" +
                "            ON UPDATE CASCADE\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci");

            statement.execute("CREATE INDEX source_stat_finished_at_index ON source_stat (finished_at)");
            statement.execute("CREATE INDEX source_stat_started_at_index ON source_stat (started_at)");
        }
    }
}
