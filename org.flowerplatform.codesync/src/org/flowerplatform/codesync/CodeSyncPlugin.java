/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico, <http://www.crispico.com/>.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation version 3.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details, at <http://www.gnu.org/licenses/>.
 * 
 * Contributors:
 *   Crispico - Initial API and implementation
 *
 * license-end
 */
package org.flowerplatform.codesync;

import static org.flowerplatform.codesync.CodeSyncConstants.CATEGORY_MODEL;
import static org.flowerplatform.codesync.CodeSyncConstants.CODESYNC_ROOT_TYPE;
import static org.flowerplatform.codesync.CodeSyncConstants.CODESYNC_TYPE;
import static org.flowerplatform.codesync.CodeSyncConstants.DIAGRAM_TYPE;
import static org.flowerplatform.codesync.CodeSyncConstants.MDA_ROOT_TYPE;
import static org.flowerplatform.codesync.CodeSyncConstants.MDA_TYPE;
import static org.flowerplatform.codesync.CodeSyncConstants.MODEL_ADAPTER_ANCESTOR;
import static org.flowerplatform.codesync.CodeSyncConstants.MODEL_ADAPTER_LEFT;
import static org.flowerplatform.core.CoreConstants.ADD_NODE_CONTROLLER;
import static org.flowerplatform.core.CoreConstants.CHILDREN_PROVIDER;
import static org.flowerplatform.core.CoreConstants.NAME;
import static org.flowerplatform.core.CoreConstants.POPULATE_WITH_PROPERTIES;
import static org.flowerplatform.core.CoreConstants.PROPERTIES_PROVIDER;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR;
import static org.flowerplatform.core.CoreConstants.PROPERTY_DESCRIPTOR_TYPE_BOOLEAN;
import static org.flowerplatform.core.CoreConstants.PROPERTY_SETTER;
import static org.flowerplatform.core.CoreConstants.REMOVE_NODE_CONTROLLER;
import static org.flowerplatform.core.CoreConstants.REPOSITORY_TYPE;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.codesync.adapter.NodeModelAdapterAncestor;
import org.flowerplatform.codesync.adapter.NodeModelAdapterLeft;
import org.flowerplatform.codesync.controller.CodeSyncAddNodeController;
import org.flowerplatform.codesync.controller.CodeSyncPropertySetter;
import org.flowerplatform.codesync.controller.CodeSyncRepositoryChildrenProvider;
import org.flowerplatform.codesync.controller.CodeSyncSubscribableResourceProvider;
import org.flowerplatform.codesync.controller.ModelResourceSetProvider;
import org.flowerplatform.codesync.project.IProjectAccessController;
import org.flowerplatform.codesync.project.ProjectAccessController;
import org.flowerplatform.codesync.remote.CodeSyncOperationsService;
import org.flowerplatform.codesync.type_provider.ITypeProvider;
import org.flowerplatform.codesync.type_provider.NodeTypeProvider;
import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.CorePlugin;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.ConstantValuePropertyProvider;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.PropertyDescriptor;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.core.node.resource.BaseResourceHandler;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Mariana Gheorghe
 * @author Cristian Spiescu
 */
public class CodeSyncPlugin extends AbstractFlowerJavaPlugin {
	
	protected static CodeSyncPlugin INSTANCE;
	
	protected List<String> srcDirs = null;
	
	private final static Logger logger = LoggerFactory.getLogger(CodeSyncPlugin.class);

	protected ComposedFullyQualifiedNameProvider fullyQualifiedNameProvider;
	
	protected Map<String, ITypeProvider> typeProviders = new HashMap<String, ITypeProvider>();

	protected Map<String, List<String>> dataProvidersForDropDownListProperties = new HashMap<String, List<String>>();

//	protected List<DependentFeature> dependentFeatures;
//	
//	protected List<CodeSyncElementDescriptor> codeSyncElementDescriptors;
//	
//	protected List<RelationDescriptor> relationDescriptors;
//
//	protected List<FeatureAccessExtension> featureAccessExtensions;
//	
//	protected List<AddNewExtension> addNewExtensions;
//	
//	protected List<AddNewRelationExtension> addNewRelationExtensions;
//	
//	/**
//	 * @author Cristina Constantinescu
//	 */
//	protected List<InplaceEditorExtension> inplaceEditorExtensions;
//	
//	protected CodeSyncTypeCriterionDispatcherProcessor codeSyncTypeCriterionDispatcherProcessor;
//	
//	/**
//	 * Runnables that create and add descriptors.
//	 * 
//	 * @author Mircea Negreanu
//	 */
//	protected List<Runnable> runnablesThatLoadDescriptors;
	
