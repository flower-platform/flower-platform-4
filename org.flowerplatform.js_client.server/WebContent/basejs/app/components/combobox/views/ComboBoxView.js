define(["basejs/app/views/BaseCollectionView",         
        "backbone",
        "text!basejs/app/components/combobox/tpl/ComboBoxTpl.html"], 
function(BaseCollectionView, 		 
		 Backbone,
		 ComboBoxTpl){
	/**
	 * This view can be customizable by setting on the constructor the labelField, valueFiled, selectedIndex
	 * 
	 * It accepts as data provider both Backbone collections and simple javascript arrays. 
	 * 
	 * In the case the dataProvider is a simple javascript array, each of the items of this array
	 * of the should store its attributes (like labelField and value field) in a property called attributes 
	 */
	return BaseCollectionView.extend({
		tpl: ComboBoxTpl,
		sortItems: false, 
		initialize: function(){
			BaseCollectionView.prototype.initialize.apply(this); 
			if (!this.labelField) {
				this.labelField = "description";
			}
			if (!this.valueField) {
				this.valueField = "id";
			}
			
			if (!this.selectedIndex) {
				this.selectedIndex = 0;
			}
			
			this.events["click .model-filter a"] = "selectedItemChangedHandler";
			if (this.basedOnBackboneCollection) {
				this.collection.fetch();
			}
		}, 
		
		events: {},
		
		render: function() {
//			if (sortItems && this.basedOnBackboneCollection) {
//				this.collection.sort();
//			}
			
			BaseCollectionView.prototype.render.apply(this); 
			if (this.basedOnBackboneCollection && this.collection.models && this.collection.models.length > 0
					|| !this.basedOnBackboneCollection && this.collection && this.collection.length > 0) {
				this.changeSelectedIndex(this.selectedIndex);
			}
			return this;
		},
		
		selectedItemChangedHandler: function(e) {
			e.preventDefault();
			this.changeSelectedItem(e.currentTarget);
		},
		
		changeSelectedIndex: function(targetIndex) {
			this.changeSelectedItem(this.$el.find(".model-filter a")[targetIndex]);
		},
		
		changeSelectedItem: function(targetItem) {
			var selectedModelId = $(targetItem).attr("data-model-id");
			
			this.selectedIndex = this.$el.find(".model-filter a").index(targetItem);
			
			
			//change the selection of the drop-down
			var button = this.$el.find(".model-filter button");
			var replace = this.$el.find('.model-filter a[data-model-id="' + selectedModelId + '"]');
			if (replace.length) {
				button.html(replace.text());
				button.append(" <span class='caret'></span>");
			}
			
			//re-render the selected report parameters
			if (this.basedOnBackboneCollection) {
				this.selectedModel = _.find(this.collection.models, function(model){
															return model.attributes.id == selectedModelId;});
			} else {
				this.selectedModel = _.find(this.collection, function(model){
					return model.attributes.id == selectedModelId;});
			}
			this.trigger("item:changed", {selectedModel:this.selectedModel});
			
		},
		
		getValue: function() {
			return this.selectedModel.attributes[this.valueField];
		}
	});
});