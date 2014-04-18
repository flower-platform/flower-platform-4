define(["backbone"], function(Backbone) {

	return Backbone.Model.extend({
		
		idAttribute: "fullNodeId",
		
		/**
		 * The node is wrapped in a map if it was returned from the server directly
		 * (i.e. not part of a list or map), so we get it from messageResult.
		 */
		parse: function(resp) {
			if (resp.hasOwnProperty("messageResult")) {
				return resp.messageResult;
			}
			return resp;
		}
		
	});
	
});