var logger = {
	
	debug: function(msg) {
		console.log(msg);
	}
		
};

var scriptCache = {};

/**
 * Loads the list of scripts. If all the scripts are
 * loaded successfully, then loadComplete is invoked.
 */
var loadScripts = function(scripts, allScriptsLoadedCallback) {
	var loaded = 0;
	angular.forEach(scripts, function(script) {
		if (scriptCache[script]) {
			loaded = scriptLoadedHandler(loaded, scripts.length, allScriptsLoadedCallback);
		} else {
			$.getScript('../' + script)
			.done(function() {
				logger.debug('Loaded script: ' + script);
				scriptCache[script] = true;
				loaded = scriptLoadedHandler(loaded, scripts.length, allScriptsLoadedCallback);
			})
			.fail(function(jqxhr, settings, exception) {
				logger.debug('Failed to load script: ' + script + ' with exception ' + exception);
			});
		}
	});
};

var scriptLoadedHandler = function(loaded, all, allScriptsLoadedCallback) {
	loaded++;
	if (loaded == all) {
		allScriptsLoadedCallback();
	}
	return loaded;
}

function getURLParam(name) {
	name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
	var regexS = "[\\?&]" + name + "=([^&#]*)";
	var regex = new RegExp(regexS);
	var results = regex.exec(window.location.href);
	if (results == null) {
		return "";
	} else {
		return results[1];
	}
}