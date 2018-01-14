package org.engineer.work.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;

public interface StorageService {

	void store(final MultipartFile file, final String username);

	Path load(final String filename);

	Resource loadAsResource(final String path);

	void deleteFile(final String author, final String filename);

	File[] getUserSharedFiles(final String username);

	File[] getUserPrivateFiles(final String username);

	void moveFile(final String src, final String dest);
}
