package org.flowerplatform.codesync.sdiff;

/**
 * 
 * @author Elena Posea
 */
public class SpecialCommentPrefix {
	public String specialPrefixText;
	public boolean isRegex;

	// specifies whether the specialPrefixText should be treated as regex or as
	// plain text
	public SpecialCommentPrefix(String specialPrefixText, boolean isRegex) {
		super();
		this.specialPrefixText = specialPrefixText;
		this.isRegex = isRegex;
	}

}
