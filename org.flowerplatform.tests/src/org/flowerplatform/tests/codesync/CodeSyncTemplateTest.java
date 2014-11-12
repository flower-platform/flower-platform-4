package org.flowerplatform.tests.codesync;

import static org.flowerplatform.codesync.CodeSyncConstants.FILE;
import static org.flowerplatform.codesync.CodeSyncConstants.FOLDER;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.ENTITY;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.INNER_TEMPLATE;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.LABEL;
import static org.flowerplatform.codesync.template.CodeSyncTemplateConstants.TEMPLATE;
import static org.flowerplatform.core.CoreConstants.FILE_NODE_TYPE;
import static org.flowerplatform.core.CoreConstants.FILE_SCHEME;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.mindmap.MindMapConstants.FREEPLANE_PERSISTENCE_RESOURCE_KEY;

import java.util.HashMap;
import java.util.Map;

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

	public static final String TEMPLATE_DIV = "div";
	
	public static final String TEMPLATE_A = "a";
	
	public static final String TEMPLATE_BUTTON = "button";
		public static final String TEMPLATE_BUTTON_TYPE = "btn-type";
		public static final String TEMPLATE_BUTTON_CLASS = "btn-cls";
		public static final String TEMPLATE_BUTTON_CLICK = "btn-click";

	public static final String TEMPLATE_FORM = "form";
	public static final String TEMPLATE_FORM_ROW = "formRow";
	
	public static final String TEMPLATE_FIELD_STRING = "stringField";
	public static final String TEMPLATE_FIELD_COMBO_BOX = "comboBoxField";
	
	public static final String TEMPLATE_TABSET = "tabset";
	public static final String TEMPLATE_TAB = "tab";
	
	public static final String TEMPLATE_TABLE = "table";
	public static final String TEMPLATE_COLUMN = "column";
	
	public static final String TEMPLATE_ROLE = "role";
	
	public static final String TEMPLATE_GLYPHICON = "glyphicon";
	public static final String TEMPLATE_ATTRIBUTES = "attrs";
	
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
		String ssp = PROJECT + "|" + fileName;
		
		// create file
		ServiceContext<NodeService> context = new ServiceContext<NodeService>();
		context.getContext().put(NAME, fileName);
		Node parent = CorePlugin.getInstance().getResourceService().getNode(Utils.getUri(FILE_SCHEME, PROJECT));
		Node file = new Node(Utils.getUri(FILE_SCHEME, ssp), FILE_NODE_TYPE);
		CorePlugin.getInstance().getNodeService().addChild(parent, file, context);
		
		// subscribe
		String resourceUri = Utils.getUri(FREEPLANE_PERSISTENCE_RESOURCE_KEY, ssp);
		CorePlugin.getInstance().getResourceService().subscribeToParentResource("dummySessionId", resourceUri, new ServiceContext<ResourceService>());
		
		// add nodes
		
		Node resource = CorePlugin.getInstance().getResourceService().getNode(resourceUri);
		
		Node srcDir = addNode(resource, FOLDER, "gen", "gen", "gen", "gen");
		
		Node employee = addNode(srcDir, FOLDER, "employee", "employee", "employee", "employee");
		
		// table: employees
		Node table = addNode(employee, FILE, TEMPLATE_TABLE, "employeeList.html", "employee", "Employee");
		setProperty(addNode(table, INNER_TEMPLATE, TEMPLATE_COLUMN, "name", "name", "Name"), "parent", "employee");
		setProperty(addNode(table, INNER_TEMPLATE, TEMPLATE_COLUMN, "email", "email", "Email"), "parent", "employee");
		setProperty(addNode(table, INNER_TEMPLATE, TEMPLATE_COLUMN, "phoneNumber", "phoneNumber", "Phone Number"), "parent", "employee");
		Node actions = setProperty(addNode(table, INNER_TEMPLATE, TEMPLATE_COLUMN, "actions", "actions", "Actions"), "parent", "employee");
		
		Node editBtn = addNode(actions, INNER_TEMPLATE, TEMPLATE_A, "edit", "edit", "");
		Map<String, String> editBtnAttrs = new HashMap<String, String>();
		editBtnAttrs.put("role", "button");
		editBtnAttrs.put("class", "btn btn-primary btn-xs");
		editBtnAttrs.put("ng-href", "#employee/{{employee.id}}");
		setProperty(editBtn, TEMPLATE_ATTRIBUTES, editBtnAttrs);
		setProperty(editBtn, TEMPLATE_GLYPHICON, "edit");
		
		Node removeBtn = addNode(actions, INNER_TEMPLATE, TEMPLATE_BUTTON, "remove", "remove", "");
		setProperty(removeBtn, TEMPLATE_BUTTON_TYPE, "button");
		setProperty(removeBtn, TEMPLATE_BUTTON_CLASS, "btn btn-danger btn-xs");
		setProperty(removeBtn, TEMPLATE_BUTTON_CLICK, "remove(employee)");
		setProperty(removeBtn, TEMPLATE_GLYPHICON, "remove");
		
		Node job = addNode(employee, FOLDER, "job", "job", "job", "job");
		
		// table: job entries
		Node entries = addNode(job, FILE, TEMPLATE_TABLE, "jobEntryList.html", "jobEntry", "JobEntry");
		setProperty(entries, "actions", "edit,remove");
		setProperty(setProperty(addNode(entries, INNER_TEMPLATE, TEMPLATE_COLUMN, "date", "date", "Date"), "parent", "jobEntry"), "filter", "date: 'medium'");
		setProperty(addNode(entries, INNER_TEMPLATE, TEMPLATE_COLUMN, "type", "type", "Type"), "parent", "jobEntry");
		setProperty(addNode(entries, INNER_TEMPLATE, TEMPLATE_COLUMN, "details", "details", "Details"), "parent", "jobEntry");
		
		// form
		Node form = addNode(employee, FILE, TEMPLATE_FORM, "employeeForm.html", "employee", "Employee");
		Node tabset = addNode(form, INNER_TEMPLATE, TEMPLATE_TABSET, "tabset", "tabset", "tabset");
		
		Node main = addNode(tabset, INNER_TEMPLATE, TEMPLATE_TAB, "details", "details", "Details");
		setProperty(addNode(main, INNER_TEMPLATE, TEMPLATE_FIELD_STRING, "name", "name", "Name"), "parent", "employee");
		setProperty(addNode(main, INNER_TEMPLATE, TEMPLATE_FIELD_STRING, "email", "email", "Email"), "parent", "employee");
		setProperty(addNode(main, INNER_TEMPLATE, TEMPLATE_FIELD_STRING, "phoneNumber", "phoneNumber", "Phone Number"), "parent", "employee");
		
		Node jobCB = addNode(main, INNER_TEMPLATE, TEMPLATE_FIELD_COMBO_BOX, "job", "job", "Job");
		setProperty(jobCB, "parent", "employee");
		setProperty(jobCB, "id", "id");
		setProperty(jobCB, "option", "job");
		setProperty(jobCB, "options", "jobs");
		setProperty(jobCB, "comboBoxLabelFunction", "jobLabel");
		
		Node saveBtn = addNode(main, INNER_TEMPLATE, TEMPLATE_BUTTON, "saveBtn", "employee", "Save");
		setProperty(saveBtn, TEMPLATE_BUTTON_TYPE, "submit");
		setProperty(saveBtn, TEMPLATE_BUTTON_CLICK, "save(employee)");
		setProperty(saveBtn, TEMPLATE_GLYPHICON, "ok");
		
		Node backBtn = addNode(main, INNER_TEMPLATE, TEMPLATE_A, "backBtn", "employee", "Back");
		Map<String, String> backBtnAttrs = new HashMap<String, String>();
		backBtnAttrs.put("role", "button");
		backBtnAttrs.put("class", "btn btn-default");
		backBtnAttrs.put("ng-href", "#employee");
		setProperty(backBtn, TEMPLATE_ATTRIBUTES, backBtnAttrs);
		
		Node jobHistory = addNode(tabset, INNER_TEMPLATE, TEMPLATE_TAB, "jobHistory", "jobHistory", "Job History");
		Map<String, String> includeAttrs = new HashMap<String, String>();
		includeAttrs.put("ng-include", "'js/employee/job/jobEntryList.html'");
		setProperty(addNode(jobHistory, INNER_TEMPLATE, TEMPLATE_DIV, "", "", ""), TEMPLATE_ATTRIBUTES, includeAttrs);
		
		// save
		CorePlugin.getInstance().getResourceService().save(resourceUri, new ServiceContext<ResourceService>());
		
		CodeSyncTemplatePlugin.getInstance().getCodeSyncTemplateService().generate(table.getNodeUri());
		CodeSyncTemplatePlugin.getInstance().getCodeSyncTemplateService().generate(form.getNodeUri());
		CodeSyncTemplatePlugin.getInstance().getCodeSyncTemplateService().generate(entries.getNodeUri());
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
	
	private Node setProperty(Node node, String key, Object val) {
		CorePlugin.getInstance().getNodeService().setProperty(node, key, val, new ServiceContext<NodeService>());
		return node;
	}
	
}
