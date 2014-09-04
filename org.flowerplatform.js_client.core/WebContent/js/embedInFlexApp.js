///**
// * Invokes a callback function on the Flex app.
// * 
// * @param callback
// * @param args
// */
//function callFlexCallback() {
//	var flexCallback = arguments[0];
//	var jsCallback = arguments[1];
//	var flexArgs = Array.prototype.slice.call(arguments, 2);
//	logger.debug("call flex: " + flexCallback);
//	logger.debug("args: " + flexArgs);
//	parent.getFlexApp()[flexCallback](flexArgs);
//}