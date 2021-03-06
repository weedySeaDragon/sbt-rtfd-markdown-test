.. _rst_demo_rstmarkup:

rst Structural Element Examples
###############################

Here's the `quick reference for RST`_.

.. _quick reference for RST: http://docutils.sourceforge.net/docs/user/rst/quickref.html


.. contents:: Table of Contents
.. section-numbering::



Transitions
-----------

Here's a transition line:

---------

It divides the section.


Inline Markup
-------------

Paragraphs contain text and may contain inline markup: *emphasis*,
**strong emphasis**, ``inline literals``, standalone hyperlinks
(http://www.python.org), external hyperlinks (Python_), internal
cross-references (example_), external hyperlinks with embedded URIs
(`Python web site <http://www.python.org>`__), footnote references
(manually numbered [1]_, anonymous auto-numbered [#]_, labeled
auto-numbered [#label]_, or symbolic [*]_), citation references
([CIT2002]_), and _`inline
hyperlink targets` (see Targets_ below for a reference back to here).
Character-level inline markup is also possible (although exceedingly
ugly!) in *re*\ ``Structured``\ *Text*.

The default role for interpreted text is `Title Reference`.  Here are
some explicit interpreted text roles: a PEP reference (:PEP:`287`); an
RFC reference (:RFC:`2822`); a :sub:`subscript`; a :sup:`superscript`;
and explicit roles for :emphasis:`standard` :strong:`inline`
:literal:`markup`.

.. DO NOT RE-WRAP THE FOLLOWING PARAGRAPH!

Let's test wrapping and whitespace significance in inline literals:
``This is an example of --inline-literal --text, --including some--
strangely--hyphenated-words.  Adjust-the-width-of-your-browser-window
to see how the text is wrapped.  -- ---- --------  Now note    the spacing    between the    words of    this sentence    (words should    be grouped    in pairs).``

If the ``--pep-references`` option was supplied, there should be a
live link to PEP 258 here.


Body Elements
-------------



Section Title
=============

That's a section title: the text just above this line.


Paragraphs
==========

A paragraph.

Bullet Lists
============

- A bullet list

  + Nested bullet list.
  + Nested item 2.

- Item 2.

  Paragraph 2 of item 2.

  * Nested bullet list.
  * Nested item 2.

    - Third level.
    - Item 2.

  * Nested item 3.

Enumerated Lists
================

1. Arabic numerals.

   a) lower alpha)

      (i) (lower roman)

          A. upper alpha.

             I) upper roman)

2. Lists that don't start at 1:

   3. Three

   4. Four

   C. C

   D. D

   iii. iii

   iv. iv

#. List items may also be auto-enumerated.

Definition Lists
================

Term
    Definition
Term : classifier
    Definition paragraph 1.

    Definition paragraph 2.
Term
    Definition

Formatting
----------


Double-dashes -- "\-\-" -- must be escaped somehow in HTML output.



Field Lists
===========

:what: Field lists map field names to field bodies, like database
           records.  They are often part of an extension syntax.  They are
           an unambiguous variant of RFC 2822 fields.

:how arg1 arg2:

        The field marker is a colon, the field name, and a colon.

        The field body may contain one or more body elements, indented
        relative to the field marker.


Here's an example of a field list:

:Field List:
            Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do
            eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad
            minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip
            ex ea commodo consequat.

            Duis aute irure dolor in reprehenderit in voluptate velit esse cillum
            dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non
            proident, sunt in culpa qui officia deserunt mollit anim id est laborum.

        some text

:Field List 2: Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor





Option Lists
============

For listing command-line options:

-a            command-line option "a"
-b file       options can have arguments
              and long descriptions
--long        options can be long also
--input=file  long options can also have
              arguments

--very-long-option
              The description can also start on the next line.

              The description may contain multiple body elements,
              regardless of where it starts.

-x, -y, -z    Multiple options are an "option group".
-v, --verbose  Commonly-seen: short & long options.
-1 file, --one=file, --two file
              Multiple options with arguments.
/V            DOS/VMS-style options too

There must be at least two spaces between the option and the
description.

Literal Blocks
--------------

Literal blocks are indicated with a double-colon ("::") at the end of
the preceding paragraph (over there ``-->``).  They can be indented::

    if literal_block:
        text = 'is left as-is'
        spaces_and_linebreaks = 'are preserved'
        markup_processing = None

