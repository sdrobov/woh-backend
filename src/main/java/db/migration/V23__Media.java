package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V23__Media extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (Statement statement = context.getConnection().createStatement()) {
            statement.execute("CREATE TABLE media (\n" +
                "    id         BIGINT AUTO_INCREMENT,\n" +
                "    title      VARCHAR(255) NULL,\n" +
                "    url        TEXT NULL,\n" +
                "    embed_code TEXT NULL,\n" +
                "    thumbnail  VARCHAR(255) NULL,\n" +
                "    CONSTRAINT media_pk\n" +
                "        PRIMARY KEY (id)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci");

            statement.execute("CREATE TABLE comment_ref_media (\n" +
                "    comment_id BIGINT NOT NULL,\n" +
                "    media_id   BIGINT NOT NULL,\n" +
                "    CONSTRAINT comment_ref_media_pk\n" +
                "        PRIMARY KEY (comment_id, media_id),\n" +
                "    CONSTRAINT comment_ref_media_comment_id_fk\n" +
                "        FOREIGN KEY (comment_id) REFERENCES comment (id),\n" +
                "    CONSTRAINT comment_ref_media_media_id_fk\n" +
                "        FOREIGN KEY (media_id) REFERENCES media (id)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci");

            statement.execute("CREATE TABLE post_ref_media (\n" +
                "    post_id  BIGINT NOT NULL,\n" +
                "    media_id BIGINT NOT NULL,\n" +
                "    CONSTRAINT post_ref_media_pk\n" +
                "        PRIMARY KEY (post_id, media_id),\n" +
                "    CONSTRAINT post_ref_media_post_id_fk\n" +
                "        FOREIGN KEY (post_id) REFERENCES post (id),\n" +
                "    CONSTRAINT post_ref_media_media_id_fk\n" +
                "        FOREIGN KEY (media_id) REFERENCES media (id)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_520_ci");
        }
    }
}
