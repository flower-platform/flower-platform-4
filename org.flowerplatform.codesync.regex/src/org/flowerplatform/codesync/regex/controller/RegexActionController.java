package org.flowerplatform.codesync.regex.controller;

import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_ATTACH_SPECIFIC_INFO;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_ATTACH_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_ATTACH_SPECIFIC_INFO_KEY_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CHECK_STATE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CLEAR_SPECIFIC_INFO;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CLEAR_SPECIFIC_INFO_KEY_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CREATE_NODE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CREATE_NODE_NEW_NODE_TYPE;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_CREATE_NODE_PROPERTIES;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_KEEP_SPECIFIC_INFO;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_LIST_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_KEEP_SPECIFIC_INFO_KEY_PROPERTY;
import static org.flowerplatform.codesync.regex.CodeSyncRegexConstants.ACTION_TYPE_VALID_STATES_PROPERTY;

import java.util.Map;

import org.flowerplatform.core.CoreConstants;
import org.flowerplatform.core.node.NodeService;
import org.flowerplatform.core.node.controller.IPropertiesProvider;
import org.flowerplatform.core.node.controller.IPropertySetter;
import org.flowerplatform.core.node.remote.Node;
import org.flowerplatform.core.node.remote.ServiceContext;
import org.flowerplatform.util.controller.AbstractController;

/**
 * @author Elena Posea
 */
public class RegexActionController extends AbstractController implements IPropertiesProvider, IPropertySetter {

	/**
	 * @author Elena Posea
	 */
	public RegexActionController() {
		super();
		// invoked after the persistence providers
		setOrderIndex(10000);
	}

	@Override
	public void setProperties(Node node, Map<String, Object> properties, ServiceContext<NodeService> context) {
	}

	@Override
	public void unsetProperty(Node node, String property, ServiceContext<NodeService> context) {
		// not used
	}

	@Override
	public void populateWithProperties(Node node, ServiceContext<NodeService> context) {
		// TODO workaround for a client bug that hides properties (even with descriptor!) from the properties view when there is no value
		node.getProperties().put(CoreConstants.NAME, node.getType());
		if (node.getType().equals(ACTION_TYPE_CREATE_NODE)) {
			if (node.getProperties().get(ACTION_TYPE_CREATE_NODE_PROPERTIES) == null) {
				node.getProperties().put(ACTION_TYPE_CREATE_NODE_PROPERTIES, "");
			}
			if (node.getProperties().get(ACTION_TYPE_CREATE_NODE_NEW_NODE_TYPE) == null) {
				node.getProperties().put(ACTION_TYPE_CREATE_NODE_NEW_NODE_TYPE, "");
			}
		} else {
			if (node.getType().equals(ACTION_TYPE_KEEP_SPECIFIC_INFO)) {

				if (node.getProperties().get(ACTION_TYPE_KEEP_SPECIFIC_INFO_KEY_PROPERTY) == null) {
					node.getProperties().put(ACTION_TYPE_KEEP_SPECIFIC_INFO_KEY_PROPERTY, "");
				}
				if (node.getProperties().get(ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_LIST_PROPERTY) == null) {
					node.getProperties().put(ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_LIST_PROPERTY, false);
				}
				if (node.getProperties().get(ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY) == null) {		
					node.getProperties().put(ACTION_TYPE_KEEP_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY, false);
				}
			} else {
				if (node.getType().equals(ACTION_TYPE_ATTACH_SPECIFIC_INFO)) {
					if (node.getProperties().get(ACTION_TYPE_ATTACH_SPECIFIC_INFO_KEY_PROPERTY) == null) {		
						node.getProperties().put(ACTION_TYPE_ATTACH_SPECIFIC_INFO_KEY_PROPERTY, "");
					}
					if (node.getProperties().get(ACTION_TYPE_ATTACH_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY) == null) {		
						node.getProperties().put(ACTION_TYPE_ATTACH_SPECIFIC_INFO_IS_CONTAINMENT_PROPERTY, false);
					}
				} else {
					if (node.getType().equals(ACTION_TYPE_CLEAR_SPECIFIC_INFO)) {
						if (node.getProperties().get(ACTION_TYPE_CLEAR_SPECIFIC_INFO_KEY_PROPERTY) == null) {		
							node.getProperties().put(ACTION_TYPE_CLEAR_SPECIFIC_INFO_KEY_PROPERTY, "");
						}
					} else {
						if (node.getType().equals(ACTION_TYPE_CHECK_STATE)) {
							if (node.getProperties().get(ACTION_TYPE_VALID_STATES_PROPERTY) == null) {		
								node.getProperties().put(ACTION_TYPE_VALID_STATES_PROPERTY, "");
						}
						}
					}
				}
			}
		}
	}

}
