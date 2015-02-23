package org.flowerplatform.tests.diff_update.entity;

/**
 * 
 * @author Claudiu Matei
 *
 */
public class SubdetailEntity extends AbstractEntity {

	public SubdetailEntity(int id) {
		super(id);
	}

	private int value;

	private DetailEntity detailEntity;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public DetailEntity getDetailEntity() {
		return detailEntity;
	}

	public void setDetailEntity(DetailEntity detailEntity) {
		this.detailEntity = detailEntity;
	}

	/**
	 * 
	 */
	public String toString() {
		return String.format("%s[id:%s value:%s]", getClass().getSimpleName(), getId(), value);
	}
	
}
