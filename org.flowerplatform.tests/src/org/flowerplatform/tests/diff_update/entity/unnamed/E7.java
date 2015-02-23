package org.flowerplatform.tests.diff_update.entity.unnamed;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.tests.diff_update.entity.AbstractEntity;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class E7 extends AbstractEntity {

	public List<E1> e1List = new ArrayList<>();
	
	public E6 e6Ref;
	
	public E7(int id) {
		super(id);
	}

	public List<E1> getE1List() {
		return e1List;
	}

	public void setE1List(List<E1> e1List) {
		this.e1List = e1List;
	}

	public E6 getE6Ref() {
		return e6Ref;
	}

	public void setE6Ref(E6 e6Ref) {
		this.e6Ref = e6Ref;
	}
	
}
