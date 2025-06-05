package com.lunarlog.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DataSourceTest {

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Test
	void testConnection() throws SQLException {
		assertThat(dataSource).isNotNull();
		assertThat(dataSource.getConnection()).isNotNull();

		String result = jdbcTemplate.queryForObject("SELECT 'Connected'", String.class);
		assertThat(result).isEqualTo("Connected");
	}
}
