package org.engineer.work.service.impl;

import org.engineer.work.exception.StorageException;
import org.engineer.work.exception.StorageFileNotFoundException;
import org.engineer.work.service.PropertiesService;
import org.engineer.work.service.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class StorageServiceImpl implements StorageService {

	private static final Logger LOG = LoggerFactory.getLogger(StorageServiceImpl.class);

	private static final String FILES_LOCATION = "dropus.files.root";
	private static final String SHARED = "/shared/";
	private static final String NOT_SHARED = "/not_shared/";

	private String filesLocation;
	private Path rootLocation;

	@Autowired
	private PropertiesService propertiesService;

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
			final Path location = Paths.get(filesLocation + username + NOT_SHARED);
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
	public Resource loadAsResource(final String path) {
		try {
			final Path file = load(path);
			final Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + path);
			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file from " + path, e);
		}
	}

	@Override
	public void deleteFile(final String author, final String filename) {
		final String filePath = filesLocation + author + NOT_SHARED + filename;
		final File file = new File(filePath);
		if (file.exists() && file.canWrite()) {
			if (!file.delete()) {
				LOG.warn("Could not delete file {}", filename);
			}
		}
	}

	@Override
	public File[] getUserSharedFiles(final String username) {
		return new File(filesLocation + username + SHARED).listFiles();
	}

	@Override
	public File[] getUserPrivateFiles(final String username) {
		return new File(filesLocation + username + NOT_SHARED).listFiles();
	}

	@Override
	public void moveFile(final String src, final String dest) {
		final File file = new File(src);
		if (file.exists() || file.canWrite()) {
			if (!file.renameTo(new File(dest))) {
				LOG.warn("Could not move file {}", file.getName());
			}
		}
	}

	@PostConstruct
	public void setLocations() {
		filesLocation = propertiesService.getProperty(FILES_LOCATION);
		rootLocation = Paths.get(filesLocation);
	}
}
