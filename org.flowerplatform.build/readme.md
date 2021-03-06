# Project Description

Build project. For the Java build, we use [Tycho](https://www.eclipse.org/tycho/) to integrate Maven with the OSGi architecture. For the Flex build, we use [flexmojos](https://flexmojos.atlassian.net/wiki/display/FLEXMOJOS/Home).

It is a multi-module build:
* **flex/pom.xml**: multi-module build that aggregates the Flex libraries. Output directories: ``flex/classes`` and ``flex/distr``.
* **java/pom.xml**: multi-module build that aggregates the Flower Platform plug-ins, the product project and test project.  Output directories:
	* ``java/classes`` - compiled classes
	* ``java/distr`` - ``.jar`` files
	* ``java/product`` - Eclipse products
	* ``java/tests`` - test reports
* **../org.flowerplatform.host.web_app/war-pck**: builds the WAR to be deployed on the server.
* **pom.xml**: aggregates all the modules above

### Limitations

Since Tycho does not support multiple modules in the same base directory, we were forced to have our pom files in the ``flex`` and ``java`` sub-directories, instead of having all three files in the project root.

For modules that participate in both builds, (i.e. packaged as both ``eclipse-plugin`` and ``swc``), the pom file for the Java build stays in the project root, per Tycho requirement, and the pom file for the Flex build stays in the ``flex`` sub-directory, for the same reason stated above.

### Flex Build

One-time operation for the Flex build: flexmojos requires a maven repository for the Flex SDK, that can be easily created using the [mavenizer tool](http://flex.apache.org/download-utilities.html) provided by Apache.

How to mavenize your FDK, as described [here](https://cwiki.apache.org/confluence/display/FLEX/Apache+Flex+SDK+Mavenizer):

1. Check-out the utilities project from: https://git-wip-us.apache.org/repos/asf/flex-utilities.git. It should contain a ``mavenizer`` dir.
1. In ``mavenizer``, run ``mvn package`` (or build using the m2 plugin in Eclipse). A jar (e.g. ``flex-sdk-converter-1.0.0-SNAPSHOT.jar``) will be generated in ``mavenizer/target``.
1. Create a ``mavenizer/target/flex-sdk`` directory. Within this dir:
	* Create a ``flex`` directory, and copy your Flex SDK(s) there. E.g. ``mavenizer/target/flex-sdk/flex/4.11.0``.
	* [OPTIONAL] Create an ``air`` directory, and copy your AIR SDK(s) there (optional; probably your SDK already has AIR embeded).
1. In ``mavenizer/target`` directory, run:
```
java -cp flex-sdk-converter-1.0.0-SNAPSHOT.jar SDKGenerator flex-sdk flex-sdk-mavenized true
```
And then deploy the artifacts to your local repository: 
```
java -cp flex-sdk-converter-1.0.0-SNAPSHOT.jar SDKInVMDeployer flex-sdk-mavenized file:\\%USERPROFILE%\.m2\repository
```

### Build

Ant build. Targets:
* **update-license-headers**: update the license headers on source files (``.java``, ``.as``, ``.mxml``)
* **set-version**: set the new version in the ``pom.xml`` files, ``MANIFEST.MF`` files and source files
* **install-flex-deps**: install Flex dependencies in the local repository (must be run once per machine, or every time the libs version changes)
* **build**: perform and validate the build for the Maven projects described above

## Launch Configs

### Flower Platform - Set Version and Build (release).launch

Runs the ``set-version`` and ``build`` targets for a release. The versions should be updated in the ``build.properties`` file, and the qualifier suffix should be set to the empty string for a release.
