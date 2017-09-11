package org.engineer.work.service.impl;

import org.engineer.work.exception.StorageException;
import org.engineer.work.exception.StorageFileNotFoundException;
import org.engineer.work.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageServiceImpl implements StorageService {

	private static final String SHARED = "/shared/";
	private static final String NOT_SHARED = "/not_shared/";

	private Path rootLocation;

	@Autowired
	private StorageProperties storageProperties;
	@Autowired
	public StorageServiceImpl(final StorageProperties properties) {
		this.rootLocation = Paths.get(properties.getLocation());
	}

	@Override
	public void store(final MultipartFile file, final String username) {
		final String filename = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if (file.isEmpty()) {
				throw new StorageException("Failed to store empty file " + filename);
			}
			if (filename.contains("..")) {
				// This is a security check
				throw new StorageException(
						"Cannot store file with relative path outside current directory "
								+ filename);
			}
			final Path location = Paths.get(storageProperties.getLocation() + username + NOT_SHARED);
			Files.copy(file.getInputStream(), location.resolve(filename),
					StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException e) {
			throw new StorageException("Failed to store file " + filename, e);
		}
	}

	@Override
	public Path load(final String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(final String filename) {
		try {
			final Path file = load(filename);
			final Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);
			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public File[] getUserSharedFiles(final String username) {
		return new File(storageProperties.getLocation() + username + SHARED).listFiles();
	}

	@Override
	public File[] getUserPrivateFiles(final String username) {
		return new File(storageProperties.getLocation() + username + NOT_SHARED).listFiles();
	}
}
