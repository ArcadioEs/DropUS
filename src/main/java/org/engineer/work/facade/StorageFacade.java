package org.engineer.work.facade;

import java.util.List;

public interface StorageFacade {

	List<String> getUserSharedFiles(final String username);
}
