package org.flowerplatform.codesync.regex.remote;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.codesync.regex.CodeSyncRegexPlugin;
import org.flowerplatform.util.Pair;
import org.flowerplatform.util.regex.RegexAction;

public class CodeSyncRegexService {

	public List<Pair<String, String>> getRegexActions() {	
		List<Pair<String, String>> list = new ArrayList<Pair<String, String>>();
		
		for (RegexAction regexAction : CodeSyncRegexPlugin.getInstance().getActions().values()) {
			list.add(new Pair<String, String>(regexAction.getName(), regexAction.getDescription()));
		}		
		return list;
	}
	
}
