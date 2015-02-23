package org.flowerplatform.tests.diff_update.entity.unnamed;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.tests.diff_update.entity.AbstractEntity;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class E2 extends AbstractEntity {

	public E1 e1Ref; 

	public List<E3> e3List = new ArrayList<>();
	
	public E5 e5Ref; 
	
	public E2(int id) {
		super(id);
	}

	public E1 getE1Ref() {
		return e1Ref;
	}

	public void setE1Ref(E1 e1Ref) {
		this.e1Ref = e1Ref;
	}

	public List<E3> getE3List() {
		return e3List;
	}

	public void setE3List(List<E3> e3List) {
		this.e3List = e3List;
	}

	public E5 getE5Ref() {
		return e5Ref;
	}

	public void setE5Ref(E5 e5Ref) {
		this.e5Ref = e5Ref;
	}
	
}
