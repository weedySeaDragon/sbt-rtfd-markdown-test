.. _rst_demo:

Misc rst Markup Examples
########################

.. This is a comment. Note how any initial comments are moved by
    transforms to after the document title, subtitle, and docinfo.

 ==============================
 reStructuredText Demonstration
 ==============================

.. Above is the document title, and below is the subtitle.
    They are transformed from section titles after parsing.

 ------------------------------
 Examples of Syntax Constructs
 ------------------------------

.. bibliographic fields (which also require a transform):

:Author: David Goodger
:Address: 123 Example Street
              Example, EX  Canada
              A1B 2C3
:Contact: docutils-develop@lists.sourceforge.net
:Authors: Me; Myself; I
:organization: humankind
:date: $Date: 2012-01-03 19:23:53 +0000 (Tue, 03 Jan 2012) $
:status: This is a "work in progress"
:revision: $Revision: 7302 $
:version: 1
:copyright: This document has been placed in the public domain. You
                may do with it as you wish. You may copy, modify,
                redistribute, reattribute, sell, buy, rent, lease,
                destroy, or improve it, quote it at length, excerpt,
                incorporate, collate, fold, staple, or mutilate it, or do
                anything else to it that your or anyone else's heart
                desires.
:field name: This is a generic bibliographic field.
:field name 2:
        Generic bibliographic fields may contain multiple body elements.

        Like this.

:Dedication:

        For Docutils users & co-developers.

:abstract:

        This document is a demonstration of the reStructuredText markup
        language, containing examples of all basic reStructuredText
        constructs and many advanced constructs.

.. meta::
    :keywords: reStructuredText, demonstration, demo, parser
    :description lang=en: A demonstration of the reStructuredText
           markup language, containing examples of all basic
           constructs and many advanced constructs.

.. contents:: Table of Contents
.. section-numbering::



Giant tables
============

+------------+------------+-----------+------------+------------+-----------+------------+------------+-----------+------------+------------+-----------+
| Header 1   | Header 2   | Header 3  | Header 1   | Header 2   | Header 3  | Header 1   | Header 2   | Header 3  | Header 1   | Header 2   | Header 3  |
+============+============+===========+============+============+===========+============+============+===========+============+============+===========+
| body row 1 | column 2   | column 3  | body row 1 | column 2   | column 3  | body row 1 | column 2   | column 3  | body row 1 | column 2   | column 3  |
+------------+------------+-----------+------------+------------+-----------+------------+------------+-----------+------------+------------+-----------+
| body row 1 | column 2   | column 3  | body row 1 | column 2   | column 3  | body row 1 | column 2   | column 3  | body row 1 | column 2   | column 3  |
+------------+------------+-----------+------------+------------+-----------+------------+------------+-----------+------------+------------+-----------+
| body row 1 | column 2   | column 3  | body row 1 | column 2   | column 3  | body row 1 | column 2   | column 3  | body row 1 | column 2   | column 3  |
+------------+------------+-----------+------------+------------+-----------+------------+------------+-----------+------------+------------+-----------+
| body row 1 | column 2   | column 3  | body row 1 | column 2   | column 3  | body row 1 | column 2   | column 3  | body row 1 | column 2   | column 3  |
+------------+------------+-----------+------------+------------+-----------+------------+------------+-----------+------------+------------+-----------+


Sidebar
=======

.. sidebar:: Ch'ien / The Creative

    .. image:: static/yi_jing_01_chien.jpg

    *Above* CH'IEN THE CREATIVE, HEAVEN

    *Below* CH'IEN THE CREATIVE, HEAVEN

The first hexagram is made up of six unbroken lines. These unbroken lines stand for the primal power, which is light-giving, active, strong, and of the spirit. The hexagram is consistently strong in character, and since it is without weakness, its essence is power or energy. Its image is heaven. Its energy is represented as unrestricted by any fixed conditions in space and is therefore conceived of as motion. Time is regarded as the basis of this motion. Thus the hexagram includes also the power of time and the power of persisting in time, that is, duration.

The power represented by the hexagram is to be interpreted in a dual sense in terms of its action on the universe and of its action on the world of men. In relation to the universe, the hexagram expresses the strong, creative action of the Deity. In relation to the human world, it denotes the creative action of the holy man or sage, of the ruler or leader of men, who through his power awakens and develops their higher nature.

Boxes
=====

.. tip:: Tip: Enable math extensions if you want equations to show up.


.. note:: This is a note about math equations.


.. danger:: Danger! Math can be addictive.


.. warning:: Warning: Math can be frustrating.


Table: Every other row
======================

The default Read The Docs rst template formats every other line in a table with white text on a white background:

    +---------+
    | Example |
    +=========+
    | Thing1  |
    +---------+
    | Thing2  |
    +---------+
    | Thing3  |
    +---------+


Citation
========

Here I am making a citation [1]_, another [2]_ and another [3]_

.. [1] This is the citation I made, let's make this extremely long so that we can tell that it doesn't follow the normal responsive table stuff.

.. [2] This citation has some ``code blocks`` in it, maybe some **bold** and
       *italics* too. Heck, lets put a link to a meta citation [3]_ too.

.. [3] This citation will have two backlinks.

======
Images
======

.. figure:: static/yi_jing_01_chien.jpg

    This is a caption for a figure.

Download links
==============

:download:`This long long long long long long long long long long long long long long long download link should be blue with icon, and should wrap white-spaces <static/yi_jing_01_chien.jpg>`



Substitution
============

See `substitutions in the official RST reference`_

.. _substitutions in the official RST reference: http://docutils.sourceforge.net/docs/ref/rst/restructuredtext.html#substitution-references


Original code for this page is from https://github.com/snide/sphinx_rtd_theme/blob/master/demo_docs/source/demo.rst
