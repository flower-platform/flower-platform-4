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
	
	protected String taskName;
	
	private IProgressMonitor task;
	
	private int lastStep;
	
	private int totalSteps;
	
	private String message;
	
	/**
	 *@author see class
	 */
	public GitProgressMonitor(IProgressMonitor ipm, String name) {
		root = ipm;
		taskName = name;
	}
	
	@Override
	public void start(int totalTasks) {
		root.beginTask(taskName, totalTasks * 1000);
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
				if (totalSteps > 0) {
					if (nextStep > 0 && nextStep <= totalSteps) {
						completed = nextStep % totalSteps;
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
