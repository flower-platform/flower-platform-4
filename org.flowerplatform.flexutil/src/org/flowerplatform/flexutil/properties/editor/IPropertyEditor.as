package org.flowerplatform.flexutil.properties.editor {
	import org.flowerplatform.flexutil.properties.PropertyEntry;

	/**
	 * A property editor should always have prepared the value to commit, which may
	 * be called by the <code>PropertyEntryRenderer</code> (e.g. on focus out).
	 * 
	 * <p>
	 * The component may dispatch an event: <code>FlexUtilConstants.EVENT_COMMIT_PROPERTY</code>
	 * (e.g. on click enter).
	 * 
	 * @author Cristian Spiescu
	 */
	public interface IPropertyEditor {
		function get valueToCommit():Object;
		function set propertyEntry(entry:PropertyEntry):void;
	}
}