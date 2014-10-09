var logger = {
	debug: function(msg) {
		// trace in flex console?
	}
}

/**
 * Invokes a callback function on the Flex mobile app.
 * 
 * @param callback
 * @param args
 */
function callFlexCallback() {
	var args = Array.prototype.slice.call(arguments, 0);
	logger.debug("call flex: " + args[0]);
	logger.debug("args: " + args.slice(1));
	args.splice(1, 0, null);
	StageWebViewBridge.call.apply(null, args);
}