	/**
	 * @see #getProjectAccessController()
	 */
	private IProjectAccessController projectAccessController;
	
	protected boolean useUIDs = true;

	public static CodeSyncPlugin getInstance() {
		return INSTANCE;
	}
	
//	public CodeSyncTypeCriterionDispatcherProcessor getCodeSyncTypeCriterionDispatcherProcessor() {
//		return codeSyncTypeCriterionDispatcherProcessor;
//	}

	public ComposedFullyQualifiedNameProvider getFullyQualifiedNameProvider() {
		return fullyQualifiedNameProvider;
	}
	
	public void addTypeProvider(String technology, ITypeProvider typeProvider) {
		typeProviders.put(technology, typeProvider);
	}
	
	public ITypeProvider getTypeProvider(String technology) {
		return typeProviders.get(technology);
	}
		
	/**
	 * Platform-dependent.
	 * 
	 * @author Mariana Gheorghe
	 */
	public IProjectAccessController getProjectAccessController() {
		return projectAccessController;
	}

	public void setProjectAccessController(IProjectAccessController projectAccessController) {
		this.projectAccessController = projectAccessController;
	}
	
//	/**
//	 * A list of feature to be deleted in case an object is deleted 
//	 * (e.g. an edge that starts or ends in a deleted view).
//	 */
//	public List<DependentFeature> getDependentFeatures() {
//		return dependentFeatures;
//	}
//	
//	public List<CodeSyncElementDescriptor> getCodeSyncElementDescriptors() {
//		return codeSyncElementDescriptors;
//	}
//	
//	public List<RelationDescriptor> getRelationDescriptors() {
//		return relationDescriptors;
//	}
//
//	public CodeSyncElementDescriptor getCodeSyncElementDescriptor(String codeSyncType) {
//		// TODO CS/JS we should have a mapping; maybe send it to flex as a map; for quick access; idem for relations
//		for (CodeSyncElementDescriptor descriptor : getCodeSyncElementDescriptors()) {
//			if (descriptor.getCodeSyncType().equals(codeSyncType)) {
//				return descriptor;
//			}
//		}
//		return null;
//	}
//	
//	// TODO CS/JS we should unify the descriptors. And have them in Model?
//	public RelationDescriptor getRelationDescriptor(String type) {
//		for (RelationDescriptor descriptor : getRelationDescriptors()) {
//			if (descriptor.getType().equals(type)) {
//				return descriptor;
//			}
//		}
//		return null;
//	}
//	
//	public List<FeatureAccessExtension> getFeatureAccessExtensions() {
//		return featureAccessExtensions;
//	}
//	
//	public List<AddNewExtension> getAddNewExtensions() {
//		return addNewExtensions;
//	}
//		
//	public List<AddNewRelationExtension> getAddNewRelationExtensions() {
//		return addNewRelationExtensions;
//	}
//
//	/**
//	 * @author Cristina Constantinescu
//	 */
//	public List<InplaceEditorExtension> getInplaceEditorExtensions() {
//		return inplaceEditorExtensions;
//	}

	public boolean useUIDs() {
		return useUIDs;
	}
	
