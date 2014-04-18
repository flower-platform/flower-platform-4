define(["backbone", "models/NodeCollection", "models/User"], function(Backbone, NodeCollection, User) {

	return NodeCollection.extend({
		
		url: "../servlet/rest/userService/getUsers",
		
		model: function(attrs, options) {
			var node = new User(attrs, options);
			node.attributes.id = node.attributes.fullNodeId;
			return node;
		}
		
	});
	
});