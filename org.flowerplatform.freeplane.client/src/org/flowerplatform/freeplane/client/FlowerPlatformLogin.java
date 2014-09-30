package org.flowerplatform.freeplane.client;

import java.awt.event.ActionEvent;

import org.freeplane.core.ui.AFreeplaneAction;

/**
 * @author Mariana Gheorghe
 */
public class FlowerPlatformLogin extends AFreeplaneAction {

	private static final long serialVersionUID = 1L;

	public FlowerPlatformLogin() {
		super("FlowerPlatformLogin");
	}

	/**
	 * 
	 */
	public void actionPerformed(ActionEvent event) {
		AuthBrowser browser = new AuthBrowser();
		browser.setVisible(true);
	}

}
