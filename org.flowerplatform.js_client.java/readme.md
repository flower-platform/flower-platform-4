# Project Description

Equinox plugin.

Client side logic, needed by the Java client. Exposes the communication between java client and server.

## Libraries

The web services are exposed using [Jersey](https://jersey.java.net/) libraries. The dependencies are managed from ``lib-from-maven-pom.xml``. Note that the ``javax.ws.rs`` dependency is intentionally excluded, to avoid class loader issues, as this library is provided by ``org.flowerplatform.util``.