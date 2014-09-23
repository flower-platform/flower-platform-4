package org.flowerplatform.core.repositories;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Cristina Brinza
 *
 */
public class ExtensionInfoInFile {

	private String id;
	
	private boolean isTransitive;
	
	private List<String> extensionsThatDependOnThis = new ArrayList<String>();
	
//	private static final long serialVersionUID = 42L;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isTransitive() {
		return isTransitive;
	}

	//CHECKSTYLE:OFF
	public void setTransitive(boolean isTransitive) {
		this.isTransitive = isTransitive;
	}
	//CHECKSTYLE:ON
	
	public List<String> getExtensionsThatDependOnThis() {
		return extensionsThatDependOnThis;
	}

	public void setExtensionsThatDependOnThis(List<String> extensionsThatDependOnThis) {
		this.extensionsThatDependOnThis = extensionsThatDependOnThis;
	}
	
	/**
	 * @author see class
	 */
	public void addDependency(String extensionId) {
		if (extensionId != null) {		
			if (extensionsThatDependOnThis == null) {
				extensionsThatDependOnThis = new ArrayList<String>();
			}
			extensionsThatDependOnThis.add(extensionId);
		}
	}
	
	/**
	 * @author see class
	 */
	public void removeDependency(String extensionId) {
		extensionsThatDependOnThis.remove(extensionId);
	}
	
	/**
	 * @author see class
	 */
	public boolean contains(String extensionId) {
		return extensionsThatDependOnThis.contains(extensionId);
	}
	
	/**
	 * @author see class
	 */
//	public int get(String extensionId) {
//		return extensionsThatDependOnThis.(extensionId);
//	}
	
	/**
	 * @author see class
	 * This is needed for the serialization / deserialization of extensions in users file
	 */
	public String toStringList() {
		if (extensionsThatDependOnThis.size() != 0) {
			String result = "(";
			for (int index = 0; index < extensionsThatDependOnThis.size() - 1; index++) {
				result += extensionsThatDependOnThis.get(index) + "#";
			}
			result += extensionsThatDependOnThis.get(extensionsThatDependOnThis.size() - 1) + ")";
			return result;
		} else {
			return "()";
		}
	}
	
	@Override
	public String toString() {
		return id + ":" + isTransitive + ":" + toStringList();
	}
}
