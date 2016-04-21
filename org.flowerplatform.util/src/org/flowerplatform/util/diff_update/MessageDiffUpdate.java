package org.flowerplatform.util.diff_update;

/**
 * 
 * @author Florin Buzatu
 */
public class MessageDiffUpdate extends DiffUpdate {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private Object data;

	public MessageDiffUpdate() {}
	
	public MessageDiffUpdate(Object data) {
		setType("serverMessage");
		this.data = data;
	}

	public Object getData() {
		return data;
	}


	public void setData(Object data) {
		this.data = data;
	}
}
