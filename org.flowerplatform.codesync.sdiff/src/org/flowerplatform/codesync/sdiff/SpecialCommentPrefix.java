package org.flowerplatform.codesync.sdiff;

/**
 * 
 * @author Elena Posea
 */
public class SpecialCommentPrefix {
	public String specialPrefixText;
	public boolean isRegex;

	/**
	 * @param specialPrefixText the special text that you want to look for in your source code
	 * @param isRegex specifies whether the specialPrefixText should be treated as regex or as plain text  
	 */
	public SpecialCommentPrefix(String specialPrefixText, boolean isRegex) {
		super();
		this.specialPrefixText = specialPrefixText;
		this.isRegex = isRegex;
	}

}
