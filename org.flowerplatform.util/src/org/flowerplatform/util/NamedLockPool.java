/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.util;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Locking mechanism that uses <code>Object</code> objects as locks. The difference between this
 * and the standard lock mechanism in Java is that <code>.equals()</code> instead of <code>==</code>.
 * More precisely:
 * 
 * <pre>
 * synchronized (new String("myString")) {
 * 	// some code
 * }
 * </pre>
 * 
 * In the above case, the block won't give the expected results. 2 threads will still be able to 
 * execute the block at the same time, because the locks are <strong>different</strong> object instances.
 * 
 * With this class, we can achieve the desired behavior.
 * First, instantiate it, probably like an attribute of the class.
 * <pre>
 * private final NamedLockPool namedLockPool = new NamedLockPool();
 * </pre> 
 * 
 * Then use it:
 * <pre>
 * namedLockPool.lock(new String("myString"));
 * try {
 * 	// some code
 * } finally {
 * 	namedLockPool.unlock(new String("myString"));
 * }
 * </pre>
 * 
 * Note:
 * It is recommended to use <code>String</code> objects as locks. 
 * Otherwise make sure your objects are unique.
 * For the moment, only <code>GenericTreeStatefulService</code> uses Object.
 * @author Cristina
 * 
 * @author Cristi
 */
public class NamedLockPool {

	static private class LockWithCounter extends ReentrantLock {
		private static final long serialVersionUID = 1L;
		
		/**
		 * @see The comment in the code, below, for details.
		 */
		private AtomicInteger numberOfThreadsWaitingToLock = new AtomicInteger();
	}
	
	private Map<Object, LockWithCounter> currentLocks = new HashMap<Object, LockWithCounter>();

	/*
	 * == Synchronization scenarios ==
	 * Other thread calls:
	 * A) lock(_same_key_)
	 * B) lock(_other_key_)
	 * 
	 * C) unlock(_same_key_)
	 * D) unlock(_other_key_)
	 * 
	 * For each case, the other thread is a little bit BEHIND me. The other way (i.e. the other thread
	 * is AHEAD OF ME) is documented in the other method. 
	 */
	
	public void lock(Object key) {
		LockWithCounter lockForCurrentKey = null;
		
		// the following block can be executed at the same time, once per instance of this
		// class, no matter the key. So in all cases, the threads will wait here. 
		synchronized (this) {
			lockForCurrentKey = currentLocks.get(key);
			if (lockForCurrentKey == null) {
				lockForCurrentKey = new LockWithCounter();
				currentLocks.put(key, lockForCurrentKey);
			}
			lockForCurrentKey.numberOfThreadsWaitingToLock.incrementAndGet();
		}
		
		/*
		 * == Explanation ==
		 * This section, until calling .lock() is tricky. While I'm in this section, 
		 * I don't want the unlock() method to remove the lock from the map. That's why we introduced 
		 * numberOfThreadsWaitingToLock: a counter of threads that are in this zone (i.e. I'm preparing to acquire the lock). 
		 * 
		 * The synchronized block doesn't cover all the method (like in the case of unlock()), because we want the waiting (to acquire
		 * the lock) to happen outside the synchronized block. Otherwise, we would create a big bottle neck, allowing only 1 thread to execute
		 * at a given moment (regardless of the key).
		 * 
		 * == Scenarios ==
		 * The following block can be executed simultaneously.
		 * For B) and D), everything OK, because the variable points towards different instances.
		 * For A) the other thread will go to sleep when calling .lock().
		 * for C) there are 2 cases
		 * 	1) this thread will block when calling .lock(), so the other thread will finish execution, but won't
		 * 		remove this lock, because numberOfThreadsWaitingToLock >= 1 while this thread waits for .lock()
		 * 	2) this thread will acquire successfully the lock. While this thread is executing methods after
		 * 		the .lock() line, other threads may not call .unlock(); only this thread may call .unsubscribe().
		 * 		However, if they try, they'll get an exception, because they try to unlock a lock that's not owned 
		 * 		by them. 
		 */
		try {
			// this call shouldn't throw an exception AFAIK; but to be sure, it's
			// wrapped around a try/finally
			lockForCurrentKey.lock(); // this call is blocking, until lock is acquired
		} finally {
			// because this doesn't happen under a synchronized block, we need to ensure that the
			// decrementation is done atomically. Otherwise what could happen (although quite rarely): 
			// 2 threads are incrementing 5, and the result could be 6 (because of the caching of the
			// current value)
			lockForCurrentKey.numberOfThreadsWaitingToLock.decrementAndGet();
		}
	}
	
