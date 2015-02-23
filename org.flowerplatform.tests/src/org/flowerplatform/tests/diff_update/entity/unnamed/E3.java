package org.flowerplatform.tests.diff_update.entity.unnamed;

import org.flowerplatform.tests.diff_update.entity.AbstractEntity;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class E3 extends AbstractEntity {

	public E2 e2Ref;

	public E4 e4Ref;

	public E8 e8Ref;
	
	public E3(int id) {
		super(id);
	}

	public E2 getE2Ref() {
		return e2Ref;
	}

	public void setE2Ref(E2 e2Ref) {
		this.e2Ref = e2Ref;
	}

	public E4 getE4Ref() {
		return e4Ref;
	}

	public void setE4Ref(E4 e4Ref) {
		this.e4Ref = e4Ref;
	}

	public E8 getE8Ref() {
		return e8Ref;
	}

	public void setE8Ref(E8 e8Ref) {
		this.e8Ref = e8Ref;
	}
	
	
}
