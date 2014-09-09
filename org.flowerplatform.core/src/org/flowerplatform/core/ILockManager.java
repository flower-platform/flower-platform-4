package org.flowerplatform.core;

/**
 * @author Claudiu Matei
 *
 */
public interface ILockManager {

	public void lock(String key);

	public void unlock(String key);

}
