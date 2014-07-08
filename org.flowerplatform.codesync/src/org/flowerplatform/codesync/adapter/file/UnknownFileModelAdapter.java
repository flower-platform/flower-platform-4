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
package org.flowerplatform.codesync.adapter.file;

import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

/**
 * Used to handle files with unknown extensions during synchronization.
 * 
 * @author Mariana Gheorghe
 */
public class UnknownFileModelAdapter extends AbstractFileModelAdapter {

	@Override
	protected Object createFileInfo(Object file) {
		return null;
	}

	@Override
	protected TextEdit rewrite(Document document, Object fileInfo) {
		return null;
	}

}