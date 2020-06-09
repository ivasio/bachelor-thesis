package com.ivasio.bachelor_thesis.shared.configuration;

import org.apache.commons.configuration2.EnvironmentConfiguration;

import java.util.Properties;


public class PostgresHibernateConfig {
    protected final EnvironmentConfiguration systemConfig = new EnvironmentConfiguration();

    public Properties getProperties() {
        final Properties properties = new Properties();
        properties.put("hibernate.connection.driver_class", "org.postgresql.Driver");
        properties.put("hibernate.connection.url", systemConfig.getString("POSTGRES_URL"));
        properties.put("hibernate.connection.username", systemConfig.getString("POSTGRES_USER"));
        properties.put("hibernate.connection.password", systemConfig.getString("POSTGRES_PASSWORD"));
        return properties;
    }
}
