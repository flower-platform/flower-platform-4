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
package org.flowerplatform.codesync.code.java;

import org.flowerplatform.codesync.code.adapter.FolderModelAdapter;
import org.flowerplatform.util.plugin.AbstractFlowerJavaPlugin;
import org.osgi.framework.BundleContext;

/**
 * @author Mariana
 */
public class CodeSyncCodeJavaPlugin extends AbstractFlowerJavaPlugin {

	public static final String TECHNOLOGY = "java";
	
	protected static CodeSyncCodeJavaPlugin INSTANCE;
	
	private FolderModelAdapter folderModelAdapter;
	
	public static CodeSyncCodeJavaPlugin getInstance() {
		return INSTANCE;
	}
	
	/**
	 * @author Mariana Gheorghe
	 * @author Mircea Negreanu
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		INSTANCE = this;
		
		// wrap the descriptor register code in a runnable
//		CodeSyncPlugin.getInstance().addRunnablesForLoadDescriptors(new Runnable() {
//			@Override
//			public void run() {
//				CodeSyncPlugin.getInstance().addSrcDir("src");
//				
//				// TODO reactivate java later
//				CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
//						new CodeSyncElementDescriptor()
//						.setCodeSyncType("javaClass")
//						.setLabel("Class")
//						.setIconUrl("images/obj16/SyncClass.gif")
//						.setDefaultName("NewJavaClass")
//						.setExtension("java")
//						.addChildrenCodeSyncTypeCategory("javaClassMember")
//						.addFeature(DOCUMENTATION)
//						.addFeature(VISIBILITY)
//						.addFeature(IS_ABSTRACT)
//						.addFeature(IS_STATIC)
//						.addFeature(IS_FINAL)
//						.addFeature(SUPER_CLASS)
//						.addFeature(SUPER_INTERFACES));
//				CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
//						new CodeSyncElementDescriptor()
//						.setCodeSyncType("javaOperation")
//						.setLabel("Operation")
//						.setIconUrl("images/obj16/SyncOperation_public.gif")
//						.setDefaultName("newOperation")
//						.addCodeSyncTypeCategory("javaClassMember")
//						.addFeature(DOCUMENTATION)
//						.addFeature(VISIBILITY)
//						.addFeature(IS_ABSTRACT)
//						.addFeature(IS_STATIC)
//						.addFeature(IS_FINAL)
//						.addFeature(TYPE));
//				CodeSyncPlugin.getInstance().getCodeSyncElementDescriptors().add(
//						new CodeSyncElementDescriptor()
//						.setCodeSyncType("javaAttribute")
//						.setLabel("Attribute")
//						.setIconUrl("images/obj16/SyncProperty_public.gif")
//						.setDefaultName("newAttribute")
//						.addCodeSyncTypeCategory("javaClassMember")
//						.addFeature(DOCUMENTATION)
//						.addFeature(VISIBILITY)
//						.addFeature(IS_ABSTRACT)
//						.addFeature(IS_STATIC)
//						.addFeature(IS_FINAL)
//						.addFeature(TYPE)
//						.addFeature(INITIALIZER));
//				
//				CodeSyncPlugin.getInstance().getFeatureAccessExtensions().add(new JavaFeatureAccessExtension());
				
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.javaClass", new JavaClassProcessor());
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.javaClass.title", new JavaClassTitleProcessor());
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.javaClass.javaAttribute", new JavaClassAttributeProcessor());
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.javaClass.javaOperation", new JavaClassOperationProcessor());
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("scenarioInterraction", new JavaScenarioElementProcessor());
//				
//				AbstractFeatureChangesProcessor processor = new AbstractFeatureChangesProcessor();
//				// if model element removed => remove view
//				processor.getDependentFeatures().add(new DependentFeature(EObject.class, NotationPackage.eINSTANCE.getView_DiagrammableElement()));
//				// if model element removed => remove relations
//				processor.getDependentFeatures().add(new DependentFeature(CodeSyncElement.class, CodeSyncPackage.eINSTANCE.getRelation_Source()));
//				processor.getDependentFeatures().add(new DependentFeature(CodeSyncElement.class, CodeSyncPackage.eINSTANCE.getRelation_Target()));
//				// if view removed => remove edges
//				processor.getDependentFeatures().add(new DependentFeature(View.class, NotationPackage.eINSTANCE.getView_SourceEdges()));
//				processor.getDependentFeatures().add(new DependentFeature(View.class, NotationPackage.eINSTANCE.getView_TargetEdges()));
//				
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.javaClass", processor);
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.javaClass.javaAttribute", processor);
//				EditorModelPlugin.getInstance().getDiagramUpdaterChangeProcessor().addDiagrammableElementFeatureChangeProcessor("classDiagram.javaClass.javaOperation", processor);
//				
//				CodeSyncPlugin.getInstance().getFullyQualifiedNameProvider().addDelegateProvider(new JavaFullyQualifiedNameProvider());
//				ResourcesPlugin.getWorkspace().addResourceChangeListener(new JavaResourceChangeListener());
//				JavaCore.addElementChangedListener(new JavaElementChangedListener(), ElementChangedEvent.POST_RECONCILE);
//			}
//		});
		
	}

	public FolderModelAdapter getFolderModelAdapter() {
		return folderModelAdapter;
	}
	
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		INSTANCE = null;
	}
	
	@Override
	public void registerMessageBundle() throws Exception {
		// nothing to do yet
	}

}
