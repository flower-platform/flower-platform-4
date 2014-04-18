define(["jquery", 
        "backbone"], 
 function($, 	   
		 Backbone) {
	
	// Create a Router class that knows how to handle our behaviour, based
	// on our views
	return Backbone.Router.extend({
		
		routes: {
		},

		showWarning: function(message) {
			window.App.showAlert({
				title: 'Warning',
				content: message,
				type: 'warning'
			});
		},
		
		showFetchListError: function() {
			window.App.showAlert({
				title: 'Fetch', 
				content: 'Error during fetch list data',
				type: 'error'
			});
		},
		
		showDeleteError: function() {
            window.App.showAlert({
            	title: 'Delete',
            	content: 'Error deleting a model',
            	type: 'error'
            });
		},
		
		/**
		 * Gets data from the server for the collection (a Backbone.Collection object),
		 * sets the collection on the view and renders the view.
		 */
		fetchAndRenderList: function(collection, view) {
			var that = this;
			collection.fetch({reset: true,
				success: function(collection, response, options){
					view.collection = collection;
					view.collection.totalCount = options.xhr.getResponseHeader("X-Total-Count");
					view.render();
					
					if (window.App && window.App.warningMessage) {
						that.showWarning(window.App.warningMessage);
						
						window.App.warningMessage = "";
					}
				},
				error: function(collection, response, options){
					view.collection = collection;
					view.collection.reset();
					view.collection.totalCount = 0;
					view.render();
					that.showFetchListError();
				}
			});
		},
		
		removeEntity: function(id, collection, onSuccessNavigateTo, onErrorNavigateTo) {
		    var model = collection.find(function(model){
		       return model.id == id; 
		    });
		    
		    var that = this;
		    if (model != null) {
		        model.destroy({wait:true,
		            success: function(model, response, options){
		                that.navigate(onSuccessNavigateTo, {trigger: true, replace: true});
		            },
		            error: function(model, xhr, options) {
		            	that.showDeleteError();
		                // even on error navigate to another part
		                if (onErrorNavigateTo) {
		                    that.navigate(onErrorNavigateTo, {trigger: true, replace: true});
		                } else {
		                    that.navigate(onSuccessNavigateTo, {trigger: true, replace: true});
		                }
		            }
		        });
		    }
		}
	});
});
