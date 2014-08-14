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
package org.flowerplatform.codesync.action;

import org.flowerplatform.codesync.Diff;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.adapter.IModelAdapter;


/**
 * @author Mariana Gheorghe
 */
public class DiffActionCopyRightToLeft extends DiffAction {

	@Override
	public ActionResult execute(Match match, int diffIndex) {
		Diff diff = match.getDiffs().get(diffIndex);
		IModelAdapter leftModelAdapter = match.getCodeSyncAlgorithm().getLeftModelAdapter(match.getLeft());
		IModelAdapter rightModelAdapter = match.getCodeSyncAlgorithm().getRightModelAdapter(match.getRight());
		
		Object value = rightModelAdapter.getValueFeatureValue(match.getRight(), diff.getFeature(), null, match.getCodeSyncAlgorithm());
		leftModelAdapter.setValueFeatureValue(match.getLeft(), diff.getFeature(), value, match.getCodeSyncAlgorithm());
		diff.setIsConflict(false);
		diff.setIsLeftModified(true);
		
		ActionResult result = new ActionResult(false, true, diff.isRightModified());
		actionPerformed(match, leftModelAdapter, match.getLeft(), rightModelAdapter, match.getRight(), diff.getFeature(), result);
	
		return result;
	}
}