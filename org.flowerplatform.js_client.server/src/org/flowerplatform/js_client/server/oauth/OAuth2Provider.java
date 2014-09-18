package org.flowerplatform.js_client.server.oauth;

import javax.xml.bind.annotation.XmlTransient;

import org.flowerplatform.core.CorePlugin;

/**
 * @author Mariana Gheorghe
 */
public class OAuth2Provider {

	private String name;
	private String uri;
	private String label;
	private String clientId;
	private String clientSecret;
	private String scope;
	
	public OAuth2Provider(String name) {
		this.name = name;
		this.uri = readProperty("uri");
		this.label = readProperty("label");
		this.clientId = readProperty("clientId");
		this.clientSecret = readProperty("clientSecret");
		this.scope = readProperty("scope");
	}
	
	private String readProperty(String key) {
		return CorePlugin.getInstance().getFlowerProperties().getProperty("oauth.provider." + name + "." + key);
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getUri() {
		return uri;
	}
	
	public void setUri(String uri) {
		this.uri = uri;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getClientId() {
		return clientId;
	}
	
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	
	@XmlTransient
	public String getClientSecret() {
		return clientSecret;
	}
	
	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}
	
	public String getScope() {
		return scope;
	}
	
	public void setScope(String scope) {
		this.scope = scope;
	}
	
}
