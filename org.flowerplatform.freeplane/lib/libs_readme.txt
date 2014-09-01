===== freeplane 27.01.2014 =====
This libraries:
freeplaneviewer.jar
commons-lang-2.6.jar
gnu-regexp-1.1.4.jar
jortho.jar
SimplyHTML.jar
freeplaneeditor.jar

have been copied from Freeplane -> build 1.3.6_beta.

A new build was needed because there are no bin sources available for a newer Freeplane version (1.3.x).
The Freeplane 1.2.x does not contain the headless behavior.

http://freeplane.sourceforge.net/wiki/index.php/How_to_build_Freeplane

===== freeplaneeditor.jar =====

Version : build 1.3.6_beta

The same with freeplaneeditor.jar except : 
- META-INF/MANIFEST.MF (removed "../" from class path)