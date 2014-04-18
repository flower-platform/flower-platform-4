define(["underscore",
        "backbone",
        "basejs/app/views/View",
        "text!basejs/app/components/stepper/tpl/NumericStepper.html"], 
 function(_,
		 Backbone,
		 View,
		 NumericStepperTpl) {
 
	return View.extend({	
 
		tpl: NumericStepperTpl,
		
		initialize: function(options) {		
			this.options = options || {}; // see http://api.jqueryui.com/spinner/
			View.prototype.initialize.apply(this);
		},
		
		render: function() {				
			if (!this.template) {
				this.template = _.template(this.tpl);
			}
			this.$el.html(this.template());
						
			if (!this.spinner) {
				this.spinner = this.$el.find('#spinner').spinner();
			}
			return this;
		},
		
		getValue: function() {
			return this.spinner.spinner("value");
		}
	});
	
});