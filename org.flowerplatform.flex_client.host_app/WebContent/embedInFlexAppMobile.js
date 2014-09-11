var logger = {
	debug: function(msg) {
		// trace in flex console?
		callFlexCallback('trace', null, msg);
	}
}

/**
 * Invokes a callback function on the Flex mobile app.
 * 
 * @param flexCallback
 * @param jsCallback
 * @param args
 */
function callFlexCallback() {
	var args = Array.prototype.slice.call(arguments, 0);
	if (args[0] != 'trace') {
		logger.debug("call flex: " + args[0]);
		logger.debug("args: " + args.slice(2));
	}
//	args.splice(1, 0, null);
	StageWebViewBridge.call.apply(StageWebViewBridge, args);
}