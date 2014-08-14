/* license-start
 * 
 * Copyright (C) 2008 - 2013 Crispico Software, <http://www.crispico.com/>.
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
package org.flowerplatform.codesync;

import java.util.Iterator;

/**
 * This class is at the same time an {@link Iterator} and an {@link Iterable}. It "wraps"
 * a {@link #masterIterator} and returns only the elements accepted by {@link #isAccepted()}
 * (this method needs to be implemented).
 * 
 * <p>
 * This class is meant to be subclassed "in-line", by providing the filter function. It's
 * purpose is to avoid spending time to copy the elements from one collection to another collection
 * which will be iterated afterwards (i.e. we eliminate the intermediate iteration).
 * 
 * @author Cristi
 */
public abstract class FilteredIterable<IN, OUT> implements Iterator<OUT>, Iterable<OUT> {

	private Iterator<IN> masterIterator;

	private OUT next;

	/**
	 *@author Mariana Gheorghe
	 */
	public FilteredIterable(Iterator<IN> masterIterator) {
		super();
		this.masterIterator = masterIterator;
	}

	/**
	 * @author see class
	 */
	protected abstract boolean isAccepted(IN candidate);
	
	/**
	 * @author see class
	 */
	@SuppressWarnings("unchecked")
	protected OUT convert(IN input) {
		return (OUT) input;
	}

	@Override
	public boolean hasNext() {
		while (masterIterator.hasNext()) {
			IN candidate = masterIterator.next();
			if (isAccepted(candidate)) {
				next = convert(candidate);
				return true;
			}
		}
		next = null;
		return false;
	}

	@Override
	public OUT next() {
		return next;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Iterator<OUT> iterator() {
		return this;
	}
}