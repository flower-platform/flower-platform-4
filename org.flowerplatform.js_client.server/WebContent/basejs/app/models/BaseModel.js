define(["backbone"], 
 function(Backbone) {

	/**
	 * Standard Backbone model, which has a validateField function to help in the validation process.
	 */
	
	return Backbone.Model.extend({
		/**
		 * Tests value according to the rules from validations and populates errors accordingly.
		 * 
		 * Also, if there is an extraValidation function on the current object, will be called with the 
		 * same arguments as the current function
		 * 
		 * @param value the value to check
		 * @param field the name of the field
		 * @param fieldName the description of the field
		 * @param validation object containing 3 types of validations (their
		 * presence triggers that kind of validation: mandatory, minChars, maxChars)
		 * @param error where the error should be put. 
		 */
		validateField: function(value, field, fieldName, validations, errors) {
			if (validations.mandatory && (value == "" || value.length == 0)) {
				errors.push({
					fieldName: field,
					fieldError: window.App.translateMessage("app.validation.mandatory", fieldName)
				});
				return
			}
			
			if (validations.minChars && value.length < validations.minChars) {
				errors.push({
					fieldName: field,
					fieldError: window.App.translateMessage("app.validation.minChars", fieldName, validations.minChars)
				});
				return;
			}
			
			if (validations.maxChars && value.length > validations.maxChars) {
				errors.push({
					fieldName: field,
					fieldError: window.App.translateMessage("app.validation.maxChars", fieldName, validations.maxChars)
				});
				return;
			}
			
			if (validations.regExp && !validations.regExp.test(value)) {
				errors.push({
					fieldName: field,
					fieldError: (validations.regExpMessage ? fieldName + ": " + validations.regExpMessage : window.App.translateMessage("app.validation.invalidFormat", fieldName))
				});
				return;
			}
			
			if (this.extraValidation && _.isFunction(this.extraValidation)) {
				this.extraValidation(value, field, fieldName, validations, errors);
			}
		},
		
		/**
		 * We need to extract warnings, if any and update them in the interface
		 */
		parse: function(resp, options) {
			this.warningMessage = null;
			if (_.isArray(resp) && resp.length > 1 && resp[1].metaType == "additional") {
				this.warningMessage = resp[1].message;
				return resp[0];
			}
			return resp;
		}
	});
	
});