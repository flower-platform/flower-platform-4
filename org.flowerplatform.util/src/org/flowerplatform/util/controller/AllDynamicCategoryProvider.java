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
