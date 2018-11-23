package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V16__CommentReplyFk extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (Statement statement = context.getConnection().createStatement()) {
            statement.execute("ALTER TABLE `comment` ADD CONSTRAINT `fk_comment_ref_reply` FOREIGN KEY (`reply_comment_id`) REFERENCES `comment` (`id`)");
        }
    }
}
