package org.engineer.work.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
public class StorageProperties {
	private static final Logger LOG = LoggerFactory.getLogger(StorageProperties.class);

	private static final String locationProperty = "dropus.files.root";
	private Properties properties = new Properties();

	public StorageProperties() {
		try {
			properties.load(new FileInputStream("src/main/resources/application.properties"));
		} catch (IOException e) {
			LOG.warn("Something went wrong while loading property file");
		}
	}

	public String getLocation() {
		return properties.getProperty(locationProperty);
	}
}
