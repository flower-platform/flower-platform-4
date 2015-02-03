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

import java.util.Map;


/**
 * 
 * @author Claudiu Matei
 *
 */
public class DiffUpdatesRequest {

	private Map<String, Object>[] notificationChannelsData;

	public Map<String, Object>[] getNotificationChannelsData() {
		return notificationChannelsData;
	}

	public void setNotificationChannelsData(Map<String, Object>[] notificationChannelsData) {
		this.notificationChannelsData = notificationChannelsData;
	} 
	
}
