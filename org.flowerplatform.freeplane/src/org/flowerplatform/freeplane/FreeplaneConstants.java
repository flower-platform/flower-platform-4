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

import org.flowerplatform.core.CoreConstants;


/**
 * @author Catalin Burcea
 * @author Valentina Bojan
 */
public final class FreeplaneConstants {
	
	/**
	 * @author Valentina Bojan
	 */
	private FreeplaneConstants() {
	}

	public static final String FREEPLANE_MINDMAP_RESOURCE_SCHEMA = "fpm1";
	public static final String FREEPLANE_MINDMAP_RESOURCE_DELEGATE_CATEGORY = CoreConstants.CATEGORY_RESOURCE_PREFIX + FREEPLANE_MINDMAP_RESOURCE_SCHEMA;
	public static final String FREEPLANE_NODE_TYPE = "freeplaneNode1";
	
	/////////////////////////////////////////////////////////////
	// for XmlParser and XmlWritter
	/////////////////////////////////////////////////////////////

	public static final String ICON = "icon";
	public static final String HOOK = "hook";
	public static final String RICHCONTENT = "richcontent";
	public static final String ATTRIBUTE = "attribute";
	public static final String UNKNOWN = "unknown";
	public static final String NODE = "node";
	public static final String HOOK_KEY_PROPERTY = "NAME";
	public static final String RICHCONTENT_KEY_PROPERTY = "TYPE";
	public static final String ICON_KEY_PROPERTY = "BUILTIN";
	public static final String ATTRIBUTE_KEY_PROPERTY = "NAME";
	public static final String ICONS = "icons";
	
	public static final String CONTENT_MARK = "_content";
}
