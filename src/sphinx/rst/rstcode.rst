.. _rst_code:

Examples of Code in rst
#######################

.. contents:: Table of Contents
.. section-numbering::


Showing Code Inline
===================

.. Error::  TBD





Inline code and references
--------------------------

`reStructuredText`_ is a markup language. It can use roles and
declarations to turn reST into HTML.

In reST, ``*hello world*`` becomes ``<em>hello world</em>``. This is
because a library called `Docutils`_ was able to parse the reST and use a
``Writer`` to output it that way.

If I type ````an inline literal```` it will wrap it in ``<tt>``. You can
see more details on the `Inline Markup`_ on the Docutils homepage.

Also with ``sphinx.ext.autodoc``, which I use in the demo, I can link to
:class:`test_py_module.test.Foo`. It will link you right to my code
documentation for it.

.. _reStructuredText: http://docutils.sourceforge.net/rst.html
.. _Docutils: http://docutils.sourceforge.net/
.. _Inline Markup: http://docutils.sourceforge.net/docs/ref/rst/restructuredtext.html#inline-markup



Blocks of Code
==============

class directive and parameter args
----------------------------------

At this point optional parameters `cannot be generated from code`_.
However, some projects will manually do it, like so:

This example comes from `django-payments module docs`_.

.. class:: payments.dotpay.DotpayProvider(seller_id, pin[, channel=0[, lock=False], lang='pl'])

    :param seller_id: Seller ID assigned by Dotpay
    :param pin: PIN assigned by Dotpay
    :param channel: Default payment channel (consult reference guide)
    :param lang: UI language
    :param lock: Whether to disable channels other than the default selected above

   This backend implements payments using a popular Polish gateway, `Dotpay.pl <http://www.dotpay.pl>`_.

   Due to API limitations there is no support for transferring purchased items.

.. _cannot be generated from code: https://groups.google.com/forum/#!topic/sphinx-users/_qfsVT5Vxpw
.. _django-payments module docs: http://django-payments.readthedocs.org/en/latest/modules.html#payments.authorizenet.AuthorizeNetProvider

This example uses the ``.. class::`` directive to format ``payments.dotpay.DotpayProvider(seller_id, pin[, channel=0[, lock=False], lang='pl'])`` and automatically put the word `class` in front of it.
The ``:param`` args follow the class line.  The optional ``:param`` args nicely format the parameters.

Here's the rst for the entire example above, including the references:

.. parsed-literal::
    At this point optional parameters `cannot be generated from code`_.
    However, some projects will manually do it, like so:

    This example comes from `django-payments module docs`_.

    .. class:: payments.dotpay.DotpayProvider(seller_id, pin[, channel=0[, lock=False], lang='pl'])

        :param seller_id: Seller ID assigned by Dotpay
        :param pin: PIN assigned by Dotpay
        :param channel: Default payment channel (consult reference guide)
        :param lang: UI language
        :param lock: Whether to disable channels other than the default selected above

       This backend implements payments using a popular Polish gateway, `Dotpay.pl <http://www.dotpay.pl>`_.

       Due to API limitations there is no support for transferring purchased items.

    .. _cannot be generated from code: https://groups.google.com/forum/#!topic/sphinx-users/_qfsVT5Vxpw
    .. _django-payments module docs: http://django-payments.readthedocs.org/en/latest/modules.html#payments.authorizenet.AuthorizeNetProvider



parsed-literal
--------------

Using the ``..parsed-literal::`` directive will show text without reformatting it.  It will do some simple formatting (coloring)
but will often show the text in an ugly, large monospaced font.  (Although you can change this with your own rst template.)


.. parsed-literal::

    # parsed-literal test
    curl -O http://someurl/release-|version|.tar-gz

Here's the rst for the above:

.. parsed-literal::
    .. parsed-literal::

        # parsed-literal test
        curl -O http://someurl/release-|version|.tar-gz



code-block
----------

The ``.. code-block::`` directive looks much better than ``.. parsed-literal::``.  (At least with the default Read The Docs template.)
The ``.. code-block::`` directive will try to display the text that follows it as programming code.  If you specify a language that rst knows about,
it will also format the text in a way that makes sense for that language.

