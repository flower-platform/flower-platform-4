define(function (require) {

   "use strict";

	var $ = require('jquery');
	var _ = require('underscore');
	var Backbone = require('backbone');
	
	var NodeFormView = require('views/NodeFormView');
	var UserFormViewTemplate = require('text!tpl/UserForm.html') 
	
	return NodeFormView.extend({
		
		tpl: UserFormViewTemplate,
		idEntity: "user",
		navigateOnSuccess: "users",
		
		create: false,
		
		events: {},
		
		render: function() {
			NodeFormView.prototype.render.apply(this);
			if (!this.create) {
				this.$el.find(".create-element").hide();
			}
		}
		
	});
	
});