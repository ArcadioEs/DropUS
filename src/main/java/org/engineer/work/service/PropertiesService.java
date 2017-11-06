package org.engineer.work.service;

/**
 * Service to read values from property file
 */
public interface PropertiesService {

	/**
	 * Gets property by given name
	 */
	String getProperty(final String property);
}
