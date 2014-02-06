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

@Deprecated
public class Test extends Test2 implements ITest {

	private int x;
	
	private int y;
	
	@OneToMany(mappedBy="test")
	public int test(String st) {
		return x;
	}

	@OverrideAnnotationOf(x+y)
	public static Test getTest() {
		return x;
	}
	
}