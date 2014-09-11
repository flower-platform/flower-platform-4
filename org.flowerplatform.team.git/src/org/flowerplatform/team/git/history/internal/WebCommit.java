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
package org.flowerplatform.team.git.history.internal;

import java.io.IOException;

import org.eclipse.jgit.lib.AnyObjectId;
import org.eclipse.jgit.revplot.PlotCommit;
import org.eclipse.jgit.revwalk.RevWalk;

/**
 *	@author Cristina Constantinescu
 */
public class WebCommit extends PlotCommit<WebLane> {
	
	private RevWalk walk;
	
	/**
	 * @author Bogdan Manica
	 */
	WebCommit(final AnyObjectId id, RevWalk walk) {
		super(id);
		this.walk = walk;
	}

	@Override
	public void reset() {	
		walk = null;
		super.reset();
	}

	/**
	 *@author see class
	 **/
	public void parseBody() throws IOException {
		if (getRawBuffer() == null) {
			walk.parseBody(this);
		}
	}

}