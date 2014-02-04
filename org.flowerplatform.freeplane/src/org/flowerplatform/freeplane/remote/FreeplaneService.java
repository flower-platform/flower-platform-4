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
	
	public void save() {
		try {
			FreeplanePlugin.getInstance().getFreeplaneUtils().save();
		} catch (Exception e) {
			// TODO CC: to log
			e.printStackTrace();
		}
	}
	
}
