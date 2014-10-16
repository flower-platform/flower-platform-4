package org.flowerplatform.codesync.template;

import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.TEMPLATE;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncTemplateService {

	private Map<String, String> templates = new HashMap<String, String>();
	
	/**
	 * 
	 */
	public void generate(String nodeUri) {
		Node node = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		
		Velocity.init();
		
		VelocityContext context = new VelocityContext();
		context.put("node", toMap(node));
		
		StringWriter w = new StringWriter();
		Velocity.evaluate(context, w, "", getTemplate((String) node.getPropertyValue(TEMPLATE)));
		
		System.out.println(w);
		
	}
	
	private String getTemplate(String name) {
		String template = templates.get(name);
		if (template == null) {
			try {
				Object file = CorePlugin.getInstance().getFileAccessController().getFile("codesync/tpl/" + name + ".vm");
				template = CorePlugin.getInstance().getFileAccessController().readFileToString(file);
			} catch (Exception e) {
				throw new RuntimeException("Error loading template " + name + " from file", e);
			}
		}
		return template;
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
