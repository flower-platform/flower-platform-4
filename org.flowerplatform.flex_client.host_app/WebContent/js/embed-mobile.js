/**
 * Invokes a callback function on the Flex mobile app.
 * 
 * @param callback
 * @param args
 */
function callFlexCallback(callback /* : String */, args) /* : Function */ {
	StageWebViewBridge.call(callback, null, JSON.stringify(args));
}