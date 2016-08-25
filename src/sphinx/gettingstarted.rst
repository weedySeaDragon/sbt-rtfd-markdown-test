.. _getting-started:


Getting Started
###############

Setup
-----

Sbt-native-packager is an *AutoPlugin*. Add it to your ``plugins.sbt``

.. code-block:: scala

  // Replace "x.y.z" with the version you want to use

  addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "x.y.z")


The latest version is |version|

The latest release is |release|


Native Tools
~~~~~~~~~~~~

Depending on the package format you want to create, you may need additional tools available on your machine.
Each :ref:`packaging format <packaging-formats>` has a requirements section.


Your first package
------------------

Native packager provides :ref:`packaging format plugins and archetype plugins <formats-and-archetypes>` to separate
configuration and actual packaging. To get started we use the basic :ref:`Java Application Archetype <java-app-plugin>`.
For more archetypes see the :ref:`archetypes page <archetypes>`.


In your ``build.sbt`` you need to enable the archetype like this

.. code-block:: scala

  enablePlugins(JavaAppPackaging)

This will also enable all supported format plugins.

Run the app
~~~~~~~~~~~

Native packager can *stage* your app so you can run it locally without having the app packaged.

.. code-block:: bash

   sbt stage
   ./target/com.typesafe.sbt.packager.universal/stage/bin/<your-app>


Create a package
~~~~~~~~~~~~~~~~

We can generate other packages via the following tasks. Note that each packaging format may needs some additional
configuration and native tools available. Here's a complete list of current formats.

* ``com.typesafe.sbt.packager.universal:packageBin`` - Generates a com.typesafe.sbt.packager.universal zip file
* ``com.typesafe.sbt.packager.universal:packageZipTarball`` - Generates a com.typesafe.sbt.packager.universal tgz file
* ``debian:packageBin`` - Generates a deb
* ``docker:publishLocal`` - Builds a Docker image using the local Docker server
* ``rpm:packageBin`` - Generates an rpm
* ``com.typesafe.sbt.packager.universal:packageOsxDmg`` - Generates a DMG file with the same contents as the com.typesafe.sbt.packager.universal zip/tgz.
* ``windows:packageBin`` - Generates an MSI
