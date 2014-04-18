define(["basejs/app/views/View.js",         
        "jqueryui",
        "text!basejs/app/components/datepicker/tpl/DatePickerTpl.html"], 
function(View, 		
		jqueryui,
		DatePickerTpl){
	
	return View.extend({
		tpl: DatePickerTpl,
		events: {
		},
		initialize: function(init) {
			this.events['click .date-button'] = "dateButtonClicked";
			this.events['click .date-input'] = "dateButtonClicked";
			this.events['change input[name="date"]'] = "dateChanged";
		},
		
		dateButtonClicked: function(e) {
			var widget = this.$el.find(".date-input");
			if (widget) {
				var dateWidget = widget.datepicker("widget");
				if (dateWidget.is(":empty") || !dateWidget.is(":visible")) {
					widget.datepicker("show");
				} else {
					widget.datepicker("hide");
				}
			}
		},
		
		dateChanged: function(e) {
			var val = $(e.currentTarget).val();
			this.trigger('dateChanged', val);
		},
		
		 render: function() {
			 View.prototype.render.apply(this);
			// instantiate the date pickers
			var dateOptions = {
				dateFormat: "dd/mm/yy",
				changeYear: true,
				changeMonth: true,
				showButtonPanel: true,
				constrainInput: true,
				defaultDate: new Date()
			};
				
			this.$el.find(".date-input").datepicker(dateOptions);
			
			this.$el.find(".date-input").val(this.getFormattedDate(new Date()));
			
			return this;
		 },
		 
		 getValue: function() {
			 return $(this.$el.find(".date-input")).val();
		 },
		 
		 getFormattedDate: function(date) {
			return date.getDate() + '/' + (date.getMonth() + 1) + '/' + date.getFullYear();
		}
	});
});