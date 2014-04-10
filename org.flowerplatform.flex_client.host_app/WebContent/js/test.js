function get(url, data, successCallback) {
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: url,
		data: data,
		success: successCallback
	});
}

function post(url, data, successCallback) {
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: url,
		data: data,
		success: successCallback
	});
}

function overrideMeHandler() {
	$("#override").text("No override from Flex! Uncomment this function from embed.js");
}