define(["underscore", 
        "backbone",
        "basejs/app/views/View", 
        "text!basejs/tpl/list/ListPaginationTpl.html"], 
 function(_,
		 Backbone,
		 View, 
		 ListPaginationTpl) {
	
	"use strict";
	
	/**
	 * Generates the page:changed event when the page was changed (with the pageNumber as parameter)
	 */
	return View.extend({
		tpl: ListPaginationTpl,
		
		model: null,
		
		initialize: function() {
			if (!this.model) {
				this.model = new Backbone.Model({
					currentPage: 1,
					maxPage: 1,
					previousPage: 1,
					nextPage: 1,
					firstPage: 1
				});
			}
			
			View.prototype.initialize.apply(this);
		},
		
		events: {
			'click a[data-page]': '_pageClick',
		},

		setPage: function(currentPage, maxPage) {
			var cPage = parseInt(currentPage, 10);
			if (!cPage) {
				cPage = 1;
			}
			
			var mPage = parseInt(maxPage, 10);
			if (!mPage) {
				mPage = 1;
			}
			
			if (cPage > mPage) {
				this.trigger("page:changed", mPage);
				mPage = cPage;
			}
			
			var nextPage = cPage + 1;
			if (nextPage > mPage) {
				nextPage = mPage;
			}
			
			var previousPage = cPage - 1;
			if (previousPage < 1) {
				previousPage = 1;
			}			
			this.model.set({currentPage: cPage, maxPage: mPage, previousPage: previousPage, nextPage: nextPage});
			
			return this;
		},
		
		_pageClick: function(e) {
			e.preventDefault();
			
			var page = $(e.currentTarget).attr('data-page');
			this.trigger("page:changed", page);
		}
	});
});