var NodeRegistry = function() {

	return {
		
		listeners: [],
		
		addListener: function(listener) {
			this.listeners.push(listener);
		},
		
		registerNode: function(node) {
			return "Node " + this.toString(node) + " registered!";
		},
			
		setType: function (node, type) {
			node.type = type;
			for (var i in this.listeners) {
	  			this.listeners[i].nodeChanged(node);
			}
		},
		
		toString: function(node) {
			return "[" + node.type + ", " + node.nodeUri + "]";
		}
	}
};

var nodeRegistry = new NodeRegistry();

