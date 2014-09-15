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
package org.flowerplatform.freeplane;

import java.lang.reflect.Field;

import org.freeplane.core.util.LogUtils;
import org.freeplane.features.attribute.ModelessAttributeController;
import org.freeplane.features.filter.FilterController;
import org.freeplane.features.format.FormatController;
import org.freeplane.features.format.ScannerController;
import org.freeplane.features.help.HelpController;
import org.freeplane.features.icon.IconController;
import org.freeplane.features.link.LinkController;
import org.freeplane.features.map.MapController;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.styles.LogicalStyleFilterController;
import org.freeplane.features.text.TextController;
import org.freeplane.features.time.TimeController;
import org.freeplane.main.application.ApplicationResourceController;
import org.freeplane.main.application.FreeplaneGUIStarter;
import org.freeplane.main.application.FreeplaneSecurityManager;
import org.freeplane.main.headlessmode.HeadlessMapViewController;
import org.freeplane.main.headlessmode.HeadlessUIController;
import org.freeplane.view.swing.features.nodehistory.NodeHistory;

/**
 * Same as {@link FreeplaneHeadlessStarter} without initializing the logger.
 * 
 * @author Cristina Constantinescu
 */
public class FreeplaneHeadlessStarter extends org.freeplane.main.headlessmode.FreeplaneHeadlessStarter {

	@Override
	public Controller createController() {
		try {
			// applicationResourceController is private in super, get its value using reflection
			Field field = org.freeplane.main.headlessmode.FreeplaneHeadlessStarter.class.getDeclaredField("applicationResourceController");
			field.setAccessible(true);
			ApplicationResourceController applicationResourceController = (ApplicationResourceController) field.get(this);
			
			Controller controller = new Controller(applicationResourceController);
			Controller.setCurrentController(controller);
			applicationResourceController.init();
//			LogUtils.createLogger();
			FreeplaneGUIStarter.showSysInfo();
			final HeadlessMapViewController mapViewController = new HeadlessMapViewController();
			controller.setMapViewManager(mapViewController);
			controller.setViewController(new HeadlessUIController(controller, mapViewController, ""));
			System.setSecurityManager(new FreeplaneSecurityManager());
			FilterController.install();
			FormatController.install(new FormatController());
	        final ScannerController scannerController = new ScannerController();
	        ScannerController.install(scannerController);
	        scannerController.addParsersForStandardFormats();
			ModelessAttributeController.install();
			TextController.install();
			TimeController.install();
			LinkController.install();
			IconController.install();
			HelpController.install();
			FilterController.getCurrentFilterController().getConditionFactory().addConditionController(7,
			    new LogicalStyleFilterController());
			MapController.install();

			NodeHistory.install(controller);
			return controller;
		} catch (final Exception e) {
			LogUtils.severe(e);
			throw new RuntimeException(e);
		}
	}

}
