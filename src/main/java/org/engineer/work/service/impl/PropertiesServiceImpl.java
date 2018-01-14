package org.engineer.work.service.impl;

import org.engineer.work.service.PropertiesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Default implementation of {@link PropertiesService}
 */
@Service
public class PropertiesServiceImpl implements PropertiesService {
	private static final Logger LOG = LoggerFactory.getLogger(PropertiesServiceImpl.class);
	private static final String CONFIG_LOCATION = "src/main/resources/application.properties";

	private Properties properties = new Properties();

	public PropertiesServiceImpl() {
		try {
			properties.load(new FileInputStream(CONFIG_LOCATION));
		} catch (IOException e) {
			LOG.warn("Something went wrong while loading property file");
		}
	}

	@Override
	public String getProperty(final String property) {
		return (property != null) ? properties.getProperty(property) : null;
	}
}
