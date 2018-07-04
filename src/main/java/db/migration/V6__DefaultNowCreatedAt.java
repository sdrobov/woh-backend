package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

public class V6__DefaultNowCreatedAt implements SpringJdbcMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.execute("ALTER TABLE `user` MODIFY `created_at` datetime DEFAULT NOW()");
        jdbcTemplate.execute("ALTER TABLE `post` MODIFY `created_at` datetime DEFAULT NOW()");
        jdbcTemplate.execute("ALTER TABLE `comment` MODIFY `created_at` datetime DEFAULT NOW()");
    }
}
