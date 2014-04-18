require.config({
	
	baseUrl: 'lib',

	paths: {
		basejs: '../basejs',
		models: '../js/models',
		views: 	'../js/views',
		tpl: 	'../tpl'
	},
	
	shim: {
        'backbone': {
            deps: ['underscore', 'jquery', 'bootstrap'],
            exports: 'Backbone'
        },
        'underscore': {
            exports: '_'
        }
    }
});

require(['jquery', 'backbone', 'basejs/app/AppUtils', '../js/UserRouter'], function ($, Backbone, AppUtils, UserRouter) {
	
	window.App = new AppUtils({
		CSRFProtection: false,
		globalAlertElement: '#alert',
		localAlertElement: '.local-alert'
	});
	
	var router = new UserRouter();
    Backbone.history.start();
});