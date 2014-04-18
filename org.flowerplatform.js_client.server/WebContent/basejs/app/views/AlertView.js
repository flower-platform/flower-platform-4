define(["underscore",
        "backbone",
        "basejs/app/views/View",
        "text!basejs/tpl/AlertTpl.html"], 
 function(_,
		 Backbone,
		 View,
		 AlertTpl) {

	return View.extend({
		
		tpl: AlertTpl,
		
		/**
		 * Options:
		 * 	title,
		 * 	content
		 * 	dismissable: true/false
		 * 	type: info/success/warning/danger
		 */
		initialize: function(options) {
			if (!this.model) {
				this.model = new Backbone.Model();
			}
			
			View.prototype.initialize.apply(this);

			this.model.set({
				'title': options.title || '',	
				'content': options.content,
				'dismissable': (_.isUndefined(options.dismissable) ? true : options.dismissable),
				'type': options.type || 'info'
			});
		},
	});
	
});