/* license-start
 * 
 * Copyright (C) 2008 - 2014 Crispico Software, <http://www.crispico.com/>.
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
 * license-end
 */
package com.crispico.flower.flexdiagram.action
{

	/**
	 * Basic data structure available for actions (in <code>IAction.context</code>).
	 * Actions can use it their methods; e.g. <code>isVisible()</code>; <code>run()</code>.
	 * 
	 * @see com.crispico.flower.flexdiagram.action.IAction#context
	 * @see com.crispico.flower.flexdiagram.action.CreateActionContext
	 * @see com.crispico.flower.flexdiagram.gantt.contextmenu.GanttCreateActionContext
	 * @author Sorin
	 * 
	 */ 	
	public dynamic class ActionContext {
		
		/**
		 * When used together with gantt4flex, this class (in fact all classes form .flexdiagram package) exist twice. This is chosen
		 * over the one from the gantt4flex lib. And it complains that this field is missing (although the class is dynamic).
		 */
		public var diagramFigure:Object;
	}
}