define(function (require) {

   "use strict";

	var $ = require('jquery');
	var _ = require('underscore');
	var Backbone = require('backbone');
   
	var ListView = require("basejs/app/views/list/ListView");
	
    return ListView.extend({
    	
    	baseRoute: "users",
    	
    	listHeaderTpl: require("text!tpl/UserListHeader.html"),
    	
    	listItemTpl: require('text!tpl/UserListItem.html'),
    	
    	hasXLSButton: false
    	
    })
    
});