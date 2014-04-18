define(["backbone",
		"basejs/app/views/View",
        "text!basejs/tpl/ModalTpl.html"],
 function(Backbone,
		 View,
		 ModalTpl) {
	
	return View.extend({
		tpl: ModalTpl,
		
		model: null,
		
		buttons: null,
		
		events: {
			'hidden.bs.modal #appModal': 'modalClosed'
		},
		
		initialize: function(options) {
			if (!this.model) {
				this.model = new Backbone.Model();
			}
			
			View.prototype.initialize.apply(this);
			
			var dim = "modal-sm";
			if (options.dimension == 'large') {
				dim = "modal-lg";
			} else if (options.dimension == 'normal') {
				dim = "";
			}
			
			this.model.set({
				title: options.title || '',
				text: options.text || '',
				html: options.html || '',
				dim: dim,
				closeText: options.closeText || 'Close'
			});
			
			if (_.isArray(options.buttons)) {
				this.buttons = options.buttons;
				for (var i = 0, l = options.buttons.length; i < l; i++) {
					options.buttons[i].id = "opt" + i;
					this.events["click #opt" + i] = this.buttonPressed;
				}
			}
		},
		
		render: function() {
			View.prototype.render.apply(this);
			
			var modalFooter = this.$el.find('.modal-last-button');
			
			if (this.buttons && _.isArray(this.buttons)) {
				for (var i = 0, l = this.buttons.length; i < l; i++) {
					var button = this.buttons[i];
					var cls = "primary";
					if (button.type) {
						cls = button.type;
						if (cls == "error") {
							cls = "danger";
						}
					}
					modalFooter.before('<button type="button" class="btn btn-' + cls + '" id="' + button.id + '">' + button.title + '</button>');
				}
			}
		},
		
		show: function() {
			this.render();
			
			this.$el.find("#appModal").modal();
		},
		
		getButtonForId: function(id) {
			var button = null;
			if (this.buttons && _.isArray(this.buttons)) {
				button = _.find(this.buttons, function(button) {
					return button.id == id;
				}, this);
			}
			
			return button;
		},
		
		buttonPressed: function(e) {
			this.id = $(e.currentTarget).prop("id");
			
			var button = this.getButtonForId(this.id);
			
			if (button && button.checkFunction) {
				// close the modal and call the buttonPressed if checkFunction returns true
				// (maybe we want to put an error message in the modal)
				if (!button.checkFunction(this.$el.find("#appModal div.modal-body"))) {
					this.id = null;
					
					return;
				}
			}
						
			this.closeModal();
		},
		
		closeModal: function() {
			this.$el.find("#appModal").modal('hide');
		},
		
		modalClosed: function(e) {
			if (this.id /*&& this.buttons && _.isArray(this.buttons)*/) {
				var button = this.getButtonForId(this.id);
				
				this.id = null;
			
				if (button && button.onPressed) {
					button.onPressed(this.$el.find("#appModal div.modal-body"));
				}
			}
			
			// clean up
			this.$el.remove();
		}
		
	});
	
});