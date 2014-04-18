define(["underscore",
        "backbone",
        "basejs/app/views/View",
        "text!basejs/tpl/list/FilterFormTpl.html"], 
 function(_,
		 Backbone, 
		 View,
		 FilterFormTpl){
	
	'use strict';
	
	/**
	 * Generates the following events:
	 * <ul>
	 * 	<li>sort:column and d/a/nothing as parameter</li>
	 *  <li>sort and {column:column, direction:d/a/nothing} as parameter</li>
	 *  <li>filter:column and the value of the filter</li>
	 *  <li>filter and {column:column, value:value} as parameter</li>
	 * </ul>
	 * <p>Needs to be fed sortElements and filterElements to know in which position to render
	 * the sort/filter buttons - simple arrays containing lines of the column:value form, where value is a/d/nothing for sort,
	 * and the filter value for filter.</p>
	 * <p>actionColumn: true/false, if there is an action column</p>
	 * <p>actionColumnTitle, title of the column (can also be a function)</p>
	 */
	return View.extend({
		
		sortNeutralCss: "sortable",
		
		sortAscCss: "sortasc",
		
		sortDescCss: "sortdesc",
		
		filterButtonTpl: '<a href="" class="btn btn-info btn-xs" data-type="filter"><span class="glyphicon glyphicon-filter"></span></a>',

		_rowTpl: '<tr></tr><tr class="filter-row"></tr>',
		
		initialize: function() {
			View.prototype.initialize.apply(this);
			
			this._initializePopover();
			this._registerClickToClosePopover();
			
			// registered here as the events array does not allow us
			// to compose dynamically a name based on this.sortNeutralCss
			this.registerSortHandler(); 
		},
		
		registerSortHandler: function() {
			this.events['click th.' + this.sortNeutralCss] = 'sortClicked';
		},

		events: {
			'click th a[data-type="filter"]': 'filterClicked',
			'show.bs.popover a[data-type="filter"]': 'popoverShow',
			'shown.bs.popover a[data-type="filter"]': 'popoverShown',
			'submit #filter-form': 'filterSubmit',
			'keyup #filter-form': 'filterKeyUp'
			
		},
		
		_initializePopover: function() {
			$('body').popover({
				placement: 'bottom',
				html: 'true',
				content: FilterFormTpl,
				selector: 'a[data-type="filter"]',
			});
		},
		
		_registerClickToClosePopover: function() {
			var that = this;
			$('html').click(function(e){
				//apply the code only if the popover is open
				if (that.currentPopover != null) {
					if (!$.contains(that.currentPopover, e.target) 
							&& that.currentPopover != e.target
							&& $(e.target).parents().index($(".popover")) == -1) {
						$(that.currentPopover).popover('hide');
						that.currentPopover = null;
					}
				} 
			});
		},
		
		render: function() {

			if (!this.originalTpl) {
				this.originalTpl = this.tpl;
				this.originalTemplate = _.template(this.originalTpl);
				this.tpl = this._rowTpl;
			}
			
			View.prototype.render.apply(this);
			
			this.$el.find("tr").first().append(this.originalTemplate());
			
			// for each th from the header, add filtering and sorting buttons
			_.each(this.$el.find("th"), this._addFilteringAndSortingButtons, this);
			
			// add header Actions if required
			if (this.actionColumn && this.actionColumnTitle) {
				var tr = this.$el.find("tr");
				tr.first().append("<th>" + (_.isFunction(this.actionColumnTitle) ? this.actionColumnTitle.call() : this.actionColumnTitle) + "</th>");
				tr.last().append("<th></th>");
			}
			return this;
		},
		
		_addFilteringAndSortingButtons: function(el) {
			var $el = $(el);
			
			if ($el.attr("data-sort") != "false") {
				$el.addClass(this.sortNeutralCss);

				var dataColumn = $el.attr("data-column");
				if (dataColumn && this.sortElements) {
					for (var i = 0, len = this.sortElements.length; i < len; i++) {
						var ele = this.sortElements[i];
						if (ele.slice(0, ele.length - 2) == dataColumn) {
							switch (ele.slice(ele.length - 1)) {
								case 'a':
									$el.addClass(this.sortAscCss);
									break;
								case 'd':
									$el.addClass(this.sortDescCss);
									break;
							}
							break;
						}
					}
				}
			}

			var parent = $el.parent().next();
			var addition = $("<th>");
			
			if ($el.attr("data-filter") != "false") {
				var app = $(this.filterButtonTpl);
				addition.append(app);
				var dataColumn = $el.attr("data-column");
				addition.attr("data-column", dataColumn);
				if (dataColumn && dataColumn in this.filterElements) {
					addition.append(' <span class="filter-value">(' + decodeURIComponent(this.filterElements[dataColumn]) + ')</span>');
				}
			}
			parent.append(addition);
		},
		
		sortClicked: function(e) {
			e.preventDefault();
			
			var column = $(e.target).attr("data-column");
			if (column) {
				var direction = "a";
				var th = $(e.target);
				if ($(th).hasClass("sortasc")) {
					direction = "d";
				}
				else if ($(th).hasClass("sortdesc")) {
					direction = "r";
				}

				this.trigger("sort:" + column, direction);
				this.trigger("sort", {column: column, direction: direction});
			}
		},
		
		filterClicked: function(e) {
			e.preventDefault();
		},
		
		popoverShow: function(e) {
			// close the existing popover
			if (this.currentPopover && this.currentPopover != e.currentTarget) {
				$(this.currentPopover).popover('hide');
			}
			
			// mark that this is the current popover
			this.currentPopover = e.currentTarget;
		},
		
		popoverShown: function(e) {
			var span = $(e.currentTarget).nextAll("span.filter-value");
			var lng = 0;
			if (span.length > 0 && span.text() != '') {
				var text = span.text().slice(1, -1);
				$("#filter-text").val(text);
				lng = text.length;
			}
			
			// set focus to the input
			var filterText = $("#filter-text");
			filterText.focus();
			if (lng > 0) {
				if (filterText[0].setSelectionRange) {
					filterText[0].setSelectionRange(lng, lng);
				}
				else if (filterText[0].createTextRange) {
					// IE 8
					var range = filterText[0].createTextRange();
					range.collapse(true);
					range.moveEnd('character', lng);
					range.moveStart('character', lng);
					range.select();
				}
			}
		},
	
		filterSubmit: function(e) {
			e.preventDefault();

			var column = $(this.currentPopover).closest("th").attr("data-column");
			var value = $(e.currentTarget).find("#filter-text").val();
			
			// close the popover
			$(this.currentPopover).popover('hide');
			this.currentPopover = null;
			
			// build the filter url
			this.trigger("filter:" + column, value);
			this.trigger("filter", {column:column, value:value});
		},

		filterKeyUp: function(e) {
			if (e.keyCode == 27 && this.currentPopover) {
				$(this.currentPopover).popover('hide');
				this.currentPopover = null;
			}
		},

	});
	
});