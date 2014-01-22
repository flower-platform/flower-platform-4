package org.flowerplatform.flex_client.server.blazeds;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.FilterRegistration.Dynamic;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

public class DummyServletContext implements ServletContext {

	@Override
	public InputStream getResourceAsStream(String arg0) {
		try {
			URL url = new URL("platform:/plugin/org.flowerplatform.flex_client.server/blazeds-services-config" + arg0);
			return url.openStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Dynamic addFilter(String arg0, String arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Dynamic addFilter(String arg0, Filter arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Dynamic addFilter(String arg0, Class<? extends Filter> arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void addListener(Class<? extends EventListener> arg0) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void addListener(String arg0) {
		throw new UnsupportedOperationException();

	}

	@Override
	public <T extends EventListener> void addListener(T arg0) {
		throw new UnsupportedOperationException();

	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
			String arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
			Servlet arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public javax.servlet.ServletRegistration.Dynamic addServlet(String arg0,
			Class<? extends Servlet> arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends Filter> T createFilter(Class<T> arg0)
			throws ServletException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends EventListener> T createListener(Class<T> arg0)
			throws ServletException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T extends Servlet> T createServlet(Class<T> arg0)
			throws ServletException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void declareRoles(String... arg0) {
		throw new UnsupportedOperationException();

	}

	@Override
	public Object getAttribute(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Enumeration<String> getAttributeNames() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ClassLoader getClassLoader() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ServletContext getContext(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getContextPath() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getEffectiveMajorVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getEffectiveMinorVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public FilterRegistration getFilterRegistration(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getInitParameter(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Enumeration<String> getInitParameterNames() {
		throw new UnsupportedOperationException();
	}

	@Override
	public JspConfigDescriptor getJspConfigDescriptor() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMajorVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getMimeType(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMinorVersion() {
		throw new UnsupportedOperationException();
	}

	@Override
	public RequestDispatcher getNamedDispatcher(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getRealPath(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public RequestDispatcher getRequestDispatcher(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public URL getResource(String arg0) throws MalformedURLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> getResourcePaths(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getServerInfo() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Servlet getServlet(String arg0) throws ServletException {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getServletContextName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Enumeration<String> getServletNames() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ServletRegistration getServletRegistration(String arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, ? extends ServletRegistration> getServletRegistrations() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Enumeration<Servlet> getServlets() {
		throw new UnsupportedOperationException();
	}

	@Override
	public SessionCookieConfig getSessionCookieConfig() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void log(String arg0) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void log(Exception arg0, String arg1) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void log(String arg0, Throwable arg1) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void removeAttribute(String arg0) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void setAttribute(String arg0, Object arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean setInitParameter(String arg0, String arg1) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setSessionTrackingModes(Set<SessionTrackingMode> arg0)
			throws IllegalStateException, IllegalArgumentException {
		throw new UnsupportedOperationException();
	}

}
