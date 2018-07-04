package db.migration;

import org.flywaydb.core.api.migration.spring.SpringJdbcMigration;
import org.springframework.jdbc.core.JdbcTemplate;

public class V8__AlterUserRequiredFields implements SpringJdbcMigration {
    @Override
    public void migrate(JdbcTemplate jdbcTemplate) throws Exception {
        jdbcTemplate.execute("ALTER TABLE `user` MODIFY `email` varchar(255) NOT NULL");
        jdbcTemplate.execute("ALTER TABLE `user` MODIFY `name` varchar(255) NOT NULL");
        jdbcTemplate.execute("ALTER TABLE `user` MODIFY `password` varchar(255) NOT NULL");
        jdbcTemplate.execute("ALTER TABLE `user` MODIFY `token` varchar(255) NOT NULL");
    }
}
