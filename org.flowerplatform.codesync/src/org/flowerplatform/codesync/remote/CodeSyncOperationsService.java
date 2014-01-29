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
package org.flowerplatform.codesync.remote;

import java.io.File;

import org.flowerplatform.codesync.CodeSyncPlugin;

/**
 * @author Mariana Gheorghe
 */
public class CodeSyncOperationsService {

	public void synchronize(String path, String technology) {
//		File diagram;
//		try {
//			diagram = (File) EditorPlugin.getInstance().getFileAccessController().getFile(path);
//		} catch (Exception e) {
//			throw new RuntimeException(path);
//		}
//		File project = CodeSyncPlugin.getInstance().getProjectsProvider().getContainingProjectForFile(diagram);
//		File srcDir = CodeSyncPlugin.getInstance().getProjectsProvider().getFile(project, "js");
//		CodeSyncPlugin.getInstance().getCodeSyncAlgorithmRunner().runCodeSyncAlgorithm(project, srcDir, technology, context.getCommunicationChannel(), true);
	
		// TODO test
		path = "D:/temp/";
		technology = "java";
		File project = new File(path);
		File resource = new File(path, "sync_test");
		CodeSyncPlugin.getInstance().getCodeSyncAlgorithmRunner().runCodeSyncAlgorithm(project, resource, technology, true);
	}
	
}
