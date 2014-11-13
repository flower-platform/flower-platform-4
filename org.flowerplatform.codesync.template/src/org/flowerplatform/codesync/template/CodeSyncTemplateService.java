package org.flowerplatform.codesync.template;

import static org.flowerplatform.core.CoreConstants.NAME;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
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
		VelocityEngine engine = new VelocityEngine();
		addTemplatesDirectory(engine, repo);
		engine.init();
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
	 * Add the templates to this engine.
	 */
	public void addTemplatesDirectory(VelocityEngine engine, String templatesDir) {
		if (templatesDir != null) {
			Object library = null;
			try {
				library = CorePlugin.getInstance().getFileAccessController().getFile(templatesDir);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			
			// set path
			String path = CorePlugin.getInstance().getFileAccessController().getAbsolutePath(library);
			engine.setProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH, 
					addToCsv(engine.getProperty(VelocityEngine.FILE_RESOURCE_LOADER_PATH), path));
			
			// add templates
			String macros = getMacros(library);
			Object vmLib = engine.getProperty(VelocityEngine.VM_LIBRARY);
			engine.setProperty(VelocityEngine.VM_LIBRARY, addToCsv(vmLib, macros));
		}
	}
	
	private String getMacros(Object location) {
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private String addToCsv(Object existing, String added) {
		if (existing == null) {
			// empty
			return added;
		}
		if (existing instanceof String) {
			// just one => append
			return existing + "," + added;
		}
		return String.join(",", (Vector) existing) + "," + added;
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
