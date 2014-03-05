package org.flowerplatform.freeplane.remote;

import org.flowerplatform.freeplane.FreeplanePlugin;

public class FreeplaneService {

	public void load() {
		try {
			FreeplanePlugin.getInstance().getFreeplaneUtils().load(null);
		} catch (Exception e) {
			// TODO CC: to log
			e.printStackTrace();
		}
	}
	
	public void save(String resource) {
		try {
			FreeplanePlugin.getInstance().getFreeplaneUtils().save(resource);
		} catch (Exception e) {
			// TODO CC: to log
			e.printStackTrace();
		}
	}
	
}
