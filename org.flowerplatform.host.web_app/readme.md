# Project Description

This project is not an Equinox plugin. It's a Java web app.

The web application (deployable in a servlet container e.g. Tomcat) that hosts the Equinox environment. 
It starts ``FlowerFrameworkLauncher`` via ``BridgeServlet`` and relays all requests to it (which are handled within Equinox, by Flower Platform plugins).