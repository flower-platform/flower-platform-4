[![Stories in Ready](https://badge.waffle.io/flower-platform/flower-platform-4.png?label=ready&title=Ready)](https://waffle.io/flower-platform/flower-platform-4)
# Flower Platform 4

Each project contains a ``readme.md`` file with a brief description of the project and associated launch configs (if present).

Most of the projects are Equinox (OSGi) plugins (including the Flex plugins). Some of them are not Equinox plugins, in which case this aspect is mentioned.

Some plugins are "abstract", meaning they contain logic intended to be used by other plugins. Them alone, are not really useful.
"Semi-abstract", are like "abstract", but also add some "final" features. E.g. ``core`` plugin. However, who would have a Flower Platform installation that only has the ``core`` plugin?