	/**
	 * @author Mariana Gheorge
	 * @author Mircea Negreanu
	 * @author Cristina Constantinescu
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		INSTANCE = this;
		
		CorePlugin.getInstance().getServiceRegistry().registerService("codeSyncOperationsService", new CodeSyncOperationsService());
		
		addTypeProvider("node", new NodeTypeProvider());
		
		CorePlugin.getInstance().getResourceService().addResourceHandler(CODESYNC_TYPE, new BaseResourceHandler(CODESYNC_TYPE));
		CorePlugin.getInstance().getResourceService().addResourceHandler(MDA_TYPE, new BaseResourceHandler(MDA_TYPE));
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(REPOSITORY_TYPE)
			.addAdditiveController(CHILDREN_PROVIDER, new CodeSyncRepositoryChildrenProvider());
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CATEGORY_MODEL)
			.addAdditiveController(PROPERTIES_PROVIDER, new ModelResourceSetProvider());
		
		CodeSyncSubscribableResourceProvider modelSubscribableResourceProvider = new CodeSyncSubscribableResourceProvider(".model");
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CODESYNC_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, modelSubscribableResourceProvider)
			.addAdditiveController(CHILDREN_PROVIDER, modelSubscribableResourceProvider)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, "Code Sync"))
			.addCategory(CATEGORY_MODEL);
	
		CodeSyncSubscribableResourceProvider mdaSubscribableResourceProvider = new CodeSyncSubscribableResourceProvider(".mda");
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(MDA_TYPE)
			.addAdditiveController(PROPERTIES_PROVIDER, mdaSubscribableResourceProvider)
			.addAdditiveController(CHILDREN_PROVIDER, mdaSubscribableResourceProvider)
			.addAdditiveController(PROPERTIES_PROVIDER, new ConstantValuePropertyProvider(CoreConstants.NAME, "MDA"))
			.addCategory(CATEGORY_MODEL);
	
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(CODESYNC_ROOT_TYPE)
			.addCategory(CATEGORY_MODEL);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(MDA_ROOT_TYPE)
			.addCategory(CATEGORY_MODEL);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateTypeDescriptor(DIAGRAM_TYPE)
			.addCategory(CATEGORY_MODEL);
		
		CorePlugin.getInstance().getNodeTypeDescriptorRegistry().getOrCreateCategoryTypeDescriptor(CodeSyncConstants.CATEGORY_CODESYNC)
			.addAdditiveController(ADD_NODE_CONTROLLER, new CodeSyncAddNodeController())
			.addAdditiveController(REMOVE_NODE_CONTROLLER, new CodeSyncRemoveNodeController())
			.addAdditiveController(PROPERTY_SETTER, new CodeSyncPropertySetter())
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(CoreConstants.NAME))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(CodeSyncConstants.ADDED).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_BOOLEAN).setReadOnlyAs(true))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(CodeSyncConstants.REMOVED).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_BOOLEAN).setReadOnlyAs(true))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(CodeSyncConstants.SYNC).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_BOOLEAN).setReadOnlyAs(true))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(CodeSyncConstants.CHILDREN_SYNC).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_BOOLEAN).setReadOnlyAs(true))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(CodeSyncConstants.CONFLICT).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_BOOLEAN).setReadOnlyAs(true))
			.addAdditiveController(PROPERTY_DESCRIPTOR, new PropertyDescriptor().setNameAs(CodeSyncConstants.CHILDREN_CONFLICT).setTypeAs(PROPERTY_DESCRIPTOR_TYPE_BOOLEAN).setReadOnlyAs(true))
			.addSingleController(MODEL_ADAPTER_ANCESTOR, new NodeModelAdapterAncestor())
			.addSingleController(MODEL_ADAPTER_LEFT, new NodeModelAdapterLeft());
		
		// TODO test
		setProjectAccessController(new ProjectAccessController());

//		// initialize the list of code that will regenerate the descriptors
//		runnablesThatLoadDescriptors = new ArrayList<>();
//
//		// initialize codesyncplugin internals
//		codeSyncTypeCriterionDispatcherProcessor = new CodeSyncTypeCriterionDispatcherProcessor();
//		
//		codeSyncElementDescriptors = new ArrayList<CodeSyncElementDescriptor>();
//		relationDescriptors = new ArrayList<RelationDescriptor>();
//		
//		addNewExtensions = new ArrayList<AddNewExtension>();
//		addNewRelationExtensions = new ArrayList<AddNewRelationExtension>();
//		inplaceEditorExtensions = new ArrayList<InplaceEditorExtension>();
//		
//		featureAccessExtensions = new ArrayList<FeatureAccessExtension>();
		
		fullyQualifiedNameProvider = new ComposedFullyQualifiedNameProvider();
		
//		dependentFeatures = new ArrayList<DependentFeature>();
//		dependentFeatures.add(new DependentFeature(NotationPackage.eINSTANCE.getView(), NotationPackage.eINSTANCE.getEdge_Source(), true));
//		dependentFeatures.add(new DependentFeature(NotationPackage.eINSTANCE.getView(), NotationPackage.eINSTANCE.getEdge_Target(), true));
//		dependentFeatures.add(new DependentFeature(NotationPackage.eINSTANCE.getView(), NotationPackage.eINSTANCE.getView_PersistentChildren(), false));
//		
//		// main list of code sync descriptors
//		addRunnablesForLoadDescriptors(new Runnable() {
//			@Override
//			public void run() {
//				// descriptors
//				List<CodeSyncElementDescriptor> descriptors = new ArrayList<>();
//				descriptors.add(
//						new CodeSyncElementDescriptor()
//						.setCodeSyncType(FOLDER)
//						.setLabel(FOLDER)
//						.addChildrenCodeSyncTypeCategory(FILE)
//						.addFeature(NamedElementFeatureAccessExtension.NAME)
//						.setKeyFeature(NamedElementFeatureAccessExtension.NAME));
//				descriptors.add(
//						new CodeSyncElementDescriptor()
//						.setCodeSyncType(FILE)
//						.setLabel(FILE)
//						.addCodeSyncTypeCategory(FILE)
//						.addFeature(NamedElementFeatureAccessExtension.NAME)
//						.setKeyFeature(NamedElementFeatureAccessExtension.NAME));
//				
//				descriptors.add(
//						new CodeSyncElementDescriptor()
//						.setCodeSyncType(WIZARD_ELEMENT)
//						.setLabel("Wizard Element")
//						.setIconUrl("images/wizard/wand-hat.png")
//						.setDefaultName("NewWizardElement")
//						.addCodeSyncTypeCategory("topLevel")
//						.addChildrenCodeSyncTypeCategory(WIZARD_ATTRIBUTE)
//						.addFeature(NamedElementFeatureAccessExtension.NAME)
//						.setKeyFeature(NamedElementFeatureAccessExtension.NAME)						
//						.setStandardDiagramControllerProviderFactory("topLevelBox")
//						.setOrderIndex(500));
//				descriptors.add(
//						new CodeSyncElementDescriptor()
//						.setCodeSyncType(WIZARD_ATTRIBUTE)
//						.addCodeSyncTypeCategory(WIZARD_ATTRIBUTE)
//						.setLabel("Wizard Attribute")
//						.setIconUrl("images/wizard/wand-hat.png")
//						.setDefaultName("NewWizardAttribute")						
//						.setCategory("children")
//						.addFeature(NamedElementFeatureAccessExtension.NAME)
//						.setKeyFeature(NamedElementFeatureAccessExtension.NAME)						
//						.setStandardDiagramControllerProviderFactory("topLevelBoxChild")
//						.setOrderIndex(501));
//						
//				
//				getCodeSyncElementDescriptors().addAll(descriptors);
//								
//				EditorModelPlugin.getInstance().getMainChangesDispatcher().addProcessor(codeSyncTypeCriterionDispatcherProcessor);
//
//				// extensions
//				getFeatureAccessExtensions().add(new NamedElementFeatureAccessExtension(descriptors));
//								
//				getAddNewExtensions().add(new AddNewExtension_Note());	
//				getAddNewExtensions().add(new AddNewExtension_TopLevelElement());					
//								
//				getInplaceEditorExtensions().add(new InplaceEditorExtension_Default());
//				getInplaceEditorExtensions().add(new InplaceEditorExtension_Note());
//				
//				// processors
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("edge", new RelationDiagramProcessor());
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.wizardElement", new ChildrenUpdaterDiagramProcessor());				
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.wizardElement.wizardAttribute", new TopLevelElementChildProcessor());
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.wizardElement.wizardAttribute", new RelationsChangesDiagramProcessor());
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.wizardElement", new RelationsChangesDiagramProcessor());
//							
//				getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(WIZARD_ATTRIBUTE, new WizardChildrenPropagatorProcessor());
//				getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(WIZARD_ATTRIBUTE, new WizardElementKeyFeatureChangedProcessor());
//				getCodeSyncTypeCriterionDispatcherProcessor().addProcessor(WIZARD_ELEMENT, new WizardElementKeyFeatureChangedProcessor());
//				
//				CustomSerializationDescriptor macroRegexSD = new CustomSerializationDescriptor(MacroRegex.class)
//				.addDeclaredProperty("name")
//				.addDeclaredProperty("regex")				
//				.register();
//				
//				new CustomSerializationDescriptor(ParserRegex.class)
//				.addDeclaredProperties(macroRegexSD.getDeclaredProperties())
//				.addDeclaredProperty("action")
//				.addDeclaredProperty("fullRegex")				
//				.register();
//			}
//		});

//		// needs custom descriptor because it uses the builder template (i.e. setters return the instance)
//		new CustomSerializationDescriptor(CodeSyncElementDescriptor.class)
//			.addDeclaredProperty("codeSyncType")
//			.addDeclaredProperty("initializationTypes")
//			.addDeclaredProperty("initializationTypesLabels")
//			.addDeclaredProperty("initializationTypesOrderIndexes")
//			.addDeclaredProperty("label")
//			.addDeclaredProperty("iconUrl")
//			.addDeclaredProperty("defaultName")
//			.addDeclaredProperty("extension")
//			.addDeclaredProperty("codeSyncTypeCategories")
//			.addDeclaredProperty("childrenCodeSyncTypeCategories")
//			.addDeclaredProperty("category")
//			.addDeclaredProperty("features")
//			.addDeclaredProperty("keyFeature")
//			.addDeclaredProperty("standardDiagramControllerProviderFactory")
//			.addDeclaredProperty("orderIndex")
//			.register();
//		
//		new CustomSerializationDescriptor(RelationDescriptor.class)
//			.addDeclaredProperty("type")
//			.addDeclaredProperty("label")
//			.addDeclaredProperty("iconUrl")
//			.addDeclaredProperty("sourceCodeSyncTypes")
//			.addDeclaredProperty("targetCodeSyncTypes")
//			.addDeclaredProperty("sourceCodeSyncTypeCategories")
//			.addDeclaredProperty("targetCodeSyncTypeCategories")
//			.addDeclaredProperty("acceptTargetNullIfNoCodeSyncTypeDetected")
//			.register();
//		
//		new CustomSerializationDescriptor(WizardDependency.class)
//			.addDeclaredProperty("type")
//			.addDeclaredProperty("label")
//			.addDeclaredProperty("targetLabel")
//			.addDeclaredProperty("targetIconUrl")
//			.register();
	}
	
//	/**
//	 * Important: the code sync mapping and cache resources <b>must</b> be loaded through the same {@link ResourceSet}.
//	 */
//	public ResourceSet getOrCreateResourceSet(Object file, String diagramEditorStatefulServiceId) {
//		Object project = getProjectAccessController().getContainingProjectForFile(file);
//		DiagramEditorStatefulService service = (DiagramEditorStatefulService) CommunicationPlugin.getInstance()
//				.getServiceRegistry().getService(diagramEditorStatefulServiceId);
//
//		DiagramEditableResource diagramEditableResource = null;		
//		if (project != null) {
//			String path = EditorPlugin.getInstance().getFileAccessController().getAbsolutePath(project);
//			for (EditableResource er : service.getEditableResources().values()) {
//				DiagramEditableResource der = (DiagramEditableResource) er;				
//				if (EditorPlugin.getInstance().getFileAccessController().getAbsolutePath(der.getFile()).startsWith(path)) {
//					diagramEditableResource = der;
//					break;
//				}
//			}
//		}
//		if (diagramEditableResource != null) {
//			return diagramEditableResource.getResourceSet();
//		}
//		
//		ResourceSet resourceSet = new ResourceSetImpl();
//		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(Resource.Factory.Registry.DEFAULT_EXTENSION, new ResourceFactoryImpl() {
//			
//			@Override
//			public Resource createResource(URI uri) {
//				return new XMIResourceImpl(uri) {
//			    	protected boolean useUUIDs() {
//			    		return true;
//			    	}
//				};
//			}
//		});
//		return resourceSet;
//	}
//
	/**
	 * @author Mariana
	 * @author Sebastian Solomon
	 */
	public Node getResource(Object file) {
//		URI uri = EditorModelPlugin.getInstance().getModelAccessController().getURIFromFile(file);
//		boolean fileExists = EditorPlugin.getInstance().getFileAccessController().exists(file);
//		return getResource(resourceSet, uri, fileExists);
		NodeService service = CorePlugin.getInstance().getNodeService();
		Node node = new Node((String) file, null);
		return service.getChildren(node, new ServiceContext<NodeService>(service).add(POPULATE_WITH_PROPERTIES, true)).get(0);
	}
	
//	
//	/**
//	 * Saves all the resources from the {@link ResourceSet} where <code>resourceToSave</code>
//	 * is contained.
//	 * 
//	 * @author Mariana
//	 */
//	public void saveResource(Resource resourceToSave) {
//		if (resourceToSave != null) {
//			List<Resource> resources = Collections.singletonList(resourceToSave);
//			if (resourceToSave.getResourceSet() != null) {
//				resources = resourceToSave.getResourceSet().getResources();
//			}
//			for (Resource resource : resources) {
//				try { 
//					Map<Object, Object> options = EditorModelPlugin.getInstance().getLoadSaveOptions();
//					resource.save(options);
//				} catch (IOException e) {
//					throw new RuntimeException(e);
//				}
//			}
//		}
//	}
//	
//	/**
//	 * Discards the modifications from all the resources from the {@link ResourceSet} 
//	 * where <code>resourceToDiscard</code> is contained.
//	 * 
//	 * @author Mariana
//	 */
//	public void discardResource(Resource resourceToDiscard) {
//		if (resourceToDiscard != null) {
//			List<Resource> resources = Collections.singletonList(resourceToDiscard);
//			if (resourceToDiscard.getResourceSet() != null) {
//				resources = resourceToDiscard.getResourceSet().getResources();
//			}
//			for (Resource resource : resources) {
//				resource.unload();
//			}
//		}
//	}
	
