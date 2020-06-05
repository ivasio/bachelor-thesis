package com.ivasio.bachelor_thesis.shared.configuration;

import org.apache.commons.configuration2.SystemConfiguration;

import java.util.HashMap;
import java.util.Map;


public class PostgresConfig {
    protected final SystemConfiguration systemConfig = new SystemConfiguration();

    public Map<String, Object> getProperties() {
        final Map<String, Object> properties = new HashMap<String, Object>();
        properties.put("POSTGRES_HOST", systemConfig.getString("POSTGRES_HOST"));
        properties.put("POSTGRES_PORT", systemConfig.getInt("POSTGRES_PORT", 5432));
        properties.put("POSTGRES_DB", "main");
        properties.put("POSTGRES_USERNAME", systemConfig.getString("POSTGRES_USERNAME"));
        properties.put("POSTGRES_PASSWORD", systemConfig.getString("POSTGRES_PASSWORD"));
        return properties;
    }
}
