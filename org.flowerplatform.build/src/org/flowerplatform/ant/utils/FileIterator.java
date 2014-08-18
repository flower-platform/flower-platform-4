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
package org.flowerplatform.ant.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author Florin
 *
 */
public class FileIterator implements Iterator<File> {

	private Queue<File> queue = new LinkedList<File>();
	
	private FileFilter secondLevelFileFilter;

	private boolean isChildLevel = true;
/**
 * @author Mariana Gheorghe
 */
	public FileIterator(File rootFolder) {
		queue.add(rootFolder);
	}
/**
 * @author Mariana Gheorghe
 */
	public FileIterator(File workspaceFolder, FileFilter secondLevelFileFilter) {
		this.secondLevelFileFilter = secondLevelFileFilter;
		queue.add(workspaceFolder);
	}

	@Override
	public boolean hasNext() {
		return queue.size() > 0;
	}

	@Override
	public File next() {
		File file = queue.remove();
		if (file.isDirectory()) {
			if (secondLevelFileFilter != null && isChildLevel) {
				for (File childFile : file.listFiles()) {
					if (secondLevelFileFilter.accept(childFile)) {
						queue.add(childFile);
					}
				}
			} else {
				queue.addAll(Arrays.asList(file.listFiles()));
			}
			isChildLevel = false;
		}
		return file;
	}

	@Override
	public void remove() {
		throw new RuntimeException();
	}

}