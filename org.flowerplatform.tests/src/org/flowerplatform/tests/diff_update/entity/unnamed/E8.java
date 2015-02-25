package org.flowerplatform.tests.diff_update.entity.unnamed;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.tests.diff_update.entity.AbstractEntity;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class E8 extends AbstractEntity {

	public List<E3> e3List = new ArrayList<>();
	
	public List<E5> e5List = new ArrayList<>();
	
	public E8(int id) {
		super(id);
	}

	public List<E3> getE3List() {
		return e3List;
	}

	public void setE3List(List<E3> e3List) {
		this.e3List = e3List;
	}

	public List<E5> getE5List() {
		return e5List;
	}

	public void setE5List(List<E5> e5List) {
		this.e5List = e5List;
	}
	
}
