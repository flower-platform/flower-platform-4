define(["backbone", "models/Node"], function(Backbone, Node) {

	return Backbone.Collection.extend({
		
		parse: function(resp) {
			return resp.messageResult;
		},
		
		/**
		 * Set the id attribute that will be used in views.
		 */
		model: function(attrs, options) {
			var node = new Node(attrs, options);
			node.attributes.id = node.attributes.fullNodeId;
			return node;
		}
	
	});
	
});