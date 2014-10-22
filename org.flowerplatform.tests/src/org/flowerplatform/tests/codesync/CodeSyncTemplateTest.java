package org.flowerplatform.tests.codesync;

import static org.flowerplatform.codesync.CodeSyncConstants.FILE;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.ENTITY;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.INNER_TEMPLATE;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.LABEL;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.TEMPLATE;
import static org.flowerplatform.codesync.template.WebAppModule.TEMPLATE_COLUMN;
import static org.flowerplatform.codesync.template.WebAppModule.TEMPLATE_FIELD_COMBO_BOX;
import static org.flowerplatform.codesync.template.WebAppModule.TEMPLATE_FIELD_STRING;
import static org.flowerplatform.codesync.template.WebAppModule.TEMPLATE_FORM;
import static org.flowerplatform.codesync.template.WebAppModule.TEMPLATE_TAB;
import static org.flowerplatform.codesync.template.WebAppModule.TEMPLATE_TABLE;
import static org.flowerplatform.codesync.template.WebAppModule.TEMPLATE_TABSET;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.FILE_SCHEME;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_PERSISTENCE_RESOURCE_KEY;

import org.flowerplatform.codesync.CodeSyncConstants;
import org.flowerplatform.codesync.template.CodeSyncTemplatePlugin;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.ResourceService;
import org.flowerplatform.util.Utils;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncTemplateTest extends CodeSyncEclipseIndependentTestBase {

	/**
	 * 
	 */
	@BeforeClass
	public static void beforeClass() {
		startPlugin(new CodeSyncTemplatePlugin());
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	@Test
	public void test() throws Exception {
		String fileName = "employees.codesyncTemplate";
		String ssp = "|" + PROJECT + "/" + fileName;
		
		// create file
		ServiceContext<NodeService> context = new ServiceContext<NodeService>();
		context.getContext().put(NAME, fileName);
		Node parent = CorePlugin.getInstance().getResourceService().getNode(Utils.getUri(FILE_SCHEME, "|" + PROJECT));
		Node file = new Node(Utils.getUri(FILE_SCHEME, ssp), FILE_NODE_TYPE);
		CorePlugin.getInstance().getNodeService().addChild(parent, file, context);
		
		// subscribe
		String resourceUri = Utils.getUri(FREEPLANE_PERSISTENCE_RESOURCE_KEY, ssp);
		CorePlugin.getInstance().getResourceService().subscribeToParentResource("dummySessionId", resourceUri, new ServiceContext<ResourceService>());
		
		// add nodes
		
		Node resource = CorePlugin.getInstance().getResourceService().getNode(resourceUri);
		
		// table
		Node table = addNode(resource, FILE, TEMPLATE_TABLE, "employeeTable.html", "employee", "Employee");
		addNode(table, INNER_TEMPLATE, TEMPLATE_COLUMN, "name", "name", "Name");
		addNode(table, INNER_TEMPLATE, TEMPLATE_COLUMN, "email", "email", "Email");
		addNode(table, INNER_TEMPLATE, TEMPLATE_COLUMN, "phoneNumber", "phoneNumber", "Phone Number");
		
		// form
		Node form = addNode(resource, FILE, TEMPLATE_FORM, "employeeForm.html", "employee", "Employee");
		Node tabset = addNode(form, INNER_TEMPLATE, TEMPLATE_TABSET, "tabset", "tabset", "tabset");
		
		Node main = addNode(tabset, INNER_TEMPLATE, TEMPLATE_TAB, "details", "details", "Details");
		CorePlugin.getInstance().getNodeService().setProperty(addNode(main, INNER_TEMPLATE, TEMPLATE_FIELD_STRING, "name", "name", "Name"), 
				"parent", "employee", new ServiceContext<NodeService>());
		CorePlugin.getInstance().getNodeService().setProperty(addNode(main, INNER_TEMPLATE, TEMPLATE_FIELD_STRING, "email", "email", "Email"),
				"parent", "employee", new ServiceContext<NodeService>());
		CorePlugin.getInstance().getNodeService().setProperty(addNode(main, INNER_TEMPLATE, TEMPLATE_FIELD_STRING, "phoneNumber", "phoneNumber", "Phone Number"), 
				"parent", "employee", new ServiceContext<NodeService>());
		CorePlugin.getInstance().getNodeService().setProperty(addNode(main, INNER_TEMPLATE, TEMPLATE_FIELD_COMBO_BOX, "job", "job", "Job"), 
				"parent", "employee", new ServiceContext<NodeService>());
		
		Node jobHistory = addNode(tabset, INNER_TEMPLATE, TEMPLATE_TAB, "jobHistory", "jobHistory", "Job History");
		Node entries = addNode(jobHistory, INNER_TEMPLATE, TEMPLATE_TABLE, "jobEntry", "jobEntry", "JobEntry");
		CorePlugin.getInstance().getNodeService().setProperty(addNode(entries, INNER_TEMPLATE, TEMPLATE_COLUMN, "date", "date", "Date"),
				"parent", "employee", new ServiceContext<NodeService>());
		CorePlugin.getInstance().getNodeService().setProperty(addNode(entries, INNER_TEMPLATE, TEMPLATE_COLUMN, "type", "type", "Type"),
				"parent", "employee", new ServiceContext<NodeService>());
		CorePlugin.getInstance().getNodeService().setProperty(addNode(entries, INNER_TEMPLATE, TEMPLATE_COLUMN, "details", "details", "Details"),
				"parent", "employee", new ServiceContext<NodeService>());
		
		// save
		CorePlugin.getInstance().getResourceService().save(resourceUri, new ServiceContext<ResourceService>());
		
//		CodeSyncTemplatePlugin.getInstance().getCodeSyncTemplateService().generate(table.getNodeUri());
		CodeSyncTemplatePlugin.getInstance().getCodeSyncTemplateService().generate(form.getNodeUri());
	}
	
	private Node addNode(Node parent, String type, String template, String name, String entity, String label) {
		Node node = new Node(null, type);
		ServiceContext<NodeService> context = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
		context.getContext().put(TEMPLATE, template);
		context.getContext().put(NAME, name);
		context.getContext().put(ENTITY, entity);
		context.getContext().put(LABEL, label);
		context.getContext().put(CodeSyncConstants.SYNC_IN_PROGRESS, true);	
		CorePlugin.getInstance().getNodeService().addChild(parent, node, context);
		return node;
	}
	
}
