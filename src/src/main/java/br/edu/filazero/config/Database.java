package br.edu.filazero.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private Database() {}
    public static Connection connect() throws SQLException {
        String url = env("DB_URL", "jdbc:postgresql://localhost:5432/filazero");
        String user = env("DB_USER", "postgres");
        String password = env("DB_PASSWORD", "");
        return DriverManager.getConnection(url, user, password);
    }
    private static String env(String name, String fallback) {
        String value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }
}
