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
package org.flowerplatform.team.git.history.internal;

import java.util.ArrayList;

import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revplot.AbstractPlotRenderer;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.team.git.GitConstants;

/**
 *	@author Cristina Constantinescu
 */
public class WebCommitPlotRenderer extends AbstractPlotRenderer<WebLane, String> {

	private final String black = "#000000";	
	private final String gray = "#bebebe";
	private final String white = "#ffffff";
	private final String commitDotFill = "#DCDCDC";
	private final String commitDotOutline = "#6E6E6E";
	
	private WebCommit commit;
				
	private Node drawings;
	
	private String specialMessage;
			
	public Node getDrawings() {
		return drawings;
	}

	public String getSpecialMessage() {
		return specialMessage;
	}

	public WebCommitPlotRenderer(String nodeUri, WebCommit commit) {		
		this.commit = commit;
		drawings = new Node(nodeUri,null);
		specialMessage = "";
	}

	public void paint() {
		paintCommit(commit, 23);
	}
	
	@Override
	protected String laneColor(WebLane myLane) {
		return myLane != null ? myLane.color : black;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void drawLine(String color, int x1, int y1, int x2, int y2, int width) {
		ArrayList<Object> existingLine = (ArrayList<Object>)drawings.getProperties().get(GitConstants.DRAW_LINE_LIST);
		if (existingLine == null) {
			ArrayList<Object> line = new ArrayList<Object>();		
			line.add(GitConstants.DRAW_LINE);
			line.add(x1);
			line.add(y1);
			line.add(x2);
			line.add(y2);		
			line.add(width);
			line.add(color);
			drawings.getProperties().put(GitConstants.DRAW_LINE_LIST, line);			
		} else {
			existingLine.add(GitConstants.DRAW_LINE);
			existingLine.add(x1);
			existingLine.add(y1);
			existingLine.add(x2);
			existingLine.add(y2);	
			existingLine.add(width);
			existingLine.add(color);			
			drawings.getProperties().put(GitConstants.DRAW_LINE_LIST, existingLine);
		}				
	}

	@Override
	protected void drawCommitDot(int x, int y, int w, int h) {
		ArrayList<Object> line = new ArrayList<Object>();
		line.add(GitConstants.DRAW_DOT);
		line.add(x);
		line.add(y);
		line.add(w);
		line.add(h);		
		line.add(commitDotOutline);
		line.add(commitDotFill);
		drawings.getProperties().put(GitConstants.DRAW_COMMIT_DOT, line);
	}

	@Override
	protected void drawBoundaryDot(int x, int y, int w, int h) {
		ArrayList<Object> line = new ArrayList<Object>();
		line.add(GitConstants.DRAW_DOT);
		line.add(x);
		line.add(y);
		line.add(w);
		line.add(h);		
		line.add(gray);
		line.add(white);
		drawings.getProperties().put(GitConstants.DRAW_BOUNDARY_DOT, line);		
	}

	@Override
	protected int drawLabel(int x, int y, Ref ref) {
		specialMessage += "[" + Repository.shortenRefName(ref.getName()) + "]";
		return 0;
	}
	
	@Override
	protected void drawText(String msg, int x, int y) {
	}
	
}