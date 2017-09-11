package org.engineer.work.facade.impl;

import org.engineer.work.facade.StorageFacade;
import org.engineer.work.service.StorageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StorageFacadeImpl implements StorageFacade {

	@Resource
	private StorageService storageService;

	@Override
	public List<String> getUserSharedFiles(final String username) {
		return Arrays.stream(storageService.getUserSharedFiles(username)).map(file -> file.getName()).filter(file -> !file.startsWith(".")).collect(Collectors.toList());
	}

	@Override
	public List<String> getUserPrivateFiles(final String username) {
		return Arrays.stream(storageService.getUserPrivateFiles(username)).map(file -> file.getName()).filter(file -> !file.startsWith(".")).collect(Collectors.toList());
	}
}
