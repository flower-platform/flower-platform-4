# Project Description

Equinox plugin.

Java Utility classes, intended to be usable as well outside Flower Platform (maybe outside of a OSGi container as well).

## Libraries

* ``commons-io``, ``logback-*``, ``slf4j-api``: common libraries useful for several plugins.
* ``javax.ws.rs-api``: expose annotations needed to customize classes exposed via web-service/JSON. E.g. serialization preferences (for beans), end points (for services). 