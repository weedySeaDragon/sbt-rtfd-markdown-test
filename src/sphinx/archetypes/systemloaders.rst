.. _systemloaders:

Systemloaders
=============

SBT native packager provides support for different systemloaders in order to register your application as a service on
your target system, start it automatically and provide systemloader specific configuration.

.. tip:: You can use systemloaders with the :ref:`java-app-plugin` or the :ref:`java-server-plugin`!

Overview
--------

There is a generic ``SystemloaderPlugin`` which configures default settings and requires necessary plugins. It gets
triggered automatically when you enable a specific systemloader plugin. If you want to implement your own loader,
you should require the ``SystemloaderPlugin``.

General Settings
~~~~~~~~~~~~~~~~

  ``serverLoading``
    Loading system to be used for application start script (SystemV, Upstart, Systemd).
    This setting can be used to trigger systemloader specific behaviour in your build.

  ``serverAutostart``
    Determines if service will be automatically started after installation.  The default value is true.

  ``startRunlevels``
    Sequence of runlevels on which the application will start up

  ``stopRunlevels``
    Sequence of runlevels on which the application will stop

  ``requiredStartFacilities``
    Names of system services that should be provided at application start

  ``requiredStopFacilities``
    Names of system services that should be provided at application stop

  ``killTimeout``
    Timeout before sigkill on stop (after term)

  ``termTimeout``
    Timeout before sigterm on stop
    
  ``retries``
    Number of retries to start service

  ``retryTimeout``
    Timeout between retries in seconds


SystemV
-------

SBT native packager provides different SysV scripts for ``rpm`` (CentOS, RHEL, Fedora) and ``debian`` (Debian, Ubuntu)
package based systems. Enable SystemV with:

.. code-block:: scala

    enablePlugins(SystemVPlugin)

The :ref:`java-server-plugin` provides a ``daemonStdoutLogFile`` setting that you can use to redirect the SystemV
output into a file.

Systemd
-------

In order to enable Systemd add this plugin:

.. code-block:: scala

    enablePlugins(SystemdPlugin)

Settings
~~~~~~~~

  ``systemdSuccessExitStatus``
    Takes a list of exit status definitions that when returned by the main service process will be considered successful
    termination, in addition to the normal successful exit code ``0`` and the signals ``SIGHUP``, ``SIGINT``,
    ``SIGTERM``, and ``SIGPIPE``. Exit status definitions can either be numeric exit codes or termination signal names.

Upstart
-------

Upstart is a SystemV alternative developed by `Ubuntu <http://upstart.ubuntu.com/>`_. SBT native packager adds support for rpm as well,
but we recommend using `Systemd` instead if possible.

.. code-block:: scala

    enablePlugins(UpstartPlugin)

.. note:: The Fedora/RHEL/Centos family of linux specifies ``Default requiretty`` in its ``/etc/sudoers``
    file. This prevents the default Upstart script from working correctly as it uses sudo to run the application
    as the ``daemonUser``. Simply disable requiretty to use Upstart or modify the Upstart template.

Customization
-------------

SBT native packager provides general settings to customize the created systemloader scripts.

SBT native packager uses **templates** to generate scripts and other files required for specific systems.
A base template (input) file can have variables; these variables will be replaced with actual values in the target file generated from the template (output).
See the examples below for more information on using templates.


Start Script Location
~~~~~~~~~~~~~~~~~~~~~

In order to change the location of the systemloader script/config file you need to adjust the
``defaultLinuxStartScriptLocation`` like this:

.. code-block:: scala

  defaultLinuxStartScriptLocation in Debian := "/lib/systemd/system"


You may need to change these paths according to your distribution. References are

- `Ubuntu systemd documentation <https://wiki.ubuntu.com/systemd>`_
- `Debian systemd documentation <https://wiki.debian.org/Teams/pkg-systemd/Packaging>`_
- `RHEL systemd documentation <https://access.redhat.com/documentation/en-US/Red_Hat_Enterprise_Linux/7/html/System_Administrators_Guide/chap-Managing_Services_with_systemd.html>`_

Customize Start Script
~~~~~~~~~~~~~~~~~~~~~~

If you want to use specific start and stop scripts, you can specify the template files to be used.
As an example, to alter the ``loader-functions`` which manage the specific start and stop process commands
for SystemLoaders, you can put your template file in a specifc location and add that
to the ``linuxScriptReplacements`` map:

.. code-block:: scala

  import com.typesafe.sbt.packager.archetypes.TemplateWriter

  linuxScriptReplacements += {
    // Specify where your template files can be found:
    val loadersTemplateDir = sourceDirectory.value / "templates" / "custom-loader-functions"

    // replacements == Nil  for this example
    // If you want to replace variables in your script (template) with values,
    // put them in a Seq[(String,String)]  (see other customizations for examples)

    // Add the "loader-functions" key. The value is a function that will
    // generate the results from the template (TemplateWriter.generateScript(..)
    "loader-functions" -> TemplateWriter.generateScript(loadersTemplateDir.toURL, Nil)
  }

which will generate the following resource file (which is used to start/stop instead of initctl in the post install script):

.. code-block:: bash

  startService() {
      app_name=$1
      start $app_name
  }

  stopService() {
      app_name=$1
      stop $app_name
  }

The :doc:`debian </formats/debian>` and :doc:`redhat </formats/rpm>` pages have further information on overriding
distribution specific actions.

Override Start Script
~~~~~~~~~~~~~~~~~~~~~

It's also possible to override the entire script/configuration for your service manager.
Create a file ``src/templates/systemloader/$loader`` and it will be used instead.

Possible values:

* ``$loader`` - ``upstart``, ``systemv`` or ``systemd``

**Syntax**

You can use ``${{variable_name}}`` to reference variables when writing your script.  The default set of variables is:

* ``descr`` - The description of the server.
* ``author`` - The configured author name.
* ``exec`` - The script/binary to execute when starting the server
* ``chdir`` - The working directory for the server.
* ``retries`` - The number of times to retry starting the server.
* ``retryTimeout`` - The amount of time to wait before trying to run the server.
* ``app_name`` - The name of the application (linux friendly)
* ``app_main_class`` - The main class / entry point of the application.
* ``app_classpath`` - The (ordered) classpath of the application.
* ``daemon_user`` - The user that the server should run as.
* ``daemon_log_file`` - Absolute path to daemon log file.
