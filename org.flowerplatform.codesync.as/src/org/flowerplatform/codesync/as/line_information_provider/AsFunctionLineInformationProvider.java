package org.flowerplatform.codesync.as.line_information_provider;

import org.apache.flex.compiler.definitions.IFunctionDefinition;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.flowerplatform.codesync.line_information_provider.ILineInformationProvider;

/**
 * @author Mariana Gheorghe
 */
public class AsFunctionLineInformationProvider implements ILineInformationProvider {

	@Override
	public int getStartLine(Object model, IDocument document) {
		try {
			return document.getLineOfOffset(getFunction(model).getStart());
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public int getEndLine(Object model, IDocument document) {
		try {
			return document.getLineOfOffset(getFunction(model).getEnd());
		} catch (BadLocationException e) {
			throw new RuntimeException(e);
		}
	}
	
	private IFunctionDefinition getFunction(Object model) {
		return (IFunctionDefinition) model;
	}

	@Override
	public boolean canHandle(Object model) {
		return model instanceof IFunctionDefinition;
	}

}
