package org.flowerplatform.core.mindmap;

import java.awt.Color;
import java.io.File;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowerplatform.core.mindmap.remote.MindMapService;
import org.flowerplatform.core.mindmap.remote.Node;
import org.flowerplatform.core.mindmap.remote.Property;
import org.freeplane.features.cloud.CloudModel;
import org.freeplane.features.cloud.CloudModel.Shape;
import org.freeplane.features.map.MapModel;
import org.freeplane.features.map.MapWriter.Mode;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;
import org.freeplane.features.url.UrlManager;
import org.freeplane.features.url.mindmapmode.MFileManager;
import org.freeplane.main.headlessmode.FreeplaneHeadlessStarter;
import org.freeplane.main.headlessmode.HeadlessMModeControllerFactory;

/**
 * @author Cristina Constantinescu
 */
public class MindMapNodeDAO implements INodeDAO {
	
	// TODO CC: temporary code
	private static Map<String, MapModel> maps = new HashMap<String, MapModel>();
	
	// TODO CC: temporary code
	private static final String TEST_PATH = "D:/temp/FAP-FlowerPlatform4.mm";
	
	static {
		// configure FreePlane starter
		new FreeplaneHeadlessStarter().createController().setMapViewManager(new HeadlessMapViewController());		
		HeadlessMModeControllerFactory.createModeController();	
	}
	
	// TODO CC: temporary code
	private URL getTestingURL() {
		try {
			return new File(TEST_PATH).toURI().toURL();
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	public Node getNodeFromId(String nodeId) {
		return convertObject(getNodeModelFromId(nodeId));
	}
	
	private NodeModel getNodeModelFromId(String nodeId) {
		if (nodeId == null) {			
			return maps.get(getTestingURL().toString()).getRootNode();
		}
		return maps.get(getTestingURL().toString()).getNodeForID(nodeId);
	}
	
	private Node convertObject(NodeModel obj) {
		NodeModel nodeModel = (NodeModel) obj;
		Node node = new Node();
		node.setId(nodeModel.createID());
		node.addProperty("body", nodeModel.getText());
		node.addProperty("hasChildren", nodeModel.hasChildren());		
		node.addProperty("type", "mindMapNode");
		node.addProperty("id", node.getId());
		node.addProperty("timestamp", nodeModel.getHistoryInformation().getLastModifiedAt().getTime());
		if (CloudModel.getModel(nodeModel) != null) {
			node.addProperty("could_shape", CloudModel.getModel(nodeModel).getShape().name());
			node.addProperty("could_color", CloudModel.getModel(nodeModel).getColor().toString());
		}
		return node;
	}
	
	public int getNodeChildCount(Node node) {
		NodeModel nodeModel = getNodeModelFromId(node.getId());		
		return nodeModel.getChildCount();
	}
	
	public Node getParentNode(Node node) {
		NodeModel nodeModel = getNodeModelFromId(node.getId());		
		return convertObject(nodeModel.getParentNode());
	}
	
	public int getNodeChildPosition(Node parentNode, Node childNode) {
		NodeModel parent = getNodeModelFromId(parentNode.getId());	
		NodeModel child = getNodeModelFromId(childNode.getId());	
		
		return parent.getChildPosition(child);
	}
	
	public long updateNodeTimestamp(Node node, boolean addOneMsIfSameAsLastTimestamp) {
		NodeModel nodeModel = getNodeModelFromId(node.getId());
		
		Date currentDate = new Date();
		if (addOneMsIfSameAsLastTimestamp) {
			currentDate = new Date(currentDate.getTime() + 1);
		}
		nodeModel.getHistoryInformation().setLastModifiedAt(currentDate);
		
		return nodeModel.getHistoryInformation().getLastModifiedAt().getTime();
	}
		
	public List<Node> getChildrenForNodeId(String nodeId) {
		List<Node> children = new ArrayList<Node>();
		if (nodeId == null) {			
			children.add(convertObject(getNodeModelFromId(null)));
		} else {		
			NodeModel nodeModel = getNodeModelFromId(nodeId);
			for (NodeModel child : nodeModel.getChildren()) {
				children.add(convertObject(child));
			}
		}
		return children;		
	}
			
	public Node addNode(Node parentNode, String type) {		
		NodeModel parentModel = getNodeModelFromId(parentNode.getId());
		NodeModel newNodeModel = new NodeModel("", parentModel.getMap());
		newNodeModel.setLeft(false);
		
		parentModel.insert(newNodeModel, parentModel.getChildCount());

		return convertObject(newNodeModel);
	}
	
	public void insertNode(Node parentNode, Node newNode, int index) {		
		NodeModel parent = getNodeModelFromId(parentNode.getId());
		NodeModel newChild = getNodeModelFromId(newNode.getId());
				
		parent.insert(newChild, index);
	}
	
	public void removeNode(Node node) {
		NodeModel nodeModel = getNodeModelFromId(node.getId());
		nodeModel.removeFromParent();
	}	
	
	public void load(URL url) throws Exception {
		url = getTestingURL();
		
		InputStreamReader urlStreamReader = null;
		try {
			urlStreamReader = new InputStreamReader(url.openStream());
			
			MapModel newModel = new MapModel();			
			newModel.setURL(url);
				
			Controller.getCurrentModeController().getMapController().getMapReader().createNodeTreeFromXml(newModel, urlStreamReader, Mode.FILE);		
			maps.put(newModel.getURL().toString(), newModel);
		} finally {
			if (urlStreamReader != null) {
				urlStreamReader.close();
			}
		}
	}
	
	@SuppressWarnings("deprecation")
	public void save() throws Exception {
		MapModel newModel = maps.get(getTestingURL().toString());
		((MFileManager) UrlManager.getController()).writeToFile(newModel, newModel.getFile());		
	}
	
	public List<Property> getPropertiesData(String nodeType) {
		// TODO CC: temporary code (testing properties view)
		List<Property> properties = new ArrayList<Property>();
		properties.add(new Property().setNameAs("type").setReadOnlyAs(true));		
		properties.add(new Property().setNameAs("id").setReadOnlyAs(true));	
		properties.add(new Property().setNameAs("could_shape").setReadOnlyAs(false));	
		properties.add(new Property().setNameAs("could_color"));		
		return properties;	
	}
		
	public void setProperty(Node node, String propertyName, Object propertyValue) {		
		// TODO CC: temporary code (testing properties view)		
		NodeModel nodeModel = getNodeModelFromId(node.getId());
		switch (propertyName) {
			case "body":
				nodeModel.setText((String) propertyValue);
				break;
			case "could_shape":
				CloudModel cloud = CloudModel.createModel(nodeModel);
				switch ((String) propertyValue) {
					case "ARC": 
						cloud.setShape(Shape.ARC);
						new MindMapService().setProperty(node.getId(), "could_color", "green");
						break;
					case "RECT": 
						cloud.setShape(Shape.RECT);
						new MindMapService().setProperty(node.getId(), "could_color", "red");
						break;
					case "STAR": 
						cloud.setShape(Shape.STAR);
						new MindMapService().setProperty(node.getId(), "could_color", "black");
						break;
				}
				break;
			case "could_color":
				CloudModel cloud1 = CloudModel.createModel(nodeModel);
				switch ((String) propertyValue) {
					case "green": 
						cloud1.setColor(Color.GREEN);						
						break;
					case "red": 
						cloud1.setColor(Color.RED);											
						break;
					case "black": 
						cloud1.setColor(Color.BLACK);											
						break;
				}
				break;
				
		}
	}

}
