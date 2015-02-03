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
package org.flowerplatform.js_client.java;

import org.mozilla.javascript.Function;

/**
 * @author Cristina Constantinescu
 */
// TODO rename Abstract*; de asemenea redenumirile facute pe js, sa se reflecte si in clasele din java
public abstract class ServiceInvocator {

	public void invoke(String serviceIdAndMethodName, Object[] parameters) throws Exception {
		this.invoke(serviceIdAndMethodName, parameters, null, null);
	}
	
	public void invoke(String serviceIdAndMethodName, Object[] parameters, Function resultCallback) throws Exception {
		this.invoke(serviceIdAndMethodName, parameters, resultCallback, null);
	}
	
	public abstract void invoke(String serviceIdAndMethodName, Object[] parameters, Function resultCallback, Function faultCallback) throws Exception;
	
}
