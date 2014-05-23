# Project Description

This project is not an Equinox plugin. It's a Java web app.

The web application (deployable in a servlet container e.g. Tomcat) that hosts the Equinox environment. 
It starts ``FlowerFrameworkLauncher`` via ``BridgeServlet`` and relays all requests to it (which are handled within Equinox, by Flower Platform plugins).

## Launch Configs

### Flower Platform - Host WebApp (write plugin config to file).launch

Holds the plugins needed for Flower Platform / Web. It needs to be ran each time a plugin is added/removed, so that Eclipse/PDE can write the proper ``config.ini`` file. 
