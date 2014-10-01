/**
 * Invokes a callback function on the Flex app.
 * 
 * @param flex callback
 * @param js callback
 * @param args
 */
function callFlexCallback() {
	var args = Array.prototype.slice.call(arguments, 0);
	var flexCallback = args[0];
	var jsCallback = args[1];
	var flexArgs = args.slice(2);
	logger.debug("call flex: " + flexCallback);
	logger.debug("args: " + flexArgs);
	var res = parent.getFlexApp()[flexCallback].apply(parent.getFlexApp(), flexArgs);
	if (jsCallback != undefined) {
		jsCallback(res);
	}
}

// open auth view in popup, as oauth providers do not work in iframe
oauthPopup = true;