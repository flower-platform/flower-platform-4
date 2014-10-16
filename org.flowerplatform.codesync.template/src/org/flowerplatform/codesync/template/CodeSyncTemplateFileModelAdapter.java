package org.flowerplatform.codesync.template;

import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.ENTITY;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.INNER_TEMPLATES;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.LABEL;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.TEMPLATE;
import static org.flowerplatform.core.CoreConstants.NAME;

import java.io.StringWriter;
import java.util.Collections;

import org.apache.velocity.app.Velocity;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.InsertEdit;
import org.eclipse.text.edits.TextEdit;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.adapter.file.AbstractFileModelAdapter;
import org.flowerplatform.core.file.IFileAccessController;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncTemplateFileModelAdapter extends AbstractFileModelAdapter {

	public CodeSyncTemplateFileModelAdapter() {
		containmentFeatures.add(INNER_TEMPLATES);
		
		valueFeatures.add(TEMPLATE);
		valueFeatures.add(ENTITY);
		valueFeatures.add(LABEL);
	}

	@Override
	public Iterable<?> getContainmentFeatureIterable(Object element, Object feature, Iterable<?> correspondingIterable, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (INNER_TEMPLATES.equals(feature)) {
			return Collections.emptyList();
		}
		return super.getContainmentFeatureIterable(element, feature, correspondingIterable, codeSyncAlgorithm);
	}

	@Override
	public Object getValueFeatureValue(Object element, Object feature, Object correspondingValue, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (NAME.equals(feature)) {
			return super.getValueFeatureValue(element, feature, correspondingValue, codeSyncAlgorithm);
		}
		if (valueFeatures.contains(feature)) {
			return (getVelocityContext(element, codeSyncAlgorithm).get((String) feature));
		}
		return super.getValueFeatureValue(element, feature, correspondingValue, codeSyncAlgorithm);
	}
	
	@Override
	public void setValueFeatureValue(Object element, Object feature, Object value, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (NAME.equals(feature)) {
			super.setValueFeatureValue(element, feature, value, codeSyncAlgorithm);
			return;
		}
		if (valueFeatures.contains(feature)) {
			getVelocityContext(element, codeSyncAlgorithm).put((String) feature, value);
			return;
		}
		super.setValueFeatureValue(element, feature, value, codeSyncAlgorithm);
	}
	
	private NestedVelocityContext getVelocityContext(Object element, CodeSyncAlgorithm codeSyncAlgorithm) {
		return (NestedVelocityContext) getOrCreateFileInfo(element, codeSyncAlgorithm.getFileAccessController());
	}
	
	@Override
	protected Object createFileInfo(Object file, IFileAccessController fileAccessController) {
		return new NestedVelocityContext();
	}

	@Override
	protected TextEdit rewrite(Document document, Object fileInfo) {
		StringWriter w = evaluate((NestedVelocityContext) fileInfo);
		return new InsertEdit(0, w.toString());
	}

	private StringWriter evaluate(NestedVelocityContext context) {
		for (NestedVelocityContext child : context.getChildren()) {
			evaluate(child);
		}
		String template = CodeSyncTemplatePlugin.getInstance().getTemplate((String) context.get(TEMPLATE));
		StringWriter writer = new StringWriter();
		Velocity.evaluate(context, writer, "", template);
		System.out.println(writer);
		if (context.getParent() != null) {
			context.getParent().put("innerTemplate", writer.toString());
		}
		return writer;
	}
	
}
