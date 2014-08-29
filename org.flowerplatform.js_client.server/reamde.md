# Project Description

Equinox plugin.

Server side logic, needed by the JavaScript client. Exposes the registered RESTful web services via a servlet mapped to ``ws-dispatcher``.

## Libraries

The web services are exposed using [Jersey](https://jersey.java.net/) libraries. The dependencies are managed from ``lib-from-maven-pom.xml``. Note that the ``javax.ws.rs`` dependency is intentionally excluded, to avoid class loader issues, as this library is provided by ``org.flowerplatform.util``.