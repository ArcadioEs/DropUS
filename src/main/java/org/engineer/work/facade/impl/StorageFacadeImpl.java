package org.engineer.work.facade.impl;

import org.engineer.work.facade.StorageFacade;
import org.engineer.work.service.PropertiesService;
import org.engineer.work.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageFacadeImpl implements StorageFacade {

	private static final String FILES_LOCATION = "dropus.files.root";
	private static final String FILE_MAX_SIZE = "dropus.files.max.size";
	private static final String SHARED = "/shared/";
	private static final String NOT_SHARED = "/not_shared/";

	@Autowired
	private StorageService storageService;
	@Autowired
	private PropertiesService propertiesService;

	@Override
	public void storeFile(final MultipartFile file, final String username) {
		checkIfMaximumSizeExceeded(file.getSize());

		storageService.store(file, username);
	}

	@Override
	public Resource getFile(final String author, final String filename) {
		final String path = author + SHARED + filename;
		return storageService.loadAsResource(path);
	}

	@Override
	public void deleteFile(final String author, final String filename) {
		storageService.deleteFile(author, filename);
	}

	@Override
	public void checkIfMaximumSizeExceeded(final Long size) {
		Long maxSize = Long.parseLong(propertiesService.getProperty(FILE_MAX_SIZE));

		if (size > maxSize) throw new MaxUploadSizeExceededException(maxSize);
	}

	@Override
	public List<String> getUserSharedFiles(final String username) {
		return Arrays.stream(storageService.getUserSharedFiles(username)).map(file -> file.getName()).filter(file -> !file.startsWith(".")).collect(Collectors.toList());
	}

	@Override
	public List<String> getUserPrivateFiles(final String username) {
		return Arrays.stream(storageService.getUserPrivateFiles(username)).map(file -> file.getName()).filter(file -> !file.startsWith(".")).collect(Collectors.toList());
	}

	@Override
	public void makeFilePublic(final String author, final String filename) {
		final String sourceFile = propertiesService.getProperty(FILES_LOCATION) + author + NOT_SHARED + filename;
		final String destination = propertiesService.getProperty(FILES_LOCATION) + author + SHARED + filename;

		storageService.moveFile(sourceFile, destination);
	}

	@Override
	public void makeFilePrivate(final String author, final String filename) {
		final String sourceFile = propertiesService.getProperty(FILES_LOCATION) + author + SHARED + filename;
		final String destination = propertiesService.getProperty(FILES_LOCATION) + author + NOT_SHARED + filename;

		storageService.moveFile(sourceFile, destination);
	}
}
