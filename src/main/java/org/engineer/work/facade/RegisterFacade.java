package org.engineer.work.facade;

/**
 * Facade used to register new users.
 */
public interface RegisterFacade {

	boolean registerUser(final String username, final String password);
}
