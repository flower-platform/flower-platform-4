function get(url, data, successCallback, headers) {
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: url,
		data: data,
		headers: headers,
		success: successCallback,
	});
}

function post(url, data, successCallback, headers) {
	$.ajax({
		type: "POST",
		contentType: "application/json",
		url: url,
		data: data,
		headers: headers,
		success: successCallback
	});
}

function put(url, data, successCallback, headers) {
	$.ajax({
		type: "PUT",
		contentType: "application/json",
		url: url,
		data: data,
		headers: headers,
		success: successCallback
	});
}

function overrideMeHandler() {
	$("#override").text("No override from Flex! Uncomment this function from embed.js");
}