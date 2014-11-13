package org.flowerplatform.codesync.template.adapter;

import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.TEMPLATES_DIRS;
import static org.flowerplatform.core.CoreConstants.NAME;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.ReplaceEdit;
import org.eclipse.text.edits.TextEdit;
import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.CodeSyncPlugin;
import org.flowerplatform.codesync.adapter.IModelAdapter;
import org.flowerplatform.codesync.adapter.IModelAdapterSet;
import org.flowerplatform.codesync.adapter.file.AbstractFileModelAdapter;
import org.flowerplatform.codesync.adapter.file.CodeSyncFile;
import org.flowerplatform.codesync.template.CodeSyncTemplatePlugin;
import org.flowerplatform.codesync.template.CodeSyncTemplateService;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;

/**
 * Mapped to files generated with Velocity. It does not declare any containment features;
 * instead, the corresponding model tree is added to a {@link VelocityContext} when the file
 * is created.
 * 
 * @author Mariana Gheorghe
 */
public class GeneratedFileModelAdapter extends AbstractFileModelAdapter {

	private static final String VELOCITY_ENGINE = "velocityEngine";
	
	@Override
	public Object createChildOnContainmentFeature(Object parent, Object feature, Object correspondingChild,
			IModelAdapterSet modelAdapterSet, CodeSyncAlgorithm codeSyncAlgorithm) {
		if (CodeSyncConstants.CHILDREN.equals(feature)) {
			Object parentFolder = ((CodeSyncFile) parent).getFile();
			IModelAdapter correspondingModelAdapter = modelAdapterSet.getModelAdapter(correspondingChild, codeSyncAlgorithm);
			String name = (String) correspondingModelAdapter.getValueFeatureValue(correspondingChild, NAME, null, codeSyncAlgorithm);
			Object file = codeSyncAlgorithm.getFileAccessController().getFile(parentFolder, name);
			CodeSyncFile csf = new CodeSyncFile(file);
			Node node = (Node) correspondingChild;
			CodeSyncTemplateService service = CodeSyncTemplatePlugin.getInstance().getCodeSyncTemplateService();
			VelocityContext context = service.createVelocityContext(node); // TODO reference to Node?
			csf.setFileInfo(context);
			
			VelocityEngine engine = (VelocityEngine) codeSyncAlgorithm.getContext().get(VELOCITY_ENGINE);
			if (engine == null) {
				engine = new VelocityEngine();
				Node resourceNode = CorePlugin.getInstance().getResourceService().getResourceNode(node.getNodeUri());
				String templatesDirs = (String) resourceNode.getPropertyValue(TEMPLATES_DIRS);
				for (String templatesDir : templatesDirs.split(",")) {
					String path = CodeSyncPlugin.getInstance().getCodeSyncOperationsService().getPath(resourceNode, templatesDir);
					service.addTemplatesDirectory(engine, path);
				}
				engine.init();
				codeSyncAlgorithm.getContext().put(VELOCITY_ENGINE, engine);
			}
			
			return csf;
		}
		return super.createChildOnContainmentFeature(parent, feature, correspondingChild, modelAdapterSet, codeSyncAlgorithm);
	}
	
	@Override
	protected Object createFileInfo(Object file, CodeSyncAlgorithm codeSyncAlgorithm) {
		return null;
	}

	@Override
	protected TextEdit rewrite(Document document, Object fileInfo, CodeSyncAlgorithm codeSyncAlgorithm) {
		VelocityEngine engine = (VelocityEngine) codeSyncAlgorithm.getContext().get(VELOCITY_ENGINE);
		VelocityContext context = (VelocityContext) fileInfo;
		String output = CodeSyncTemplatePlugin.getInstance().getCodeSyncTemplateService()
				.generate(engine, context);
		return new ReplaceEdit(0, document.getLength(), output);
	}

}