Or they can be quoted without indentation::

>> Great idea!
>
> Why didn't I think of that?


Line Blocks
===========

| This is a line block.  It ends with a blank line.
|     Each new line begins with a vertical bar ("|").
|     Line breaks and initial indents are preserved.
| Continuation lines are wrapped portions of long lines;
  they begin with a space in place of the vertical bar.
|     The left edge of a continuation line need not be aligned with
  the left edge of the text above it.

| This is a second line block.
|
| Blank lines are permitted internally, but they must begin with a "|".

Take it away, Eric the Orchestra Leader!

    | A one, two, a one two three four
    |
    | Half a bee, philosophically,
    |     must, *ipso facto*, half not be.
    | But half the bee has got to be,
    |     *vis a vis* its entity.  D'you see?
    |
    | But can a bee be said to be
    |     or not to be an entire bee,
    |         when half the bee is not a bee,
    |             due to some ancient injury?
    |
    | Singing...

Block Quotes
============

Block quotes consist of indented body elements:

    My theory by A. Elk.  Brackets Miss, brackets.  This theory goes
    as follows and begins now.  All brontosauruses are thin at one
    end, much much thicker in the middle and then thin again at the
    far end.  That is my theory, it is mine, and belongs to me and I
    own it, and what it is too.

    -- Anne Elk (Miss)

Doctest Blocks
==============

>>> print 'Python-specific usage examples; begun with ">>>"'
Python-specific usage examples; begun with ">>>"
>>> print '(cut and pasted from interactive Python sessions)'
(cut and pasted from interactive Python sessions)

Tables
------

Here's a grid table followed by a simple table:

+------------------------+------------+----------+----------+
| Header row, column 1   | Header 2   | Header 3 | Header 4 |
| (header rows optional) |            |          |          |
+========================+============+==========+==========+
| body row 1, column 1   | column 2   | column 3 | column 4 |
+------------------------+------------+----------+----------+
| body row 2             | Cells may span columns.          |
+------------------------+------------+---------------------+
| body row 3             | Cells may  | - Table cells       |
+------------------------+ span rows. | - contain           |
| body row 4             |            | - body elements.    |
+------------------------+------------+----------+----------+
| body row 5             | Cells may also be     |          |
|                        | empty: ``-->``        |          |
+------------------------+-----------------------+----------+

=====  =====  ======
   Inputs     Output
------------  ------
  A      B    A or B
=====  =====  ======
False  False  False
True   False  True
False  True   True
True   True   True
=====  =====  ======

Footnotes
---------

.. [1] A footnote contains body elements, consistently indented by at
   least 3 spaces.

   This is the footnote's second paragraph.

