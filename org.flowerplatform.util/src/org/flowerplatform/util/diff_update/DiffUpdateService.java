/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
 * license-end
 */
package org.flowerplatform.util.diff_update;

import java.util.List;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class DiffUpdateService {
	
	private INotificationChannelProvider notificationChannelProvider;	
	
	private DiffUpdateRegistry diffUpdateRegistry;
	
	public DiffUpdateService(INotificationChannelProvider notificationChannelProvider) {
		this.notificationChannelProvider = notificationChannelProvider;
		this.diffUpdateRegistry = new DiffUpdateRegistry();
	}
	
	/**
	 * @author see class
	 */
	public void addUpdate(String notificationChannel, DiffUpdate update) {
		diffUpdateRegistry.addUpdate(notificationChannel, update);
	}
	
	/**
	 * 
	 * @param update
	 */
	public void addUpdate(DiffUpdate update) {
		String entityUid = update.getEntityUid();
		List<String> updateChannels = notificationChannelProvider.getNotificationChannels(entityUid);
		if (updateChannels == null) {
			return;
		}
		for (String updateChannel : updateChannels) {
			diffUpdateRegistry.addUpdate(updateChannel, update);
		}
	}

	/**
	 * 
	 * @param updateChannel
	 * @param firstId
	 * @param lastId
	 */
	public List<DiffUpdate> getUpdates(String updateChannel, long firstId, long lastId) {
		return diffUpdateRegistry.getUpdates(updateChannel, firstId, lastId);
	}

	/**
	 * 
	 * @param notificationChannel
	 */
	public void registerNotificationChannel(String notificationChannel) {
		diffUpdateRegistry.registerNotificationChannel(notificationChannel);
	}

	/**
	 * 
	 * @param notificationChannel
	 */
	public void unregisterNotificationChannel(String notificationChannel) {
		diffUpdateRegistry.unregisterNotificationChannel(notificationChannel);
	}

	/**
	 * @author see class
	 */
	public long getLastUpdateId(String notificationChannel) {
		return diffUpdateRegistry.getLastUpdateId(notificationChannel);
	}
	
}
