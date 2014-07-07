/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.util.controller;

import java.util.Collections;
import java.util.List;

import org.flowerplatform.util.UtilConstants;

/**
 * @author Cristina Constantinescu
 */
public class AllDynamicCategoryProvider implements IDynamicCategoryProvider {

	@Override
	public List<String> getDynamicCategories(Object object) {		
		return Collections.singletonList(UtilConstants.CATEGORY_ALL);
	}

}