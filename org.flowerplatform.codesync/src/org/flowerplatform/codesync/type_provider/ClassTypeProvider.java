package org.flowerplatform.codesync.type_provider;

import java.util.HashMap;
import java.util.Map;

/**
 * Convenience implementation for {@link ITypeProvider}s that have a 
 * class to type correspondence. Extending classes should add entries
 * to {@link #classToTypeMap}.
 * 
 * @author Mariana Gheorghe
 */
public abstract class ClassTypeProvider implements ITypeProvider {

	protected Map<Class<?>, String> classToTypeMap = new HashMap<Class<?>, String>();
	
	@Override
	public String getType(Object object) {
		return classToTypeMap.get(object.getClass());
	}

}
