package edu.java.scrapper;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static java.sql.DriverManager.getConnection;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class IntegrationEnvironmentTest extends IntegrationTest {
    @Test
    public void checkingContainerLaunch() throws SQLException {
        Connection connection = getConnection(POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
        String[] types = {"TABLE"};
        assertThat(connection.getMetaData()
            .getTables(null, null, "chat", types).first()).isTrue();
        assertThat(connection.getMetaData()
            .getTables(null, null, "link", types).first()).isTrue();
        assertThat(connection.getMetaData()
            .getTables(null, null, "chat_link", types).first()).isTrue();
    }
}