.. [#label] Footnotes may be numbered, either manually (as in [1]_) or
   automatically using a "#"-prefixed label.  This footnote has a
   label so it can be referred to from multiple places, both as a
   footnote reference ([#label]_) and as a hyperlink reference
   (label_).

.. [#] This footnote is numbered automatically and anonymously using a
   label of "#" only.

.. [*] Footnotes may also use symbols, specified with a "*" label.
   Here's a reference to the next footnote: [*]_.

.. [*] This footnote shows the next symbol in the sequence.

.. [4] Here's an unreferenced footnote


Citations
---------

.. [CIT2002] Citations are text-labeled footnotes. They may be
   rendered separately and differently from footnotes.

Here's a reference to the above, [CIT2002]_

Targets
-------

.. _example:

This paragraph is pointed to by the explicit ``_example`` target. A
reference can be found under `Inline Markup`_, above. `Inline
hyperlink targets`_ are also possible.

Section headers are implicit targets, referred to by name. See
Targets_, which is a subsection of `Body Elements`_.

External targets
================

Explicit external targets are interpolated into references such as
"Python_".

.. _Python: http://www.python.org/


Here's a reference to the Definitinve RST_ Reference documentation.

.. _RST: http://docutils.sourceforge.net/docs/ref/rst/restructuredtext.html

You can refer to another rst document within the site with a **Sphinx** directive.  A reference to the :ref:`rst_code` like this: ``:ref:`rst_code```

Targets may be indirect and anonymous.  Thus `this phrase`__ may also
refer to the Targets_ section.

__ Targets_



Target Footnotes
================

If you use the ``.. target-notes::`` directive, footnotes for **all external references** will be generated, and the
footnotes themselves will be put after that directive.
(Thus you usually want to put the directive at the bottom of a document so the footnotes will be at the bottom \-\- the *foot* of the document.

Target footnoes are not used in this document.  But you can see it in action in `this one <rst_tiny>`.



Directives
----------

.. contents:: :local:

These are just a sample of the many reStructuredText Directives.  For
others, please see
http://docutils.sourceforge.net/docs/ref/rst/directives.html.

Document Parts
``````````````

An example of the "contents" directive can be seen above this section
(a local, untitled table of contents_) and at the beginning of the
document (a document-wide `table of contents`_).

Images
------

An image directive with a link (target) to the Targets section. (The image is a clickable link):

.. image:: static/SmilingMandyOnPatioSmallish-DSC00249-rc.png
    :target: example_
    :alt: Happy, wonderful, smiling Mandy.


A figure is an image with a caption and/or a legend:


.. figure:: static/SmilingMandyOnPatioSmallish-DSC00249-rc.png
  :align: center

  Cats on the internet are fine.  But dogs on the internet are **fanTAStic!**



A figure directive with center alignment and width of 100. (If you click on it, you'll see the lovely full-sized image.)

.. figure:: static/SmilingMandyOnPatioDSC00249-rc.png
    :align: center
    :width: 100

Admonition Boxes
----------------

.. Attention:: Attention - Directives at large.

.. Caution:: Don't take any wooden nickels.

.. DANGER:: Mad scientist at work!

.. Error:: Does not compute.

.. Hint:: It's bigger than a bread box.

.. Important:: These things are imporant:
    - Wash behind your ears.
    - Be nice.
    - Clean up your room.
    - Back up your data.

.. Note:: This is a note.

.. Tip:: 15% if the service is good.

.. WARNING:: Strong prose may provoke extreme mental exertion.
    Reader discretion is strongly advised.


.. admonition:: And, by the way...

   You can make up your own admonition too.


Topics, Sidebars, and Rubrics
-----------------------------

.. sidebar:: Sidebar Title
    :subtitle: This a subtitle

    This is a sidebar.  It is for text outside the flow of the main
    text. The subtitle above doesn't have any interesting formatting so it's hard to tell that it has any import at all.

    .. rubric:: This is a rubric inside a sidebar

    Sidebars often appears beside the main text with a border and
    background color.

.. topic:: Topic Title

   This is a topic.

.. rubric:: This is a rubric



Compound Paragraph
``````````````````

.. compound::

   This paragraph contains a literal block::

       Connecting... OK
       Transmitting data... OK
       Disconnecting... OK

   and thus consists of a simple paragraph, a literal block, and
   another simple paragraph.  Nonetheless it is semantically *one*
   paragraph.

This construct is called a *compound paragraph* and can be produced
with the "compound" directive.

Substitution
------------

An inline image example:  Instead of showing the words ``biohazard``, show  (|biohazard|)

.. |biohazard| image:: static/tiny-Biohazard_symbol.png

The code to accomplish a substitution (a.k.a. *replacement*) is:

.. code-block:: text

    An inline image example:  Instead of showing the words ``biohazard``, show  (|biohazard|)

    .. |biohazard| image:: static/tiny-Biohazard_symbol.png



I recommend that you try |`Python web site <http://www.python.org>`__|, *the* best language around.

.. |`Python web site <http://www.python.org>`__| replace:: `Smalltalk <http://c2.com/cgi/wiki?SmalltalkLanguage>`__

In the preceding text, ``|`Python web site <http://www.python.org>`__|`` was replaced with ```Smalltalk <http://c2.com/cgi/wiki?SmalltalkLanguage>`__``



Comments
--------

Here's one:

.. Comments begin with two dots and a space. Anything may
    follow, except for the syntax of footnotes, hyperlink
    targets, directives, or substitution definitions.

Of course you can't see it, because it's a comment in the source for this file.
Here's the what the rst for the comment looks like in the rst source for this file:

.. parsed-literal::
    .. Comments begin with two dots and a space. Anything may
    follow, except for the syntax of footnotes, hyperlink
    targets, directives, or substitution definitions.
