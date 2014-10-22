package org.flowerplatform.codesync.template;

/**
 * @author Mariana Gheorghe
 */
public class WebAppAngularJSModule {

	/**
	 * 
	 */
	public void init() {
		CodeSyncTemplatePlugin.getInstance().getCodeSyncTemplateService()
				.addMacros("tabset.vm,tab.vm,form.vm,stringField.vm,comboBoxField.vm,table.vm,column.vm");
	}

}
