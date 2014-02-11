package org.flowerplatform.util.controller;

import java.util.Collections;
import java.util.List;

/**
 * @author Cristina Constantinescu
 */
public class AllDynamicCategoryProvider implements IDynamicCategoryProvider {

	public static final String CATEGORY_ALL = TypeDescriptor.CATEGORY_PREFIX + "all";
	
	@Override
	public List<String> getDynamicCategories(Object object) {		
		return Collections.singletonList(CATEGORY_ALL);
	}

}
