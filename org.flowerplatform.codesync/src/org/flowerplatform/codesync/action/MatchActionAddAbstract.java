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
package org.flowerplatform.codesync.action;

import org.flowerplatform.codesync.CodeSyncAlgorithm;
import org.flowerplatform.codesync.Match;
import org.flowerplatform.codesync.adapter.IModelAdapter;
import org.flowerplatform.codesync.feature_provider.FeatureProvider;


/**
 * 
 */
public abstract class MatchActionAddAbstract extends DiffAction {

	protected boolean processDiffs;
	
	protected abstract Object getThis(Match match); 
	protected abstract Object getOpposite(Match match);
	protected abstract IModelAdapter getThisModelAdapter(Match match);
	protected abstract IModelAdapter getOppositeModelAdapter(Match match);
	protected abstract void setOpposite(Match match, Object elment);
	protected abstract void processDiffs(Match match);
	protected abstract void setChildrenModified(Match match);

	public MatchActionAddAbstract(boolean processDiffs) {
		super();
		this.processDiffs = processDiffs;
	}

	@Override
	public ActionResult execute(Match match, int diffIndex) {
		processMatch(match.getParentMatch(), match, true);
		Object child = getThis(match);
		return new ActionResult(false, true, true, getThisModelAdapter(match).getMatchKey(child), true);
	}
	
	protected void processMatch(Match parentMatch, Match match, boolean isFirst) {
		Object this_ = getThis(match);
		if (this_ == null) // this happens when parentMatch was a 2-match-ancestor-left/right and match is 1-match-ancestor (i.e. del left & right)
			return;
		IModelAdapter oppositeParentMa = getOppositeModelAdapter(parentMatch);
		Object opposite = oppositeParentMa.createChildOnContainmentFeature(getOpposite(parentMatch), match.getFeature(), this_, match.getCodeSyncAlgorithm().getTypeProvider());
		setOpposite(match, opposite);
		// from 1-match-left or 1-match-right, the match became 2-match-left-right

		// process value features 
		IModelAdapter thisMa = getThisModelAdapter(match);
		IModelAdapter oppositeMa = getOppositeModelAdapter(match);
		FeatureProvider featureProvider = match.getCodeSyncAlgorithm().getFeatureProvider(this_);
		for (Object childFeature : featureProvider.getFeatures(this_)) {
			switch (featureProvider.getFeatureType(childFeature)) {
			case IModelAdapter.FEATURE_TYPE_VALUE:
				Object value = thisMa.getValueFeatureValue(this_, childFeature, null);
				Object valueOpposite = oppositeMa.getValueFeatureValue(opposite, childFeature, null);
				if (!CodeSyncAlgorithm.safeEquals(value, valueOpposite)) {
					oppositeMa.setValueFeatureValue(opposite, childFeature, value);
					actionPerformed(match.getCodeSyncAlgorithm(), thisMa, this_, oppositeMa, opposite, childFeature, new ActionResult(false, true, true));
				}
				break;
			}
		}
		
		if (processDiffs) 
			processDiffs(match);
		
		match.setChildrenConflict(false);
				
		if (!match.getSubMatches().isEmpty()) {
			setChildrenModified(match);
			// process child match
			for (Match childMatch : match.getSubMatches())
				processMatch(match, childMatch, false);
		}
		
		ActionResult result = new ActionResult(false, true, true, thisMa.getMatchKey(this_), true);
		IModelAdapter thisParentMa = getThisModelAdapter(parentMatch);
		actionPerformed(match.getCodeSyncAlgorithm(), thisParentMa, getThis(parentMatch), oppositeParentMa, getOpposite(parentMatch), match.getFeature(), result);
	}
}