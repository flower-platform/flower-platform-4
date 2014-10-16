package org.flowerplatform.tests.codesync;

import static org.flowerplatform.codesync.CodeSyncConstants.FILE;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.ENTITY;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.HTML;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.INNER_TEMPLATE_TYPE;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.LABEL;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.TEMPLATE;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.TEMPLATE_COLUMN;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.TEMPLATE_FIELD_COMBO_BOX;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.TEMPLATE_FIELD_STRING;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.TEMPLATE_FORM;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.TEMPLATE_TAB;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.TEMPLATE_TABLE;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.TEMPLATE_TABSET;
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
		Node table = addNode(resource, FILE, TEMPLATE_TABLE, "employee", "Employee");
		addNode(table, INNER_TEMPLATE_TYPE, TEMPLATE_COLUMN, "name", "Name");
		addNode(table, INNER_TEMPLATE_TYPE, TEMPLATE_COLUMN, "email", "Email");
		addNode(table, INNER_TEMPLATE_TYPE, TEMPLATE_COLUMN, "phoneNumber", "Phone Number");
		
		// form
		Node form = addNode(resource, FILE, TEMPLATE_FORM, "employee", "Employee");
		Node tabset = addNode(form, INNER_TEMPLATE_TYPE, TEMPLATE_TABSET, "tabset", null);
		
		Node main = addNode(tabset, INNER_TEMPLATE_TYPE, TEMPLATE_TAB, "details", "Details");
		addNode(main, INNER_TEMPLATE_TYPE, TEMPLATE_FIELD_STRING, "name", "Name");
		addNode(main, INNER_TEMPLATE_TYPE, TEMPLATE_FIELD_STRING, "email", "Email");
		addNode(main, INNER_TEMPLATE_TYPE, TEMPLATE_FIELD_STRING, "phoneNumber", "Phone Number");
		addNode(main, INNER_TEMPLATE_TYPE, TEMPLATE_FIELD_COMBO_BOX, "job", "Job");
		
		Node jobHistory = addNode(tabset, INNER_TEMPLATE_TYPE, TEMPLATE_TAB, "jobHistory", "Job History");
		Node entries = addNode(jobHistory, INNER_TEMPLATE_TYPE, TEMPLATE_TABLE, "jobEntry", "JobEntry");
		addNode(entries, INNER_TEMPLATE_TYPE, TEMPLATE_COLUMN, "date", "Date");
		addNode(entries, INNER_TEMPLATE_TYPE, TEMPLATE_COLUMN, "type", "Type");
		addNode(entries, INNER_TEMPLATE_TYPE, TEMPLATE_COLUMN, "details", "Details");
		
		// save
		CorePlugin.getInstance().getResourceService().save(resourceUri, new ServiceContext<ResourceService>());
		
//		CodeSyncTemplatePlugin.getInstance().getCodeSyncTemplateService().generate(table.getNodeUri());
//		CodeSyncTemplatePlugin.getInstance().getCodeSyncTemplateService().generate(form.getNodeUri());
		
		codeSyncOperationsService.synchronize(form.getNodeUri(), 
				CorePlugin.getInstance().getFileAccessController().getFile(PROJECT + "/gen/employeeForm.html"), HTML, true);
	}
	
	private Node addNode(Node parent, String type, String template, String entity, String label) {
		Node node = new Node(null, type);
		ServiceContext<NodeService> context = new ServiceContext<NodeService>(CorePlugin.getInstance().getNodeService());
		context.getContext().put(TEMPLATE, template);
		context.getContext().put(ENTITY, entity);
		context.getContext().put(NAME, entity); // match key during sync
		if (label != null) {
			context.getContext().put(LABEL, label);
		}
		context.getContext().put(CodeSyncConstants.SYNC_IN_PROGRESS, true);	
		CorePlugin.getInstance().getNodeService().addChild(parent, node, context);
		return node;
	}
	
}
