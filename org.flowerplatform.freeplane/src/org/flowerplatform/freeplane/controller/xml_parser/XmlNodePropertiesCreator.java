package org.flowerplatform.freeplane.controller.xml_parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.freeplane.FreeplaneConstants;

public class XmlNodePropertiesCreator {

	private Node node;
	
	public XmlNodePropertiesCreator(Node node) {
		this.node = node;
	}
	
	@SuppressWarnings("unchecked")
	public StringBuffer createXmlFromNodeProperties() {
		StringBuffer xmlContent = new StringBuffer();
		
		Map<String, Object> nodeProperties = new TreeMap<String, Object>(node.getProperties());
		System.out.println(nodeProperties);
		
		
		// Write the node tag with its attributes
		xmlContent.append("<" + FreeplaneConstants.NODE);
		for(Iterator<Map.Entry<String, Object>> iterator = nodeProperties.entrySet().iterator(); iterator.hasNext(); ) {
		      Map.Entry<String, Object> entry = iterator.next();
		      String property = entry.getKey();
			  Object value = entry.getValue();
		      if (property.split("[.(]").length == 1 && !property.equals(FreeplaneConstants.ICONS)) {
			    	xmlContent.append(" " + property + "='" + value + "'");
			    	iterator.remove();
		      }
		}	
		xmlContent.append(">" + "\n");
		
		
		Iterator<Map.Entry<String, Object>> iterator = nodeProperties.entrySet().iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Object> entry = iterator.next();
			String property = entry.getKey();
		    Object value = entry.getValue();
		    
		    if (property.equals(FreeplaneConstants.ICONS)) {
		    	for (String icon : (ArrayList<String>) value) {
					xmlContent.append("\t<" + FreeplaneConstants.ICON + " " + FreeplaneConstants.ICON_KEY_PROPERTY + "=" + icon + "/>\n");
				}
		    } else if (property.equals(FreeplaneConstants.UNKNOWN)) {
		    	xmlContent.append(value);
		    } else {
		    	String[] propertySplit = property.split("(");
		    	if (propertySplit.length == 1) {
		    		propertySplit = property.split(".");
		    		
		    	}
		    }
		}
		
		
//		for(Entry<String, Object> entry : nodeProperties.entrySet()) {
//		    String property = entry.getKey();
//		    Object value = entry.getValue();
//		    
//		    if (property.equals(FreeplaneConstants.ICONS)) {
//		    	for (String icon : (ArrayList<String>) value) {
//					xmlContent.append("\t<" + FreeplaneConstants.ICON + " " + FreeplaneConstants.ICON_KEY_PROPERTY + "=" + icon + "/>\n");
//				}
//		    } else if (property.equals(FreeplaneConstants.UNKNOWN)) {
//		    	xmlContent.append(value);
//		    } else {
//		    	
//		    	
//		    	
//		    	
//		    	if (property.equals(FreeplaneConstants.HOOK) || 
//		    }
//		    			property.equals(FreeplaneConstants.RICHCONTENT) || 
//		    			property.equals(FreeplaneConstants.ATTRIBUTE)) {
//		    	if (nodeProperties.containsKey(key))
//		    	
//		    }
		    
			
//		}
		
		xmlContent.append("</" + FreeplaneConstants.NODE + ">");
		
		//System.out.println(xmlContent);
		
		
		
		return xmlContent;
	}
	
}
