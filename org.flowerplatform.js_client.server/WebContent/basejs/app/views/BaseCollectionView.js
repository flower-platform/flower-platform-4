define(["backbone", 
        "underscore",
        "basejs/app/views/View"], 
function(Backbone, 
		 _, 
		 View) {
	/**
	 * A view that is able to display a collection. When the constructor of this
	 * CollectionView receives the collection it registers listeners on the
	 * collection to handle common cases when the collection changes. As a
	 * result after calling collection.fetch, this View knows it has to
	 * re-render itself.
	 * 
	 * Example:
	 * 
	 * <pre>
	 * var view = new MyCollectionView({
	 * 	collection : new MyCollection(),
	 * 	el : $content
	 * });
	 * var options = {
	 * 	contentType : 'application/json',
	 * 	type : 'POST',
	 * 	data : JSON.stringify(someDataForServer),
	 * 	url : view.collection.url,
	 * 	reset : true
	 * };
	 * view.collection.fetch(options);
	 * </pre>
	 * 
	 * There is no need to specify error and success handles in options.
	 * 
	 */
	return View.extend({
		
		initialize: function() {
			View.prototype.initialize.apply(this); 
			if (this.collection) {
				this.basedOnBackboneCollection = this.collection instanceof Backbone.Collection;
				if (this.basedOnBackboneCollection) {
					this.listenTo(this.collection, "sync", this.render);
					this.listenTo(this.collection, "error", this.onError);
				}
			}
		},
		
		render: function(){
			if (!this.template) {
				this.template = _.template(this.tpl);
			}
			
			if (this.basedOnBackboneCollection) {
				this.$el.html(this.template({models:this.collection.models, labelField:this.labelField}));
			} else {
				this.$el.html(this.template({models:this.collection, labelField:this.labelField}));
			}
			
		},
	
		onError: function(modelOrCollection, response, options) {
			window.App.showAlert({
				title: 'Error on server call.',
				content: 'An error occured while calling the server: ' + response.statusText, 
				type: 'error'
			});
			this.render(); 
		}
		
	});
	
});