var logger = {
	debug: function(msg) {
		javaLog(msg);
	}
}

// perform oauth (with password grant) for embedded client
oauth = true;
embeddingClientId = getClientId();