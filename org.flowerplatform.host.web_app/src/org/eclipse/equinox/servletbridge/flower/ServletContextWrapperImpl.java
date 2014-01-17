package org.eclipse.equinox.servletbridge.flower;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

import javax.servlet.ServletContext;

/**
 * @author Cristian Spiescu
 */
public class ServletContextWrapperImpl implements ServletContextWrapper {

	protected ServletContext servletContext; 
	
	public ServletContextWrapperImpl(ServletContext servletContext) {
		super();
		this.servletContext = servletContext;
	}

	@Override
	public String getTempDir() {
		return ServletContext.TEMPDIR;
	}

	@Override
	public URL getResource(String path) throws MalformedURLException {
		return servletContext.getResource(path);
	}

	@Override
	public void log(String msg) {
		servletContext.log(msg);
	}

	@Override
	public void log(String msg, Throwable throwable) {
		servletContext.log(msg, throwable);
	}

	@Override
	public Object getAttribute(String name) {
		return servletContext.getAttribute(name);
	}

	@Override
	public int getMajorVersion() {
		return servletContext.getMajorVersion();
	}

	@Override
	public int getMinorVersion() {
		return servletContext.getMinorVersion();
	}

	@Override
	public Set<String> getResourcePaths(String path) {
		return servletContext.getResourcePaths(path);
	}

	@Override
	public InputStream getResourceAsStream(String path) {
		return servletContext.getResourceAsStream(path);
	}

	@Override
	public String getRealPath(String path) {
		return servletContext.getRealPath(path);
	}

	@Override
	public String getContextPath() {
		return servletContext.getContextPath();
	}

}
