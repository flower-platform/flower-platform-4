package org.flowerplatform.tests.diff_update.entity.unnamed;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.tests.diff_update.entity.AbstractEntity;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class E1 extends AbstractEntity {

	public List<E2> e2List = new ArrayList<>();
	
	public E7 e7Ref; 
	
	public E1(int id) {
		super(id);
	}

	public boolean isRoot() {
		return true;
	}

	public List<E2> getE2List() {
		return e2List;
	}

	public void setE2List(List<E2> e2List) {
		this.e2List = e2List;
	}

	public E7 getE7Ref() {
		return e7Ref;
	}

	public void setE7Ref(E7 e7Ref) {
		this.e7Ref = e7Ref;
	}
	
}
