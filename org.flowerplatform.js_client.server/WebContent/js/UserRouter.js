define(function (require) {

    "use strict";

    var $ = require('jquery');
    var _ = require('underscore');
    var Backbone = require('backbone');
    
    var Router = require("basejs/app/routers/Router");
    
    var UserListView = require("views/UserListView");
    var UserFormView = require("views/UserFormView");
    
    var User = require("models/User");
    var UserCollection = require("models/UserCollection");
    
	return Router.extend({
		
		userListView: new UserListView({el: $("#content")}),
		userFormView: new UserFormView({el: $("#content")}),
		userClass: User,
		userList: new UserCollection(),
		
		routes: {
			"users"		: "showUserList",
			"users/new" : "newUser",
			"users/:id"	: "showUser"
		},
		
		showUserList: function() {
			this.fetchAndRenderList(this.userList, this.userListView);
		},
		
		showUser: function(id) {
			this.userFormView.create = false;
			this.userFormView.fetchModelAndShowDetails(id, this.userList, this.userClass);
		},
		
		newUser: function() {
			var user = new User();
			user.properties = {};
			user.collection = this.userList;
			this.userFormView.model = user;
			this.userFormView.create = true;
			this.userFormView.render();
		}
		
	});
	
});