	public Node getCodeSyncMappingRoot(Object project) {
		Node root = getResource(project);
//		if (!EditorPlugin.getInstance().getFileAccessController().exists(codeSyncElementMappingFile)) {
//			// first clear the resource in case the mapping file was deleted 
//			// after it has been loaded at a previous moment
//			cseResource.getContents().clear();
//			
//			for (String srcDir : getSrcDirs()) {
//				CodeSyncRoot cseRoot = (CodeSyncRoot) getRoot(cseResource, srcDir);
//				if (cseRoot == null) {
//					// create the CSE for the SrcDir
//					cseRoot = CodeSyncPackage.eINSTANCE.getCodeSyncFactory().createCodeSyncRoot();
//					cseRoot.setName(srcDir);
//					cseRoot.setType(FOLDER);
//				}
//				cseResource.getContents().add(cseRoot);
//			}
//			
//			CodeSyncPlugin.getInstance().saveResource(cseResource);
//		}
		return root;
	}
	
//	/**
//	 * @author Mariana
//	 */
//	public Resource getAstCache(Object project, ResourceSet resourceSet) {
//		Object astCacheElementFile = CodeSyncPlugin.getInstance().getProjectAccessController().getFile(project, ACE_FILE_LOCATION); 
//		Resource resource = CodeSyncPlugin.getInstance().getResource(resourceSet, astCacheElementFile);
//		if (!EditorPlugin.getInstance().getFileAccessController().exists(astCacheElementFile)) {
//			resource.getContents().clear();
//			CodeSyncPlugin.getInstance().saveResource(resource);
//		}
//		return resource;
//	}
//	
//	/**
//	 * @author Mariana
//	 */
//	protected CodeSyncRoot getRoot(Resource resource, String srcDir) {
//		for (EObject eObj : resource.getContents()) {
//			if (eObj instanceof CodeSyncRoot) {
//				CodeSyncRoot root = (CodeSyncRoot) eObj;
//				if (CodeSyncOperationsService.getInstance()
//						.getKeyFeatureValue(root).equals(srcDir))
//					return root;
//			}
//		}
//		return null;
//	}
//	
	/**
	 * @author Mariana
	 */
	public Node getSrcDir(Node root, String name) {
		NodeService service = CorePlugin.getInstance().getNodeService();
		List<Node> children = CorePlugin.getInstance().getNodeService().getChildren(root, new ServiceContext<NodeService>(service).add(POPULATE_WITH_PROPERTIES, true));
		for (Node child : children) {
			if (name.equals(child.getOrPopulateProperties().get(NAME))) {
				return child;
			}
		}
		return null;
	}
	
