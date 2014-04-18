define(["backbone", "underscore"], function(Backbone, _) {
	/**
	 * Generic view that knows to render itself.
	 * <p>It needs the template to apply on the model in the <code>tpl</code> variable
	 * </p>
	 */
	return Backbone.View.extend({
		
		initialize: function() {
			if (this.model) {
				this.listenTo(this.model, "change", this.render);
				this.listenTo(this.model, 'destroy', this.onModelRemoved);		
				if (this.model.collection) {
					this.listenTo(this.model.collection, 'reset', this.onModelRemoved);
				}
			}
		},
		
		render: function() {
			if (!this.template) {
				this.template = _.template(this.tpl);
			}
			if (this.model) {
				this.$el.html(this.template(this.model.toJSON()));
			} else {
				this.$el.html(this.template());
			}
			
			return this;
		},
		
		/** Model removed, so we remove the view also */
		onModelRemoved: function() {
			this.remove();
		},
		
		fetchModelAndShowDetails: function(id, collection, modelClass) {
			var model = collection.find(function(model) {
				return model.id == id;
			});
			
			if (model == null) {
				// not found in collection, create a new one
				model = new modelClass({id: id});
				model.collection = collection;
			}
			
			// now get the latest version from server
			var that = this;
			model.fetch({
				success: function(model, response, options) {
					that.model = model;
					that.render();
				},
				error: function(model, response, options) {
					that.showFetchModelError();
				}
			});
		},
		
		showFetchModelError: function() {
			window.App.showAlert({
				title: 'Fetch',
				content: 'Error during feching a model',
				type: 'error'
			});
		}
	});
	
});