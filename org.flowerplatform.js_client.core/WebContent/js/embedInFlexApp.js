/**
 * Invokes a callback function on the Flex app.
 * 
 * @param callback
 * @param args
 */
function callFlexCallback(callback /* : String */, args) /* : Function */ {
	console.log("call flex: " + callback);
	console.log("args: " + JSON.stringify(args));
	parent.getFlexApp()[callback](JSON.stringify(args));
}