define(["backbone", "models/Node"], function(Backbone, Node) {

	return Node.extend({

		defaults: function() {
			return	{
				type: "user",
				resource: null,
				idWithinResource: "",
				properties: {
					login: "",
					name: "",
					email: ""
				}
			};
		},
		
		/**
		 * Override the default url.
		 */
		methodToURL: {
			'read': '../servlet/rest/userService/getUser/:id',
			'create': '../servlet/rest/userService/createUser',
			'update': '../servlet/rest/userService/saveUser',
			'delete': '../servlet/rest/userService/deleteUser/:id'
		},

		sync: function(method, model, options) {
			options = options || {};
			options.url = model.methodToURL[method.toLowerCase()].replace(":id", model.id);

			return Backbone.sync.apply(this, arguments);
		}
		
		
	});
	
});