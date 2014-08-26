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
package org.flowerplatform.team.git;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jgit.lib.ProgressMonitor;

/**
 * 
 * @author Alina Bratu
 *
 */

public class GitProgressMonitor implements ProgressMonitor {

	IProgressMonitor root;
	
	protected String TASK_NAME;
	
	private IProgressMonitor task;
	
	private int lastStep;
	
	private int totalSteps;
	
	private String message;
	
	public GitProgressMonitor(IProgressMonitor ipm, String name) {
		root = ipm;
		TASK_NAME = name;
	}
	
	@Override
	public void start(int totalTasks) {
		root.beginTask(TASK_NAME, totalTasks * 1000);
	}

	@Override
	public void beginTask(String title, int total) {
		endTask();
		lastStep = 0;
		totalSteps = total;
		message = title;
		task = new SubProgressMonitor(root, totalSteps);
		task.beginTask(title, totalSteps);
		task.subTask(message);
	}

	@Override
	public void update(int step) {
		if (task != null) {
			int nextStep = lastStep + step;
			
			if (!(totalSteps < 0)) {
				int completed = nextStep;
				String total = " completed";
				if (totalSteps > 0 ) {
					if (nextStep > 0 && nextStep <= totalSteps) {
						completed = nextStep%totalSteps;
						total = " of " + String.valueOf(totalSteps) + " completed";
					}
				}
				if (nextStep != lastStep) {
					String newMessage = message + " : " + completed + total;
					task.subTask(newMessage);
				}
			}
			lastStep = nextStep;
			task.worked(lastStep);
		}
		
	}

	@Override
	public void endTask() {
		if (task != null) {
			task.done();
		}
		task = null;
	}

	@Override
	public boolean isCancelled() {
		return task.isCanceled();
	}
	

}