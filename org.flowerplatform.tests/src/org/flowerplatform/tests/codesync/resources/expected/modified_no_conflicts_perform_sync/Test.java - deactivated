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
import javax.persistence.OneToMany;

import com.sun.xml.internal.bind.annotation.OverrideAnnotationOf;

public @Deprecated(test) class Test extends SuperClassFromModel implements IFromSource, IFromModel {

	/**
	 * modified from source
	 * @author test
	 */
	private Test x = 1;
	
	private int z = 3;
	
	@OneToMany(mappedBy="modified_by_model", orphanRemoval=true)
	static private int test(final String st) {
		return x;
	}

	/**
	 * modified from model
	 * @author  test
	 */
	public @OverrideAnnotationOf(value1=true, value2=false) Test getTest(int a) {
		return x;
	}

	public enum ActionType {
		
		ACTION_TYPE_COPY_LEFT_RIGHT(new Test()) {
			
		},
		ACTION_TYPE_COPY_RIGHT_LEFT(new InternalClsFromSource());
		
		public Object diffAction;
		
		private ActionType(Object action) {
			this.diffAction = action;
		}
	}
	
	public @interface AnnotationTest {
		
		boolean value1() default true;
		boolean value2() default false;
		
	}
	
	public class InternalClsFromSource {
		private int x;
	}

	/**
	 * doc from model @author test
	 */
	public int t;

	class InternalClassFromModel {
	}
	
}