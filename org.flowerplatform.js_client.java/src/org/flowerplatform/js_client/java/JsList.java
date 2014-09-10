package org.flowerplatform.js_client.java;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Provides additional behavior to match mx.collections.IList.
 * @author Cristina Constantinescu
 */
public class JsList<E> extends ArrayList<E> {

	private static final long serialVersionUID = 1L;
		
	public int length;
	
	public JsList() {	
	}
	
	public JsList(Collection<? extends E> c) {
		super(c);
		length = size();
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
	
	public void addItem(E o) {
		add(o);		
	}
	
	public void addItemAt(E o, int index) {
		add(index, o);		
	}

	@Override
	public boolean add(E e) {		
		boolean flag = super.add(e);
		length = size();
		return flag;
	}

	@Override
	public void add(int index, E element) {		
		super.add(index, element);
		length = size();
	}

	@Override
	public E remove(int index) {		
		E obj = super.remove(index);
		length = size();
		return obj;
	}

	@Override
	public boolean remove(Object o) {		
		boolean flag = super.remove(o);
		length = size();
		return flag;
	}
	
	
}
