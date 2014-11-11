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
		String libraryPath = repo + "/tpl/";
		String outputPath = repo + "/gen/";
		
		// initialize engine
		VelocityEngine engine = new VelocityEngine();
		Object library = null;
		try {
			library = CorePlugin.getInstance().getFileAccessController().getFile(libraryPath);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		String path = CorePlugin.getInstance().getFileAccessController().getAbsolutePath(library);
				engine.setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, path);
		engine.setProperty(VM_LIBRARY, getMacros(library));
		engine.init();
		
		// create context
		StringWriter writer = new StringWriter();
		VelocityContext context = new VelocityContext();
		context.put("node", toMap(node));
		context.put("Indenter", Indenter.class);

		// generate
		engine.mergeTemplate("base.vm", "UTF-8", context, writer);
		String output = writer.toString();
		System.out.println(output);

		// write output
		try {
			Object outputFile = CorePlugin.getInstance().getFileAccessController().getFile(outputPath + node.getPropertyValue(NAME));
			CorePlugin.getInstance().getFileAccessController().writeStringToFile(outputFile, output.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
			map.put("children", children);
		}
		return map;
	}
	
}
