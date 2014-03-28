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
package org.flowerplatform.util;

/**
 * @author Cristian Spiescu
 */
public class Pair<A, B> {
	public A a;
	public B b;
	
	/**
	 * @author Cristina Constantinescu
	 */
	public Pair() {
	}
	
	public Pair(A a, B b) {
		super();
		this.a = a;
		this.b = b;
	}

	@Override
	public int hashCode() {
		return a.hashCode() ^ b.hashCode();
	}

	@Override
	public String toString() {
		return "(" + a + ", " + b + ")";
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Pair<?, ?>)) {
			return false;
		}
		Pair<?, ?> otherObj = (Pair<?, ?>) obj;
		return 
			((a == null && otherObj.a == null) || (a != null && a.equals(otherObj.a))) &&
			((b == null && otherObj.b == null) || (b != null && b.equals(otherObj.b)));
	}
	
}