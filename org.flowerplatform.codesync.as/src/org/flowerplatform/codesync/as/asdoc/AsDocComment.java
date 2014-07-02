package org.flowerplatform.codesync.as.asdoc;

import org.apache.flex.compiler.asdoc.IASDocComment;

/**
 * @author Mariana Gheorghe
 */
public class AsDocComment implements IASDocComment {

	private String text;

	public AsDocComment(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
	
}
