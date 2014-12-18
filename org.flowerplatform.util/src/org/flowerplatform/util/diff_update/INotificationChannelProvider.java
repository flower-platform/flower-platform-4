package org.flowerplatform.util.diff_update;

import java.util.List;

/**
 * 
 * @author Claudiu Matei
 *
 */
public interface INotificationChannelProvider {

	/**
	 * 
	 * @param entity
	 * @return
	 */
	List<String> getNotificationChannels(String entityUid);
	
}
