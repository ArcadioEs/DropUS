package org.engineer.work.service.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("storage")
@Component
public class StorageProperties {

	/**
	 * Folder location for storing files
	 */
	private String location = "/Users/i332319/Desktop/DropUS_files/";

	public String getLocation() {
		return location;
	}

	public void setLocation(final String location) {
		this.location = location;
	}

}
