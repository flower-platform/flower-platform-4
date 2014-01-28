package org.flowerplatform.core.mindmap;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class MindMapNodeDAO {
	
	// TODO CC: temporary code
	public static Map<String, MapModel> maps = new HashMap<String, MapModel>();
	
	// TODO CC: temporary code
	private static final String TEST_PATH = "D:/temp/FAP-FlowerPlatform4.mm";
	
	static {
		// configure freeplane starter
		new FreeplaneHeadlessStarter().createController().setMapViewManager(new HeadlessMapViewController());		
		HeadlessMModeControllerFactory.createModeController();	
	}
	
	public MindMapNodeDAO() {		
	}
			
	// TODO CC: temporary code
	private URL getTestingURL() {
		try {
			return new File(TEST_PATH).toURI().toURL();
		} catch (MalformedURLException e) {
			return null;
		}
	}
	
	private NodeModel getNodeModel(String nodeId) {		
		if (nodeId == null) {
			if (!maps.containsKey(getTestingURL().toString())) {
				try {
					load(null);
				} catch (Exception e) {	
					// TODO CC: To log
					e.printStackTrace();
				}
			}
			return maps.get(getTestingURL().toString()).getRootNode();
		} 
		return maps.get(getTestingURL().toString()).getNodeForID(nodeId);		
	}
	
	public Node convert(NodeModel nodeModel) {
		Node node = new Node();
		node.setId(nodeModel.createID());
		node.setBody(nodeModel.getText());
		node.setHasChildren(nodeModel.hasChildren());
		node.addProperty("type", "mindMapNode");
		node.addProperty("id", node.getId());
		if (CloudModel.getModel(nodeModel) != null) {
			node.addProperty("could_shape", CloudModel.getModel(nodeModel).getShape().name());
			node.addProperty("could_color", CloudModel.getModel(nodeModel).getColor().toString());
		}
		return node;
	}
	
	public Node getNode(String nodeId) {		
		return convert(getNodeModel(nodeId));
	}
		
	public List<Object> getChildren(String nodeId) {		
		NodeModel nodeModel = getNodeModel(nodeId);
		if (nodeId == null) {
			return Arrays.asList(nodeId, Collections.singletonList(convert(nodeModel)));
		}
		List<Node> children = new ArrayList<Node>();		
		for (NodeModel child : nodeModel.getChildren()) {
			children.add(convert(child));
		}
			
		return Arrays.asList(nodeId, children);
	}
	
	public void setBody(String nodeId, String newBodyValue) {
		NodeModel nodeModel = getNodeModel(nodeId);
		nodeModel.setText(newBodyValue);
	}
	
	public Node addNode(String parentNodeId, String type) {
		NodeModel parent = getNodeModel(parentNodeId);
		NodeModel newNode = new NodeModel("", parent.getMap());
		newNode.setLeft(false);
		
		parent.insert(newNode, parent.getChildCount());
		
		return convert(newNode);
	}
	
	public void removeNode(String nodeId) {
		NodeModel nodeModel = getNodeModel(nodeId);
		nodeModel.removeFromParent();
	}	
	
	public void moveNode(String nodeId, String newParentId, int newIndex) {
		NodeModel nodeModel = getNodeModel(nodeId);
		NodeModel newParentModel = getNodeModel(newParentId);
		
		if (newIndex != -1) {
			if (newIndex == -2) {
				newIndex = newParentModel.getChildCount();
			}
			NodeModel oldParent = nodeModel.getParentNode();
			oldParent.remove(nodeModel);
						
			if (oldParent.equals(newParentModel) && newIndex > oldParent.getChildCount()) {
				newIndex--;
			}	
			newParentModel.insert(nodeModel, newIndex);
		}	
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
	public void save() {
		MapModel newModel = maps.get(getTestingURL().toString());
		try {
			((MFileManager) UrlManager.getController()).writeToFile(newModel, newModel.getFile());
		} catch (IOException e) {
			// TODO CC: To log
			e.printStackTrace();
		}
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
		
	public void setProperty(String nodeId, String propertyName, String propertyValue) {
		// TODO CC: temporary code (testing properties view)
		NodeModel nodeModel = getNodeModel(nodeId);
		if (propertyName.equals("could_shape")) {
			CloudModel cloud = CloudModel.createModel(nodeModel);
			switch (propertyValue) {
				case "ARC": 
					cloud.setShape(Shape.ARC);
					break;
				case "RECT": 
					cloud.setShape(Shape.RECT);
					break;
				case "STAR": 
					cloud.setShape(Shape.STAR);
					break;
			}
		}
	}
	
}
