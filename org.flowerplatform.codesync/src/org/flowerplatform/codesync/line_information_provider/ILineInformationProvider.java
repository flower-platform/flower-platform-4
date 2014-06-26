package org.flowerplatform.codesync.line_information_provider;

/**
 * @author Mariana Gheorghe
 */
public interface ILineInformationProvider {

	int getStartLine(Object model);
	int getEndLine(Object model);
	
	boolean canHandle(Object model);
}
