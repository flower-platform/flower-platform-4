package org.flowerplatform.js_client.java.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.remote.FullNodeIdWithChildren;

/**
 * @author Cristina Constantinescu
 */
public class JavaHostInvocator {

	/**
	 * Used to instantiate <code>children</code> in node object.
	 */
	public List<Object> createListInstance() {
		return new ArrayList<>();
	}

	public FullNodeIdWithChildren createFullNodeIdWithChildrenInstance() {
		return new FullNodeIdWithChildren();
	}

	public Map<String, Object> createMapInstance() {
		return new HashMap<String, Object>();
	}

	public void addInMap(Map<String, Object> map, String key, Object value) {
		map.put(key, value);
	}
	
	public void showMessageBox(String titleKeyMessage, String textKeyMessage, String[] textParams) {
		// TODO implement
		System.out.println("showMessageBox(" + titleKeyMessage + " " + textKeyMessage + " " + textParams);
	}
	
	public boolean contains(List<Object> list, Object o) {
		return list.contains(o);
	}
	
	public int getLength(List<Object> list) {
		return list.size();
	}
	
	public int getItemIndex(List<Object> list, Object o) {		
		return list.indexOf(o);
	}
	
	public Object getItemAt(List<Object> list, int index) {		
		return list.get(index);
	}
	
	public Object removeItemAt(List<Object> list, int index) {	
		return list.remove(index);
	}
	
	public void addItem(List<Object> list, Object o) {
		list.add(o);		
	}
	
	public void addItemAt(List<Object> list, Object o, int index) {
		list.add(index, o);		
	}	
	
}
