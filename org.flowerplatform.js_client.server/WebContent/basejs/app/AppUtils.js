define(["underscore", 
        "jquery",
        "backbone",
        "basejs/app/views/AlertView",
        "basejs/app/views/ModalView"], 
 function(_,
		 $,
		 Backbone,
		 AlertView,
		 ModalView) {
	
	var AppUtils = function(options) {
		options || (options = {});
		this.initialize.apply(this, arguments);
	};
	
	_.extend(AppUtils.prototype, {
		
		globalAlertElement: null,
		
		localAlertElement: null,
		
		autoClose: false,
		
		autoCloseTime: 8000,
		
		// This is a handler for ajax calls through backbone (sync method), not every ajax calls.
		h401Handler: true,
		
		blockUIOnAjaxRequest: false,
		
		CSRFProtection: true,
		
		/**
		 * Options:
		 * 	globalAlertElement - jquery selector where showAlert will insert the alert (default none). Can be overwritten by
		 * the el option when calling showAlert.
		 *  localAlertElement - jquery selector where showAlert will insert the alert (default none). If it exists
		 * it overwrites the globalAlertElement and is overwritten by el (if it exists)
		 * The order is: el if exists otherwise localAlertElement if exists otherwise globalAlertElement
		 *  autoClose - if the showAlert will display a autoClosable alert (default false)
		 *  autoCloseTime - time before autoClosing the alert (default: 8 sec)
		 *  h401Handler - how to react if the statusCode from the server is 401 (during Backboe.sync)
		 *  	- true (default), redirects to #login, reloading the page (to generate the session)
		 *  	- false, do nothing
		 *  	- function, calls the function provided with (jqXHR, responseText, errorThrown) as parameter
		 *  CSRFProtection - if we want the CSRF token to be included on every POST, PUT, DELETE or PATCH ajax call
		 *  	- true/false
		 * @param options
		 */
		initialize: function(options) {
			var opts = options || {};
			
			this.globalAlertElement = opts.globalAlertElement;
			this.localAlertElement = opts.localAlertElement;
			this.autoClose = opts.autoClose || this.autoClose;
			this.autoCloseTime = opts.autoCloseTime || this.autoCloseTime;
			
			if (options.h401Handler == false) {
				this.h401Handler = false;
			} 
			else {
				this.h401Handler = options.h401Handler || this.h401Handler;
			}
			var is401Function = _.isFunction(options.h401Handler);
			if (this.h401Handler == true || is401Function) {
				this.register401Handler(is401Function ? options.h401Handler : this.default401Handler); 
			} 
			
			if (options.blockUIOnAjaxRequest) {
				$(document).ajaxStart(function() {
					var $screenBlocker = $("#screenBlockerDiv");							
					if (!$.contains(document, $screenBlocker)) {
						$('body').addClass('modal-open');
						// image from http://www.ajaxload.info/						
						$('body').append('<div id="screenBlockerDiv" class="modal fade" data-backdrop="static" data-keyboard="false" tabindex="-1" aria-hidden="true" style="display: block; opacity: 1;"><img src="basejs/img/ajax-loader.gif" class="ajax-loader" /></div>');
						// background-color: #888888; opacity: 0.5; cursor: progress
					}
					
				});
				$(document).ajaxComplete(function() {
					$('body').removeClass('modal-open'); 
					 //document.getElementById("screenBlockerDiv").remove();
					var $screenBlocker = $("#screenBlockerDiv");
					$screenBlocker.remove(); 
				});
			}
			
			if (options.CSRFProtection == false) {  
				this.CSRFProtection = false;
			}
			if (this.CSRFProtection) {
				this.registerCSRFProtection(); 
			}
		},
		 
		/**
		 * Show an alert
		 * 
		 * @param el element where the alert will be displayed
		 * @param options object containing definitions for the alert
		 * 		el: element that will contain the alert,
		 * 		title,
		 * 		content
		 * 		dismissable: true/false
		 * 		type: info/success/warning/(danger|error) 
		 * @returns
		 */
		showAlert: function(options) {
			if (options.type == 'error') {
				options.type = 'danger';
			}
			
			var el = options.el;
			if (!el || $(el).length == 0) {
				el = this.localAlertElement;
				if (!el || $(el).length == 0) {
					el = this.globalAlertElement;
				}
			}
			
			// need to remove the el, before passing to the view.
			delete options.el;
			
			var al = new AlertView(options);
			
			$(el).html(al.render().el);

			var autoClose = _.isUndefined(options.autoClose) ? this.autoClose : options.autoClose;
			delete options.autoClose;
			
			var autoCloseTime = _.isUndefined(options.autoCloseTime) ? this.autoCloseTime : options.autoCloseTime;
			delete options.autoCloseTime;
			
			if (autoClose) {
				window.setTimeout(function() {
					al.remove();
				}, autoCloseTime);
			}
		},
		
		/**
		 * Intercepts Backbone.sync errors and check for 401 returned by
		 * the server, and 
		 * <ul>
		 * 	<li>If there is a handler, calls that</li>
		 *  
		 */
		register401Handler: function(handler) {
			var originalBackboneSync = Backbone.sync;
			Backbone.sync = function(model, method, options) {
				var initialErrorHandler = options.error;
				options.error = function(jqXHR, textStatus, errorThrown) {
					if (jqXHR.status == 401) {
						handler(jqXHR, textStatus, errorThrown);
					} else {
						initialErrorHandler(jqXHR, textStatus, errorThrown);
					}
				};
				return originalBackboneSync.apply(this, arguments);
			};
		},
		
		/**
		 * Default functionality is to redirect to #login page.
		 */
		default401Handler: function(jqXHR, textStatus, errorThrown) {
			window.location.replace("login.jsp?after_login=" + encodeURIComponent(Backbone.history.getFragment()));
		},
		
		/**
		 * Register a global AJAX handler that adds the CSRF header if the request is POST,
		 * PUT, PATCH or DELETE
		 */
		registerCSRFProtection: function() {
			var token = $("meta[name='_csrf']").attr("content");
			var header = $("meta[name='_csrf_header']").attr("content");
			$(document).ajaxSend(function(e, xhr, options) {
				if (options.type === 'POST' 
					|| options.type === 'DELETE'
					|| options.type === 'PUT'
					|| options.type === 'PATCH') {
					xhr.setRequestHeader(header, token);
				}
			});
		},
		
		/**
		 * Creates a modal window and returns it.
		 * Options:
		 * 	title: title of the modal
		 * 	text: Content of the modal
		 * 	html: If the content is Html, that we want redered.
		 * 	dimension: large|normal|small (default: small)
		 * 	closeText: Text to be shown on the close button (default 'Close')
		 * 	buttons: array of buttons with the following options:
		 * 		title: title of the button
		 * 		type: primary/default/info/warning/error|danger
		 * 		onPresed: what to be executed when the button is pressed
		 * 	(N.B.: onPressed is executed after the modal is closed)
		 */
		showModal: function(options) {
			
			if (!options.el) {
				// if the element is not specified, it is created at the end of body
				var $el = $('body').find("#myModal");
				if (!$el.length) {
					$('body').append("<div id='myModal'></div>");
					$el = $('body').find("#myModal");
				}
				options.el = $el;
			}
			var mdl = new ModalView(options);
			
			mdl.show();
			
			return mdl;
		},
		
		translateMessage: function(message) {
			var trans = this.translationMessage[message];
			if (trans && trans != '' && arguments.length > 1) {
				for (var i = 1, l = arguments.length; i < l; i++) {
					trans = trans.replace("{" + (i - 1) + "}", arguments[i]);
				}
			}
			
			if (trans == "") {
				return message;
			}
			
			return trans;
		}
	});
	
	AppUtils.extend = Backbone.Model.extend;
	
	return AppUtils;
	
});
