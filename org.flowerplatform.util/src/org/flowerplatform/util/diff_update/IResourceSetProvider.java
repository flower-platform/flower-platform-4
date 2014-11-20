package org.flowerplatform.util.diff_update;

import java.util.List;

/**
 * 
 * @author Claudiu Matei
 *
 */
public interface IResourceSetProvider {

	/**
	 * 
	 * @param uid
	 * @return
	 */
	List<String> getResourceSets(String entityUid);
	
}
