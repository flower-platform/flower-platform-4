define(["underscore", 
        "basejs/app/views/View", 
        "text!basejs/tpl/list/ListTpl.html",
        "basejs/app/views/list/ListPaginationView",
        "basejs/app/views/list/ListHeaderView",
        "basejs/app/views/list/ListTableHeaderView",
        "text!basejs/tpl/list/ListActionButtonsTpl.html"], 
 function(_, 
		 View,
		 ListTpl, 
		 ListPaginationView,
		 ListHeaderView,
		 ListTableHeaderView,
		 ListActionButtonsTpl){
	
	"use strict";
	
	/**
	 * Generic Table View that appends the data from collection to a table.
	 * <p>It needs:
	 * <ul>
	 * 	<li>itemView - pointing to a javascript class (backbone extend) that will know to render a model</li>
	 *  <li>tpl - pointing to the template of the list (usually a table with the head row)</li>
	 * </ul>
	 * </p>
	 */
	return View.extend({
		/**
		 * primary part of the route (/baseRoute/:id, /baseRoute/new, /baseRoute/delete/:id, /baseRoute s).
		 * Mandatory Field.
		 */
		baseRoute: null,
		/**
		 * Template for the header of the table inside the list.
		 * Mandatory Field
		 */
		listHeaderTpl:  null,
		/**
		 * Template for the items that will be shown in the table 
		 * Mandatory Field
		 */
		listItemTpl: null,
		
		/**
		 * A dictionary where key is the data-column attribute value (of the td element)
		 * and the value is a backbone view to be added to the td.
		 */
		customColumnViews: null,
		
		/**
		 * If the add button from the header should be shown
		 */
		hasAddButton: true,
		
		/**
		 * Whether the export XLS button from the header should be shown
		 */
		hasXLSButton: true,
		
		infoMessage: '',
		
		/**
		 * The generic template of the list.
		 */
		listTpl: ListTpl,

		/**
		 * If the action Columns should be added by this.
		 */
		actionColumn: true,
		
		/**
		 * Title of the action column.
		 * Can also be a function.
		 */
		actionColumnTitle: 'Actions',
		
		/**
		 * Template for the Edit/Delete buttons
		 */
		editButtonsTpl: ListActionButtonsTpl,
		
		paginationViewCls: ListPaginationView,
		
		paginationView: null,
		
		headerViewCls: ListHeaderView,
		
		headerView: null,
		 
		tableHeaderViewCls: ListTableHeaderView,
		
		tableHeaderView: null,
		
		tableHover: false,
		
		initialize: function() {
			View.prototype.initialize.apply(this);
			
			if (!this.paginationView) {
				this.paginationView = new this.paginationViewCls();
			}
			
			if (!this.headerView) {
				this.headerView = new this.headerViewCls();
			}
			
			if (!this.tableHeaderView) {
				this.tableHeaderView = new this.tableHeaderViewCls();
			}
			
			if (!this.baseRoute) {
				throw new Error("baseRoute is mandatory");
			}
			
			if (!this.listHeaderTpl) {
				throw new Error("listHeaderTpl is mandatory");
			}
			
			if (!this.listItemTpl) {
				throw new Error("listItemTpl is mandatory");
			}
			
			this.editButtonsTemplate = _.template(this.editButtonsTpl);
			
			this.headerView.setHeaderAttr({
				hasAdd: this.hasAddButton,
				hasXLS: this.hasXLSButton,
				infoMessage: this.infoMessage
			});
			
			this.tableHeaderView.tpl = this.listHeaderTpl;
			this.tableHeaderView.actionColumn = this.actionColumn;
			this.tableHeaderView.actionColumnTitle = this.actionColumnTitle;
			
			this.listenTo(this.paginationView, "page:changed", this.pageChanged);
			this.listenTo(this.headerView, "header", this.headerButtonClicked);
			this.listenTo(this.tableHeaderView, "sort", this.sortClicked);
			this.listenTo(this.tableHeaderView, "filter", this.filterChanged);
			
			// clean events.
			// they will be created dynamically by initialize
			// and we want them empty, not to encompass the modifications from inheritance
			this.events = {};
			
			// since the events will be dinamically, we need to put them in initialize
			this.events['click a[data-type="edit-' + this.cid + '"]'] = "editItem";
			this.events['click a[data-type="delete-' + this.cid + '"]'] = "deleteItem";
		},
		
		/**
		 * Adds the pair element/value to the url, at the requested parameter
		 * E.g.:
		 * 	adds srt:d for /sort: parameter,
		 * _replaceUrlPart('srt', 'd', {
		 * 		condition: '/sort:',
		 * 		extractRe: /\/sort:\[([^\]]*?)\]/,
		 * 		defaultValue: 'r',
		 * 		cleanRe: /\/sort:[^\/]*?/,
		 * 		putBefore: '/filter:'
		 * });
		 */
		_replaceUrlPart: function(element, value , options) {
			var fragment = Backbone.history.getFragment();
			
			var fragmentFound = false;
			if (fragment.indexOf(options.condition) != -1) {
				if (fragment.search(options.extractRe) != -1) {
					fragmentFound = true;
					var list = fragment.match(options.extractRe)[1];
					var elements = list.split(options.splitList || ',');
					var found = false;
					for (var i = 0, len = elements.length; i < len; i++) {
						if (elements[i].indexOf(element + ":") != -1) {
							if (value != options.defaultValue) {
								elements[i] = element + ":" + value;
							} else {
								elements.splice(i, 1);
							}
							found = true;
							break;
						}
					}
				
					if (!found && value != options.defaultValue) {
						elements[elements.length] = element + ":" + value;
					}
					
					var replaceFragment = "";
					if (elements.length > 0) {
						replaceFragment = options.condition + "[" + elements.join(",") + "]";
					}
					fragment =fragment.replace(options.extractRe, replaceFragment);
				}
				else if (options.cleanRe) {
					// remove the "/xx:" that should not be threre
					fragment = fragment.replace(options.cleanRe, "");
				}
			}
			if (!fragmentFound) {
				if (value != options.defaultValue) {
					if (options.putBefore && fragment.indexOf(options.putBefore) != -1) {
						fragment = fragment.replace(options.putBefore, (options.condition + '[' + element + ":" + value + "]" + options.putBefore));
					} else {
						fragment += options.condition + "[" + element + ":" + value + "]";
					}
				}
			}
			
			return fragment;
		},
		
		/**
		 * Called when filter was set on a column.
		 * data: {columnName, filterValue}
		 */
		filterChanged: function(data) {
			if (data.column) {
				Backbone.history.navigate(
						this._replaceUrlPart(
								data.column,
								encodeURIComponent(data.value),
								{
									condition: "/filter:",
									extractRe: /\/filter:\[([^\]]*?)\]/,
									splitList: /,(?=[^,]*?:)/,
									defaultValue: '',
									cleanRe: /\/filter:[^\/]*?/
								}), 
						{trigger: true}
				);
			}
			
		},
		
		/**
		 * Called when sorting was changed.
		 * data: {column: columnName, direction: a/d/r}
		 * direction = r meaning the sort should be removed
		 */
		sortClicked: function(data) {
			if (data.column && data.direction) {
				Backbone.history.navigate(
						this._replaceUrlPart(
								data.column, 
								data.direction,
								{
									condition: "/sort:",
									extractRe: /\/sort:\[([^\]]*?)\]/,
									defaultValue: 'r',
									cleanRe: /\/sort:[^\/]*?/,
									putBefore: '/filter:'
								}
						),
						{trigger: true}
				);
			}
		},
		
		/**
		 * Called when the user changed the page
		 */
		pageChanged: function(page) {
			// validation
			var currentPage = parseInt(page, 10);
			if (currentPage < 1) {
				currentPage = 1;
			}
			
			if (currentPage > this.collection.maxPage) {
				currentPage = this.collection.maxPage;
			}
			
			// now build the url
			var fragment = Backbone.history.getFragment();
			if (fragment.indexOf('/page:') != -1) {
				var pageRe = /\/page:([^\/]*)/;
				fragment = fragment.replace(pageRe, ("/page:" + currentPage));
			}
			else {
				if (fragment.indexOf('/sort:') != -1) {
					fragment = fragment.replace('/sort:', ('/page:' + currentPage + '/sort:'));
				}
				else if (fragment.indexOf('/filter:') != -1) {
					fragment = fragment.replace('/filter:', ('/page:' + currentPage + '/filter:'));
				} else {
					fragment += "/page:" + currentPage;
				}
			}
			
			Backbone.history.navigate(fragment, {trigger: true});			
		},
		
		/**
		 * Caseld when the user pressed one of the buttons from the header
		 */
		headerButtonClicked: function(button) {
			switch(button) {
				case 'add':
					// map the current page to navigate after edit
					window.App.customData = window.App.customData || {};
					window.App.customData.navigateAfterEdit = Backbone.history.getFragment();
					
					Backbone.history.navigate(this.baseRoute + "/new", {trigger: true});
					break;
			}
		},
		
		/**
		 * Called when the edit buttons was pressed
		 */
		editItem: function(e) {
			e.preventDefault();
			
			// extract id
			var id = $(e.currentTarget).attr('data-id');
			
			// map the current page to navigate after edit
			window.App.customData = window.App.customData || {};
			window.App.customData.navigateAfterEdit = Backbone.history.getFragment();
			
			Backbone.history.navigate(this.baseRoute + '/' + id, {trigger: true});
		},
		
		showDeleteError: function() {
			window.App.showAlert({
				title: 'Delete',
				content: 'Server error when deleting',
				type: 'error'
			});
		},
		
		/**
		 * Called when the delete button was pressed.
		 * 
		 * opts:
		 * 	success: function to do on success
		 * 	error: function to execute on error
		 * If opts.success is not defined, the current page route is retriggered.
		 * If opts.error is not defined a alert is shown using window.App.showAlert()
		 */
		deleteItem: function(e, opts) {
			e.preventDefault();

			var id = $(e.currentTarget).attr('data-id');
			
			var model = this.collection.find(function(model){
				return model.id == id; 
			});

			if (model != null) {
				var that = this;
				model.destroy({wait:true,
					success: function(model, response, options){
						if (opts && opts.success && _.isFunction(opts.success)) {
							opts.success(model, response, options);
						} else {
							Backbone.history.loadUrl(Backbone.history.getFragment());
						}
					},
					error: function(model, xhr, options) {
						if (opts && opts.error && _.isFunction(opts.error)) {
							opts.error(model, xhr, options);
						} else {
							that.showDeleteError();
						}
					}
				});
			}			
		},
		
		render: function() {
			// render the list
			this.tpl = ListTpl;
			View.prototype.render.apply(this);
			
			if (this.tableHover) {
				this.$el.find("table").addClass("table-hover");
			}
			
			// render different parts of the list
			this.renderHeaderList();
			this.renderDataHeader();
			this.renderDataItems();
			this.renderFooterList();
		},
		
		renderHeaderList: function() {
			this.headerView
					.setElement(this.$el.find(".panel-heading"))
					.render();
		},
		
		getSortElements: function(fragment) {
			var sortRe = /\/sort:\[([^\]]*?)\]/;
			var sortUrlElements = null;
			if (fragment.search(sortRe) != -1) {
				var element = fragment.match(sortRe)[1];
				sortUrlElements = element.split(",");
			}
			
			return sortUrlElements;
		},

		
		getFilterElements: function(fragment) {
			var filterRe = /\/filter:\[([^\]]*?)\]/;
			var filterElements = {};
			if (fragment.search(filterRe) != -1) {
				var element = fragment.match(filterRe)[1];
				var filterUrlElements = element.split(/,(?=[^,]*?:)/);
				for (var i = 0, l = filterUrlElements.length; i < l; i++) {
					var ele = filterUrlElements[i];
					filterElements[ele.slice(0, ele.indexOf(':'))] = ele.slice(ele.indexOf(':') + 1);
				}
			}
			
			return filterElements;
		},
		
		renderDataHeader: function() {
			var fragment = Backbone.history.getFragment();

			this.tableHeaderView.sortElements = this.getSortElements(fragment);
			this.tableHeaderView.filterElements = this.getFilterElements(fragment);
			
			this.tableHeaderView
					.setElement(this.$el.find("table > thead"))
					.render();
			
		},
		
		renderDataItems: function() {
			var ItemView = View.extend({
				tagName: "tr",
				tpl: this.listItemTpl
			});
			
			// render the elements   
			var table = this.$el.find("table");
			_.each(this.collection.models, function(model){
				var tr = new ItemView({
					model: model
				}).render().el;
				this.renderCustomColumnViews(tr, model);
				if (this.actionColumn) {
					var attributes = _.clone(model.attributes);
					attributes['viewId'] = this.cid;
					$(tr).append(this.editButtonsTemplate(attributes));
				}
				table.append(tr);
			}, this);
		}, 
		 
		renderCustomColumnViews: function(tr, model) {
			if (this.customColumnViews != null) {
				
				for (var key in this.customColumnViews) { 
					var view = new this.customColumnViews[key]( {
						model: model
					});
					$(tr).find('[data-column="' + key +'"]').html(view.render().el);
				}
			}
		},
		
		renderFooterList: function() {
			var maxPage = Math.ceil(this.collection.totalCount / this.collection.pageSize);
			if (maxPage == 0) {
				maxPage = 1;
			}
			if (!this.collection.page || this.collection.page == 0) {
				this.collection.page = 1;
			}
			this.collection.maxPage = maxPage;
			
			var currentPage = this.collection.page;
			
			// render the pagination (table footer)
			this.paginationView
					.setElement( this.$el.find(".panel-footer") )
					.setPage(currentPage, maxPage)
					.render();
		},

	});
	
});
