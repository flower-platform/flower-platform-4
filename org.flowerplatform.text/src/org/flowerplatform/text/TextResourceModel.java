package org.flowerplatform.text;

/**
 * @author Cristina Constantinescu
 */
public class TextResourceModel {

	private Object resource;
	
	private StringBuffer resourceContent;
	
	private boolean dirty;
	
	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public StringBuffer getResourceContent() {
		return resourceContent;
	}

	public void setResourceContent(StringBuffer resourceContent) {
		this.resourceContent = resourceContent;
	}

	public Object getResource() {
		return resource;
	}

	public void setResource(Object resource) {
		this.resource = resource;
	}	
	
}
