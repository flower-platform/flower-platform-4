# Project Description

This project is not an Equinox plugin. It's a Java lib.

The Equinox "servletbridge" code, modified in order to:
* be usable in a "normal" (non-web) Java app (e.g. Idea IDE), that doesn't know about ``javax.servlet``.
* make minimum modifications to the original code (so that we may update it in the future if needed).

See ``FlowerFrameworkLauncher``.