	/**
	 * @author Mariana
	 */
	public List<String> getSrcDirs() {
		if (srcDirs == null) {
			// TODO Mariana : get user input
			return Collections.singletonList("src");
		} 
		return srcDirs;
	}
	
	public void addSrcDir(String srcDir) {
		if (srcDirs == null) {
			srcDirs = new ArrayList<String>();
		}
		if (!srcDirs.contains(srcDir)) {
			srcDirs.add(srcDir);
		}
	}
	
	/**
	 * Executes the runnable and keeps it around to be executed
	 * when the descriptors need to be refreshed.
	 * 
	 * @param runnable
	 * 
	 * @author Mircea Negreanu
	 */
	public void addRunnablesForLoadDescriptors(Runnable runnable) {
		runnable.run();
		
//		runnablesThatLoadDescriptors.add(runnable);
	}

	@Override
	public void registerMessageBundle() throws Exception {
		// no messages yet
	}
	
//	/**
//	 * Reruns all the registered runnable to regenerate descriptors,
//	 * processors, ..
//	 * 
//	 * @return String containing errors thrown during run (if any)
//	 */
//	public String regenerateDescriptors() {
//		// clear the descriptors
//		getCodeSyncElementDescriptors().clear();
//		getRelationDescriptors().clear();
//		getFeatureAccessExtensions().clear();
//		getAddNewExtensions().clear();
//		getInplaceEditorExtensions().clear();
//		getCodeSyncTypeCriterionDispatcherProcessor().clear();
//		RegexService.getInstance().clearRegexActionsAndCompiledRegexConfigurations();
//		
//		StringBuilder errorsCollected = new StringBuilder();
//		for (Runnable run: runnablesThatLoadDescriptors) {
//			try {
//				run.run();
//			} catch (Exception ex) {
//				errorsCollected.append(ex.toString());
//				errorsCollected.append("\n");
//			}
//		}
//		
//		return errorsCollected.toString();
//	}
//	
//	public List<RelationDescriptor> getRelationDescriptorsHavingThisTypeAsSourceCodeSyncType(String type) {
//		List<RelationDescriptor> descriptors = new ArrayList<RelationDescriptor>();
//		for (RelationDescriptor descriptor : getRelationDescriptors()) {
//			if (descriptor.getSourceCodeSyncTypes().contains(type)) {
//				descriptors.add(descriptor);
//			}
//		}
//		return descriptors;
//	}
//	
//	public List<RelationDescriptor> getRelationDescriptorsHavingThisEndTypes(String sourceType, String targetType) {
//		CodeSyncElementDescriptor sourceDescriptor = getCodeSyncElementDescriptor(sourceType);
//		CodeSyncElementDescriptor targetDescriptor = getCodeSyncElementDescriptor(targetType);
//		List<RelationDescriptor> descriptors = new ArrayList<RelationDescriptor>();
//		for (RelationDescriptor descriptor : getRelationDescriptors()) {
//			boolean sourceTypeOk = false;
//			boolean targetTypeOk = false;
//			if (descriptor.getSourceCodeSyncTypes() != null && descriptor.getSourceCodeSyncTypes().contains(sourceType)) {
//				sourceTypeOk = true;				
//			} else {
//				for (String category : sourceDescriptor.getCodeSyncTypeCategories()) {
//					if (descriptor.getSourceCodeSyncTypeCategories() != null && descriptor.getSourceCodeSyncTypeCategories().contains(category)) {
//						sourceTypeOk = true;
//					}
//				}
//			}
//			if (descriptor.getTargetCodeSyncTypes() != null && descriptor.getTargetCodeSyncTypes().contains(targetType)) {
//				targetTypeOk = true;				
//			} else {
//				for (String category : targetDescriptor.getCodeSyncTypeCategories()) {
//					if (descriptor.getTargetCodeSyncTypeCategories() != null && descriptor.getTargetCodeSyncTypeCategories().contains(category)) {
//						targetTypeOk = true;
//					}
//				}
//			}
//			if (sourceTypeOk && targetTypeOk) {
//				descriptors.add(descriptor);
//			}
//		}
//		return descriptors;
//	}
	
}
