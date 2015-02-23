package org.flowerplatform.tests.diff_update.entity.unnamed;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.tests.diff_update.entity.AbstractEntity;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class E5 extends AbstractEntity {

	public List<E2> e2List = new ArrayList<>();

	public E4 e4Ref;
	
	public List<E6> e6List = new ArrayList<>();
	
	public E5(int id) {
		super(id);
	}

	public E8 e8Ref;

	public List<E2> getE2List() {
		return e2List;
	}

	public void setE2List(List<E2> e2List) {
		this.e2List = e2List;
	}

	public E4 getE4Ref() {
		return e4Ref;
	}

	public void setE4Ref(E4 e4Ref) {
		this.e4Ref = e4Ref;
	}

	public List<E6> getE6List() {
		return e6List;
	}

	public void setE6List(List<E6> e6List) {
		this.e6List = e6List;
	}

	public E8 getE8Ref() {
		return e8Ref;
	}

	public void setE8Ref(E8 e8Ref) {
		this.e8Ref = e8Ref;
	}
	
}
