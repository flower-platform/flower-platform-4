package org.flowerplatform.js_client.java;

import org.flowerplatform.core.node.remote.Node;

public class JsNode extends Node {

	public JsList<JsNode> children;
	
	public String nodeUri() {
		return getNodeUri();
	};
	
}
