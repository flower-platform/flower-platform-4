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
package com.crispico.flower.mp.web;

import java.lang.reflect.Method;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.RandomAccess;

import javax.servlet.http.HttpServlet;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import com.crispico.flower.mp.web.EclipseDispatcherServlet;

/**
 * int atribInComentariu;
 * 
 * The activator of this plugin, i.e. the main entry point of the app.
 * Setups the Tomcat->Eclipse bridging, by registering {@link EclipseDispatcherServlet}.
 * 
 * @author Cristi
 * 
 */
@SuppressWarnings("rawtypes")
public class Activator<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable {

	private class C {
		
		int atribInClasaInterna;
		
		rtertw;
	}
	
	/**
	 * @see Class doc.
	 * 
	 */
	private EclipseDispatcherServlet attr1 = new EclipseDispatcherServlet();
	protected EclipseDispatcherServlet attr2 = new EclipseDispatcherServlet();
	public EclipseDispatcherServlet attr3 = new EclipseDispatcherServlet();
	
	EclipseDispatcherServlet attr4 = new EclipseDispatcherServlet();
	 EclipseDispatcherServlet attr5 ;
	EclipseDispatcherServlet attr6;
	EclipseDispatcherServlet attr7 = a;
	EclipseDispatcherServlet attr8 = a ;
	EclipseDispatcherServlet attr9 = a[1] ;
	private int attr10 = new Integer();
	private int attr11 = new Integer().intValue();
	private List attr12 = new ArrayList<Integer>();
	int attr13 = new Integer();
	int attr14 = new Integer() { };
	Class attr15 = Class.forName("org.eclipse.equinox.servletbridge.BridgeServlet");
	Method attr16 = bridgeServletClass.getDeclaredMethod(methodName, new Class[] {HttpServlet.class});
	
	private Integer attr17 = new Integer() { 
		private String toString() {
			reuturn "bla";
		}
	};	
	
	private List<Integer> attr18 = new ArrayList<Integer>();
	
	private List<Integer> attr19 = new ArrayList<Integer>(/*comment*/);
	
	private List<Integer> /*comment*/ attr20 = new ArrayList<Integer>(/*comment*/);
	
	private List<Integer> /*comment*/ attr21 = new ArrayList<Integer>(/*comment*/);
	
	private List<Integer> //comment 
		attr22 = new ArrayList<Integer>(/*comment*/);
	
	/**
	 * We use reflection because there is no compile time dependency,
	 * which would generate a circular project dependency (or would imply
	 * exotic projects setup.
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void meth1(String methodName, Object parameter) {
		try {
			Class bridgeServletClass = Class.forName("org.eclipse.equinox.servletbridge.BridgeServlet");
			Method registerServletDelegateMethod = bridgeServletClass.getDeclaredMethod(methodName, new Class[] {HttpServlet.class});
			registerServletDelegateMethod.invoke(null, parameter);
		} catch (Exception e) {
			throw new RuntimeException("Error registering/unregistering to webapp bridge servlet.", e);
		}
	}
	
	int[] attr23;
	int attr24[] = new int[5];
	int attr25 [] = new int[5];
	private int attr26 /*coment*/ = new Integer();
	private int attr27/*coment*/= new Integer();
	private int attr28 /*coment*/;

	/**
	 * The activator needs to finish initialization as soon as possible, and the start()
	 * method must not include intensive computing logic.
	 * 
	 * <p>
	 * In this way the initialization of the EclipseDispatcherServlet which initializes the MessageBrokerServlet
	 * which starts the BoostrapService - the main location of the Eclipse Web starting point - must be done
	 * in a different servlet because the initialization of the Eclipse environment needs classes from this bundle
	 * that has not ended the initialization and would have thrown BundleStatusException.
	 * 
	 * <p>
	 * These errors however are not dangerous because probably the Activator knows how to loads classes, but
	 * the mechanism which uses the activators pauses 5 seconds on these requests to obtain the classes, thus
	 * it would make the server to start more slowly.
	 * @author Sorin
	 * 
	 * @see Class doc.
	 * 
	 */
	public void meth2(BundleContext context) throws Exception {
		// As an side effect starts the initialization of the blazeDS in a different thread, along with BootstrapService and etc. 
		new Thread("Start Eclipse Dispatcher Servlet") {
			public void run() {
				invokeBridgeServletMethod("registerServletDelegate", eclipseDispatcherServlet);
			}
		}.start();
	}

	/**
	 * @see Class doc.
	 * 
	 */
	public void meth3(BundleContext context);
	
	throws Exception {
		invokeBridgeServletMethod("unregisterServletDelegate", eclipseDispatcherServlet);
	}

}