Here's and example of a code block without any formatting or coloring:

.. code-block:: text

    print "Hello world"

    def some_function():
        interesting = False
        if interesting print 'This is a nonsensical function.'


The language specified is `text`.  That tells rst to **not** apply any formatting or coloring.

.. parsed-literal:: text
    .. code-block:: text

        print "Hello world"

        def some_function():
            interesting = False
            if interesting print 'This is a nonsensical function.'


You **must** specify a language after ``.. code-block::``.  If you don't rst will produce a console warning and won't display the code at all.


Here's that same example with ``ruby`` specified as the language.  With the language specified, keywords are now nicely colored:

.. code-block:: ruby

    print "Hello world"
    def some_function():
        interesting = False
        if interesting print 'This is a nonsensical function.'


Here's the rst; the only difference is the word `ruby` after the ``.. code-block::`` directive to specify the language.  (Note the space required between ``.. code-block::`` and ``ruby``!)

.. parsed-literal::
    .. code-block:: ruby

        print "Hello world"

        def some_function():
            interesting = False
            if interesting print 'This is a nonsensical function.'





.. code-block:: json

    {
    "windows": [
        {
        "panes": [
            {
            "shell_command": [
                "echo 'did you know'",
                "echo 'you can inline'"
            ]
            },
            {
            "shell_command": "echo 'single commands'"
            },
            "echo 'for panes'"
        ],
        "window_name": "long form"
        }
    ],
    "session_name": "shorthands"
    }


Include code from a source file
-------------------------------

.. literalinclude:: /Keys.scala
    :language: scala
    :linenos:
    :lines: 1-40
    :caption: Live code from /src/main/scala/com/typesafe/sbt/packager/universal/Keys.scala


This is taken from the actual source file.  The rst code can reference either absolute file paths or
relative file paths.  (Absolute file paths are brittle, so should be avoided of course.)  With *relative file paths,*
rst can only refer to files under the project's `.../src/sphinx` directory.  But you can create a symbolic link in
`.../src/sphinx` that refers to some other file.  That's how this code is referenced: within rst, it refers to a file in
`.../src/sphinx` that is actually a symbolic link to the real source file.


literalinclude includes content from a file without interpreting it
-------------------------------------------------------------------

If you want to include the contents of a file but not have rst interpret it, use the `literalinclude` directive.
For example, if you want to show the contents of a file (or parts of it) but that file contains text that are rst directives
or some other code that rst might read as instructions
and you don't want rst to execute (do) those directives, use `literalinclude`.

(TBD: link to the rst documentation)

Here's how `literalinclude` was used to read lines from a source file and include them above:

.. parsed-literal::
   .. literalinclude:: /Keys.scala
        :language: scala
        :linenos:
        :lines: 1-40
        :caption: Live code from /src/main/scala/com/typesafe/sbt/packager/universal/Keys.scala`


parsed-literal shows text without interpreting it
-------------------------------------------------

And here is how to get the above to show as code and not be interpreted by the rst parser as directives:

.. parsed-literal::
    .. parsed-literal::
        .. literalinclude:: /Keys.scala
            :language: scala
            :linenos:
            :lines: 1-40
            :caption: Live code from /src/main/scala/com/typesafe/sbt/packager/universal/Keys.scala`



Emphasized lines with line numbers
==================================

.. code-block:: ruby
   :linenos:
   :emphasize-lines: 3,5

       def some_function():
           interesting = False
           print 'This line is highlighted.'
           print 'This one is not...'
           print '...but this one is.'

You can use the ``:linenos`` and ``:emphasize-lines:`` codes to show line numbers and to highlight specific lines of code, respectively.

Here's the rst used for the above ruby code:

.. parsed-literal::
    .. code-block:: ruby
        :linenos:
        :emphasize-lines: 3,5

        def some_function():
            interesting = False
            print 'This line is highlighted.'
            print 'This one is not...'
            print '...but this one is.'




