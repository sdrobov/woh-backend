package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import java.sql.Statement;

public class V15__PostProposedBy extends BaseJavaMigration {
    @Override
    public void migrate(Context context) throws Exception {
        try (Statement statement = context.getConnection().createStatement()) {
            statement.execute("ALTER TABLE `post` ADD COLUMN `proposed_by` bigint(20) DEFAULT NULL");
            statement.execute("ALTER TABLE `post` ADD CONSTRAINT `fk_post_ref_user_proposed` FOREIGN KEY (`proposed_by`) REFERENCES `user` (`id`)");
        }
    }
}
