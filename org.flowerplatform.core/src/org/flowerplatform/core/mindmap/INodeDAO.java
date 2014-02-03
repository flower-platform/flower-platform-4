package org.flowerplatform.core.mindmap;

import java.net.URL;
import java.util.List;

import org.flowerplatform.core.mindmap.remote.Node;
import org.flowerplatform.core.mindmap.remote.Property;

public interface INodeDAO {

	Node getNodeFromId(String nodeId);
	
	Node getParentNode(Node node);
	
	Node addNode(Node parentNode, String type);
	void insertNode(Node parentNode, Node newNode, int index);	
	void removeNode(Node node);
	
	List<Node> getChildrenForNodeId(String nodeId);
	int getNodeChildCount(Node node);
	int getNodeChildPosition(Node parentNode, Node childNode);
	
	long updateNodeTimestamp(Node node, boolean addOneMsIfSameAsLastTimestamp);
	
	List<Property> getPropertiesData(String nodeType);
	void setProperty(Node node, String propertyName, Object propertyValue);
	
	void load(URL url) throws Exception;
	void save() throws Exception;
	
}
