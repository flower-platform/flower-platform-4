package org.flowerplatform.freeplane.client;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.ws.rs.core.UriBuilder;

import org.flowerplatform.js_client.java.JsClientJavaPlugin;
import org.flowerplatform.js_client.java.node.ClientNode;
import org.flowerplatform.js_client.java.node.JavaHostServiceInvocator;
import org.mozilla.javascript.BaseFunction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAdapter;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserFunction;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserNavigationEvent;
import chrriis.dj.nativeswing.swtimpl.core.SWTNativeInterface;

/**
 * @author Mariana Gheorghe
 */
public class AuthBrowser extends JDialog {

	private static final long serialVersionUID = 1L;

	private JPanel webBrowserPanel;
	private JWebBrowser webBrowser;
	
	public AuthBrowser() {
		super((Frame) null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		init();
		
		// override default console.log() which would throw an error
		webBrowser.registerFunction(new WebBrowserFunction("javaLog") {
			@Override
			public Object invoke(JWebBrowser browser, Object... msg) {
				System.out.println(msg[0]);
				return null;
			}
		});
		
		webBrowser.registerFunction(new WebBrowserFunction("setAccessToken") {
			
			@Override
			public Object invoke(JWebBrowser browser, Object... args) {
				SWTNativeInterface.close();
				setVisible(false);
				dispose();
				
				final String token = (String) args[0];
				JsClientJavaPlugin.getInstance().setAccessToken(token);
				JavaHostServiceInvocator host = new JavaHostServiceInvocator();
				try {
					host.invoke("userService.getCurrentUser", null, new BaseFunction() {

						private static final long serialVersionUID = 1L;

						@Override
						public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
							ClientNode user = (ClientNode) args[0];
							JOptionPane.showMessageDialog(null, user.getNodeUri() + " " + token, "User has logged in", JOptionPane.INFORMATION_MESSAGE);
							return null;
						}
						
					});
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				
				return null;
			}
		});
		
		webBrowser.registerFunction(new WebBrowserFunction("getClientId") {
			@Override
			public Object invoke(JWebBrowser arg0, Object... arg1) {
				return "testClientId";
			}
		});
		
		// wait for access token
		webBrowser.addWebBrowserListener(new WebBrowserAdapter() {
			
			@Override
			public void locationChanged(WebBrowserNavigationEvent event) {
				String location = event.getNewResourceLocation();
				if (!location.contains("embedInApp")) {
					location = UriBuilder.fromUri(location).queryParam("embedInApp", "java").build().toString();
					event.getWebBrowser().navigate(location);
				}
				super.locationChanged(event);
			}
		});
		
		webBrowser.navigate(getAuthUrl());
	}
	
	/**
	 * Create and add a web browser to this dialog.
	 */
	private void init() {
		SWTNativeInterface.open();
		
		webBrowserPanel = new JPanel(new BorderLayout());
		webBrowser = new JWebBrowser();
		webBrowserPanel.add(webBrowser, BorderLayout.CENTER);
		webBrowser.setMenuBarVisible(false);
		webBrowser.setJavascriptEnabled(true);
		
		add(webBrowserPanel, BorderLayout.CENTER);
		this.setSize(1100, 600);
	}
	
	private String getAuthUrl() {
		return "http://localhost:8080/org.flowerplatform.host.web_app/js_client.core/index.html#auth";
	}
	
}
