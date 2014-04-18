define(["underscore",
        "backbone",
        "basejs/app/views/list/ListTableHeaderView",
        "text!basejs/tpl/list/FilterFormTpl.html"], 
 function(_,
		 Backbone, 
		 ListTableHeaderView,
		 FilterFormTpl){
	
	'use strict';
	
	return ListTableHeaderView.extend({
		 
		sortNeutralCss: "sortableTop",
		
		relativeTableHeaderCss: "relativeTableHeader",
		
		filterButtonTpl: '<a href="" class="btn btn-info btn-xs absoluteFilterButton" data-type="filter" style="position: absolute; top: 0px; right: 0px;"><span class="glyphicon glyphicon-filter"></span></a>',
		
		initialize: function() {
			ListTableHeaderView.prototype.initialize.apply(this);	
		},

		registerSortHandler: function() {
			this.events['click th div.' + this.sortNeutralCss] = 'sortClicked'; 
		},
		
		events: {
			'click th div a[data-type="filter"]': 'filterClicked',
			'show.bs.popover a[data-type="filter"]': 'popoverShow',
			'shown.bs.popover a[data-type="filter"]': 'popoverShown'			
		},
		
		_initializePopover: function() {
			$('body').popover({
				placement: 'bottom',
				html: 'true',
				content: FilterFormTpl,
				selector: 'a[data-type="filter"]',
				container: 'body'
			});
		}, 
		
		_addFilteringAndSortingButtons: function(el) {
			var $el = $(el); 
			
			$el.html('<div style="position:relative; padding-right:30px;">' + $el.html() + '</div>');
			$div = $el.find("div");
			
			if ($el.attr("data-sort") != "false") {
				$div.addClass(this.sortNeutralCss);

				var dataColumn = $el.attr("data-column"); 
				if (dataColumn && this.sortElements) {
					for (var i = 0, len = this.sortElements.length; i < len; i++) {
						var ele = this.sortElements[i];
						if (ele.slice(0, ele.length - 2) == dataColumn) {
							switch (ele.slice(ele.length - 1)) {
								case 'a':
									$div.addClass(this.sortAscCss);
									break;
								case 'd':
									$div.addClass(this.sortDescCss);
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
			if (!$(e.target).hasClass("filter-value")
					&& $(e.target).prop("tagName") != "DIV") {
				return;
			} 
			
			e.preventDefault();			
			
			var th = $(e.target).closest("th");
			var div = $(e.target).closest("div");
			var column = th.attr("data-column");
			if (column) {
				var direction = "a";
				if ($(div).hasClass("sortasc")) {
					direction = "d";
				}
				else if ($(div).hasClass("sortdesc")) {
					direction = "r";
				}

				this.trigger("sort:" + column, direction);
				this.trigger("sort", {column: column, direction: direction});
			}
		},
		
		popoverShown: function(e) {
			var filterSubmit = $.proxy(this.filterSubmit, this);
			var filterKeyUp = $.proxy(this.filterKeyUp, this);
			
			$("#filter-form").submit(filterSubmit);
			$("#filter-form").keyup(filterKeyUp);	
			
			ListTableHeaderView.prototype.popoverShown.apply(this, arguments);	
		},
	
		filterSubmit: function(e) {
			// jquery makes sure it unbinds the correct function even if we do not pass the proxy function
			$("#filter-form").unbind("submit", this.filterSubmit);
			$("#filter-text").unbind("keyup", this.filterKeyUp);
			
			ListTableHeaderView.prototype.filterSubmit.apply(this, arguments);	
		},

		filterKeyUp: function(e) {
			if (e.keyCode == 27 && this.currentPopover) {
				$("#filter-form").unbind("submit", this.filterSubmit);
				$("#filter-text").unbind("keyup", this.filterKeyUp);
			}
			ListTableHeaderView.prototype.filterKeyUp.apply(this, arguments);
		},

	});
	
});