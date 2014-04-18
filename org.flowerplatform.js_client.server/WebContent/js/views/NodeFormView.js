define(["backbone", "basejs/app/views/FormView"], function(Backbone, FormView) {
	
	return FormView.extend({
		
		getDataFromForm: function() {
			var formData = FormView.prototype.getDataFromForm.apply(this);
			var result = { properties: {} };
			_.each(formData, function(value, key) {
				result.properties[key] = value;
			});
			return result;
		}
		
	});
	
});