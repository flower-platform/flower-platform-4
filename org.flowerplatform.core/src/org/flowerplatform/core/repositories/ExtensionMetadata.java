package org.flowerplatform.core.repositories;

import java.util.ArrayList;
import java.util.List;

import org.flowerplatform.util.controller.AbstractController;
import org.flowerplatform.util.controller.IDescriptor;

/**
 * @author Cristina Brinza
 *
 */
public class ExtensionMetadata extends AbstractController implements IDescriptor {

	private String id;
	
	private String label;
	
	private String description;
	
	private List<String> dependencies = new ArrayList<String>();
	
	private String applyMessage;
	
	private String unapplyMessage;
	
	private String color;
		
	public String getId() {
		return id;
	}
	
	//CHECKSTYLE:OFF
	public ExtensionMetadata setId(String id) {
		this.id = id;
		return this;
	}
	//CHECKSTYLE:ON
	
	public String getLabel() {
		return label;
	}
	
	//CHECKSTYLE:OFF
	public ExtensionMetadata setLabel(String label) {
		this.label = label;
		return this;
	}
	//CHECKSTYLE:ON
	
	public String getDescription() {
		return description;
	}
	
	//CHECKSTYLE:OFF
	public ExtensionMetadata setDescription(String description) {
		this.description = description;
		return this;
	}
	//CHECKSTYLE:ON
	
	public List<String> getDependencies() {
		return dependencies;
	}
	
	//CHECKSTYLE:OFF
	public ExtensionMetadata setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
		return this;
	}
	//CHECKSTYLE:ON
	
	public String getApplyMessage() {
		return applyMessage;
	}
	
	//CHECKSTYLE:OFF
	public ExtensionMetadata setApplyMessage(String applyMessage) {
		this.applyMessage = applyMessage;
		return this;
	}
	//CHECKSTYLE:ON
	
	public String getUnapplyMessage() {
		return unapplyMessage;
	}
	
	//CHECKSTYLE:OFF
	public ExtensionMetadata setUnapplyMessage(String unapplyMessage) {
		this.unapplyMessage = unapplyMessage;
		return this;
	}
	//CHECKSTYLE:ON
	
	public String getColor() {
		return color;
	}
	
	//CHECKSTYLE:OFF
	public ExtensionMetadata setColor(String color) {
		this.color = color;
		return this;
	}
	//CHECKSTYLE:ON
	
	@Override
	public String toString() {
		return "ExtensionMetadata [id=" + id + ", label=" + label 
				+ ", description=" + description 
				+ ", dependencies=" + dependencies
				+ ", applyMessage=" + applyMessage
				+ ", unapplyMessage=" + unapplyMessage
				+ ", color=" + color + "]";
	}
}
