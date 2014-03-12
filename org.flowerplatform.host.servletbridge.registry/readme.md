# Project Description

Equinox plugin.

Hooks the ``HttpServiceServlet`` from Equinox / OSGI HttpService implementation into the ``BridgeServlet`` available in
the host web app. By doing this, plugins can use ``org.eclipse.equinox.http.registry`` extension points for serving resources, servlets, etc.