package org.flowerplatform.codesync.template;

import static org.apache.velocity.runtime.RuntimeConstants.VM_LIBRARY;
import static org.flowerplatform.core.CoreConstants.NAME;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;
import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.CoreUtils;
import org.flowerplatform.core.file.IFileAccessController;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncTemplateService {

	/**
	 * 
	 */
	public void generate(String nodeUri) {
		Node node = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		String repo = CoreUtils.getRepoFromNode(node);

		// merge
		VelocityEngine engine = createVelocityEngine(repo);
		VelocityContext context = createVelocityContext(node);
		String output = generate(engine, context);
		
		// write output
		String outputPath = repo + "/gen/";
		try {
			Object outputFile = CorePlugin.getInstance().getFileAccessController().getFile(outputPath + node.getPropertyValue(NAME));
			CorePlugin.getInstance().getFileAccessController().writeStringToFile(outputFile, output.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Create and initialize a {@link VelocityEngine} with the templates from the repository.
	 */
	public VelocityEngine createVelocityEngine(String repository) {
		VelocityEngine engine = new VelocityEngine();
		
		// get templates library from repository
		if (repository != null) {
			Object library = null;
			try {
				library = CorePlugin.getInstance().getFileAccessController().getFile(repository + "/tpl/");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			String path = CorePlugin.getInstance().getFileAccessController().getAbsolutePath(library);
			engine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, path);
			engine.setProperty(VM_LIBRARY, getMacros(library));
		}
		
		engine.init();
		return engine;
	}
	
	private Object getMacros(Object location) {
		IFileAccessController controller = CorePlugin.getInstance().getFileAccessController();
		String csv = "";
		Object[] macros = controller.listFiles(location);
		if (macros != null) {
			for (Object macro : macros) {
				csv += controller.getName(macro) + ","; 
			}
		}
		return csv;
	}

	/**
	 * Create a {@link VelocityContext} containing the full node hierarchy and the {@link Indenter} class.
	 */
	public VelocityContext createVelocityContext(Node node) {
		VelocityContext context = new VelocityContext();
		context.put("node", toMap(node));
		context.put("Indenter", Indenter.class);
		return context;
	}
	
	/**
	 * Convert to a map for an easy way to use in templates.
	 */
	private Map<String, Object> toMap(Node node) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.putAll(node.getOrPopulateProperties(new ServiceContext<NodeService>()));
		List<Map<String, Object>> children = new ArrayList<Map<String, Object>>();
		for (Node child : CorePlugin.getInstance().getNodeService().getChildren(node, new ServiceContext<NodeService>())) {
			children.add(toMap(child));
		}
		if (children.size() > 0) {
			map.put(CodeSyncConstants.CHILDREN, children);
		}
		return map;
	}
	
	/**
	 * Merge the base template with the context containing the node hierarchy.
	 */
	public String generate(VelocityEngine engine, VelocityContext context) {
		StringWriter writer = new StringWriter();
		engine.mergeTemplate("base.vm", "UTF-8", context, writer);
		String output = writer.toString();
		System.out.println(output);
		return output;
	}
	
}
