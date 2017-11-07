package org.engineer.work.facade;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageFacade {

	void storeFile(final MultipartFile file, final String username);

	Resource getFile(final String author, final String filename);

	void deleteFile(final String author, final String filename);

	void checkIfMaximumSizeExceeded(final Long size);

	List<String> getUserSharedFiles(final String username);

	List<String> getUserPrivateFiles(final String username);

	void makeFilePublic(final String author, final String filename);

	void makeFilePrivate(final String author, final String filename);
}
