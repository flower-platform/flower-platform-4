package org.flowerplatform.js_client.java;

import java.util.ArrayList;

public class JsList<E> extends ArrayList<E> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Override
	public boolean contains(Object o) {		
		return super.contains(o);
	}
	
	public int getItemIndex(Object o) {		
		return indexOf(o);
	}
	
	public E getItemAt(int index) {		
		return get(index);
	}
	
	public E removeItemAt(int index) {		
		return remove(index);
	}
	
	public int length() {		
		return super.size();
	}
		
}
