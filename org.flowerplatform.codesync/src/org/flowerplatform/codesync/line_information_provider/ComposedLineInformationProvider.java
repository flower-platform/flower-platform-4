package org.flowerplatform.codesync.line_information_provider;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Mariana Gheorghe
 */
public class ComposedLineInformationProvider implements ILineInformationProvider {

	private List<ILineInformationProvider> lineInformationProviders = new ArrayList<ILineInformationProvider>();
	
	public void addLineInformationProvider(ILineInformationProvider provider) {
		lineInformationProviders.add(provider);
	}
	
	@Override
	public int getStartLine(Object model) {
		for (ILineInformationProvider lineInformationProvider : lineInformationProviders) {
			if (lineInformationProvider.canHandle(model)) {
				return lineInformationProvider.getStartLine(model);
			}
		}
		return -1;
	}

	@Override
	public int getEndLine(Object model) {
		for (ILineInformationProvider lineInformationProvider : lineInformationProviders) {
			if (lineInformationProvider.canHandle(model)) {
				return lineInformationProvider.getEndLine(model);
			}
		}
		return -1;
	}

	@Override
	public boolean canHandle(Object model) {
		return true;
	}

}
