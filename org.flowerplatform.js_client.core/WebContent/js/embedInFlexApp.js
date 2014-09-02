/**
 * Invokes a callback function on the Flex app.
 * 
 * @param callback
 * @param args
 */
function callFlexCallback(callback /* : String */, args) /* : Function */ {
	logger.debug("call flex: " + callback);
	logger.debug("args: " + JSON.stringify(args));
	parent.getFlexApp()[callback](JSON.stringify(args));
}