package org.flowerplatform.codesync.template;

import static org.apache.velocity.runtime.RuntimeConstants.VM_LIBRARY;

import java.io.File;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;
import org.apache.velocity.runtime.RuntimeSingleton;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncTemplateService {

	public CodeSyncTemplateService() {
		try {
			String path = CorePlugin.getInstance().getFileAccessController().getAbsolutePath(
					CorePlugin.getInstance().getFileAccessController().getFile("codesync/tpl"));
			RuntimeSingleton.getRuntimeServices().setProperty(RuntimeConstants.FILE_RESOURCE_LOADER_PATH, path);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		addMacros("callMacro.vm,indent.vm");
	}
	
	/**
	 * Add a macro to the local velocimacro library.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void addMacros(String macros) {
		String newVmLib = null;
		Object vmLib = RuntimeSingleton.getRuntimeServices().getProperty(VM_LIBRARY);
		if (vmLib == null) {
			// no existing local macros
			newVmLib = macros;
		} else if (vmLib instanceof String) {
			// only one existing local macro => append new macros
			newVmLib = vmLib + "," + macros;
		} else if (vmLib instanceof Vector) {
			// multiple existing local macros => add new macros to vector
			newVmLib = String.join(",", (Vector) vmLib);
			newVmLib += "," + macros;
		}
		RuntimeSingleton.getRuntimeServices().setProperty(VM_LIBRARY, newVmLib);
	}
	
	/**
	 * 
	 */
	public void generate(String nodeUri) {
		Node node = CorePlugin.getInstance().getResourceService().getNode(nodeUri);
		
		StringWriter writer = new StringWriter();
		VelocityContext context = new VelocityContext();
		context.put("node", toMap(node));
		
		context.put("LineSplitter", LineSplitter.class);

		Velocity.mergeTemplate("base.vm", "UTF-8", context, writer);
		String output = writer.toString();
		System.out.println(output);
		
		try {
			FileUtils.write((File) CorePlugin.getInstance().getFileAccessController()
					.getFile("codesync/gen/employeeForm.html"), output.toString());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
