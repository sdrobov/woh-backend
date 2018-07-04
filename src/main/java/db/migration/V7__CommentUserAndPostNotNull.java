package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

public class V7__CommentUserAndPostNotNull implements SpringJdbcMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.execute("ALTER TABLE `comment` MODIFY `user_id` bigint(20) NOT NULL");
        jdbcTemplate.execute("ALTER TABLE `comment` MODIFY `post_id` bigint(20) NOT NULL");
    }
}
