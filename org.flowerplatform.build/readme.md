# Project Description

Build project. For the Java build, we use [Tycho](https://www.eclipse.org/tycho/) to integrate Maven with the OSGi architecture. For the Flex build, we use [flexmojos](https://flexmojos.atlassian.net/wiki/display/FLEXMOJOS/Home).

It is a multi-module build:
* ``flex/pom.xml`` multi-module build that aggregates the Flex libraries.
* ``java/pom.xml`` multi-module build that aggregates the Flower Platform plug-ins, the product project and test project.
* ``../org.flowerplatform.host.web_app/war-pck11 builds the WAR to be deployed on the server.
* ``pom.xml`` aggregates all the modules above

### Limitations

Since Tycho does not support multiple modules in the same base directory, we were forced to have our pom files in the ``flex`` and ``java`` sub-directories, instead of having all three files in the project root.

For modules that participate in both builds, (i.e. packaged as both ``eclipse-plugin`` and ``swc``), the pom file for the Java build stays in the project root, per Tycho requirement, and the pom file for the Flex build stays in the ``flex`` sub-directory, for the same reason stated above.

### Flex Build

One-time operation for the Flex build: flexmojos requires a maven repository for the Flex SDK, that can be easily created using the [mavenizer tool](http://flex.apache.org/download-utilities.html) provided by Apache.

How to mavenize your FDK, as described [here](https://cwiki.apache.org/confluence/display/FLEX/Apache+Flex+SDK+Mavenizer):
1. Check-out the utilities project from: https://git-wip-us.apache.org/repos/asf/flex-utilities.git
2. Run ``mvn package`` from the ``mavenizer`` directory (or build using the m2 plugin in Eclipse). The ``flex-sdk-converter-1.0.jar`` should now be in the ``target`` directory.
3. Create a [home] directory. Create a ``[home]/flex-sdk/flex`` directory, and copy your FDK there.
4. From the ``mavenizer/target`` directory, run ``java -cp flex-sdk-converter-1.0.jar SDKGenerator "[home]/flex-sdk/" "[home]/flex-sdk-mavenized" true``
5. Deploy the artifacts to your local repository: ``java -cp flex-sdk-converter-1.0.jar SDKInVMDeployer "[home]/flex-sdk-mavenized" "file:\\C:\Users\NormalUser\.m2\repository"``

## Launch Configs

### Flower Platform - Build (Flex).launch

Builds the flex ``.swc`` and ``.swf`` files. Output directories: ``flex/classes`` and ``flex/distr``.

### Flower Platform - Build (Flex verbose).launch

Same, with full logging and errors stack traces enabled.

### Flower Platform - Build (Java).launch

Builds the FP plug-ins, runs tests and builds the Eclipse product with the packaged FP plug-ins and dependencies. Output directories:
* ``java/classes`` - compiled classes
* ``java/distr`` - ``.jar`` files
* ``java/product`` - Eclipse products
* ``java/tests`` - test reports

### Flower Platform - Build (Java verbose).launch

Same, with full logging and errors stack traces enabled.

### Flower Platform - Build (win32 | linux x86).launch

Performs both the Flex build and the Java build. Builds the WAR with the Eclipse product built for the specified OS.

### Flower Platform - Build (verbose).launch

Same, with full logging and errors stack traces enabled.