package org.flowerplatform.codesync.template;

import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.INNER_TEMPLATE_TYPE;

import org.apache.velocity.VelocityContext;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.type_provider.FileTypeProvider;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncTemplateTypeProvider extends FileTypeProvider {

	@Override
	public String getType(Object object, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (object instanceof VelocityContext) {
			return INNER_TEMPLATE_TYPE;
		}
		return super.getType(object, codeSyncAlgorithm);
	}

}
