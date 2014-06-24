package org.flowerplatform.core.preference.remote;

/**
 * @author Cristina Constantinescu
 */
public class PreferencesServiceRemote {

	public String getPreferenceNodeUri() {
		// NodeService.getNode wasn't used because the nodeUri must be subscribed first		
		return "fpp:user1/repo-1/preferences.mm";
	}
	
}
