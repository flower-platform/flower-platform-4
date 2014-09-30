package org.flowerplatform.freeplane.client;

import java.awt.BorderLayout;
import java.awt.Frame;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import org.flowerplatform.js_client.java.JsClientJavaPlugin;

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

		// wait for access token
		webBrowser.addWebBrowserListener(new WebBrowserAdapter() {
			@Override
			public void locationChanged(WebBrowserNavigationEvent event) {
				String location = event.getNewResourceLocation();
				if (location.contains("authAccess.html")) {
					// get the access token
					String accessToken = location.substring(location.indexOf("access_token=") + 13);
					JsClientJavaPlugin.getInstance().setAccessToken(accessToken);
					setVisible(false);
					dispose();
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
		return "http://localhost:8080/org.flowerplatform.host.web_app/js_client.core/index.html?embedInApp=java";
	}
	
}
