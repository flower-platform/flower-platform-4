package org.flowerplatform.core.node;

import org.flowerplatform.core.node.remote.PropertyDescriptor;
import java.util.Comparator;

/**
 * @author Sebastian Solomon
 */
public class CategoryComparator implements Comparator<PropertyDescriptor>{

	@Override
	public int compare(PropertyDescriptor o1, PropertyDescriptor o2) {
		return o1.getCategory().compareTo(o2.getCategory());
	}

}
