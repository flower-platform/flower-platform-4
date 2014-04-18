define(["underscore", "basejs/app/views/View", "backbone"], function(_, View, Backbone){
	/**
	 * View that handles viewing/editing/saving a model based on a template.
	 * <p>It needs:
	 * <ul>
	 * 	<li>tpl - the template on which it operates</li>
	 *  <li>idEntitiy - to diferentiate the buttons</li>
	 *  <li>navigateOnSuccess - where to navigate when the save to server succeeds</li>
	 * </ul
	 * </p>
	 */
	return View.extend({
		
		isEdit: true,
		
		initialize: function() {
			this.events["click #save-" + this.idEntity] = "saveEntity";
			this.events["click #cancel-"  + this.idEntity] = "cancelEditing";
		},
		
		events: {},
		
		saveEntity: function() {
			// get the data
            var formData = this.getDataFromForm();
            
            this.postProcessFormData(formData);
            
            var that = this;
            
            var result = this.model.save(formData, {wait: true,
            	success: function(model, response, options) {
            		if (model.warningMessage) {
            			window.App.warningMessage = model.warningMessage;
            		}
            		if (window.App 
            				&& window.App.customData 
            				&& window.App.customData.navigateAfterEdit) {
            			var navigate = window.App.customData.navigateAfterEdit;
            			window.App.customData.navigateAfterEdit = null;
            			Backbone.history.navigate(navigate, {trigger: true, replace: true});
            		} else if (that.navigateOnSuccess) {
            			Backbone.history.navigate(that.navigateOnSuccess, {trigger: true, replace: true});
            		}
            	},
            	error: function(model, response, options) {
            		that.onError(model, response, options);
            	}
            });
            
            if (result == false && this.model.validationError != null) {
            	// show the validation
            	// first reset the old validation
            	this.parseValidationErrors(this.model.validationError);
			}
		},
		
		getDataFromForm: function() {
			// get the data
            var formData = {};
            
            this.$el.find("input").each(function(i, el){
            	var $el = $(el);
            	if (el.id !== "") {
	            	if ($el.prop("type") == "checkbox") {
	            		formData[el.id] = $el.prop("checked");
	            	} else if($el.prop("type") == "radio") {
	            		if ($el.prop("checked")) {
	            			formData[$el.prop("name")] = $el.val();
	            		}
	            	} else {
	            		formData[el.id] = $el.val();
	            	}
            	}
            });
            
            // also add the select ones
            this.$el.find("select").each(function(i, el){
            	var $el = $(el);
            	if (el.id !== "") {
            		formData[el.id] = $el.val();
            	}
            });
			
			
            return formData;
		},
		
		postProcessFormData: function(formData) {
			if (formData && _.isObject(formData)) {
				_.each(formData, function(value, key) {
					if (value && _.isString(value)) {
						if (value !== "true" && value !== "false") {
							formData[key] = formData[key].toUpperCase();
						}
					}
				}, this);
			}
		},
		
		parseValidationErrors: function(errors) {
        	// show the validation
        	// first reset the old validation
        	this.$el.find("div.has-error").removeClass("has-error");
        	this.$el.find("input").parent().removeClass("has-error");
        	this.$el.find("label.has-error").removeClass("has-error");
        	
        	var errorMessage = '<ul>';
        	if (_.isArray(errors)) {
            	_.each(errors, function(error) {
            		if (_.isArray(error.fieldName)) {
            			for (var i = 0, l = error.fieldName.length; i < l; i++) {
    	            		this.$el.find("#" + error.fieldName[i]).parent().addClass("has-error");
    	            		this.$el.find('label[for="' + error.fieldName[i] + '"]').addClass("has-error");
            			}
            		}
            		else {
	            		this.$el.find("#" + error.fieldName).parent().addClass("has-error");
	            		this.$el.find('label[for="' + error.fieldName + '"]').addClass("has-error");
            		}
            		if (error.fieldError) {
            			errorMessage += "<li>" + error.fieldError + "</li>";
            		}
            	}, this);
        	} else {
	        	for (key in errors) {
	        		this.$el.find("#" + key).parent().addClass("has-error");
	        		this.$el.find('label[for="' + key + '"]').addClass("has-error");
	        		
	        		if (errors[key][0]) {
	        			errorMessage += "<li>" + errors[key][0] + "</li>";
	        		}
	        	}
        	}
        	errorMessage += "</ul>";
        	window.App.showAlert({
        		title: window.App.translateMessage("app.contact.pleaseFix"),
        		content: errorMessage,
        		type: "error",
        		dismissable: false
        	});	        	
        },
		
		cancelEditing: function() {
    		if (window.App 
    				&& window.App.customData 
    				&& window.App.customData.navigateAfterEdit) {
    			var navigate = window.App.customData.navigateAfterEdit;
    			window.App.customData.navigateAfterEdit = null;
    			Backbone.history.navigate(navigate, {trigger: true, replace: true});
    		} else if (this.navigateOnSuccess) {
    			Backbone.history.navigate(this.navigateOnSuccess, {trigger: true, replace: true});
    		}
		},
		
		onError: function(modelOrCollection, response, options) {
       		if (response.status == 400 
    				&& response.responseJSON
    				&& response.responseJSON.type 
    				&& response.responseJSON.type == "error") {
    			// we have errors validating data
    			this.parseValidationErrors(response.responseJSON.errors);
    			return;
    		}
    		window.App.showAlert({
    			title: window.App.translateMessage("app.error.save"),
    			content: window.App.translateMessage("app.error.saveMessage"),
    			type: 'error'
    		});			
		}
	});
	
});