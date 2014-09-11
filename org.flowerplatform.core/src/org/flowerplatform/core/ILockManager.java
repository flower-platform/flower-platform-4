package org.flowerplatform.core;

/**
 * @author Claudiu Matei
 *
 */
public interface ILockManager {

	/**
	 *@author see class
	 **/
	void lock(String key);

	/**
	 *@author see class
	 **/
	void unlock(String key);

}
