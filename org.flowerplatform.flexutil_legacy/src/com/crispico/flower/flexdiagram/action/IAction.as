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
package com.crispico.flower.flexdiagram.action {

	import com.crispico.flower.flexdiagram.contextmenu.IMenuEntryModel;
	
	import mx.collections.ArrayCollection;

	/**
	 * <p>
	 * Actions contain logic that is executed against
	 * the current selection. The <code>isVisible()</code>
	 * and <code>run()</code> methods need to be implemented.
	 * 
	 * <p>
	 * Actions are usually instantiated when the application
	 * starts. They are added to the <code>FlowerContextMenu</code>
	 * each time <code>fillContextMenuFunction</code> or 
	 * <code>fillCreateContextMenuFunction</code> callbacks are 
	 * invoked.
	 * 
	 * <p>
	 * Usually the code from the <code>isVisible()</code>
	 * method decides if the action is visible or not, based on
	 * the current selection (and/or other "global" settings
	 * specific to your application). The utility method <code>
	 * FlowerContextMenu.addActionEntryIfVisible()</code> is
	 * usefull during the <code>fill...ContextMenu()</code> methods. <br/>
	 * 
	 * <p>
	 * However depending on your needs the code from the <code>fill...ContextMenu()
	 * </code> methods might decide what needs to be added or not
	 * and/or create new actions "on the fly".
	 * 
	 * @see BaseAction
	 * @see com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu
	 * @author Cristina
	 * 
	 */
	public interface IAction extends IMenuEntryModel{
 
		/**
		 * Contains additional parameters that might be used by the action. Look at
		 * <code>ActionContext</code>, <code>CreateActionContext</code> and <code>GanttCreateActionContext</code> 
		 * to see what properties are available.
		 * 
		 * @see com.crispico.flower.flexdiagram.action.ActionContext
		 * @see com.crispico.flower.flexdiagram.action.CreateActionContext
		 * @see com.crispico.flower.flexdiagram.gantt.contextmenu.GanttCreateActionContext
		 */
		 function set context(value:ActionContext):void;

		/**
		 * Decides wheter the action should be displayed or not within the context menu.
		 * This method is evaluated by <code>FlowerContextMenu.addActionEntryIfVisible()</code>.
		 * 
		 * <p>
		 * If you don't use the former method (and you create manually an <code>ActionEntry</code>),
		 * you need to invoke this method. 
		 * 
		 * <p>
		 * If the selection is empty, actions can still be added (=> the context menu of the diagram).
		 * 
		 * <p>
		 * <b>NOTE:</b> The <code>selectedEditParts</code> parameter contains <code>EditPart</code>s of the
		 * selected model elements (<b>NOT</b> the model elements directly). To access the model elements,
		 * use <code>editPart.getModel()</code> (e.g. <code>selection[i].getModel()</code>). 
		 * 
		 * @see com.crispico.flower.flexdiagram.contextmenu.FlowerContextMenu#addActionEntryIfVisible()
		 * 
		 */
		 function isVisible(selectedEditParts:ArrayCollection):Boolean;

		/**
		 * Contains the logic that is executed when the user clicks on the action
		 * (from the context menu).
		 * 
		 * <p>
		 * <b>NOTE1:</b> The <code>selectedEditParts</code> parameter contains <code>EditPart</code>s of the
		 * selected model elements (<b>NOT</b> the model elements directly). To access the model elements,
		 * use <code>editPart.getModel()</code> (e.g. <code>selection[i].getModel()</code>). 
		 * 
		 * <p>
		 * <b>NOTE2:</b> If the code removes model element(s) from the data provider (e.g. an action that 
		 * deletes an element), please copy the selection array and operate on the copy rather than the
		 * original array. This is necessary because when the system detects a removal, the selection
		 * is reinitialized.
		 * 
		 * 
		 */
		 function run(selectedEditParts:ArrayCollection):void;
	}

}