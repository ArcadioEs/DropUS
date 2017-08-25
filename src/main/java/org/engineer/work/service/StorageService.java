package org.engineer.work.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

	void init();

	void store(MultipartFile file, String username);

	Stream<Path> loadAll(String username);

	Path load(String filename);

	Resource loadAsResource(String filename);

	File[] getUserSharedFiles(final String username);

	void deleteAll();
}
