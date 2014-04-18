define(["backbone"], 
  function(Backbone){
	
	return Backbone.Collection.extend({
		
		/**
		 * Compose the parameters to append based on sort/filter/page
		 */
		composeUrl: function() {
			var url = '';
			if (this.sortUrlParameters) {
				url += "?sort=" + encodeURIComponent(this.sortUrlParameters);
			}
			if (this.filterUrlParameters) {
				if (url != '') {
					url += "&";
				}
				else {
					url += "?";
				}
				url += "filter=" + encodeURIComponent(this.filterUrlParameters);
			}
			if (this.page) {
				if (url != '') {
					url += '&';
				} else {
					url += "?";
				}
				url += "page=" + this.page;
			}
			if (this.pageSize) {
				if (url != '') {
					url += '&';
				} else {
					url += '?';
				}
				url += "page_size=" + this.pageSize;
			}

			return url;
		}
	});
});