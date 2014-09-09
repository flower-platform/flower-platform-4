/**
 * Invokes a callback function on the Flex app.
 * 
 * @param callback
 * @param args
 */
function callFlexCallback() {
	var args = Array.prototype.slice.call(arguments, 0);
	var flexCallback = args[0];
	var flexArgs = args.slice(1);
	logger.debug("call flex: " + flexCallback);
	logger.debug("args: " + flexArgs);
	parent.getFlexApp()[flexCallback].apply(parent.getFlexApp(), flexArgs);
}