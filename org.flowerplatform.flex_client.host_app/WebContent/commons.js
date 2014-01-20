function getURL() /* : String */ {
	// Using the string property and not the window.location object.
	try {
		var url = window.location.href; // Obtaining url, if exceptions then skip to next. 
		if (url.length != 0) // Must have some characters or skip to next. If null then skip to next.
			return url;
	} catch(err) { }

	try {
		var url = window.document.URL;
		if (url.length != 0)
			return url;
	} catch(err) { }
	
	// document.location seems to be deprecated to window.location
	try {
		var url = document.location.href; 
		if (url.length != 0) 
			return url;
	} catch(err) { }

	alert("The JavaScript code that relays the query string to the Flex app has failed. Please contact support!");
}

function getQueryString() /* : String */ {
	try {			
		if (document.location.search.indexOf("?") > -1)	{			
			return document.location.search.substring(1);
		}
	} catch(err) {		
		// swallowed error
		try {
			var index = window.location.href.indexOf("?");
			if (index > -1) { 
				queryString = new String(window.location.href);
				return queryString.substring(index + 1);
			}
		} catch(err) {
			// swallowed error
			try {
				if (window.location.search.indexOf("?") > -1) {
					return window.location.search.substring(1);
				}
			} catch(err) {
				// swallowed error
				try {
					var index = window.document.URL.indexOf("?");
					if (index > -1)	{
						return window.document.URL.substring(index + 1);
					}
				} catch(err) {
					// swallowed error
					alert("Could not find URL query string parameters. Please contact support!");
				}
			}
		}
	}
}

// Internet Explorer and Mozilla-based browsers refer to the Flash application 
// object differently.
// This function returns the appropriate reference, depending on the browser.
function getFlexApp() {
    if (navigator.appName.indexOf("Microsoft") !=-1) {
        return window["FlexHostApp"];
    } else {
        return document["FlexHostApp"];
    }
}

///**
// * @author Sebastian Solomon
// * 
// */
//function dragOnDiagram(paths) {
//	window.document.getElementById("FlexHostApp").dragOnDiagram(paths);
//}
//
//function handleLink(link) {
//	var isSafariBrowser = navigator.userAgent.toLowerCase().indexOf("safari") != -1;
////		if (!isSafariBrowser) {
////			window.document.temp_form.temp_field.handleLink(link);
////		}
//	window.document.getElementById("FlexHostApp").handleLink(link);
//}
//
///**
// * @author Sebastian Solomon
// * 
// */
//function isFileOpened(path) {
//	var isOpen = window.document.getElementById("FlexHostApp")
//			.isEditableResoucesOpened(path);
//	sendIsDiagramOpenedToJava(isOpen);
//}
///**
// * @author Sebastian Solomon
// * 
// */
//function doSave() {
//	window.document.getElementById("FlexHostApp").doSaveAll();
//}
//
///**
// * @author Sebastian Solomon
// * 
// */
//function sendGlobalDirtyState(dirtyState) {
//	sendGlobalDirtyStateToJava(dirtyState);
//}
