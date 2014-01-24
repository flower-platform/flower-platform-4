# Project Description

Hooks the ``HttpServiceServlet`` from Equinox / OSGI HttpService implementation into the ``BridgeServlet`` available in
the host web app. By doing this, the ``org.eclipse.equinox.http.registry`` extension points can be used for serving resources, servlets, etc.