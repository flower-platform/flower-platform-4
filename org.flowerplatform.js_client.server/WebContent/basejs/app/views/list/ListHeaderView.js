define(["underscore",
        "backbone",
        "basejs/app/views/View",
        "text!basejs/tpl/list/ListHeaderTpl.html"], 
 function (_,
		 Backbone,
		 View,
		 ListHeaderTpl){
	
	"use strict";
	
	/**
	 * Uses hasAdd as parameters that can be set during instantiation (new ListHaderView({hasAdd:true, hasXLS: false}),
	 * or with setHeaderAttr as setHeaderAttr({hasAdd: true, hasXLS: false})
	 * <p>Triggers events:
	 * 	<ul>
	 * 		<li>header:button_code or </li>
	 * 		<li>header with button_code as parameter</li>
	 * </ul></p> 
	 */
	return View.extend({
		tpl: ListHeaderTpl,
		
		model: null,
		
		initialize: function(init) {
			// if we have a tpl, overwrite the generic one
			if (init && init.tpl) {
				this.tpl = init.tpl;
			}
			
			if (!this.model) {
				this.model = new Backbone.Model({
					hasAdd: false,
					hasXLS: false,
					infoMessage: ''
				});
			}
			
			View.prototype.initialize.call(this, init);
			
			if (init) {
				this.model.set({hasAdd: init.hasAdd || false, hasXLS: init.hasXLS || false, infoMessage: init.infoMessage || ''});
			}
		},
		
		events: {
			'click a[data-type]': 'buttonClick'
		},

		buttonClick: function(e) {
			e.preventDefault();
			
			var buttonType = $(e.currentTarget).attr('data-type');
			
			// in case we listen only on some buttons
			this.trigger('header:' + buttonType, buttonType);
			
			// in case we listen on all the bttons
			this.trigger('header', buttonType);
		},
		
		setHeaderAttr: function(attrs) {
			this.model.set({hasAdd: attrs.hasAdd || false, hasXLS: attrs.hasXLS || false, infoMessage: attrs.infoMessage || ''});
		}
	});
});