package org.flowerplatform.codesync.template.adapter;

import static org.flowerplatform.codesync.CodeSyncConstants.CODE_SYNC_CONFIG_NOTYPE;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.CODE_SYNC_CONFIG_VELOCITY_ENGINE;
import static org.flowerplatform.core.CoreConstants.NAME;

import org.apache.velocity.VelocityContext;
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
import org.flowerplatform.codesync.template.CodeSyncTemplateConstants;
import org.flowerplatform.codesync.template.CodeSyncTemplatePlugin;
import org.flowerplatform.codesync.template.CodeSyncTemplateService;
import org.flowerplatform.codesync.template.config_loader.TemplatesEngineController;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.util.controller.TypeDescriptor;
import org.flowerplatform.util.controller.TypeDescriptorRegistry;

/**
 * Mapped to files generated with Velocity. It does not declare any containment features;
 * instead, the corresponding model tree is added to a {@link VelocityContext} when the file
 * is created.
 * 
 * @author Mariana Gheorghe
 */
public class GeneratedFileModelAdapter extends AbstractFileModelAdapter {

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
			VelocityContext context = service.createVelocityContext(node);
			csf.setFileInfo(context);
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
		VelocityContext context = (VelocityContext) fileInfo;
		Node node = (Node) context.get("node");
		TemplatesEngineController engine = getEngine(node);
		String output = engine.merge(context);
		return new ReplaceEdit(0, document.getLength(), output);
	}

	private TemplatesEngineController getEngine(Node node) {
		Node resourceNode = CorePlugin.getInstance().getResourceService().getResourceNode(node.getNodeUri());
		String path = CodeSyncPlugin.getInstance().getCodeSyncOperationsService().getPath(resourceNode, null);
		String codeSyncConfigDirs = (String) resourceNode.getPropertyValue(CodeSyncTemplateConstants.TEMPLATES_DIRS);
		TypeDescriptorRegistry config = CodeSyncPlugin.getInstance().getCodeSyncOperationsService().getOrLoadCodeSyncConfig(codeSyncConfigDirs, path);
		TypeDescriptor descriptor = config.getExpectedTypeDescriptor(CODE_SYNC_CONFIG_NOTYPE);
		return descriptor.getSingleController(CODE_SYNC_CONFIG_VELOCITY_ENGINE, null);
	}
}
