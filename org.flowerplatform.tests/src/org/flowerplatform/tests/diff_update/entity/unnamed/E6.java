package org.flowerplatform.tests.diff_update.entity.unnamed;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.tests.diff_update.entity.AbstractEntity;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class E6 extends AbstractEntity {
	
	public E5 e5Ref;
	
	public List<E7> e7List = new ArrayList<>();
	
	public E6(int id) {
		super(id);
	}

	public E5 getE5Ref() {
		return e5Ref;
	}

	public void setE5Ref(E5 e5Ref) {
		this.e5Ref = e5Ref;
	}

	public List<E7> getE7List() {
		return e7List;
	}

	public void setE7List(List<E7> e7List) {
		this.e7List = e7List;
	}
	
}
