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
	
	public DiffUpdateService() {
		this.diffUpdateRegistry = new DiffUpdateRegistry();
	}
	
	public DiffUpdateService(INotificationChannelProvider notificationChannelProvider) {
		this.notificationChannelProvider = notificationChannelProvider;
		this.diffUpdateRegistry = new DiffUpdateRegistry();
	}
	
	public INotificationChannelProvider getNotificationChannelProvider() {
		return notificationChannelProvider;
	}

	public void setNotificationChannelProvider(INotificationChannelProvider notificationChannelProvider) {
		this.notificationChannelProvider = notificationChannelProvider;
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
	
	// TODO CC: temporary code
	public void clearDiffUpdateRegistry() {
		diffUpdateRegistry.clearDiffUpdates();
	}
	
}
