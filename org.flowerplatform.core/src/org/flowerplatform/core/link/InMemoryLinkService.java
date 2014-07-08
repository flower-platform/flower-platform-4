package org.flowerplatform.core.link;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.node.remote.Node;

public class InMemoryLinkService extends LinkService {
	
	private Map<String, Map<String, List<String>>> reverseLinks = new HashMap<>();

	@Override
	public List<String> getReverseLinks(Node node) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addReverseLink(Node node, String linkUri) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeReverseLinks(String resourceUri) {
		// TODO Auto-generated method stub
		
	}
	
}
