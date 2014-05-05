# Project Description

Build project. Uses Tycho to integrate Maven with the OSGi architecture.

It is a multi-module project, where the ``pom.xml`` file lists the Flower Platform plug-ins to build, the product project and test project.

## Launch Configs

### Flower Platform - Build.launch

Builds the FP plug-ins, runs tests and builds the Eclipse product with the packaged FP plug-ins and dependencies.

### Flower Platform - Build (verbose).launch

Same, with full logging and errors stack traces enabled.