	public void unlock(Object key) {
		/*
		 * C) and D) other thread will wait for this whole method to end. Same for A),
		 * 		if the execution pointer of the other thread is before its synchronized block
		 * A) if the execution pointer is after the synchronized block (e.g. the other thread got suspended/delayed
		 * 		right after the synchronized block, resulting in this thread to be AHEAD of the other thread), see 
		 * 		below:
		 */
		synchronized (this) {
			LockWithCounter lockForCurrentKey = null;
			lockForCurrentKey = currentLocks.get(key);
			
			if (lockForCurrentKey == null) {
				throw new IllegalArgumentException(String.format("Attempt to unlock the lock with key = %s, by thread = %s; but there is no lock for this key!", key, Thread.currentThread()));
			}
			if (!lockForCurrentKey.isHeldByCurrentThread()) {
				// sanity check; anyway, without this here, the unlock() method would have thrown an exception as well; but at least,
				// this is gives a bit more information
				throw new IllegalStateException(String.format("Attempt to unlock the lock with key = %s, by thread = %s; but this thread is not the owner of the lock!", key, Thread.currentThread()));
			}
			
			 // case A) until here, the other thread will wait, at it's .lock() line
			lockForCurrentKey.unlock();
			/*
			 * case A) the other thread will continue. If it is the only one waiting, 
			 * numberOfThreadsWaitingToLock will drop to 0, but I won't remove the lock, because the lock
			 * will be held by other thread. If the other thread quickly finishes and calls .unlock(), we
			 * have the C) case, discussed at the beginning of this doc
			 */ 
			if (lockForCurrentKey.numberOfThreadsWaitingToLock.get() == 0 && !lockForCurrentKey.isLocked()) {
				currentLocks.remove(key);
			}
		}
	}
	
	/**
	 * To facilitate JMX exposure, in order to check the state.
	 */
	public int getNumberOfRegisteredLocks() {
		return currentLocks.size();
	}
	
//	static public void main(String[] args) {
//		final NamedLockPool pool = new NamedLockPool();
//		// 2 separate implementations (instead of only one) to facilitate testing with the debugger
//		Thread thread1 = new Thread("User 1") {
//			@Override
//			public void run() {
//				System.out.println(String.format("%s: Outside sync block; preparing to enter", getName()));
//				pool.lock(new String("key"));
//				try {
//					System.out.println(String.format("%s: Inside sync block; instruction 1", getName()));
//					System.out.println(String.format("%s: Inside sync block; instruction 2", getName()));
//					System.out.println(String.format("%s: Inside sync block; instruction 3", getName()));
//				} finally {
//					pool.unlock(new String("key"));
//				}
//				System.out.println(String.format("%s: Outside sync block; after exit", getName()));
//			}
//		};
//		
//		Thread thread2 = new Thread("User 2") {
//			@Override
//			public void run() {
//				System.out.println(String.format("%s: Outside sync block; preparing to enter", getName()));
//				pool.lock(new String("key"));
//				try {
//					System.out.println(String.format("%s: Inside sync block; instruction 1", getName()));
//					System.out.println(String.format("%s: Inside sync block; instruction 2", getName()));
//					System.out.println(String.format("%s: Inside sync block; instruction 3", getName()));
//				} finally {
//					pool.unlock(new String("key"));
//				}
//				System.out.println(String.format("%s: Outside sync block; after exit", getName()));
//			}
//		};
//		
//		thread1.start();
//		thread2.start();
//	}
}