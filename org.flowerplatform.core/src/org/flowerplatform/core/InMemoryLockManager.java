package org.flowerplatform.core;

import org.flowerplatform.util.NamedLockPool;

/**
 * @author Claudiu Matei
 * 
 */
public class InMemoryLockManager implements ILockManager {
private NamedLockPool lockPool=new NamedLockPool();
	
	@Override
	public void lock(String key) {
		lockPool.lock(key);
	}

	@Override
	public void unlock(String key) {
		lockPool.unlock(key);
	}
	
}
