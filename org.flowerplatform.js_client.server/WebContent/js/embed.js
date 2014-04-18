/**
 * These functions are used on the embedded IFrame in a Flex app.
 */


/**
 * Called from Flex app.
 * 
 * @param args [url, data, callback]
 */
function getFromFlex(args) {
	get(args[0], JSON.stringify(args[1]), function(data, textStatus, jqXHR) {
		callFlexCallback(args[2], data);
	});
}

function postFromFlex(args) {
	post(args[0], JSON.stringify(args[1]), function(data, textStatus, jqXHR) {
		callFlexCallback(args[2], data);
	});
}

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

//function getJSessionId() {
//	return parent.getFlexApp().getJSessionId();
//}

function overrideMeHandler() {
	$("#override").text("Override from Flex! Original is in test.js");
}

function setFlexSessionId(sessionId) {
	console.log(sessionId);
}
